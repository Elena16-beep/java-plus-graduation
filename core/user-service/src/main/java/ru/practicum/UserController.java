package ru.practicum;

import feign.FeignException;
//import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
//import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.function.Supplier;

@Slf4j
@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class UserController {
    private final UserClient userClient;

//    @GetMapping
//    @Retry(name = "event-service-retry", fallbackMethod = "fallbackGetUsers")
//    @CircuitBreaker(name = "event-service-cb", fallbackMethod = "fallbackGetUsers")
//    public ResponseEntity<String> getUsers(@RequestParam(required = false) List<Long> ids,
//                                           @RequestParam(required = false) Integer from,
//                                           @RequestParam(required = false) Integer size) {
//        log.info("Getting users: ids={}, from={}, size={}", ids, from, size);
//
//        return userClient.getUsers(ids, from, size);
//    }
//
//    @PostMapping
//    @Retry(name = "event-service-retry", fallbackMethod = "fallbackCreateUser")
//    @CircuitBreaker(name = "event-service-cb", fallbackMethod = "fallbackCreateUser")
//    public ResponseEntity<String> createUser(@RequestBody(required = false) String body) {
//        log.info("Creating user: body={}", body);
//
//        return userClient.createUser(body);
//    }
//
//    @DeleteMapping("/{userId}")
//    @Retry(name = "event-service-retry", fallbackMethod = "fallbackDeleteUser")
//    @CircuitBreaker(name = "event-service-cb", fallbackMethod = "fallbackDeleteUser")
//    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
//        log.info("Deleting user: userId={}", userId);
//
//        return userClient.deleteUser(userId);
//    }
//
//    // Fallback методы
//    private ResponseEntity<String> fallbackGetUsers(List<Long> ids, Integer from, Integer size, Exception ex) {
//        log.warn("Fallback getUsers: ids={}, from={}, size={}, ошибка: {}", ids, from, size, ex.getMessage());
//
//        // Возвращаем пустой список пользователей вместо ошибки
//        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
//                .body("{\"error\": \"Event service is temporarily unavailable. Please try again later.\"}");
////        return ResponseEntity.ok("{\"users\": [], \"fallback\": true, \"message\": \"Сервис временно недоступен\"}");
//    }
//
//    private ResponseEntity<String> fallbackCreateUser(String body, Exception ex) {
//        log.warn("Fallback createUser: body={}, ошибка: {}", body, ex.getMessage());
//
//        // Возвращаем статус ACCEPTED - запрос будет обработан позже
//        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
//                .body("{\"error\": \"Event service is temporarily unavailable. Please try again later.\"}");
////        return ResponseEntity.status(HttpStatus.ACCEPTED)
////                .body("{\"status\": \"QUEUED\", \"fallback\": true, \"message\": \"Создание пользователя поставлено в очередь\"}");
//    }
//
//    private ResponseEntity<String> fallbackDeleteUser(Long userId, Exception ex) {
//        log.warn("Fallback deleteUser: userId={}, ошибка: {}", userId, ex.getMessage());
//
//        // Возвращаем статус ACCEPTED
//        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
//                .body("{\"error\": \"Event service is temporarily unavailable. Please try again later.\"}");
////        return ResponseEntity.status(HttpStatus.ACCEPTED)
////                .body("{\"status\": \"QUEUED\", \"fallback\": true, \"message\": \"Удаление пользователя поставлено в очередь\", \"userId\": " + userId + "}");
//    }

    @GetMapping
    public ResponseEntity<String> getUsers(@RequestParam(required = false) List<Long> ids,
                                           @RequestParam(required = false) Integer from,
                                           @RequestParam(required = false) Integer size) {
        return forward(() -> userClient.getUsers(ids, from, size));
    }

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody(required = false) String body) {
        return forward(() -> userClient.createUser(body));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        return forward(() -> userClient.deleteUser(userId));
    }

    private ResponseEntity<String> forward(Supplier<ResponseEntity<String>> call) {
        try {
            return call.get();
        } catch (FeignException e) {
            int status = e.status() >= 0 ? e.status() : HttpStatus.SERVICE_UNAVAILABLE.value();

            return ResponseEntity.status(status).body(e.contentUTF8());
        }
    }
}