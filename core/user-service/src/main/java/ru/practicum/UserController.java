package ru.practicum;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class UserController {
    private final UserClient userClient;

    @GetMapping
    @Retry(name = "event-service-retry")
    @CircuitBreaker(name = "event-service-cb")
    public ResponseEntity<String> getUsers(@RequestParam(required = false) List<Long> ids,
                                           @RequestParam(required = false) Integer from,
                                           @RequestParam(required = false) Integer size) {
        log.info("Getting users: ids={}, from={}, size={}", ids, from, size);
        ResponseEntity<String> response = userClient.getUsers(ids, from, size);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        // Обработка различных кодов ошибок
        if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
            log.warn("Users not found");

            return ResponseEntity.ok("{\"users\": [], \"message\": \"No users found\"}");
        }

        log.warn("Event service returned error: {}", response.getStatusCode());

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("{\"error\": \"Event service unavailable\", \"status\": " + response.getStatusCodeValue() + "}");
    }

    @PostMapping
    @Retry(name = "event-service-retry")
    @CircuitBreaker(name = "event-service-cb")
    public ResponseEntity<String> createUser(@RequestBody(required = false) String body) {
        log.info("Creating user: body={}", body);
        ResponseEntity<String> response = userClient.createUser(body);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        if (response.getStatusCode() == HttpStatus.CONFLICT) {
            log.warn("User already exists");

            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("{\"error\": \"User already exists\"}");
        }

        log.warn("Event service returned error on create user: {}", response.getStatusCode());

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body("{\"status\": \"QUEUED\", \"message\": \"User creation queued\", \"data\": " + body + "}");
    }

    @DeleteMapping("/{userId}")
    @Retry(name = "event-service-retry")
    @CircuitBreaker(name = "event-service-cb")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        log.info("Deleting user: userId={}", userId);
        ResponseEntity<String> response = userClient.deleteUser(userId);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
            log.warn("User not found: userId={}", userId);

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\": \"User not found\", \"userId\": " + userId + "}");
        }

        log.warn("Event service returned error on delete user: {}", response.getStatusCode());

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body("{\"status\": \"QUEUED\", \"message\": \"User deletion queued\", \"userId\": " + userId + "}");
    }
}