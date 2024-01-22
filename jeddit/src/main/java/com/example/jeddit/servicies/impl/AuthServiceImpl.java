package com.example.jeddit.servicies.impl;

import com.example.jeddit.exceptions.DataNotFoundException;
import com.example.jeddit.exceptions.NotCorrectDataException;
import com.example.jeddit.exceptions.NotUniqueDataException;
import com.example.jeddit.exceptions.WrongPasswordException;
import com.example.jeddit.models.entitys.User;
import com.example.jeddit.models.models.auth.UserRegistrationRequest;
import com.example.jeddit.models.models.auth.UserSignInRequest;
import com.example.jeddit.repositories.UserRepository;
import com.example.jeddit.servicies.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AuthServiceImpl implements AuthService {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$";

    private static final Pattern emailPattern = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    public static boolean isValidEmail(String email) {
        Matcher matcher = emailPattern.matcher(email);
        return matcher.matches();
    }

    @Override
    public User registrationUser(UserRegistrationRequest request) throws NotUniqueDataException, NotCorrectDataException {
        logger.debug("Starting user registration process for {}", request.getLogin());
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        if (request.getLogin().length() > 50) {
            logger.warn("User registration failed, login length must be less then 50 characters");
            throw new NotCorrectDataException("Login length must be less then 50 characters");
        }
        if (request.getLogin().length() < 3) {
            logger.warn("User registration failed, login length must be more then 2 characters");
            throw new NotCorrectDataException("Login length must be more then 2 characters");
        }
        if (request.getLogin().split(" ").length != 1) {
            logger.warn("User registration failed, login must contains only one word");
            throw new NotCorrectDataException("Login must contains only one word");
        }
        if (request.getPassword().length() > 255) {
            logger.warn("User registration failed, login length must be more then 2 characters");
            throw new NotCorrectDataException("Password length must be less then 200 characters");
        }
        if (request.getEmail().length() > 320) {
            logger.warn("User registration failed, email length must be less then 200 characters");
            throw new NotCorrectDataException("Email length must be less then 200 characters");
        }

        User user = new User();
        user.setLogin(request.getLogin());
        user.setEmail(request.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        user.setRole("USER");
        user.setCarma(0);

        if (user.getLogin().length() < 5 || !isValidEmail(user.getEmail()) || request.getPassword().length() < 5) {
            logger.warn("User registration failed, not correct data");
            throw new NotCorrectDataException("Not correct data");
        }

        try {
            logger.info("User {} successfully registered", user.getLogin());
            return userRepository.save(user);
        } catch (Exception ex) {
            logger.warn("User registration failed, not unique data");
            throw new NotUniqueDataException(ex.getMessage());
        }
    }

    @Override
    public User signIn(UserSignInRequest request) throws DataNotFoundException, WrongPasswordException {
        logger.debug("Starting user sign in process for {}", request.getLogin());
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        Optional<User> user = userRepository.findByLogin(request.getLogin());

        if (user.isEmpty()) {
            logger.warn("User sign in failed, user not found");
            throw new DataNotFoundException("User not found");
        }
        if (!bCryptPasswordEncoder.matches(request.getPassword(), user.get().getPassword())) {
            logger.warn("User sign in failed, wrong password");
            throw new WrongPasswordException("Wrong password");
        }
        logger.info("User {} successfully sign in", user.get().getLogin());
        return user.get();
    }
}

