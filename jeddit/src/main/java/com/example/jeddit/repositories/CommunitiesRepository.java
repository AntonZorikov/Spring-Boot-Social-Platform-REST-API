package com.example.jeddit.repositories;

import com.example.jeddit.models.entitys.Community;
import com.example.jeddit.models.models.posts.MostRatedPostProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommunitiesRepository extends CrudRepository<Community, Long> {

    Optional<Community> findByTitle(String title);

    Page<Community> findByTitleContaining(String text, Pageable pageable);

    Page<Community> findByDescriptionContaining(String text, Pageable pageable);

    @Query(value = "SELECT post_id as postId, SUM(value) AS totalVotes FROM posts p\n" +
            "JOIN votes ON votes.post_id = p.id\n" +
            "WHERE p.communityid = :communityId\n" +
            "GROUP BY post_id\n" +
            "ORDER BY totalVotes DESC", nativeQuery = true)
    Page<MostRatedPostProjection> findMostRatedPostsByCommunityId(@Param("communityId") Long communityId, Pageable pageable);

}
