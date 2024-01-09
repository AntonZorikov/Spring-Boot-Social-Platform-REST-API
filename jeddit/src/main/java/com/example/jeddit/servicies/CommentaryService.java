package com.example.jeddit.servicies;

import com.example.jeddit.exceptions.DataNotFoundException;
import com.example.jeddit.exceptions.NotCorrectDataException;
import com.example.jeddit.exceptions.NotEnoughRightsException;
import com.example.jeddit.exceptions.NotValidToken;
import com.example.jeddit.models.entitys.Commentary;
import com.example.jeddit.models.models.CommentaryCreateRequest;
import com.example.jeddit.models.models.JWTTokenRequest;

public interface CommentaryService {

    void create(long postId, CommentaryCreateRequest request) throws NotValidToken, DataNotFoundException, NotCorrectDataException;

    Commentary get(long id) throws DataNotFoundException;

    void delete(long id, JWTTokenRequest request) throws DataNotFoundException, NotValidToken, NotEnoughRightsException;

}


