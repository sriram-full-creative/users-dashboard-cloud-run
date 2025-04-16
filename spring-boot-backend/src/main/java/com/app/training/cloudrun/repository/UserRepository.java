package com.app.training.cloudrun.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.training.cloudrun.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
}
