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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    @Autowired
    private AuthServiceImpl authService;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/register")
    @ResponseBody
    private ResponseEntity<Object> userRegistration(@RequestBody UserRegistrationRequest request) {
        try {
            logger.debug("User {} registration request received ", request.getLogin());
            User user = authService.registrationUser(request);
            String token = JWTService.generateToken(user);
            logger.info("User {} successfully registered", request.getLogin());
            return ResponseEntity.status(HttpStatus.CREATED).body(new UserAuthResponse(token));
        } catch (NotCorrectDataException e) {
            logger.warn("User {} registration failed due to incorrect data", request.getLogin());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, e.getMessage()));
        } catch (NotUniqueDataException e) {
            logger.warn("User {} registration failed due to duplicate data", request.getLogin());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            logger.error("Unexpected error during user registration: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PostMapping("/signin")
    @ResponseBody
    private ResponseEntity<Object> signin(@RequestBody UserSignInRequest request) {
        try {
            logger.debug("User {} sign in request received ", request.getLogin());
            User user = authService.signIn(request);
            String token = JWTService.generateToken(user);
            logger.info("User {} successfully sign in", request.getLogin());
            return ResponseEntity.status(HttpStatus.OK).body(new UserAuthResponse(token));
        } catch (DataNotFoundException e){
            logger.warn("User {} registration failed due to data not found", request.getLogin());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, "error"));
        } catch(WrongPasswordException e) {
            logger.warn("User {} registration failed due to wrong password", request.getLogin());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, "error"));
        }
    }
}
