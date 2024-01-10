package com.example.jeddit.servicies.impl;

import com.example.jeddit.exceptions.*;
import com.example.jeddit.models.entitys.Community;
import com.example.jeddit.models.entitys.Post;
import com.example.jeddit.models.entitys.User;
import com.example.jeddit.models.models.JWTTokenRequest;
import com.example.jeddit.models.models.communities.CommunitiesCreateRequest;
import com.example.jeddit.models.models.communities.CommunityChangeDescriptionRequest;
import com.example.jeddit.models.models.communities.CommunityInfoResponse;
import com.example.jeddit.repositories.CommunitiesRepository;
import com.example.jeddit.repositories.UserRepository;
import com.example.jeddit.servicies.CommunitiesService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CommunitiesServiceImpl implements CommunitiesService {

    @Autowired
    private CommunitiesRepository communitiesRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTService jwtService;

    @Override
    @Transactional
    public void create(CommunitiesCreateRequest request) throws NotUniqueDataException, NotValidToken, NotCorrectDataException {
        Optional<Community> community = communitiesRepository.findByTitle(request.getTitle());

        if (!jwtService.validateToken(request.getJwttoken())) {
            throw new NotValidToken("Not valid token");
        }
        if (community.isPresent()) {
            throw new NotUniqueDataException("Not unique community title");
        }
        if (request.getDescription().length() > 200) {
            throw new NotCorrectDataException("Description length must be less then 200 characters");
        }
        if (request.getTitle().length() > 50) {
            throw new NotCorrectDataException("Title length must be less then 200 characters");
        }
        if (request.getTitle().length() < 3) {
            throw new NotCorrectDataException("Title length must be more then 2 characters");
        }
        if (request.getTitle().split(" ").length != 1) {
            throw new NotCorrectDataException("Title must contains only one word");
        }

        User owner = userRepository.findById(jwtService.extractUserId(request.getJwttoken())).get();
        owner.setRole("MODERATOR");

        Community newCommunity = new Community(request.getTitle(), request.getDescription(), owner);
        newCommunity.getModerators().add(owner);
        owner.getModeratedCommunities().add(newCommunity);

        communitiesRepository.save(newCommunity);
    }

    @Override
    public CommunityInfoResponse get(String title) throws DataNotFoundException {
        Optional<Community> community = communitiesRepository.findByTitle(title);

        if (community.isEmpty()) {
            throw new DataNotFoundException("Community not found");
        }

        return new CommunityInfoResponse(community.get());
    }

    @Override
    public void delete(String title, JWTTokenRequest request) throws DataNotFoundException, NotEnoughRightsException, NotValidToken {
        Optional<Community> community = communitiesRepository.findByTitle(title);

        if (community.isEmpty()) {
            throw new DataNotFoundException("Community not found");
        }
        if (!jwtService.validateToken(request.getJwttoken())) {
            throw new NotValidToken("Not valid token");
        }
        if (community.get().getOwner().getId() != jwtService.extractUserId(request.getJwttoken())) {
            throw new NotEnoughRightsException("Not enough rights exception");
        }

        communitiesRepository.delete(community.get());
    }

    @Override
    public void changeDescription(String title, CommunityChangeDescriptionRequest request) throws DataNotFoundException, NotEnoughRightsException, NotCorrectDataException, NotValidToken {
        Optional<Community> community = communitiesRepository.findByTitle(title);

        if (community.isEmpty()) {
            throw new DataNotFoundException("Community not found");
        }
        if (!jwtService.validateToken(request.getJwttoken())) {
            throw new NotValidToken("Not valid token");
        }
        if (community.get().getOwner().getId() != jwtService.extractUserId(request.getJwttoken())) {
            throw new NotEnoughRightsException("Not enough rights exception");
        }
        if (request.getNewDescription().length() > 200) {
            throw new NotCorrectDataException("Description length must be less then 200 characters");
        }

        community.get().setDescription(request.getNewDescription());

        communitiesRepository.save(community.get());
    }

    @Override
    @Transactional
    public void follow(String title, JWTTokenRequest request) throws NotValidToken, DataNotFoundException, NotUniqueDataException {
        Optional<Community> community = communitiesRepository.findByTitle(title);
        Optional<User> user = userRepository.findById(jwtService.extractUserId(request.getJwttoken()));

        if (!jwtService.validateToken(request.getJwttoken())) {
            throw new NotValidToken("Not valid token");
        }
        if (community.isEmpty()) {
            throw new DataNotFoundException("Community not found");
        }
        if (user.isEmpty()) {
            throw new DataNotFoundException("User not found");
        }
        if (community.get().getFollowers().contains(user.get())) {
            throw new NotUniqueDataException("You are already follow");
        }

        community.get().getFollowers().add(user.get());
        user.get().getCommunities().add(community.get());

        communitiesRepository.save(community.get());
    }

    @Override
    @Transactional
    public void unfollow(String title, JWTTokenRequest request) throws NotValidToken, DataNotFoundException, NotUniqueDataException {
        if (!jwtService.validateToken(request.getJwttoken())) {
            throw new NotValidToken("Not valid token");
        }

        Optional<Community> community = communitiesRepository.findByTitle(title);
        Optional<User> user = userRepository.findById(jwtService.extractUserId(request.getJwttoken()));

        if (community.isEmpty()) {
            throw new DataNotFoundException("Community not found");
        }
        if (user.isEmpty()) {
            throw new DataNotFoundException("User not found");
        }
        if (!community.get().getFollowers().contains(user.get())) {
            throw new NotUniqueDataException("You are already unfollow");
        }

        community.get().getFollowers().remove(user.get());
        user.get().getCommunities().remove(community.get());

        communitiesRepository.save(community.get());
    }

    public Page<User> getFollowers(String title, int page, int size) throws DataNotFoundException {
        Optional<Community> community = communitiesRepository.findByTitle(title);

        if (community.isEmpty()) {
            throw new DataNotFoundException("Community not found");
        }

        List<User> allFollowers = community.get().getFollowers();
        int totalFollowers = allFollowers.size();

        int start = page * size;
        int end = Math.min((start + size), totalFollowers);

        List<User> followersForPage = allFollowers.subList(start, end);

        return new PageImpl<>(followersForPage, PageRequest.of(page, size), totalFollowers);
    }

    @Override
    public Page<Post> getPosts(String title, int page, int size) throws DataNotFoundException {
        Optional<Community> community = communitiesRepository.findByTitle(title);

        if (community.isEmpty()) {
            throw new DataNotFoundException("Community not found");
        }

        List<Post> allPosts = community.get().getPosts();
        int postCount = allPosts.size();

        int start = page * size;
        int end = Math.min((start + size), postCount);

        List<Post> followersForPage = allPosts.subList(start, end);

        return new PageImpl<>(followersForPage, PageRequest.of(page, size), postCount);
    }
}
