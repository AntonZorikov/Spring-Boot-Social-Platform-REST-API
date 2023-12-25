package com.example.jeddit.models.entitys;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(columnDefinition = "varchar(50)")
    private String login;

    @Column(columnDefinition = "varchar(255)")
    private String password;

    @Column(columnDefinition = "varchar(320)")
    private String email;

    @Column(columnDefinition = "varchar(20)")
    private String role;

    @Column(columnDefinition = "integer")
    private Integer carma;

}
