package com.example.jeddit.controllers;

import com.example.jeddit.models.entitys.Community;
import com.example.jeddit.models.entitys.Post;
import com.example.jeddit.models.entitys.User;
import com.example.jeddit.models.models.SearchRequest;
import com.example.jeddit.servicies.impl.SearchServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(SearchController.class);

    @GetMapping("/users")
    private ResponseEntity<Object> searchUsers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestBody SearchRequest request) {
        Page<User> users = searchService.searchUser(request, page, size);
        logger.info("Successfully retrieved users based on search criteria");
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @GetMapping("/communities")
    private ResponseEntity<Object> searchCommunities(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestBody SearchRequest request) {
        Page<Community> communities = searchService.searchCommunity(request, page, size);
        logger.info("Successfully retrieved communities based on search criteria");
        return ResponseEntity.status(HttpStatus.OK).body(communities);
    }

    @GetMapping("/communitiesByDescription")
    private ResponseEntity<Object> searchCommunitiesByDescription(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestBody SearchRequest request) {
        Page<Community> communities = searchService.searchCommunityByDescription(request, page, size);
        logger.info("Successfully retrieved communities based on search criteria (by description)");
        return ResponseEntity.status(HttpStatus.OK).body(communities);
    }

    @GetMapping("/postsByText")
    private ResponseEntity<Object> searchPostsByText(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestBody SearchRequest request) {
        Page<Post> posts = searchService.searchPostByText(request, page, size);
        logger.info("Successfully retrieved posts based on search criteria (by text)");
        return ResponseEntity.status(HttpStatus.OK).body(posts);
    }
}
