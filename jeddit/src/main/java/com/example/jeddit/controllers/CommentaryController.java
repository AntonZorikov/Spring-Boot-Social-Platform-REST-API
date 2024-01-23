package com.example.jeddit.controllers;

import com.example.jeddit.exceptions.DataNotFoundException;
import com.example.jeddit.exceptions.NotCorrectDataException;
import com.example.jeddit.exceptions.NotEnoughRightsException;
import com.example.jeddit.exceptions.NotValidToken;
import com.example.jeddit.models.entitys.Commentary;
import com.example.jeddit.models.models.ApiResponse;
import com.example.jeddit.models.models.commentaries.CommentaryCreateRequest;
import com.example.jeddit.models.models.JWTTokenRequest;
import com.example.jeddit.models.models.commentaries.CommentaryUpdateRequest;
import com.example.jeddit.servicies.impl.CommentaryServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/commentaries")
public class CommentaryController {

    @Autowired
    private CommentaryServiceImpl commentaryService;

    private static final Logger logger = LoggerFactory.getLogger(CommentaryController.class);

    @PostMapping("/{postId}/")
    private ResponseEntity<Object> createCommentary(@PathVariable long postId, @RequestBody CommentaryCreateRequest request) {
        try {
            commentaryService.create(postId, request);
            logger.info("Commentary created successfully");
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, "Success commentary create"));
        } catch (DataNotFoundException e){
            logger.warn("Commentary creation failed: DataNotFoundException");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, e.getMessage()));
        } catch(NotCorrectDataException e) {
            logger.warn("Commentary creation failed: NotCorrectDataException");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, e.getMessage()));
        } catch (NotValidToken e) {
            logger.warn("Commentary creation failed: NotValidToken");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    private ResponseEntity<Object> getCommentary(@PathVariable long id) {
        try {
            Commentary commentary = commentaryService.get(id);
            logger.info("Commentary successfully get");
            return ResponseEntity.status(HttpStatus.OK).body(commentary);
        } catch (DataNotFoundException e) {
            logger.warn("Commentary get failed: DataNotFoundException");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    private ResponseEntity<Object> updateCommentary(@PathVariable long id, @RequestBody CommentaryUpdateRequest request) {
        try {
            commentaryService.update(id, request);
            logger.info("Commentary successfully update");
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, "Success commentary update"));
        } catch (DataNotFoundException e) {
            logger.warn("Commentary get failed: DataNotFoundException");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, e.getMessage()));
        } catch (NotEnoughRightsException e){
            logger.warn("Commentary get failed: NotEnoughRightsException");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, e.getMessage()));
        } catch(NotValidToken e) {
            logger.warn("Commentary get failed: NotValidToken");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<Object> deleteCommentary(@PathVariable long id, @RequestBody JWTTokenRequest request) {
        try {
            commentaryService.delete(id, request);
            logger.info("Commentary successfully delete");
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, "Success commentary delete"));
        } catch (DataNotFoundException e) {
            logger.warn("Commentary delete failed: DataNotFoundException");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, e.getMessage()));
        } catch (NotValidToken e){
            logger.warn("Commentary delete failed: NotValidToken");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, e.getMessage()));
        } catch(NotEnoughRightsException e) {
            logger.warn("Commentary delete failed: NotEnoughRightsException");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, e.getMessage()));
        }
    }
}
