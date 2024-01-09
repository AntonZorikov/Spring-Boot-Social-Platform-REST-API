package com.example.jeddit.servicies;

import com.example.jeddit.exceptions.*;
import com.example.jeddit.models.entitys.Post;
import com.example.jeddit.models.entitys.Vote;
import com.example.jeddit.models.models.JWTTokenRequest;
import com.example.jeddit.models.models.posts.PostCreateRequest;
import com.example.jeddit.models.models.posts.PostUpdateRequest;

public interface PostService {

    void create(PostCreateRequest request) throws NotValidToken, DataNotFoundException, NotCorrectDataException, NotEnoughRightsException;

    Post get(long id) throws DataNotFoundException;

    void update(long id, PostUpdateRequest request) throws DataNotFoundException, NotCorrectDataException, NotValidToken, NotEnoughRightsException;

    void delete(long id, JWTTokenRequest request) throws DataNotFoundException, NotCorrectDataException, NotValidToken, NotEnoughRightsException;

    void upvote(long id, JWTTokenRequest request) throws NotValidToken, DataNotFoundException, NotUniqueDataException;

    void downvote(long id, JWTTokenRequest request) throws NotValidToken, DataNotFoundException, NotUniqueDataException;

    Vote getVote(long id, JWTTokenRequest request) throws NotValidToken, DataNotFoundException, NotUniqueDataException;

    void deleteVote(long id, JWTTokenRequest request) throws NotValidToken, DataNotFoundException, NotUniqueDataException;

}
