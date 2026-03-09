package com.valhala.optimacvspring.iam.repository;

import com.valhala.optimacvspring.iam.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<AppUser,Long> {

    boolean existsAppUserByEmail(String email);
}
