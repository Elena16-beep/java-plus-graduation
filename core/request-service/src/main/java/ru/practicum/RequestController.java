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
    @Retry(name = "event-service-retry")
    @CircuitBreaker(name = "event-service-cb")
    public ResponseEntity<String> getUserRequests(@PathVariable Long userId) {
        log.info("Получение запросов для пользователя: {}", userId);
        ResponseEntity<String> response = eventRequestClient.getUserRequests(userId);

        // Если сервис вернул ошибку, но не выбросил исключение
        if (!response.getStatusCode().is2xxSuccessful()) {
            log.warn("Event service вернул ошибку: {}", response.getStatusCode());

            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("{\"error\": \"Event service returned error\", \"status\": " + response.getStatusCodeValue() + "}");
        }

        return response;
    }

    @PostMapping("/{userId}/requests")
    @Retry(name = "event-service-retry")
    @CircuitBreaker(name = "event-service-cb")
    @TimeLimiter(name = "event-service-tl")
    public CompletableFuture<ResponseEntity<String>> addParticipationRequest(
            @PathVariable Long userId,
            @RequestParam Long eventId) {
        log.info("Создание запроса на участие: userId={}, eventId={}", userId, eventId);

        return CompletableFuture.supplyAsync(() -> {
            ResponseEntity<String> response = eventRequestClient.addParticipationRequest(userId, eventId);

            if (!response.getStatusCode().is2xxSuccessful()) {
                log.warn("Event service вернул ошибку при создании запроса: {}", response.getStatusCode());

                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                        .body("{\"error\": \"Failed to create request\", \"status\": " + response.getStatusCodeValue() + "}");
            }

            return response;
        });
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    @Retry(name = "event-service-retry")
    @CircuitBreaker(name = "event-service-cb")
    public ResponseEntity<String> cancelRequest(@PathVariable Long userId,
                                                @PathVariable Long requestId) {
        log.info("Отмена запроса: userId={}, requestId={}", userId, requestId);
        ResponseEntity<String> response = eventRequestClient.cancelRequest(userId, requestId);

        if (!response.getStatusCode().is2xxSuccessful()) {
            log.warn("Event service вернул ошибку при отмене запроса: {}", response.getStatusCode());

            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("{\"error\": \"Failed to cancel request\", \"status\": " + response.getStatusCodeValue() + "}");
        }

        return response;
    }
}