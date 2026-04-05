package ru.practicum;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

        return userClient.getUsers(ids, from, size);
    }

    @PostMapping
    @Retry(name = "event-service-retry")
    @CircuitBreaker(name = "event-service-cb")
    public ResponseEntity<String> createUser(@RequestBody(required = false) String body) {
        log.info("Creating user: body={}", body);

        return userClient.createUser(body);
    }

    @DeleteMapping("/{userId}")
    @Retry(name = "event-service-retry")
    @CircuitBreaker(name = "event-service-cb")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        log.info("Deleting user: userId={}", userId);

        return userClient.deleteUser(userId);
    }
}