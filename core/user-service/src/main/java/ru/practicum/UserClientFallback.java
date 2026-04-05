package ru.practicum;

//import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
//import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import java.util.List;

@Slf4j
@Component
public class UserClientFallback implements UserClient {

    @Override
    public ResponseEntity<String> getUsers(List<Long> ids, Integer from, Integer size) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("{\"error\": \"Event service is temporarily unavailable. Please try again later.\"}");
    }

    @Override
    public ResponseEntity<String> createUser(String body) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("{\"error\": \"Event service is temporarily unavailable. Please try again later.\"}");
    }

    @Override
    public ResponseEntity<String> deleteUser(Long userId) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("{\"error\": \"Event service is temporarily unavailable. Please try again later.\"}");
    }
}