package com.example.jeddit.servicies;

import com.example.jeddit.exceptions.DataNotFoundException;
import com.example.jeddit.exceptions.NotUniqueDataException;
import com.example.jeddit.exceptions.NotValidToken;
import com.example.jeddit.models.entitys.Community;
import com.example.jeddit.models.models.communities.CommunitiesCreateRequest;
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

    public void createCommunity(CommunitiesCreateRequest request) throws NotUniqueDataException, NotValidToken {
        Optional<Community> community = communitiesRepository.findByTitle(request.getTitle());
        if(!jwtService.validateToken(request.getJwttoken())){
            throw new NotValidToken("Not valid token");
        }
        if(community.isPresent()){
            throw new NotUniqueDataException("Not unique community title");
        }
        Community newCommunity = new Community(request.getTitle(), request.getDescription(), userRepository.findById(jwtService.extractUserId(request.getJwttoken())).get());
        communitiesRepository.save(newCommunity);
    }

    public CommunityInfoResponse getCommunity(String title) throws DataNotFoundException {
        Optional<Community> community = communitiesRepository.findByTitle(title);
        if(community.isEmpty()){
            throw new DataNotFoundException("Community not found");
        }
        return new CommunityInfoResponse(community.get());
    }
}
