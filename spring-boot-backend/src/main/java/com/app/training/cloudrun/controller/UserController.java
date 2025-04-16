package com.app.training.cloudrun.controller;

import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.training.cloudrun.dto.UserDto;
import com.app.training.cloudrun.mapper.UserMapper;
import com.app.training.cloudrun.service.UserService;

import lombok.RequiredArgsConstructor;

@Slf4j
@RestController
@RequestMapping(UserController.USERS_API_BASE_URL_V1)
@RequiredArgsConstructor
public class UserController {
    public static final String USERS_API_BASE_URL_V1 = "/api/v1/users";
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDto>> getAllUsers() {
        log.info("Fetching all the users");
        List<UserDto> users = userService.getAllUsers().stream().map(userMapper::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }
}