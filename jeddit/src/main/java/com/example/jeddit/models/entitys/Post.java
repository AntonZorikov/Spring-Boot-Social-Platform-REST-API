package com.example.jeddit.models.entitys;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Entity
@Table(name = "posts")
@Getter
@Setter
@NoArgsConstructor
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

    @Column(nullable = false)
    private int rating;

    @ManyToOne
    @JoinColumn(name = "communityid", nullable = false)
    private Community community;

    @ManyToOne
    @JoinColumn(name = "userid", nullable = false)
    private User user;

}
