package com.example.jeddit.servicies.impl;

import com.example.jeddit.models.entitys.Community;
import com.example.jeddit.models.entitys.Post;
import com.example.jeddit.models.entitys.User;
import com.example.jeddit.models.models.SearchRequest;
import com.example.jeddit.repositories.CommunitiesRepository;
import com.example.jeddit.repositories.PostRepository;
import com.example.jeddit.repositories.UserRepository;
import com.example.jeddit.servicies.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private CommunitiesRepository communitiesRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Page<User> searchUser(SearchRequest request, int page, int size) {
        return userRepository.findByLoginContaining(request.getText(), PageRequest.of(page, size));
    }

    @Override
    public Page<Community> searchCommunity(SearchRequest request, int page, int size) {
        return communitiesRepository.findByTitleContaining(request.getText(), PageRequest.of(page, size));
    }

    @Override
    public Page<Post> searchPost(SearchRequest request, int page, int size) {
        return postRepository.findByTitleContaining(request.getText(), PageRequest.of(page, size));
    }
}
