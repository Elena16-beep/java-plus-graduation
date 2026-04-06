package ru.practicum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import java.util.List;

@Slf4j
@Component
public class UserClientFallback implements UserClient {

    @Override
    public ResponseEntity<String> getUsers(List<Long> ids, Integer from, Integer size) {
        log.warn("Fallback getUsers: ids={}, from={}, size={}", ids, from, size);
        // Для GET запросов возвращаем 200 OK с пустым списком (не ошибку!)
        return ResponseEntity.ok("{\"users\": [], \"fallback\": true, \"message\": \"Service temporarily unavailable\"}");
    }

    @Override
    public ResponseEntity<String> createUser(String body) {
        log.warn("Fallback createUser: body={}", body);
        // Для создания пользователя возвращаем ACCEPTED
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body("{\"status\": \"QUEUED\", \"fallback\": true, \"message\": \"User creation will be processed later\"}");
    }

    @Override
    public ResponseEntity<String> deleteUser(Long userId) {
        log.warn("Fallback deleteUser: userId={}", userId);
        // Для удаления пользователя возвращаем ACCEPTED
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body("{\"status\": \"QUEUED\", \"fallback\": true, \"message\": \"User deletion queued\", \"userId\": " + userId + "}");
    }
}