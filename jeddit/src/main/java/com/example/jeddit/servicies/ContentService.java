package com.example.jeddit.servicies;

import com.example.jeddit.exceptions.DataNotFoundException;
import com.example.jeddit.exceptions.NotValidToken;
import com.example.jeddit.models.entitys.Post;
import com.example.jeddit.models.models.JWTTokenRequest;

import java.util.List;

public interface ContentService {

    public List<Post> getUserNewsline(JWTTokenRequest request, int from, int to) throws NotValidToken, DataNotFoundException;

}
