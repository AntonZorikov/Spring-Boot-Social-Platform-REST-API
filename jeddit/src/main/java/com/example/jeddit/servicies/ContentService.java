package com.example.jeddit.servicies;

import com.example.jeddit.exceptions.DataNotFoundException;
import com.example.jeddit.exceptions.NotValidToken;
import com.example.jeddit.models.entitys.Community;
import com.example.jeddit.models.entitys.Post;
import com.example.jeddit.models.entitys.User;
import com.example.jeddit.models.models.JWTTokenRequest;
import com.example.jeddit.repositories.CommunitiesRepository;
import com.example.jeddit.repositories.PostRepository;
import com.example.jeddit.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ContentService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTService jwtService;

    public List<Post> getUserNewsline(JWTTokenRequest request, int from, int to) throws NotValidToken, DataNotFoundException {
        if (!jwtService.validateToken(request.getJwttoken())) {
            throw new NotValidToken("Not valid token");
        }
        Optional<User> user = userRepository.findById(jwtService.extractUserId(request.getJwttoken()));
        if (user.isEmpty()) {
            throw new DataNotFoundException("User not found");
        }
        List<Long> communityId = user.get().getCommunities().stream().map(Community::getId).collect(Collectors.toList());
        return postRepository.findPostsByCommunitiesAndTimeRange(communityId, from, to - from);
    }

}
