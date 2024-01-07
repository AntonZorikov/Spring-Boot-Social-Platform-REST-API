package com.example.jeddit.repositories;

import com.example.jeddit.models.entitys.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    @Query(value = "SELECT * FROM users WHERE login = :login", nativeQuery = true)
    Optional<User> findByLogin(@Param("login") String login);

    @Query(value = "SELECT * FROM users WHERE login LIKE %:text% LIMIT :quantity OFFSET :offset", nativeQuery = true)
    List<User> searchAllByLogin(@Param("text") String text, @Param("offset") int offset, @Param("quantity") int quantity);

}
