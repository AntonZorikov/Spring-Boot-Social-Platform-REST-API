package com.example.jeddit.models.models.users;

import com.example.jeddit.models.entitys.User;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAllInfoResponse {

    private long id;
    private String login;
    private String email;
    private String role;
    private Integer carma;

    public UserAllInfoResponse(User user) {
        this.id = user.getId();
        this.login = user.getLogin();
        this.role = user.getRole();
        this.carma = user.getCarma();
        this.email = user.getEmail();
    }

}
