package com.example.jeddit.servicies.impl;

import com.example.jeddit.exceptions.*;
import com.example.jeddit.models.entitys.Post;
import com.example.jeddit.models.entitys.User;
import com.example.jeddit.models.models.JWTTokenRequest;
import com.example.jeddit.models.models.users.UserAllInfoResponse;
import com.example.jeddit.models.models.users.UserBaseInfoPesponse;
import com.example.jeddit.models.models.users.UserChangePasswordRequest;
import com.example.jeddit.repositories.UserRepository;
import com.example.jeddit.servicies.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UsersServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTService jwtService;

    @Override
    public void changePassword(UserChangePasswordRequest request) throws NotValidToken, DataNotFoundException, WrongPasswordException, NotCorrectDataException, NotEnoughRightsException {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        if (!jwtService.validateToken(request.getJwttoken())) {
            throw new NotValidToken("Not valid token");
        }

        long userid = jwtService.extractUserId(request.getJwttoken());
        Optional<User> user = userRepository.findById(userid);

        if (user.isEmpty()) {
            throw new DataNotFoundException("User not found");
        }
        if (request.getNewPassword().length() < 5) {
            throw new NotCorrectDataException("Not correct data");
        }
        if (request.getNewPassword().length() > 255) {
            throw new NotCorrectDataException("Password length must be less then 200 characters");
        }
        if (!bCryptPasswordEncoder.matches(request.getOldPassword(), user.get().getPassword())) {
            throw new WrongPasswordException("Wrong password");
        }

        User newUser = user.get();
        newUser.setPassword(bCryptPasswordEncoder.encode(request.getNewPassword()));

        userRepository.save(newUser);
    }

    @Override
    public UserBaseInfoPesponse getBaseInfo(long id) throws DataNotFoundException {
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()) {
            throw new DataNotFoundException("User not found");
        }

        return new UserBaseInfoPesponse(user.get());
    }

    @Override
    public UserAllInfoResponse getAllInfo(JWTTokenRequest request, long id) throws DataNotFoundException, NotValidToken, NotEnoughRightsException {
        if (!jwtService.validateToken(request.getJwttoken())) {
            throw new NotValidToken("Not valid token");
        }

        Optional<User> user = userRepository.findById(jwtService.extractUserId(request.getJwttoken()));

        if (user.isEmpty()) {
            throw new DataNotFoundException("User not found");
        }
        if (Objects.equals(user.get().getRole(), "ADMIN") || user.get().getId() == id) {
            return new UserAllInfoResponse(user.get());
        } else {
            throw new NotEnoughRightsException("Not enough rights");
        }
    }

    @Override
    public void delete(JWTTokenRequest request, long id) throws DataNotFoundException, NotValidToken, NotEnoughRightsException {
        if (!jwtService.validateToken(request.getJwttoken())) {
            throw new NotValidToken("Not valid token");
        }

        Optional<User> user = userRepository.findById(jwtService.extractUserId(request.getJwttoken()));

        if (user.isEmpty()) {
            throw new DataNotFoundException("User not found");
        }
        if (Objects.equals(user.get().getRole(), "ADMIN") || user.get().getId() == id) {
            userRepository.delete(user.get());
        } else {
            throw new NotEnoughRightsException("Not enough rights");
        }
    }

    @Override
    public Page<Post> getPosts(long id, int page, int size) throws DataNotFoundException {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new DataNotFoundException("Community not found");
        }

        List<Post> allPosts = user.get().getPosts();
        int postCount = allPosts.size();

        int start = page * size;
        int end = Math.min((start + size), postCount);

        List<Post> postPage = allPosts.subList(start, end);

        return new PageImpl<>(postPage, PageRequest.of(page, size), postCount);
    }
}
