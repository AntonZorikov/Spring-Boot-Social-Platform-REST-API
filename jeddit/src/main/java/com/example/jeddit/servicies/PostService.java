package com.example.jeddit.servicies;

import com.example.jeddit.exceptions.*;
import com.example.jeddit.models.entitys.Community;
import com.example.jeddit.models.entitys.Post;
import com.example.jeddit.models.entitys.User;
import com.example.jeddit.models.entitys.Vote;
import com.example.jeddit.models.models.JWTTokenRequest;
import com.example.jeddit.models.models.posts.PostCreateRequest;
import com.example.jeddit.models.models.posts.PostUpdateRequest;
import com.example.jeddit.repositories.CommunitiesRepository;
import com.example.jeddit.repositories.PostRepository;
import com.example.jeddit.repositories.UserRepository;
import com.example.jeddit.repositories.VoterRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommunitiesRepository communitiesRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private VoterRepository voterRepository;

    public void create(PostCreateRequest request) throws NotValidToken, DataNotFoundException, NotCorrectDataException, NotEnoughRightsException {
        if (!jwtService.validateToken(request.getJwttoken())) {
            throw new NotValidToken("Not valid token");
        }

        Optional<Community> community = communitiesRepository.findByTitle(request.getCommunityTitle());
        Optional<User> user = userRepository.findById(jwtService.extractUserId(request.getJwttoken()));

        if (community.isEmpty()) {
            throw new DataNotFoundException("Community not found");
        }
        if (user.isEmpty()) {
            throw new DataNotFoundException("User not found");
        }
        if (request.getTitle() == null || request.getTitle().length() > 300 || request.getTitle().length() < 5) {
            throw new NotCorrectDataException("Title length must be less then 300 characters, more then 5 characters ant not null");
        }
        if (request.getText().length() > 40000) {
            throw new NotCorrectDataException("Text length must be less then 40000 characters");
        }
        if (!community.get().getFollowers().contains(user.get())) {
            throw new NotEnoughRightsException("You are not follow");
        }

        long currentTimestamp = Instant.now().toEpochMilli();
        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setText(request.getText());
        post.setDate(new Timestamp(currentTimestamp));
        post.setCommunity(community.get());
        post.setUser(user.get());
        postRepository.save(post);
    }

    public Post get(long id) throws DataNotFoundException {
        Optional<Post> post = postRepository.findById(id);
        if (post.isEmpty()) {
            throw new DataNotFoundException("Post not found");
        }
        return post.get();
    }

    public void update(long id, PostUpdateRequest request) throws DataNotFoundException, NotCorrectDataException, NotValidToken, NotEnoughRightsException {
        Optional<Post> post = postRepository.findById(id);
        if (post.isEmpty()) {
            throw new DataNotFoundException("Post not found");
        }
        if (post.get().getText().length() + request.getText().length() > 40000) {
            throw new NotCorrectDataException("Text length must be less then 40000 characters");
        }
        if (!jwtService.validateToken(request.getJwttoken())) {
            throw new NotValidToken("Not valid token");
        }
        if (jwtService.extractUserId(request.getJwttoken()) != post.get().getUser().getId()) {
            throw new NotEnoughRightsException("Not enough rights exception");
        }
        post.get().setText(post.get().getText() + "\n" + request.getText());
        postRepository.save(post.get());
    }

    public void delete(long id, JWTTokenRequest request) throws DataNotFoundException, NotCorrectDataException, NotValidToken, NotEnoughRightsException {
        Optional<Post> post = postRepository.findById(id);
        if (post.isEmpty()) {
            throw new DataNotFoundException("Post not found");
        }
        if (!jwtService.validateToken(request.getJwttoken())) {
            throw new NotValidToken("Not valid token");
        }
        Optional<User> user = userRepository.findById(jwtService.extractUserId(request.getJwttoken()));
        if (jwtService.extractUserId(request.getJwttoken()) != post.get().getUser().getId() && !(user.isPresent() && post.get().getCommunity().getModerators().contains(user.get()))) {
            throw new NotEnoughRightsException("Not enough rights exception");
        }
        postRepository.delete(post.get());
    }

    @Transactional
    public void upvote(long id, JWTTokenRequest request) throws NotValidToken, DataNotFoundException, NotUniqueDataException {
        if (!jwtService.validateToken(request.getJwttoken())) {
            throw new NotValidToken("Not valid token");
        }

        Optional<Post> post = postRepository.findById(id);
        Optional<User> user = userRepository.findById(jwtService.extractUserId(request.getJwttoken()));
        Optional<Vote> vote = voterRepository.findByUSerIdAndPostId(jwtService.extractUserId(request.getJwttoken()), id);

        if (post.isEmpty()) {
            throw new DataNotFoundException("Post not found");
        }
        if (user.isEmpty()) {
            throw new DataNotFoundException("User not found");
        }
        if (vote.isPresent() && vote.get().getValue() == 1) {
            throw new NotUniqueDataException("You already voted like this");
        }
        vote.ifPresent(value -> voterRepository.delete(value));
        voterRepository.save(new Vote(user.get(), post.get(), 1));
    }

    public void downvote(long id, JWTTokenRequest request) throws NotValidToken, DataNotFoundException, NotUniqueDataException {
        if (!jwtService.validateToken(request.getJwttoken())) {
            throw new NotValidToken("Not valid token");
        }

        Optional<Post> post = postRepository.findById(id);
        Optional<User> user = userRepository.findById(jwtService.extractUserId(request.getJwttoken()));
        Optional<Vote> vote = voterRepository.findByUSerIdAndPostId(jwtService.extractUserId(request.getJwttoken()), id);

        if (post.isEmpty()) {
            throw new DataNotFoundException("Post not found");
        }
        if (user.isEmpty()) {
            throw new DataNotFoundException("User not found");
        }
        if (vote.isPresent() && vote.get().getValue() == -1) {
            throw new NotUniqueDataException("You already voted like this");
        }
        vote.ifPresent(value -> voterRepository.delete(value));
        voterRepository.save(new Vote(user.get(), post.get(), -1));
    }

    public Vote getVote(long id, JWTTokenRequest request) throws NotValidToken, DataNotFoundException, NotUniqueDataException {
        if (!jwtService.validateToken(request.getJwttoken())) {
            throw new NotValidToken("Not valid token");
        }

        Optional<Post> post = postRepository.findById(id);
        Optional<User> user = userRepository.findById(jwtService.extractUserId(request.getJwttoken()));
        Optional<Vote> vote = voterRepository.findByUSerIdAndPostId(jwtService.extractUserId(request.getJwttoken()), id);

        if (post.isEmpty()) {
            throw new DataNotFoundException("Post not found");
        }
        if (user.isEmpty()) {
            throw new DataNotFoundException("User not found");
        }
        if (vote.isEmpty()) {
            throw new DataNotFoundException("Vote not found");
        }
        return vote.get();
    }

    public void deleteVote(long id, JWTTokenRequest request) throws NotValidToken, DataNotFoundException, NotUniqueDataException {
        if (!jwtService.validateToken(request.getJwttoken())) {
            throw new NotValidToken("Not valid token");
        }

        Optional<Post> post = postRepository.findById(id);
        Optional<User> user = userRepository.findById(jwtService.extractUserId(request.getJwttoken()));
        Optional<Vote> vote = voterRepository.findByUSerIdAndPostId(jwtService.extractUserId(request.getJwttoken()), id);

        if (post.isEmpty()) {
            throw new DataNotFoundException("Post not found");
        }
        if (user.isEmpty()) {
            throw new DataNotFoundException("User not found");
        }
        if (vote.isEmpty()) {
            throw new DataNotFoundException("Vote not found");
        }
        voterRepository.delete(vote.get());
    }
}
