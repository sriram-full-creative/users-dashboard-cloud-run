package com.app.training.cloudrun;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@SpringBootApplication
@RestController
@RequiredArgsConstructor
public class SpringBootDockerApplication {

	private final JdbcTemplate jdbcTemplate;

	@RequestMapping("/")
	public String home() {
		return "Hello Docker World";
	}

	@GetMapping(value = "/db-test", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, String>> testDbConnection() {
		try {
			String result = jdbcTemplate.queryForObject("SELECT H2VERSION() FROM DUAL", String.class);
			jdbcTemplate.execute("""
							INSERT INTO users (id, name, age)
							VALUES
							    (RANDOM_UUID(), 'user1', 24),
							    (RANDOM_UUID(), 'user2', 40);
							""");
			String userCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM USERS", String.class);
			return ResponseEntity.ok(Map.of(
							"connectionEstablished", "true",
							"dbVersion", result,
							"userCount", userCount
			));
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(Map.of("connectionEstablished", "false", "error", e.getMessage()));
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringBootDockerApplication.class, args);
	}

}
