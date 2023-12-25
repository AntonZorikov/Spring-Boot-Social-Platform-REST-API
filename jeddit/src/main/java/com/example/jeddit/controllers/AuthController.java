package com.example.jeddit.controllers;

import com.example.jeddit.exceptions.NotCorrectDataException;
import com.example.jeddit.exceptions.NotUniqueDataException;
import com.example.jeddit.models.entitys.User;
import com.example.jeddit.models.models.StandartResponse;
import com.example.jeddit.models.models.UserRegistrationRequest;
import com.example.jeddit.models.models.UserAuthResponse;
import com.example.jeddit.models.models.UserSignInRequest;
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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StandartResponse(true, e.getMessage()));
        } catch (NotUniqueDataException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new StandartResponse(true, e.getMessage()));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new StandartResponse(true, e.getMessage()));
        }
    }

    @PostMapping("/signin")
    @ResponseBody
    private ResponseEntity<Object> signin(@RequestBody UserSignInRequest request){
        try {
            User user = authService.signIn(request);
            String token = JWTService.generateToken(user);
            return ResponseEntity.status(HttpStatus.OK).body(new UserAuthResponse(token));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new StandartResponse(true, e.getMessage()));
        }
    }
}
