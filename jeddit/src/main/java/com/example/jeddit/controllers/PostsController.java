package com.example.jeddit.controllers;

import com.example.jeddit.exceptions.*;
import com.example.jeddit.models.entitys.Post;
import com.example.jeddit.models.entitys.Vote;
import com.example.jeddit.models.models.CommentaryCreateRequest;
import com.example.jeddit.models.models.ErrorModel;
import com.example.jeddit.models.models.JWTTokenRequest;
import com.example.jeddit.models.models.StandardResponse;
import com.example.jeddit.models.models.posts.PostCreateRequest;
import com.example.jeddit.models.models.posts.PostUpdateRequest;
import com.example.jeddit.models.models.vote.VoteGetResponse;
import com.example.jeddit.servicies.CommentaryService;
import com.example.jeddit.servicies.CommunitiesService;
import com.example.jeddit.servicies.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/posts")
public class PostsController {

    @Autowired
    private PostService postService;

    @Autowired
    private CommentaryService commentaryService;

    @PostMapping("/")
    private ResponseEntity<Object> createPost(@RequestBody PostCreateRequest request) {
        try {
            postService.create(request);
            return ResponseEntity.status(HttpStatus.OK).body(new StandardResponse(true, "Success create"));
        } catch (DataNotFoundException | NotCorrectDataException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StandardResponse(false, new ErrorModel(400, "BAD_REQUEST", e.getMessage()), "error"));
        } catch (NotEnoughRightsException | NotValidToken e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new StandardResponse(false, new ErrorModel(401, "UNAUTHORIZED", e.getMessage()), "error"));
        }
    }

    @GetMapping("/{id}")
    private ResponseEntity<Object> getPostById(@PathVariable long id) {
        try {
            Post post = postService.get(id);
            return ResponseEntity.status(HttpStatus.OK).body(post);
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StandardResponse(false, new ErrorModel(400, "BAD_REQUEST", e.getMessage()), "error"));
        }
    }

    @PutMapping("/{id}")
    private ResponseEntity<Object> updatePost(@PathVariable long id, @RequestBody PostUpdateRequest request) {
        try {
            postService.update(id, request);
            return ResponseEntity.status(HttpStatus.OK).body(new StandardResponse(true, "Success update"));
        } catch (DataNotFoundException | NotCorrectDataException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StandardResponse(false, new ErrorModel(400, "BAD_REQUEST", e.getMessage()), "error"));
        } catch (NotEnoughRightsException | NotValidToken e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new StandardResponse(false, new ErrorModel(401, "UNAUTHORIZED", e.getMessage()), "error"));
        }
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<Object> deletePost(@PathVariable long id, @RequestBody JWTTokenRequest request) {
        try {
            postService.delete(id, request);
            return ResponseEntity.status(HttpStatus.OK).body(new StandardResponse(true, "Success delete"));
        } catch (DataNotFoundException | NotCorrectDataException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StandardResponse(false, new ErrorModel(400, "BAD_REQUEST", e.getMessage()), "error"));
        } catch (NotEnoughRightsException | NotValidToken e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new StandardResponse(false, new ErrorModel(401, "UNAUTHORIZED", e.getMessage()), "error"));
        }
    }

    @GetMapping("/{id}/vote")
    private ResponseEntity<Object> getVote(@PathVariable long id, @RequestBody JWTTokenRequest request) {
        try {
            Vote vote = postService.getVote(id, request);
            return ResponseEntity.status(HttpStatus.OK).body(new VoteGetResponse(vote));
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StandardResponse(false, new ErrorModel(400, "BAD_REQUEST", e.getMessage()), "error"));
        } catch (NotUniqueDataException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new StandardResponse(false, new ErrorModel(409, "CONFLICT", e.getMessage()), "error"));
        } catch (NotValidToken e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new StandardResponse(false, new ErrorModel(401, "UNAUTHORIZED", e.getMessage()), "error"));
        }
    }

    @PostMapping("/{id}/upvote")
    private ResponseEntity<Object> upvotePost(@PathVariable long id, @RequestBody JWTTokenRequest request) {
        try {
            postService.upvote(id, request);
            return ResponseEntity.status(HttpStatus.OK).body(new StandardResponse(true, "Success upvote"));
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StandardResponse(false, new ErrorModel(400, "BAD_REQUEST", e.getMessage()), "error"));
        } catch (NotUniqueDataException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new StandardResponse(false, new ErrorModel(409, "CONFLICT", e.getMessage()), "error"));
        } catch (NotValidToken e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new StandardResponse(false, new ErrorModel(401, "UNAUTHORIZED", e.getMessage()), "error"));
        }
    }

    @PostMapping("/{id}/downvote")
    private ResponseEntity<Object> downVotePost(@PathVariable long id, @RequestBody JWTTokenRequest request) {
        try {
            postService.downvote(id, request);
            return ResponseEntity.status(HttpStatus.OK).body(new StandardResponse(true, "Success downvote"));
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StandardResponse(false, new ErrorModel(400, "BAD_REQUEST", e.getMessage()), "error"));
        } catch (NotUniqueDataException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new StandardResponse(false, new ErrorModel(409, "CONFLICT", e.getMessage()), "error"));
        } catch (NotValidToken e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new StandardResponse(false, new ErrorModel(401, "UNAUTHORIZED", e.getMessage()), "error"));
        }
    }

    @DeleteMapping("/{id}/vote")
    private ResponseEntity<Object> deleteVote(@PathVariable long id, @RequestBody JWTTokenRequest request) {
        try {
            postService.deleteVote(id, request);
            return ResponseEntity.status(HttpStatus.OK).body(new StandardResponse(true, "Success delete"));
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StandardResponse(false, new ErrorModel(400, "BAD_REQUEST", e.getMessage()), "error"));
        } catch (NotUniqueDataException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new StandardResponse(false, new ErrorModel(409, "CONFLICT", e.getMessage()), "error"));
        } catch (NotValidToken e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new StandardResponse(false, new ErrorModel(401, "UNAUTHORIZED", e.getMessage()), "error"));
        }
    }
}
