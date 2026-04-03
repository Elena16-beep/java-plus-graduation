package ru.practicum;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import java.util.List;

@Slf4j
@Component
public class UserClientFallbackFactory implements FallbackFactory<UserClient> {

    @Override
    public UserClient create(Throwable cause) {
        return new UserClient() {

            @Override
            public ResponseEntity<String> getUsers(List<Long> ids, Integer from, Integer size) {
                log.warn("Fallback getUsers: ids={}, from={}, size={}, причина: {}",
                        ids, from, size, cause.getMessage());

                if (cause instanceof FeignException && ((FeignException) cause).status() == 404) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body("{\"error\": \"Пользователи не найдены\"}");
                }

                // Возвращаем пустой список пользователей
//                return ResponseEntity.ok("{\"users\": [], \"default\": true, \"message\": \"Сервис пользователей временно недоступен\"}");
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                        .body("{\"error\": \"Event service is temporarily unavailable. Please try again later.\"}");
            }

            @Override
            public ResponseEntity<String> createUser(String body) {
                log.warn("Fallback createUser: body={}, причина: {}", body, cause.getMessage());

                // Для создания пользователя возвращаем ACCEPTED с обещанием обработать позже
//                return ResponseEntity.status(HttpStatus.ACCEPTED)
//                        .body("{\"status\": \"QUEUED\", \"message\": \"Создание пользователя будет выполнено позже\", \"data\": " + body + "}");
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                        .body("{\"error\": \"Event service is temporarily unavailable. Please try again later.\"}");
            }

            @Override
            public ResponseEntity<String> deleteUser(Long userId) {
                log.warn("Fallback deleteUser: userId={}, причина: {}", userId, cause.getMessage());

                // Для удаления возвращаем ACCEPTED
//                return ResponseEntity.status(HttpStatus.ACCEPTED)
//                        .body("{\"status\": \"QUEUED\", \"message\": \"Удаление пользователя запланировано\", \"userId\": " + userId + "}");
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                        .body("{\"error\": \"Event service is temporarily unavailable. Please try again later.\"}");
            }
        };
    }
}