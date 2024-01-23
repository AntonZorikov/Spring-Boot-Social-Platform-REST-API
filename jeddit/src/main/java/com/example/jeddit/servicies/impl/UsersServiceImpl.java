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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(CommentaryServiceImpl.class);

    @Override
    public void changePassword(UserChangePasswordRequest request) throws NotValidToken, DataNotFoundException, WrongPasswordException, NotCorrectDataException, NotEnoughRightsException {
        try {
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

            if (!jwtService.validateToken(request.getJwttoken())) {
                throw new NotValidToken("Not valid token");
            }

            long userId = jwtService.extractUserId(request.getJwttoken());
            Optional<User> user = userRepository.findById(userId);

            if (user.isEmpty()) {
                throw new DataNotFoundException("User not found");
            }
            if (request.getNewPassword().length() < 5) {
                throw new NotCorrectDataException("New password length must be at least 5 characters");
            }
            if (request.getNewPassword().length() > 255) {
                throw new NotCorrectDataException("New password length must be less than 255 characters");
            }
            if (!bCryptPasswordEncoder.matches(request.getOldPassword(), user.get().getPassword())) {
                throw new WrongPasswordException("Old password is incorrect");
            }

            User newUser = user.get();
            newUser.setPassword(bCryptPasswordEncoder.encode(request.getNewPassword()));

            userRepository.save(newUser);
        } catch (Exception e) {
            logger.error("Failed to change password", e);
            throw e;
        }
    }

    @Override
    public UserBaseInfoPesponse getBaseInfo(long id) throws DataNotFoundException {
        try {
            Optional<User> user = userRepository.findById(id);

            if (user.isEmpty()) {
                throw new DataNotFoundException("User not found");
            }

            return new UserBaseInfoPesponse(user.get());
        } catch (Exception e) {
            logger.error("Failed to get base user info", e);
            throw e;
        }
    }

    @Override
    public UserAllInfoResponse getAllInfo(JWTTokenRequest request, long id) throws DataNotFoundException, NotValidToken, NotEnoughRightsException {
        try {
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
        } catch (Exception e) {
            logger.error("Failed to get all user info", e);
            throw e;
        }
    }

    @Override
    public void delete(JWTTokenRequest request, long id) throws DataNotFoundException, NotValidToken, NotEnoughRightsException {
        try {
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
        } catch (Exception e) {
            logger.error("Failed to delete user", e);
            throw e;
        }
    }

    @Override
    public Page<Post> getPosts(long id, int page, int size) throws DataNotFoundException {
        try {
            Optional<User> user = userRepository.findById(id);
            if (user.isEmpty()) {
                throw new DataNotFoundException("User not found");
            }

            List<Post> allPosts = user.get().getPosts();
            int postCount = allPosts.size();

            int start = page * size;
            int end = Math.min((start + size), postCount);

            List<Post> postPage = allPosts.subList(start, end);

            return new PageImpl<>(postPage, PageRequest.of(page, size), postCount);
        } catch (Exception e) {
            logger.error("Failed to get user posts", e);
            throw e;
        }
    }

}
