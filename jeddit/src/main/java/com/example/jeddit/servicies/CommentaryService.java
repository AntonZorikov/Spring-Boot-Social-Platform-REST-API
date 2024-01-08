package com.example.jeddit.servicies;

import com.example.jeddit.exceptions.DataNotFoundException;
import com.example.jeddit.exceptions.NotCorrectDataException;
import com.example.jeddit.exceptions.NotEnoughRightsException;
import com.example.jeddit.exceptions.NotValidToken;
import com.example.jeddit.models.entitys.Commentary;
import com.example.jeddit.models.entitys.Post;
import com.example.jeddit.models.entitys.User;
import com.example.jeddit.models.models.CommentaryCreateRequest;
import com.example.jeddit.models.models.JWTTokenRequest;
import com.example.jeddit.repositories.CommentaryRepository;
import com.example.jeddit.repositories.PostRepository;
import com.example.jeddit.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

@Service
public class CommentaryService {

    @Autowired
    private CommentaryRepository commentaryRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTService jwtService;


    public void create(long postId, CommentaryCreateRequest request) throws NotValidToken, DataNotFoundException, NotCorrectDataException {
        if (!jwtService.validateToken(request.getJwttoken())) {
            throw new NotValidToken("Not valid token");
        }

        Optional<Post> post = postRepository.findById(postId);
        Optional<User> user = userRepository.findById(jwtService.extractUserId(request.getJwttoken()));

        if (post.isEmpty()) {
            throw new DataNotFoundException("Post not found");
        }
        if (user.isEmpty()) {
            throw new DataNotFoundException("User not found");
        }
        if (request.getText().length() > 40000) {
            throw new NotCorrectDataException("Text length must be less then 40000 characters");
        }

        long currentTimestamp = Instant.now().toEpochMilli();
        Commentary commentary = new Commentary();
        commentary.setText(request.getText());
        commentary.setDate(new Timestamp(currentTimestamp));
        commentary.setOwner(user.get());
        commentary.setPost(post.get());
        commentaryRepository.save(commentary);
    }

    public Commentary get(long id) throws DataNotFoundException {
        Optional<Commentary> commentary = commentaryRepository.findById(id);

        if (commentary.isEmpty()) {
            throw new DataNotFoundException("Commentary not found");
        }

        return commentary.get();
    }

    public void delete(long id, JWTTokenRequest request) throws DataNotFoundException, NotValidToken, NotEnoughRightsException {
        if (!jwtService.validateToken(request.getJwttoken())) {
            throw new NotValidToken("Not valid token");
        }

        Optional<Commentary> commentary = commentaryRepository.findById(id);
        Optional<User> user = userRepository.findById(jwtService.extractUserId(request.getJwttoken()));

        if (commentary.isEmpty()) {
            throw new DataNotFoundException("Commentary not found");
        }
        if (user.isEmpty()) {
            throw new DataNotFoundException("User not found");
        }
        if(commentary.get().getOwner().getId() != user.get().getId() && !Objects.equals(user.get().getRole(), "ADMIN") && !commentary.get().getPost().getCommunity().getModerators().contains(user.get())){
            throw new NotEnoughRightsException("Not enough rights exception");
        }

        commentaryRepository.delete(commentary.get());
    }
}
