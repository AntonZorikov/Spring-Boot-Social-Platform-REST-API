package com.example.jeddit.repositories;

import com.example.jeddit.models.entitys.Vote;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoterRepository extends CrudRepository<Vote, Long> {

    @Query(value = "SELECT * FROM votes WHERE user_id = :user_id AND post_id = :post_id", nativeQuery = true)
    Optional<Vote> findByUSerIdAndPostId(@Param("user_id") long user_id, @Param("post_id") long post_id);

}
