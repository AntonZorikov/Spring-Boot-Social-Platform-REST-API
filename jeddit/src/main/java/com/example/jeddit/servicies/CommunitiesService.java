package com.example.jeddit.servicies;

import com.example.jeddit.exceptions.*;
import com.example.jeddit.models.entitys.Community;
import com.example.jeddit.models.models.JWTTokenRequest;
import com.example.jeddit.models.models.communities.CommunitiesCreateRequest;
import com.example.jeddit.models.models.communities.CommunityChangeDescriptionRequest;
import com.example.jeddit.models.models.communities.CommunityInfoResponse;
import com.example.jeddit.repositories.CommunitiesRepository;
import com.example.jeddit.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    }

    public void changeDescription(String title, CommunityChangeDescriptionRequest request) throws DataNotFoundException, NotEnoughRightsException, NotCorrectDataException, NotValidToken {
        Optional<Community> community = communitiesRepository.findByTitle(title);
        if (community.isEmpty()) {
            throw new DataNotFoundException("Community not found");
        }
        if (!jwtService.validateToken(request.getJwttoken())) {
            throw new NotValidToken("Not valid token");
        }
        if(community.get().getOwner().getId() != jwtService.extractUserId(request.getJwttoken())){
            throw new NotEnoughRightsException("Not enough rights exception");
        }
        if(request.getNewDescription().length() > 200){
            throw new NotCorrectDataException("Description length must be less then 200 characters");
        }
    }

}
