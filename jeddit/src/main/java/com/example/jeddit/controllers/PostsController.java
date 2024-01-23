package com.example.jeddit.controllers;

import com.example.jeddit.exceptions.*;
import com.example.jeddit.models.entitys.Post;
import com.example.jeddit.models.entitys.Vote;
import com.example.jeddit.models.models.ApiResponse;
import com.example.jeddit.models.models.JWTTokenRequest;
import com.example.jeddit.models.models.posts.PostCreateRequest;
import com.example.jeddit.models.models.posts.PostUpdateRequest;
import com.example.jeddit.models.models.votes.VoteGetResponse;
import com.example.jeddit.servicies.impl.CommentaryServiceImpl;
import com.example.jeddit.servicies.impl.PostServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/posts")
public class PostsController {

    @Autowired
    private PostServiceImpl postService;

    @Autowired
    private CommentaryServiceImpl commentaryService;

    private static final Logger logger = LoggerFactory.getLogger(PostsController.class);

    @PostMapping("/")
    private ResponseEntity<Object> createPost(@RequestBody PostCreateRequest request) {
        try {
            postService.create(request);
            logger.info("Post successfully created");
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, "Success create"));
        } catch (DataNotFoundException | NotCorrectDataException e) {
            logger.warn("Post creation failed: {}", e.getClass().getSimpleName());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, e.getMessage()));
        } catch (NotEnoughRightsException | NotValidToken e) {
            logger.warn("Post creation failed: {}", e.getClass().getSimpleName());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    private ResponseEntity<Object> getPostById(@PathVariable long id) {
        try {
            Post post = postService.get(id);
            logger.info("Successfully retrieved post with ID {}", id);
            return ResponseEntity.status(HttpStatus.OK).body(post);
        } catch (DataNotFoundException e) {
            logger.warn("Failed to retrieve post with ID {}: DataNotFoundException", id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    private ResponseEntity<Object> updatePost(@PathVariable long id, @RequestBody PostUpdateRequest request) {
        try {
            postService.update(id, request);
            logger.info("Post successfully updated for ID {}", id);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, "Success update"));
        } catch (DataNotFoundException | NotCorrectDataException e) {
            logger.warn("Failed to update post for ID {}: {}", id, e.getClass().getSimpleName());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, e.getMessage()));
        } catch (NotEnoughRightsException | NotValidToken e) {
            logger.warn("Failed to update post for ID {}: {}", id, e.getClass().getSimpleName());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<Object> deletePost(@PathVariable long id, @RequestBody JWTTokenRequest request) {
        try {
            postService.delete(id, request);
            logger.info("Post successfully deleted for ID {}", id);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, "Success delete"));
        } catch (DataNotFoundException | NotCorrectDataException e) {
            logger.warn("Failed to delete post for ID {}: {}", id, e.getClass().getSimpleName());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, e.getMessage()));
        } catch (NotEnoughRightsException | NotValidToken e) {
            logger.warn("Failed to delete post for ID {}: {}", id, e.getClass().getSimpleName());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/{id}/vote")
    private ResponseEntity<Object> getVote(@PathVariable long id, @RequestBody JWTTokenRequest request) {
        try {
            Vote vote = postService.getVote(id, request);
            logger.info("Successfully retrieved vote for post with ID {}", id);
            return ResponseEntity.status(HttpStatus.OK).body(new VoteGetResponse(vote));
        } catch (DataNotFoundException e) {
            logger.warn("Failed to retrieve vote for post with ID {}: DataNotFoundException", id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, e.getMessage()));
        } catch (NotUniqueDataException e) {
            logger.warn("Failed to retrieve vote for post with ID {}: NotUniqueDataException", id);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(false, e.getMessage()));
        } catch (NotValidToken e) {
            logger.warn("Failed to retrieve vote for post with ID {}: NotValidToken", id);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PostMapping("/{id}/upvote")
    private ResponseEntity<Object> upvotePost(@PathVariable long id, @RequestBody JWTTokenRequest request) {
        try {
            postService.upvote(id, request);
            logger.info("Successfully upvoted post with ID {}", id);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, "Success upvote"));
        } catch (DataNotFoundException e) {
            logger.warn("Failed to upvote post with ID {}: DataNotFoundException", id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, e.getMessage()));
        } catch (NotUniqueDataException e) {
            logger.warn("Failed to upvote post with ID {}: NotUniqueDataException", id);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(false, e.getMessage()));
        } catch (NotValidToken e) {
            logger.warn("Failed to upvote post with ID {}: NotValidToken", id);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PostMapping("/{id}/downvote")
    private ResponseEntity<Object> downVotePost(@PathVariable long id, @RequestBody JWTTokenRequest request) {
        try {
            postService.downvote(id, request);
            logger.info("Successfully downvoted post with ID {}", id);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, "Success downvote"));
        } catch (DataNotFoundException e) {
            logger.warn("Failed to downvote post with ID {}: DataNotFoundException", id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, e.getMessage()));
        } catch (NotUniqueDataException e) {
            logger.warn("Failed to downvote post with ID {}: NotUniqueDataException", id);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(false, e.getMessage()));
        } catch (NotValidToken e) {
            logger.warn("Failed to downvote post with ID {}: NotValidToken", id);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, e.getMessage()));
        }
    }

    @DeleteMapping("/{id}/vote")
    private ResponseEntity<Object> deleteVote(@PathVariable long id, @RequestBody JWTTokenRequest request) {
        try {
            postService.deleteVote(id, request);
            logger.info("Successfully deleted vote for post with ID {}", id);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, "Success delete"));
        } catch (DataNotFoundException e) {
            logger.warn("Failed to delete vote for post with ID {}: DataNotFoundException", id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, e.getMessage()));
        } catch (NotUniqueDataException e) {
            logger.warn("Failed to delete vote for post with ID {}: NotUniqueDataException", id);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(false, e.getMessage()));
        } catch (NotValidToken e) {
            logger.warn("Failed to delete vote for post with ID {}: NotValidToken", id);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, e.getMessage()));
        }
    }
}
