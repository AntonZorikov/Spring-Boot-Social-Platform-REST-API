package com.example.jeddit.servicies;

import com.example.jeddit.exceptions.*;
import com.example.jeddit.models.entitys.User;
import com.example.jeddit.models.models.users.*;
import com.example.jeddit.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class UsersService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTService jwtService;

    public void changePassword(UserChangePasswordRequest request) throws NotValidToken, DataNotFoundException, WrongPasswordException, NotCorrectDataException {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        if(!jwtService.validateToken(request.getJwttoken())){
            throw new NotValidToken("Not valid token");
        }
        long userid = jwtService.extractUserId(request.getJwttoken());
        Optional<User> user = userRepository.findById(userid);
        if (user.isEmpty()) {
            throw new DataNotFoundException("User not found");
        }
        if(request.getNewPassword().length() < 5){
            throw new NotCorrectDataException("Not correct data");
        }
        if (!bCryptPasswordEncoder.matches(request.getOldPassword(), user.get().getPassword())) {
            throw new WrongPasswordException("Wrong password");
        }
        User newUser = user.get();
        newUser.setPassword(bCryptPasswordEncoder.encode(request.getNewPassword()));
        userRepository.save(newUser);
    }

    public UserBaseInfoPesponse getBaseInfo(long id) throws DataNotFoundException {
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()){
            throw new DataNotFoundException("User not found");
        }
        return new UserBaseInfoPesponse(user.get());
    }

    public UserAllInfoResponse getAllInfo(UserJWTTokenRequest request, long id) throws DataNotFoundException, NotValidToken, NotEnoughRightsException {
        if(!jwtService.validateToken(request.getJwttoken())){
            throw new NotValidToken("Not valid token");
        }
        Optional<User> user = userRepository.findById(jwtService.extractUserId(request.getJwttoken()));
        if(user.isEmpty()){
            throw new DataNotFoundException("User not found");
        }
        if(Objects.equals(user.get().getRole(), "ADMIN") || user.get().getId() == id) {
            return new UserAllInfoResponse(user.get());
        }
        else{
            throw new NotEnoughRightsException("Not enough rights");
        }
    }

    public void deleteUser(UserJWTTokenRequest request, long id) throws DataNotFoundException, NotValidToken, NotEnoughRightsException {
        if(!jwtService.validateToken(request.getJwttoken())){
            throw new NotValidToken("Not valid token");
        }
        Optional<User> user = userRepository.findById(jwtService.extractUserId(request.getJwttoken()));
        if(user.isEmpty()){
            throw new DataNotFoundException("User not found");
        }
        if(Objects.equals(user.get().getRole(), "ADMIN") || user.get().getId() == id) {
            userRepository.delete(user.get());
        }
        else{
            throw new NotEnoughRightsException("Not enough rights");
        }
    }
}
