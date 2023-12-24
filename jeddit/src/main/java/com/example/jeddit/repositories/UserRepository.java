package com.example.jeddit.repositories;

import com.example.jeddit.models.entitys.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    @Query(value = "SELECT * FROM users WHERE login = :login", nativeQuery = true)
    Optional<User> findByLogin(@Param("login") String login);

}
