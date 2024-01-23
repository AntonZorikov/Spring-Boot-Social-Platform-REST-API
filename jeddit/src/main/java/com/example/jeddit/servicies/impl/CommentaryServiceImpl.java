package com.example.jeddit.servicies.impl;

import com.example.jeddit.exceptions.DataNotFoundException;
import com.example.jeddit.exceptions.NotCorrectDataException;
import com.example.jeddit.exceptions.NotEnoughRightsException;
import com.example.jeddit.exceptions.NotValidToken;
import com.example.jeddit.models.entitys.Commentary;
import com.example.jeddit.models.entitys.Post;
import com.example.jeddit.models.entitys.User;
import com.example.jeddit.models.models.commentaries.CommentaryCreateRequest;
import com.example.jeddit.models.models.JWTTokenRequest;
import com.example.jeddit.models.models.commentaries.CommentaryUpdateRequest;
import com.example.jeddit.repositories.CommentaryRepository;
import com.example.jeddit.repositories.PostRepository;
import com.example.jeddit.repositories.UserRepository;
import com.example.jeddit.servicies.CommentaryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

@Service
public class CommentaryServiceImpl implements CommentaryService {

    @Autowired
    private CommentaryRepository commentaryRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTService jwtService;

    private static final Logger logger = LoggerFactory.getLogger(CommentaryServiceImpl.class);

    @Override
    public void create(long postId, CommentaryCreateRequest request) throws NotValidToken, DataNotFoundException, NotCorrectDataException {
        if (!jwtService.validateToken(request.getJwttoken())) {
            logger.warn("Failed to create commentary: Not valid token");
            throw new NotValidToken("Not valid token");
        }

        Optional<Post> post = postRepository.findById(postId);
        Optional<User> user = userRepository.findById(jwtService.extractUserId(request.getJwttoken()));

        if (post.isEmpty()) {
            logger.warn("Failed to create commentary: Post not found");
            throw new DataNotFoundException("Post not found");
        }
        if (user.isEmpty()) {
            logger.warn("Failed to create commentary: User not found");
            throw new DataNotFoundException("User not found");
        }
        if (request.getText().length() > 40000) {
            logger.warn("Failed to create commentary: Text length must be less than 40000 characters");
            throw new NotCorrectDataException("Text length must be less than 40000 characters");
        }

        long currentTimestamp = Instant.now().toEpochMilli();
        Commentary commentary = new Commentary();
        commentary.setText(request.getText());
        commentary.setDate(new Timestamp(currentTimestamp));
        commentary.setOwner(user.get());
        commentary.setPost(post.get());

        commentaryRepository.save(commentary);
        logger.info("Commentary created successfully");
    }

    @Override
    public Commentary get(long id) throws DataNotFoundException {
        Optional<Commentary> commentary = commentaryRepository.findById(id);

        if (commentary.isEmpty()) {
            logger.warn("Failed to get commentary: Commentary not found");
            throw new DataNotFoundException("Commentary not found");
        }

        return commentary.get();
    }

    @Override
    public void update(long id, CommentaryUpdateRequest request) throws DataNotFoundException, NotValidToken, NotEnoughRightsException {
        Optional<Commentary> commentary = commentaryRepository.findById(id);

        if (commentary.isEmpty()) {
            logger.warn("Failed to update commentary: Commentary not found");
            throw new DataNotFoundException("Commentary not found");
        }
        if (!jwtService.validateToken(request.getJwttoken())) {
            logger.warn("Failed to update commentary: Not valid token");
            throw new NotValidToken("Not valid token");
        }
        if (jwtService.extractUserId(request.getJwttoken()) != commentary.get().getOwner().getId()) {
            logger.warn("Failed to update commentary: Not enough rights exception");
            throw new NotEnoughRightsException("Not enough rights exception");
        }

        commentary.get().setText(commentary.get().getText() + "\n" + request.getText());

        commentaryRepository.save(commentary.get());
        logger.info("Commentary updated successfully");
    }

    @Override
    public void delete(long id, JWTTokenRequest request) throws DataNotFoundException, NotValidToken, NotEnoughRightsException {
        if (!jwtService.validateToken(request.getJwttoken())) {
            logger.warn("Failed to delete commentary: Not valid token");
            throw new NotValidToken("Not valid token");
        }

        Optional<Commentary> commentary = commentaryRepository.findById(id);
        Optional<User> user = userRepository.findById(jwtService.extractUserId(request.getJwttoken()));

        if (commentary.isEmpty()) {
            logger.warn("Failed to delete commentary: Commentary not found");
            throw new DataNotFoundException("Commentary not found");
        }
        if (user.isEmpty()) {
            logger.warn("Failed to delete commentary: User not found");
            throw new DataNotFoundException("User not found");
        }
        if (commentary.get().getOwner().getId() != user.get().getId() && !Objects.equals(user.get().getRole(), "ADMIN") && !commentary.get().getPost().getCommunity().getModerators().contains(user.get())) {
            logger.warn("Failed to delete commentary: Not enough rights exception");
            throw new NotEnoughRightsException("Not enough rights exception");
        }

        commentaryRepository.delete(commentary.get());
        logger.info("Commentary deleted successfully");
    }

}
