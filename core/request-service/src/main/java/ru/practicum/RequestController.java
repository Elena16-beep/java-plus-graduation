package ru.practicum;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class RequestController {
    private final EventRequestClient eventRequestClient;

    @GetMapping("/{userId}/requests")
    @Retry(name = "event-service-retry", fallbackMethod = "fallbackGetUserRequests")
    @CircuitBreaker(name = "event-service-cb", fallbackMethod = "fallbackGetUserRequests")
    public ResponseEntity<String> getUserRequests(@PathVariable Long userId) {
        log.info("Получение запросов для пользователя: {}", userId);

        return eventRequestClient.getUserRequests(userId);
    }

    @PostMapping("/{userId}/requests")
    @Retry(name = "event-service-retry", fallbackMethod = "fallbackAddRequest")
    @CircuitBreaker(name = "event-service-cb", fallbackMethod = "fallbackAddRequest")
    @TimeLimiter(name = "event-service-tl", fallbackMethod = "fallbackAddRequest")
    public CompletableFuture<ResponseEntity<String>> addParticipationRequest(
            @PathVariable Long userId,
            @RequestParam Long eventId) {
        log.info("Создание запроса на участие: userId={}, eventId={}", userId, eventId);

        return CompletableFuture.completedFuture(
                eventRequestClient.addParticipationRequest(userId, eventId)
        );
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    @Retry(name = "event-service-retry", fallbackMethod = "fallbackCancelRequest")
    @CircuitBreaker(name = "event-service-cb", fallbackMethod = "fallbackCancelRequest")
    public ResponseEntity<String> cancelRequest(@PathVariable Long userId,
                                                @PathVariable Long requestId) {
        log.info("Отмена запроса: userId={}, requestId={}", userId, requestId);

        return eventRequestClient.cancelRequest(userId, requestId);
    }

    // Fallback методы
    private ResponseEntity<String> fallbackGetUserRequests(Long userId, Exception ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("{\"error\": \"Event service unavailable\", \"userId\": " + userId + "}");
    }

    private ResponseEntity<String> fallbackAddRequest(Long userId, Long eventId, Exception ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("{\"error\": \"Event service unavailable\", \"userId\": " + userId + ", \"eventId\": " + eventId + "}");
    }

    private ResponseEntity<String> fallbackCancelRequest(Long userId, Long requestId, Exception ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("{\"error\": \"Event service unavailable\", \"userId\": " + userId + ", \"requestId\": " + requestId + "}");
    }
}