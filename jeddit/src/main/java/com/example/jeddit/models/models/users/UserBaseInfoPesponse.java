package com.example.jeddit.models.models.users;

import com.example.jeddit.models.entitys.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserBaseInfoPesponse {

    private long id;
    private String login;
    private String role;
    private Integer carma;

    public UserBaseInfoPesponse(long id, String login, String role, Integer carma) {
        this.id = id;
        this.login = login;
        this.role = role;
        this.carma = carma;
    }

    public UserBaseInfoPesponse(User user) {
        this.id = user.getId();
        this.login = user.getLogin();
        this.role = user.getRole();
        this.carma = user.getCarma();
    }
}
