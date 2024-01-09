package com.example.jeddit.controllers;

import com.example.jeddit.exceptions.*;
import com.example.jeddit.models.entitys.Post;
import com.example.jeddit.models.models.JWTTokenRequest;
import com.example.jeddit.models.models.ApiResponse;
import com.example.jeddit.models.models.users.UserAllInfoResponse;
import com.example.jeddit.models.models.users.UserBaseInfoPesponse;
import com.example.jeddit.models.models.users.UserChangePasswordRequest;
import com.example.jeddit.servicies.impl.ContentServiceImpl;
import com.example.jeddit.servicies.impl.UsersServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PutMapping("/change_password")
    @ResponseBody
    private ResponseEntity<Object> changePassword(@RequestBody UserChangePasswordRequest request) {
        try {
            usersService.changePassword(request);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, "Password changed successfully"));
        } catch (WrongPasswordException | NotValidToken e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, e.getMessage()));
        } catch (DataNotFoundException | NotCorrectDataException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, e.getMessage()));
        } catch (NotEnoughRightsException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/{id}/base_info")
    @ResponseBody
    private ResponseEntity<Object> baseInfo(@PathVariable long id) {
        try {
            UserBaseInfoPesponse response = usersService.getBaseInfo(id);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/{id}/all_info")
    @ResponseBody
    private ResponseEntity<Object> allInfo(@PathVariable long id, @RequestBody JWTTokenRequest request) {
        try {
            UserAllInfoResponse response = usersService.getAllInfo(request, id);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false, e.getMessage()));
        } catch (NotValidToken e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, e.getMessage()));
        } catch (NotEnoughRightsException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse(false, e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    private ResponseEntity<Object> deleteUser(@PathVariable long id, @RequestBody JWTTokenRequest request) {
        try {
            usersService.delete(request, id);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, "Success delete"));
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false, e.getMessage()));
        } catch (NotValidToken e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, e.getMessage()));
        } catch (NotEnoughRightsException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/{id}/posts")
    @ResponseBody
    private ResponseEntity<Object> getPosts(@PathVariable long id, @RequestParam(defaultValue = "0") int from, @RequestParam(defaultValue = "10") int to) {
        try {
            List<Post> posts = usersService.getPosts(id, from, to);
            return ResponseEntity.status(HttpStatus.OK).body(posts);
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/newsline")
    @ResponseBody
    private ResponseEntity<Object> getNewsline(@RequestBody JWTTokenRequest request, @RequestParam(defaultValue = "0") int from, @RequestParam(defaultValue = "10") int to) {
        try {
            List<Post> posts = contentService.getUserNewsline(request, from, to);
            return ResponseEntity.status(HttpStatus.OK).body(posts);
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, e.getMessage()));
        } catch (NotValidToken e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, e.getMessage()));
        }
    }
}
