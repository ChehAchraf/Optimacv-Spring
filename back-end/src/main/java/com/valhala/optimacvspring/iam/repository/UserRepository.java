package com.valhala.optimacvspring.iam.repository;

import com.valhala.optimacvspring.iam.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<AppUser, UUID> {

    boolean existsAppUserByEmail(String email);
    
    boolean existsByEmailAndIdNot(String email, UUID id);

    Optional<AppUser> findByEmail(String email);

}
