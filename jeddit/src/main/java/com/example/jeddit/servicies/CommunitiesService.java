package com.example.jeddit.servicies;

import com.example.jeddit.exceptions.*;
import com.example.jeddit.models.entitys.Post;
import com.example.jeddit.models.entitys.User;
import com.example.jeddit.models.models.JWTTokenRequest;
import com.example.jeddit.models.models.communities.CommunitiesCreateRequest;
import com.example.jeddit.models.models.communities.CommunityChangeDescriptionRequest;
import com.example.jeddit.models.models.communities.CommunityInfoResponse;
import com.example.jeddit.models.models.posts.MostRatedPostProjection;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CommunitiesService {

    void create(CommunitiesCreateRequest request) throws NotUniqueDataException, NotValidToken, NotCorrectDataException;

    CommunityInfoResponse get(String title) throws DataNotFoundException;

    void delete(String title, JWTTokenRequest request) throws DataNotFoundException, NotEnoughRightsException, NotValidToken;

    void changeDescription(String title, CommunityChangeDescriptionRequest request) throws DataNotFoundException, NotEnoughRightsException, NotCorrectDataException, NotValidToken;

    void follow(String title, JWTTokenRequest request) throws NotValidToken, DataNotFoundException, NotUniqueDataException;

    void unfollow(String title, JWTTokenRequest request) throws NotValidToken, DataNotFoundException, NotUniqueDataException;

    Page<User> getFollowers(String title, int page, int size) throws DataNotFoundException;

    Page<Post> getPosts(String title, int page, int size) throws DataNotFoundException;

    Page<MostRatedPostProjection> getMostRatedPosts(String title, int page, int size) throws DataNotFoundException;

}
