package com.example.jeddit.repositories;

import com.example.jeddit.models.entitys.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends CrudRepository<Post, Long> {

    Page<Post> findByTitleContaining(String text, Pageable pageable);

    Page<Post> findByTextContaining(String text, Pageable pageable);

    @Query(value = "SELECT * FROM posts p WHERE p.communityid IN :communityIds", nativeQuery = true)
    Page<Post> findPostsByCommunityId(@Param("communityIds") List<Long> communityIds, Pageable pageable);

}
