package com.example.jeddit.models.entitys;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"voters", "commentaries"})
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(columnDefinition = "varchar(300)", nullable = false)
    private String title;

    @Column(columnDefinition = "varchar(40000)", name = "posttext")
    private String text;

    @Column(columnDefinition = "timestamp", name = "postdate", nullable = false)
    private Timestamp date;

    @ManyToOne
    @JoinColumn(name = "communityid", nullable = false)
    private Community community;

    @ManyToOne
    @JoinColumn(name = "userid", nullable = false)
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Vote> voters = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Commentary> commentaries;

}
