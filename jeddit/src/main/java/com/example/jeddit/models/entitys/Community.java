package com.example.jeddit.models.entitys;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "communities")
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"followers", "posts", "moderators"})
public class Community {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(columnDefinition = "varchar(50)")
    private String title;

    @Column(columnDefinition = "varchar(200)")
    private String description;

    @ManyToOne
    @JoinColumn(name = "owner")
    private User owner;

    @ManyToMany(mappedBy = "communities")
    private List<User> followers = new ArrayList<>();

    @OneToMany(mappedBy = "community", cascade = CascadeType.REMOVE)
    private List<Post> posts;

    @ManyToMany(mappedBy = "moderatedCommunities")
    private List<User> moderators = new ArrayList<>();

    public Community(String title, String description, User owner) {
        this.title = title;
        this.description = description;
        this.owner = owner;
        owner.addCommunity(this);
    }
}
