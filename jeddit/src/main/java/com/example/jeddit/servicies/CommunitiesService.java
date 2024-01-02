package com.example.jeddit.servicies;

import com.example.jeddit.exceptions.*;
import com.example.jeddit.models.entitys.Community;
import com.example.jeddit.models.entitys.User;
import com.example.jeddit.models.models.JWTTokenRequest;
import com.example.jeddit.models.models.communities.CommunitiesCreateRequest;
import com.example.jeddit.models.models.communities.CommunityChangeDescriptionRequest;
import com.example.jeddit.models.models.communities.CommunityInfoResponse;
import com.example.jeddit.repositories.CommunitiesRepository;
import com.example.jeddit.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class CommunitiesService {

    @Autowired
    private CommunitiesRepository communitiesRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTService jwtService;

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
        Community newCommunity = new Community(request.getTitle(), request.getDescription(), userRepository.findById(jwtService.extractUserId(request.getJwttoken())).get());
        communitiesRepository.save(newCommunity);
    }

    public CommunityInfoResponse get(String title) throws DataNotFoundException {
        Optional<Community> community = communitiesRepository.findByTitle(title);
        if (community.isEmpty()) {
            throw new DataNotFoundException("Community not found");
        }
        return new CommunityInfoResponse(community.get());
    }

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

    @Transactional
    public void unfollow(String title, JWTTokenRequest request) throws NotValidToken, DataNotFoundException, NotUniqueDataException {
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
        if (!community.get().getFollowers().contains(user.get())) {
            throw new NotUniqueDataException("You are already unfollow");
        }
        community.get().getFollowers().remove(user.get());
        user.get().getCommunities().remove(community.get());
        communitiesRepository.save(community.get());
    }

    public List<User> getFollowers(String title, int from, int to) throws DataNotFoundException {
        Optional<Community> community = communitiesRepository.findByTitle(title);
        if (community.isEmpty()) {
            throw new DataNotFoundException("Community not found");
        }

        List<User> allFollowers = community.get().getFollowers();
        int followersCount = allFollowers.size();

        if (from >= followersCount || to <= 0 || from >= to) {
            return new ArrayList<>();
        }

        from = Math.max(0, from);
        to = Math.min(followersCount, to);

        return allFollowers.subList(from, to);
    }
}
