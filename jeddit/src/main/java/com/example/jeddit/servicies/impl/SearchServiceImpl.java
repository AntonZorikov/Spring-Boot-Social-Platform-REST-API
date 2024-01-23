package com.example.jeddit.servicies.impl;

import com.example.jeddit.models.entitys.Community;
import com.example.jeddit.models.entitys.Post;
import com.example.jeddit.models.entitys.User;
import com.example.jeddit.models.models.SearchRequest;
import com.example.jeddit.repositories.CommunitiesRepository;
import com.example.jeddit.repositories.PostRepository;
import com.example.jeddit.repositories.UserRepository;
import com.example.jeddit.servicies.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private CommunitiesRepository communitiesRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(SearchServiceImpl.class);

    @Override
    public Page<User> searchUser(SearchRequest request, int page, int size) {
        try {
            return userRepository.findByLoginContaining(request.getText(), PageRequest.of(page, size));
        } catch (Exception e) {
            logger.error("Failed to search users", e);
            throw e;
        }
    }

    @Override
    public Page<Community> searchCommunity(SearchRequest request, int page, int size) {
        try {
            return communitiesRepository.findByTitleContaining(request.getText(), PageRequest.of(page, size));
        } catch (Exception e) {
            logger.error("Failed to search communities", e);
            throw e;
        }
    }

    @Override
    public Page<Community> searchCommunityByDescription(SearchRequest request, int page, int size) {
        try {
            return communitiesRepository.findByDescriptionContaining(request.getText(), PageRequest.of(page, size));
        } catch (Exception e) {
            logger.error("Failed to search communities by description", e);
            throw e;
        }
    }

    @Override
    public Page<Post> searchPost(SearchRequest request, int page, int size) {
        try {
            return postRepository.findByTitleContaining(request.getText(), PageRequest.of(page, size));
        } catch (Exception e) {
            logger.error("Failed to search posts", e);
            throw e;
        }
    }

    @Override
    public Page<Post> searchPostByText(SearchRequest request, int page, int size) {
        try {
            return postRepository.findByTextContaining(request.getText(), PageRequest.of(page, size));
        } catch (Exception e) {
            logger.error("Failed to search posts by text", e);
            throw e;
        }
    }

}
