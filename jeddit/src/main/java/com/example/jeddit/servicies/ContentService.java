package com.example.jeddit.servicies;

import com.example.jeddit.exceptions.DataNotFoundException;
import com.example.jeddit.exceptions.NotValidToken;
import com.example.jeddit.models.entitys.Post;
import com.example.jeddit.models.models.JWTTokenRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ContentService {

    Page<Post> getUserNewsline(JWTTokenRequest request, int page, int size) throws NotValidToken, DataNotFoundException;

}
