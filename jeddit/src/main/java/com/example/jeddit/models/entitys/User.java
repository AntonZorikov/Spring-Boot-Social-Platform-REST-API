package com.example.jeddit.models.entitys;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"communitiesOwner", "communities", "password", "email", "posts", "votedPosts", "commentaries"})
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

    @ManyToMany
    @JoinTable(name = "users_communities",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "community_id"))
    private List<Community> communities;

    @OneToMany(mappedBy = "user")
    private List<Post> posts;

    @OneToMany(mappedBy = "user")
    private List<Vote> votedPosts = new ArrayList<>();

    @OneToMany(mappedBy = "owner")
    private List<Commentary> commentaries;

    public void addCommunity(Community community) {
        communitiesOwner.add(community);
        community.setOwner(this);
    }
}
