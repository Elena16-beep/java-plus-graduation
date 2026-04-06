package ru.practicum;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class EventRequestClientFallback implements EventRequestClient {

    @Override
    public ResponseEntity<String> getUserRequests(Long userId) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("{\"error\": \"Event service is temporarily unavailable. Please try again later.\"}");
    }

    @Override
    public ResponseEntity<String> addParticipationRequest(Long userId, Long eventId) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("{\"error\": \"Unable to create request. Event service is unavailable.\"}");
    }

    @Override
    public ResponseEntity<String> cancelRequest(Long userId, Long requestId) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("{\"error\": \"Unable to cancel request. Event service is unavailable.\"}");
    }
}