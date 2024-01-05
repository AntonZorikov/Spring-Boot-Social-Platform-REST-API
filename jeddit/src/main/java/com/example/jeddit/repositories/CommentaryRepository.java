package com.example.jeddit.repositories;

import com.example.jeddit.models.entitys.Commentary;
import org.springframework.data.repository.CrudRepository;

public interface CommentaryRepository extends CrudRepository<Commentary, Long> {
}
