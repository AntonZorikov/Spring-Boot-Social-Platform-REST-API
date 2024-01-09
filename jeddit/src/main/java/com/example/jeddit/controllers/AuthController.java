package com.example.jeddit.controllers;

import com.example.jeddit.exceptions.DataNotFoundException;
import com.example.jeddit.exceptions.NotCorrectDataException;
import com.example.jeddit.exceptions.NotUniqueDataException;
import com.example.jeddit.exceptions.WrongPasswordException;
import com.example.jeddit.models.entitys.User;
import com.example.jeddit.models.models.ApiResponse;
import com.example.jeddit.models.models.auth.UserAuthResponse;
import com.example.jeddit.models.models.auth.UserRegistrationRequest;
import com.example.jeddit.models.models.auth.UserSignInRequest;
import com.example.jeddit.servicies.impl.AuthServiceImpl;
import com.example.jeddit.servicies.impl.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    @Autowired
    private AuthServiceImpl authService;

    @PostMapping("/register")
    @ResponseBody
    private ResponseEntity<Object> userRegistration(@RequestBody UserRegistrationRequest request) {
        try {
            User user = authService.registrationUser(request);
            String token = JWTService.generateToken(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(new UserAuthResponse(token));
        } catch (NotCorrectDataException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, e.getMessage()));
        } catch (NotUniqueDataException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PostMapping("/signin")
    @ResponseBody
    private ResponseEntity<Object> signin(@RequestBody UserSignInRequest request) {
        try {
            User user = authService.signIn(request);
            String token = JWTService.generateToken(user);
            return ResponseEntity.status(HttpStatus.OK).body(new UserAuthResponse(token));
        } catch (DataNotFoundException | WrongPasswordException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, "error"));
        }
    }
}
