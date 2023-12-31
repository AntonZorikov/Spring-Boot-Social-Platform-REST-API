package com.example.jeddit.models.entitys;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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

    @OneToMany(mappedBy = "owner")
    private List<Community> communitiesOwner;

    public void addCommunity(Community community) {
        communitiesOwner.add(community);
        community.setOwner(this);
    }
}
