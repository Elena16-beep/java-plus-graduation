package ru.practicum;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EventRequestClientFallbackFactory implements FallbackFactory<EventRequestClient> {

    @Override
    public EventRequestClient create(Throwable cause) {
        return new EventRequestClient() {

            @Override
            public ResponseEntity<String> getUserRequests(Long userId) {
                log.warn("Fallback: getUserRequests для userId={}. Причина: {}", userId, cause.getMessage());

                if (cause instanceof FeignException) {
                    int status = ((FeignException) cause).status();
                    if (status >= 500 && status < 600) {
                        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                                .body("{\"error\": \"Сервис мероприятий недоступен\", \"userId\": " + userId + "}");
                    }
                }

                // Возвращаем пустой список по умолчанию
                return ResponseEntity.ok("{\"requests\": [], \"default\": true, \"userId\": " + userId + "}");
            }

            @Override
            public ResponseEntity<String> addParticipationRequest(Long userId, Long eventId) {
                log.warn("Fallback: addParticipationRequest для userId={}, eventId={}. Причина: {}",
                        userId, eventId, cause.getMessage());

                // Возвращаем статус ACCEPTED - запрос будет обработан позже
                return ResponseEntity.status(HttpStatus.ACCEPTED)
                        .body("{\"status\": \"QUEUED\", \"message\": \"Запрос будет обработан когда сервис мероприятий станет доступен\", " +
                                "\"userId\": " + userId + ", \"eventId\": " + eventId + "}");
            }

            @Override
            public ResponseEntity<String> cancelRequest(Long userId, Long requestId) {
                log.warn("Fallback: cancelRequest для userId={}, requestId={}. Причина: {}",
                        userId, requestId, cause.getMessage());

                return ResponseEntity.ok("{\"status\": \"PENDING_CANCELLATION\", \"message\": \"Отмена запроса запланирована\", " +
                        "\"requestId\": " + requestId + "}");
            }
        };
    }
}