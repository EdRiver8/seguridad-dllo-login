package com.example.proyectoseguridaddllologin.repository;

import com.example.proyectoseguridaddllologin.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // query nativa String query = "SELECT * FROM users WHERE username = '" + username + "'";
    @Query(value = "SELECT * FROM users WHERE username = ?1", nativeQuery = true)
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);
}
