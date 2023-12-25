package com.example.jeddit.controllers;

import com.example.jeddit.exceptions.NotCorrectDataException;
import com.example.jeddit.exceptions.NotUniqueDataException;
import com.example.jeddit.exceptions.UserNotFoundException;
import com.example.jeddit.exceptions.WrongPasswordException;
import com.example.jeddit.models.entitys.User;
import com.example.jeddit.models.models.*;
import com.example.jeddit.servicies.AuthService;
import com.example.jeddit.servicies.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    @ResponseBody
    private ResponseEntity<Object> register(@RequestBody UserRegistrationRequest request){
        try {
            User user = authService.registrationUser(request);
            String token = JWTService.generateToken(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(new UserAuthResponse(token));
        } catch (NotCorrectDataException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StandardResponse(false, new ErrorModel(400, "BAD_REQUEST", e.getMessage()), "error"));
        } catch (NotUniqueDataException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new StandardResponse(false, new ErrorModel(409, "CONFLICT", e.getMessage()), "error"));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new StandardResponse(false, new ErrorModel(500, "INTERNAL_SERVER_ERROR", e.getMessage()), "error"));
        }
    }

    @PostMapping("/signin")
    @ResponseBody
    private ResponseEntity<Object> signin(@RequestBody UserSignInRequest request){
        try {
            User user = authService.signIn(request);
            String token = JWTService.generateToken(user);
            return ResponseEntity.status(HttpStatus.OK).body(new UserAuthResponse(token));
        } catch (UserNotFoundException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new StandardResponse(false, new ErrorModel(401, "UNAUTHORIZED", e.getMessage()), "error"));
        } catch (WrongPasswordException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new StandardResponse(false, new ErrorModel(401, "UNAUTHORIZED", e.getMessage()), "error"));
        }
    }
}
