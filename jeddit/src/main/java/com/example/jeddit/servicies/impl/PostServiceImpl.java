package com.example.jeddit.servicies.impl;

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
import com.example.jeddit.servicies.PostService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

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

    private static final Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

    @Override
    public void create(PostCreateRequest request) throws NotValidToken, DataNotFoundException, NotCorrectDataException, NotEnoughRightsException {
        try {
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
                throw new NotCorrectDataException("Title length must be less than 300 characters, more than 5 characters, and not null");
            }
            if (request.getText().length() > 40000) {
                throw new NotCorrectDataException("Text length must be less than 40000 characters");
            }
            if (!community.get().getFollowers().contains(user.get())) {
                throw new NotEnoughRightsException("You are not following the community");
            }

            long currentTimestamp = Instant.now().toEpochMilli();
            Post post = new Post();
            post.setTitle(request.getTitle());
            post.setText(request.getText());
            post.setDate(new Timestamp(currentTimestamp));
            post.setCommunity(community.get());
            post.setUser(user.get());

            postRepository.save(post);
        } catch (Exception e) {
            logger.error("Failed to create post", e);
            throw e;
        }
    }

    @Override
    public Post get(long id) throws DataNotFoundException {
        try {
            Optional<Post> post = postRepository.findById(id);

            if (post.isEmpty()) {
                throw new DataNotFoundException("Post not found");
            }

            return post.get();
        } catch (Exception e) {
            logger.error("Failed to get post", e);
            throw e;
        }
    }

    @Override
    public void update(long id, PostUpdateRequest request) throws DataNotFoundException, NotCorrectDataException, NotValidToken, NotEnoughRightsException {
        try {
            Optional<Post> post = postRepository.findById(id);

            if (post.isEmpty()) {
                throw new DataNotFoundException("Post not found");
            }
            if (post.get().getText().length() + request.getText().length() > 40000) {
                throw new NotCorrectDataException("Text length must be less than 40000 characters");
            }
            if (!jwtService.validateToken(request.getJwttoken())) {
                throw new NotValidToken("Not valid token");
            }
            if (jwtService.extractUserId(request.getJwttoken()) != post.get().getUser().getId()) {
                throw new NotEnoughRightsException("Not enough rights exception");
            }

            post.get().setText(post.get().getText() + "\n" + request.getText());

            postRepository.save(post.get());
        } catch (Exception e) {
            logger.error("Failed to update post", e);
            throw e;
        }
    }

    @Override
    public void delete(long id, JWTTokenRequest request) throws DataNotFoundException, NotCorrectDataException, NotValidToken, NotEnoughRightsException {
        try {
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
        } catch (Exception e) {
            logger.error("Failed to delete post", e);
            throw e;
        }
    }

    @Override
    @Transactional
    public void upvote(long id, JWTTokenRequest request) throws NotValidToken, DataNotFoundException, NotUniqueDataException {
        try {
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
                throw new NotUniqueDataException("You already voted up for this post");
            }

            vote.ifPresent(value -> voterRepository.delete(value));

            voterRepository.save(new Vote(user.get(), post.get(), 1));
        } catch (Exception e) {
            logger.error("Failed to upvote post", e);
            throw e;
        }
    }

    @Override
    public void downvote(long id, JWTTokenRequest request) throws NotValidToken, DataNotFoundException, NotUniqueDataException {
        try {
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
                throw new NotUniqueDataException("You already voted down for this post");
            }

            vote.ifPresent(value -> voterRepository.delete(value));

            voterRepository.save(new Vote(user.get(), post.get(), -1));
        } catch (Exception e) {
            logger.error("Failed to downvote post", e);
            throw e;
        }
    }

    @Override
    public Vote getVote(long id, JWTTokenRequest request) throws NotValidToken, DataNotFoundException, NotUniqueDataException {
        try {
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
        } catch (Exception e) {
            logger.error("Failed to get vote", e);
            throw e;
        }
    }

    @Override
    public void deleteVote(long id, JWTTokenRequest request) throws NotValidToken, DataNotFoundException, NotUniqueDataException {
        try {
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
        } catch (Exception e) {
            logger.error("Failed to delete vote", e);
            throw e;
        }
    }
}
