package com.example.jeddit.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.jeddit.models.entitys.Post;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends CrudRepository<Post, Long> {

    Page<Post> findByTitleContaining(String text, Pageable pageable);

    @Query(value = "SELECT * FROM posts WHERE posttext LIKE %:text% LIMIT :quantity OFFSET :offset", nativeQuery = true)
    List<Post> searchAllByText(@Param("text") String text, @Param("offset") int offset, @Param("quantity") int quantity);

    @Query(value = "SELECT * FROM posts p WHERE p.communityid IN :communityIds", nativeQuery = true)
    Page<Post> findPostsByCommunityId(@Param("communityIds") List<Long> communityIds, Pageable pageable);

}
