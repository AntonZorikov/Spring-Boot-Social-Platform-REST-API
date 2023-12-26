package com.example.jeddit.controllers;

import com.example.jeddit.exceptions.*;
import com.example.jeddit.models.models.ErrorModel;
import com.example.jeddit.models.models.StandardResponse;
import com.example.jeddit.models.models.users.*;
import com.example.jeddit.servicies.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users")
public class UsersController {

    @Autowired
    private UsersService usersService;

    @PutMapping("/change_password")
    @ResponseBody
    private ResponseEntity<Object> changePassword(@RequestBody UserChangePasswordRequest request) {
        try {
            System.out.println(request.toString());
            usersService.changePassword(request);
            return ResponseEntity.status(HttpStatus.OK).body(new StandardResponse(true, "Password changed successfully"));
        } catch (UserNotFoundException | WrongPasswordException | NotValidToken e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new StandardResponse(false, new ErrorModel(401, "UNAUTHORIZED", e.getMessage()), "error"));
        } catch (NotCorrectDataException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StandardResponse(false, new ErrorModel(400, "BAD_REQUEST", e.getMessage()), "error"));
        }
    }

    @GetMapping("/base_info/{id}")
    @ResponseBody
    private ResponseEntity<Object> baseInfo(@PathVariable long id) {
        try {
            UserBaseInfoPesponse response = usersService.getBaseInfo(id);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new StandardResponse(false, new ErrorModel(404, "NOT_FOUND", e.getMessage()), "error"));
        }
    }

    @GetMapping("/all_info/{id}")
    @ResponseBody
    private ResponseEntity<Object> allInfo(@PathVariable long id, @RequestBody UserJWTTokenRequest request) {
        try {
            UserAllInfoResponse response = usersService.getAllInfo(request, id);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (UserNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new StandardResponse(false, new ErrorModel(404, "NOT_FOUND", e.getMessage()), "error"));
        } catch(NotValidToken e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new StandardResponse(false, new ErrorModel(401, "UNAUTHORIZED", e.getMessage()), "error"));
        } catch (NotEnoughRightsException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new StandardResponse(false, new ErrorModel(403, "FORBIDDEN", e.getMessage()), "error"));
        }
    }

    //@DeleteMapping()
}
