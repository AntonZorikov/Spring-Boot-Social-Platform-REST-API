package com.example.jeddit.repositories;

import com.example.jeddit.models.entitys.Community;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommunitiesRepository extends CrudRepository<Community, Long> {

    @Query(value = "SELECT * FROM communities WHERE title = :title", nativeQuery = true)
    Optional<Community> findByTitle(@Param("title") String title);

}
