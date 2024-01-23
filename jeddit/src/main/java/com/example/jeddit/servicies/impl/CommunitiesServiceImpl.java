package com.example.jeddit.servicies.impl;

import com.example.jeddit.exceptions.*;
import com.example.jeddit.models.entitys.Community;
import com.example.jeddit.models.entitys.Post;
import com.example.jeddit.models.entitys.User;
import com.example.jeddit.models.models.JWTTokenRequest;
import com.example.jeddit.models.models.communities.CommunitiesCreateRequest;
import com.example.jeddit.models.models.communities.CommunityChangeDescriptionRequest;
import com.example.jeddit.models.models.communities.CommunityInfoResponse;
import com.example.jeddit.models.models.posts.MostRatedPostProjection;
import com.example.jeddit.repositories.CommunitiesRepository;
import com.example.jeddit.repositories.UserRepository;
import com.example.jeddit.servicies.CommunitiesService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

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

    private static final Logger logger = LoggerFactory.getLogger(CommunitiesServiceImpl.class);

    @Override
    @Transactional
    public void create(CommunitiesCreateRequest request) throws NotUniqueDataException, NotValidToken, NotCorrectDataException {
        if (!jwtService.validateToken(request.getJwttoken())) {
            logger.warn("Failed to create community: Not valid token");
            throw new NotValidToken("Not valid token");
        }

        Optional<Community> community = communitiesRepository.findByTitle(request.getTitle());

        if (community.isPresent()) {
            logger.warn("Failed to create community: Not unique community title");
            throw new NotUniqueDataException("Not unique community title");
        }

        if (request.getDescription().length() > 200) {
            logger.warn("Failed to create community: Description length must be less than 200 characters");
            throw new NotCorrectDataException("Description length must be less than 200 characters");
        }
        if (request.getTitle().length() > 50) {
            logger.warn("Failed to create community: Title length must be less than 50 characters");
            throw new NotCorrectDataException("Title length must be less than 50 characters");
        }
        if (request.getTitle().length() < 3) {
            logger.warn("Failed to create community: Title length must be more than 2 characters");
            throw new NotCorrectDataException("Title length must be more than 2 characters");
        }
        if (request.getTitle().split(" ").length != 1) {
            logger.warn("Failed to create community: Title must contain only one word");
            throw new NotCorrectDataException("Title must contain only one word");
        }

        User owner = userRepository.findById(jwtService.extractUserId(request.getJwttoken())).get();
        owner.setRole("MODERATOR");

        Community newCommunity = new Community(request.getTitle(), request.getDescription(), owner);
        newCommunity.getModerators().add(owner);
        owner.getModeratedCommunities().add(newCommunity);

        communitiesRepository.save(newCommunity);
        logger.info("Community created successfully");
    }

    @Override
    public CommunityInfoResponse get(String title) throws DataNotFoundException {
        Optional<Community> community = communitiesRepository.findByTitle(title);

        if (community.isEmpty()) {
            logger.warn("Failed to get community: Community not found");
            throw new DataNotFoundException("Community not found");
        }

        return new CommunityInfoResponse(community.get());
    }

    @Override
    public void delete(String title, JWTTokenRequest request) throws DataNotFoundException, NotEnoughRightsException, NotValidToken {
        Optional<Community> community = communitiesRepository.findByTitle(title);

        if (community.isEmpty()) {
            logger.warn("Failed to delete community: Community not found");
            throw new DataNotFoundException("Community not found");
        }
        if (!jwtService.validateToken(request.getJwttoken())) {
            logger.warn("Failed to delete community: Not valid token");
            throw new NotValidToken("Not valid token");
        }
        if (community.get().getOwner().getId() != jwtService.extractUserId(request.getJwttoken())) {
            logger.warn("Failed to delete community: Not enough rights exception");
            throw new NotEnoughRightsException("Not enough rights exception");
        }

        communitiesRepository.delete(community.get());
        logger.info("Community deleted successfully");
    }

    @Override
    public void changeDescription(String title, CommunityChangeDescriptionRequest request) throws DataNotFoundException, NotEnoughRightsException, NotCorrectDataException, NotValidToken {
        Optional<Community> community = communitiesRepository.findByTitle(title);

        if (community.isEmpty()) {
            logger.warn("Failed to change community description: Community not found");
            throw new DataNotFoundException("Community not found");
        }
        if (!jwtService.validateToken(request.getJwttoken())) {
            logger.warn("Failed to change community description: Not valid token");
            throw new NotValidToken("Not valid token");
        }
        if (community.get().getOwner().getId() != jwtService.extractUserId(request.getJwttoken())) {
            logger.warn("Failed to change community description: Not enough rights exception");
            throw new NotEnoughRightsException("Not enough rights exception");
        }
        if (request.getNewDescription().length() > 200) {
            logger.warn("Failed to change community description: Description length must be less than 200 characters");
            throw new NotCorrectDataException("Description length must be less than 200 characters");
        }

        community.get().setDescription(request.getNewDescription());
        communitiesRepository.save(community.get());
        logger.info("Community description changed successfully");
    }

    @Override
    @Transactional
    public void follow(String title, JWTTokenRequest request) throws NotValidToken, DataNotFoundException, NotUniqueDataException {
        Optional<Community> community = communitiesRepository.findByTitle(title);
        Optional<User> user = userRepository.findById(jwtService.extractUserId(request.getJwttoken()));

        if (!jwtService.validateToken(request.getJwttoken())) {
            logger.warn("Failed to follow community: Not valid token");
            throw new NotValidToken("Not valid token");
        }
        if (community.isEmpty()) {
            logger.warn("Failed to follow community: Community not found");
            throw new DataNotFoundException("Community not found");
        }
        if (user.isEmpty()) {
            logger.warn("Failed to follow community: User not found");
            throw new DataNotFoundException("User not found");
        }
        if (community.get().getFollowers().contains(user.get())) {
            logger.warn("Failed to follow community: You are already following");
            throw new NotUniqueDataException("You are already following");
        }

        community.get().getFollowers().add(user.get());
        user.get().getCommunities().add(community.get());

        communitiesRepository.save(community.get());
        logger.info("User followed community successfully");
    }

    @Override
    @Transactional
    public void unfollow(String title, JWTTokenRequest request) throws NotValidToken, DataNotFoundException, NotUniqueDataException {
        if (!jwtService.validateToken(request.getJwttoken())) {
            logger.warn("Failed to unfollow community: Not valid token");
            throw new NotValidToken("Not valid token");
        }

        Optional<Community> community = communitiesRepository.findByTitle(title);
        Optional<User> user = userRepository.findById(jwtService.extractUserId(request.getJwttoken()));

        if (community.isEmpty()) {
            logger.warn("Failed to unfollow community: Community not found");
            throw new DataNotFoundException("Community not found");
        }
        if (user.isEmpty()) {
            logger.warn("Failed to unfollow community: User not found");
            throw new DataNotFoundException("User not found");
        }
        if (!community.get().getFollowers().contains(user.get())) {
            logger.warn("Failed to unfollow community: You are already unfollowing");
            throw new NotUniqueDataException("You are already unfollowing");
        }

        community.get().getFollowers().remove(user.get());
        user.get().getCommunities().remove(community.get());

        communitiesRepository.save(community.get());
        logger.info("User unfollowed community successfully");
    }

    @Override
    public Page<User> getFollowers(String title, int page, int size) throws DataNotFoundException {
        Optional<Community> community = communitiesRepository.findByTitle(title);

        if (community.isEmpty()) {
            logger.warn("Failed to get followers: Community not found");
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
            logger.warn("Failed to get posts: Community not found");
            throw new DataNotFoundException("Community not found");
        }

        List<Post> allPosts = community.get().getPosts();
        int postCount = allPosts.size();

        int start = page * size;
        int end = Math.min((start + size), postCount);

        List<Post> postsForPage = allPosts.subList(start, end);

        return new PageImpl<>(postsForPage, PageRequest.of(page, size), postCount);
    }

    @Override
    public Page<MostRatedPostProjection> getMostRatedPosts(String title, int page, int size) throws DataNotFoundException {
        Optional<Community> community = communitiesRepository.findByTitle(title);

        if (community.isEmpty()) {
            logger.warn("Failed to get most rated posts: Community not found");
            throw new DataNotFoundException("Community not found");
        }

        return communitiesRepository.findMostRatedPostsByCommunityId(community.get().getId(), PageRequest.of(page, size));
    }
}
