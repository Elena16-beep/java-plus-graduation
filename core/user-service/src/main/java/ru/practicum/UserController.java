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
        log.info("=== GET /admin/users called ===");
        log.info("ids={}, from={}, size={}", ids, from, size);

        try {
            ResponseEntity<String> response = userClient.getUsers(ids, from, size);
            log.info("Response status: {}", response.getStatusCode());
            log.info("Response body: {}", response.getBody());

            if (!response.getStatusCode().is2xxSuccessful()) {
                log.warn("Event service returned error: {}", response.getStatusCode());
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                        .body("{\"error\": \"Event service returned error\", \"status\": " + response.getStatusCodeValue() + "}");
            }

            return response;
        } catch (Exception e) {
            log.error("Exception in getUsers: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"Internal error: " + e.getMessage() + "\"}");
        }
    }

    @PostMapping
    @Retry(name = "event-service-retry")
    @CircuitBreaker(name = "event-service-cb")
    public ResponseEntity<String> createUser(@RequestBody(required = false) String body) {
        log.info("=== POST /admin/users called ===");
        log.info("Request body: {}", body);

        try {
            // Валидация на уровне контроллера
            if (body == null || body.trim().isEmpty()) {
                log.warn("Empty request body");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("{\"error\": \"Request body is required\"}");
            }

            ResponseEntity<String> response = userClient.createUser(body);
            log.info("Response status: {}", response.getStatusCode());

            if (!response.getStatusCode().is2xxSuccessful()) {
                log.warn("Event service returned error on create: {}", response.getStatusCode());
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                        .body("{\"error\": \"Failed to create user\", \"status\": " + response.getStatusCodeValue() + "}");
            }

            return response;
        } catch (Exception e) {
            log.error("Exception in createUser: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"Internal error: " + e.getMessage() + "\"}");
        }
    }

    @DeleteMapping("/{userId}")
    @Retry(name = "event-service-retry")
    @CircuitBreaker(name = "event-service-cb")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        log.info("=== DELETE /admin/users/{} called ===", userId);

        try {
            ResponseEntity<String> response = userClient.deleteUser(userId);
            log.info("Response status: {}", response.getStatusCode());

            if (!response.getStatusCode().is2xxSuccessful()) {
                log.warn("Event service returned error on delete: {}", response.getStatusCode());
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                        .body("{\"error\": \"Failed to delete user\", \"status\": " + response.getStatusCodeValue() + "}");
            }

            return response;
        } catch (Exception e) {
            log.error("Exception in deleteUser: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"Internal error: " + e.getMessage() + "\"}");
        }
    }
}