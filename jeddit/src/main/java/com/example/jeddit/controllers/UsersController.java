package com.example.jeddit.controllers;

import com.example.jeddit.exceptions.*;
import com.example.jeddit.models.entitys.Post;
import com.example.jeddit.models.models.ApiResponse;
import com.example.jeddit.models.models.JWTTokenRequest;
import com.example.jeddit.models.models.users.UserAllInfoResponse;
import com.example.jeddit.models.models.users.UserBaseInfoPesponse;
import com.example.jeddit.models.models.users.UserChangePasswordRequest;
import com.example.jeddit.servicies.impl.ContentServiceImpl;
import com.example.jeddit.servicies.impl.UsersServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/users")
public class UsersController {

    @Autowired
    private UsersServiceImpl usersService;

    @Autowired
    private ContentServiceImpl contentService;

    private static final Logger logger = LoggerFactory.getLogger(UsersController.class);

    @PutMapping("/change_password")
    @ResponseBody
    private ResponseEntity<Object> changePassword(@RequestBody UserChangePasswordRequest request) {
        try {
            usersService.changePassword(request);
            logger.info("Password changed successfully");
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, "Password changed successfully"));
        } catch (WrongPasswordException | NotValidToken e) {
            logger.warn("Failed to change password: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, e.getMessage()));
        } catch (DataNotFoundException | NotCorrectDataException e) {
            logger.warn("Failed to change password: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, e.getMessage()));
        } catch (NotEnoughRightsException e) {
            logger.warn("Failed to change password: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/{id}/base_info")
    @ResponseBody
    private ResponseEntity<Object> baseInfo(@PathVariable long id) {
        try {
            UserBaseInfoPesponse response = usersService.getBaseInfo(id);
            logger.info("Successfully retrieved base info for user with ID {}", id);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (DataNotFoundException e) {
            logger.warn("Failed to retrieve base info for user with ID {}: DataNotFoundException", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/{id}/all_info")
    @ResponseBody
    private ResponseEntity<Object> allInfo(@PathVariable long id, @RequestBody JWTTokenRequest request) {
        try {
            UserAllInfoResponse response = usersService.getAllInfo(request, id);
            logger.info("Successfully retrieved all info for user with ID {}", id);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (DataNotFoundException e) {
            logger.warn("Failed to retrieve all info for user with ID {}: DataNotFoundException", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false, e.getMessage()));
        } catch (NotValidToken e) {
            logger.warn("Failed to retrieve all info for user with ID {}: NotValidToken", id);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, e.getMessage()));
        } catch (NotEnoughRightsException e) {
            logger.warn("Failed to retrieve all info for user with ID {}: NotEnoughRightsException", id);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse(false, e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    private ResponseEntity<Object> deleteUser(@PathVariable long id, @RequestBody JWTTokenRequest request) {
        try {
            usersService.delete(request, id);
            logger.info("Successfully deleted user with ID {}", id);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, "Success delete"));
        } catch (DataNotFoundException e) {
            logger.warn("Failed to delete user with ID {}: DataNotFoundException", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false, e.getMessage()));
        } catch (NotValidToken e) {
            logger.warn("Failed to delete user with ID {}: NotValidToken", id);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, e.getMessage()));
        } catch (NotEnoughRightsException e) {
            logger.warn("Failed to delete user with ID {}: NotEnoughRightsException", id);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/{id}/posts")
    @ResponseBody
    private ResponseEntity<Object> getPosts(@PathVariable long id, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        try {
            Page<Post> posts = usersService.getPosts(id, page, size);
            logger.info("Successfully retrieved posts for user with ID {}", id);
            return ResponseEntity.status(HttpStatus.OK).body(posts);
        } catch (DataNotFoundException e) {
            logger.warn("Failed to retrieve posts for user with ID {}: DataNotFoundException", id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/newsline")
    @ResponseBody
    private ResponseEntity<Object> getNewsLine(@RequestBody JWTTokenRequest request, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        try {
            Page<Post> posts = contentService.getUserNewsline(request, page, size);
            logger.info("Successfully retrieved news line for user");
            return ResponseEntity.status(HttpStatus.OK).body(posts);
        } catch (DataNotFoundException e) {
            logger.warn("Failed to retrieve news line for user: DataNotFoundException");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, e.getMessage()));
        } catch (NotValidToken e) {
            logger.warn("Failed to retrieve news line for user: NotValidToken");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, e.getMessage()));
        }
    }
}
