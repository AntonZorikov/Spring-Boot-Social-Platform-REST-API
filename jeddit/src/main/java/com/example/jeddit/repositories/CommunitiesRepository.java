package com.example.jeddit.repositories;

import com.example.jeddit.models.entitys.Community;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommunitiesRepository extends CrudRepository<Community, Long> {

    Optional<Community> findByTitle(String title);

    Page<Community> findByTitleContaining(String text, Pageable pageable);

    Page<Community> findByDescriptionContaining(String text, Pageable pageable);

}
