package com.app.training.cloudrun.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.app.training.cloudrun.entity.User;
import com.app.training.cloudrun.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> getAllUsers() {
        log.info("Fetching all users");
        return userRepository.findAll();
    }

}
