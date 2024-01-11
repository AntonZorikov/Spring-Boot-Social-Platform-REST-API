package com.example.jeddit.servicies;

import com.example.jeddit.exceptions.DataNotFoundException;
import com.example.jeddit.exceptions.NotCorrectDataException;
import com.example.jeddit.exceptions.NotUniqueDataException;
import com.example.jeddit.exceptions.WrongPasswordException;
import com.example.jeddit.models.entitys.User;
import com.example.jeddit.models.models.auth.UserRegistrationRequest;
import com.example.jeddit.models.models.auth.UserSignInRequest;

public interface AuthService {

    User registrationUser(UserRegistrationRequest request) throws NotUniqueDataException, NotCorrectDataException;

    User signIn(UserSignInRequest request) throws DataNotFoundException, WrongPasswordException;

}
