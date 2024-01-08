package com.example.jeddit.repositories;

import com.example.jeddit.models.entitys.Community;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommunitiesRepository extends CrudRepository<Community, Long> {

    @Query(value = "SELECT * FROM communities WHERE title = :title", nativeQuery = true)
    Optional<Community> findByTitle(@Param("title") String title);

    @Query(value = "SELECT * FROM communities WHERE title LIKE %:text% LIMIT :quantity OFFSET :offset", nativeQuery = true)
    List<Community> searchAllByTitle(@Param("text") String text, @Param("offset") int offset, @Param("quantity") int quantity);

    @Query(value = "SELECT * FROM communities WHERE description LIKE %:text% LIMIT :quantity OFFSET :offset", nativeQuery = true)
    List<Community> searchAllByDescription(@Param("text") String text, @Param("offset") int offset, @Param("quantity") int quantity);


}
