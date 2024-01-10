package com.example.jeddit.servicies;

import com.example.jeddit.exceptions.*;
import com.example.jeddit.models.entitys.Post;
import com.example.jeddit.models.models.JWTTokenRequest;
import com.example.jeddit.models.models.users.UserAllInfoResponse;
import com.example.jeddit.models.models.users.UserBaseInfoPesponse;
import com.example.jeddit.models.models.users.UserChangePasswordRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {

    void changePassword(UserChangePasswordRequest request) throws NotValidToken, DataNotFoundException, WrongPasswordException, NotCorrectDataException, NotEnoughRightsException;

    UserBaseInfoPesponse getBaseInfo(long id) throws DataNotFoundException;

    UserAllInfoResponse getAllInfo(JWTTokenRequest request, long id) throws DataNotFoundException, NotValidToken, NotEnoughRightsException;

    void delete(JWTTokenRequest request, long id) throws DataNotFoundException, NotValidToken, NotEnoughRightsException;

    Page<Post> getPosts(long id, int from, int to) throws DataNotFoundException;

}
