package com.example.jeddit.controllers;

import com.example.jeddit.exceptions.*;
import com.example.jeddit.models.entitys.Post;
import com.example.jeddit.models.entitys.User;
import com.example.jeddit.models.models.ApiResponse;
import com.example.jeddit.models.models.JWTTokenRequest;
import com.example.jeddit.models.models.communities.CommunitiesCreateRequest;
import com.example.jeddit.models.models.communities.CommunityChangeDescriptionRequest;
import com.example.jeddit.models.models.communities.CommunityInfoResponse;
import com.example.jeddit.models.models.posts.MostRatedPostProjection;
import com.example.jeddit.servicies.impl.CommunitiesServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/communities")
public class CommunitiesController {

    @Autowired
    private CommunitiesServiceImpl communitiesService;

    @PostMapping("/")
    @ResponseBody
    private ResponseEntity<Object> createCommunity(@RequestBody CommunitiesCreateRequest request) {
        try {
            communitiesService.create(request);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, "Success create"));
        } catch (NotUniqueDataException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(false, e.getMessage()));
        } catch (NotValidToken e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, e.getMessage()));
        } catch (NotCorrectDataException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/{title}")
    @ResponseBody
    private ResponseEntity<Object> getCommunity(@PathVariable String title) {
        try {
            CommunityInfoResponse response = communitiesService.get(title);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PutMapping("/{title}/change_description")
    @ResponseBody
    private ResponseEntity<Object> changePassword(@PathVariable String title, @RequestBody CommunityChangeDescriptionRequest request) {
        try {
            communitiesService.changeDescription(title, request);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, "Success description change"));
        } catch (DataNotFoundException | NotCorrectDataException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, e.getMessage()));
        } catch (NotEnoughRightsException | NotValidToken e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, e.getMessage()));
        }
    }

    @DeleteMapping("/{title}")
    @ResponseBody
    private ResponseEntity<Object> deleteCommunity(@PathVariable String title, @RequestBody JWTTokenRequest request) {
        try {
            communitiesService.delete(title, request);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, "Success delete"));
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, e.getMessage()));
        } catch (NotEnoughRightsException | NotValidToken e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PostMapping("/{title}/follow")
    @ResponseBody
    private ResponseEntity<Object> followCommunity(@PathVariable String title, @RequestBody JWTTokenRequest request) {
        System.out.println(title);
        try {
            communitiesService.follow(title, request);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, "Success follow"));
        } catch (NotValidToken e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, e.getMessage()));
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, e.getMessage()));
        } catch (NotUniqueDataException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PostMapping("/{title}/unfollow")
    @ResponseBody
    private ResponseEntity<Object> unfollowCommunity(@PathVariable String title, @RequestBody JWTTokenRequest request) {
        System.out.println(title);
        try {
            communitiesService.unfollow(title, request);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, "Success unfollow"));
        } catch (NotValidToken e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, e.getMessage()));
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, e.getMessage()));
        } catch (NotUniqueDataException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/{title}/followers")
    @ResponseBody
    private ResponseEntity<Object> getFollowersCommunity(@PathVariable String title, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        try {
            Page<User> users = communitiesService.getFollowers(title, page, size);
            return ResponseEntity.status(HttpStatus.OK).body(users);
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/{title}/posts")
    @ResponseBody
    private ResponseEntity<Object> getPostsCommunity(@PathVariable String title, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        try {
            Page<Post> posts = communitiesService.getPosts(title, page, size);
            return ResponseEntity.status(HttpStatus.OK).body(posts);
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/{title}/topPosts")
    @ResponseBody
    private ResponseEntity<Object> getMostRatedPostsCommunity(@PathVariable String title, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        try {
            Page<MostRatedPostProjection> posts = communitiesService.getMostRatedPosts(title, page, size);
            return ResponseEntity.status(HttpStatus.OK).body(posts);
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, e.getMessage()));
        }
    }
}
