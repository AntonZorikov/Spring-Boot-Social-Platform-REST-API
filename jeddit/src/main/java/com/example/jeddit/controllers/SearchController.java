package com.example.jeddit.controllers;

import com.example.jeddit.models.entitys.Community;
import com.example.jeddit.models.entitys.Post;
import com.example.jeddit.models.entitys.User;
import com.example.jeddit.models.models.SearchRequest;
import com.example.jeddit.servicies.impl.SearchServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/search")
public class SearchController {

    @Autowired
    private SearchServiceImpl searchService;

    @GetMapping("/users")
    private ResponseEntity<Object> searchUsers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestBody SearchRequest request) {
        Page<User> users = searchService.searchUser(request, page, size);
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @GetMapping("/communities")
    private ResponseEntity<Object> searchCommunities(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestBody SearchRequest request) {
        Page<Community> communities = searchService.searchCommunity(request, page, size);
        return ResponseEntity.status(HttpStatus.OK).body(communities);
    }

    @GetMapping("/communitiesByDescription")
    private ResponseEntity<Object> searchCommunitiesByDescription(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestBody SearchRequest request) {
        Page<Community> communities = searchService.searchCommunityByDescription(request, page, size);
        return ResponseEntity.status(HttpStatus.OK).body(communities);
    }

    @GetMapping("/postsByText")
    private ResponseEntity<Object> searchPostsByText(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestBody SearchRequest request) {
        Page<Post> posts = searchService.searchPostByText(request, page, size);
        return ResponseEntity.status(HttpStatus.OK).body(posts);
    }

}
