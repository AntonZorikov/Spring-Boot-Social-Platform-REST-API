package com.example.jeddit.controllers;

import com.example.jeddit.models.entitys.Community;
import com.example.jeddit.models.entitys.Post;
import com.example.jeddit.models.entitys.User;
import com.example.jeddit.models.models.SearchRequest;
import com.example.jeddit.servicies.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping("/users")
    private ResponseEntity<Object> searchUsers(@RequestParam(defaultValue = "0") int from, @RequestParam(defaultValue = "10") int to, @RequestBody SearchRequest request) {
        List<User> users = searchService.searchUser(request, from, to);
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @GetMapping("/communities")
    private ResponseEntity<Object> searchCommunities(@RequestParam(defaultValue = "0") int from, @RequestParam(defaultValue = "10") int to, @RequestBody SearchRequest request) {
        List<Community> communities = searchService.searchCommunity(request, from, to);
        return ResponseEntity.status(HttpStatus.OK).body(communities);
    }

    @GetMapping("/posts")
    private ResponseEntity<Object> searchPosts(@RequestParam(defaultValue = "0") int from, @RequestParam(defaultValue = "10") int to, @RequestBody SearchRequest request) {
        List<Post> posts = searchService.searchPost(request, from, to);
        return ResponseEntity.status(HttpStatus.OK).body(posts);
    }

}
