package com.example.nomnomapp.repository;

import com.example.nomnomapp.model.NomNomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<NomNomUser, Integer> {

    Optional<NomNomUser> findByUsername(String username);

    Optional<NomNomUser> findByEmailAddress(String emailAddress);

    boolean existsById(int userId);

}
