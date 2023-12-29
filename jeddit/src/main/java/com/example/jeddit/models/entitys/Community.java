package com.example.jeddit.models.entitys;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "communities")
@Getter
@Setter
@NoArgsConstructor
public class Community {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(columnDefinition = "varchar(50)")
    private String title;

    @Column(columnDefinition = "varchar(200)")
    private String description;

    public Community(String title, String description) {
        this.title = title;
        this.description = description;
    }
}
