package com.example.jeddit.repositories;

import com.example.jeddit.models.entitys.Community;
import com.example.jeddit.models.entitys.Post;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends CrudRepository<Post, Long> {

    @Query(value = "SELECT * FROM posts WHERE title LIKE %:text% LIMIT :quantity OFFSET :offset", nativeQuery = true)
    List<Post> searchAllByTitle(@Param("text") String text, @Param("offset") int offset, @Param("quantity") int quantity);

    @Query(value = "SELECT * FROM posts WHERE posttext LIKE %:text% LIMIT :quantity OFFSET :offset", nativeQuery = true)
    List<Post> searchAllByText(@Param("text") String text, @Param("offset") int offset, @Param("quantity") int quantity);

}
