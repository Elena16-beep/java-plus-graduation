package ru.practicum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CommentClientFallbackFactory implements FallbackFactory<CommentClient> {

    @Override
    public CommentClient create(Throwable cause) {
        return new CommentClient() {

            @Override
            public ResponseEntity<String> addComment(String body, Long eventId, Long userId) {
                log.warn("Fallback addComment: eventId={}, userId={}, причина: {}",
                        eventId, userId, cause.getMessage());

//                return ResponseEntity.status(HttpStatus.ACCEPTED)
//                        .body("{\"status\": \"QUEUED\", \"message\": \"Комментарий будет добавлен позже\", " +
//                                "\"eventId\": " + eventId + ", \"userId\": " + userId + "}");
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                        .body("{\"error\": \"Event service is temporarily unavailable. Please try again later.\"}");
            }

            @Override
            public ResponseEntity<String> patchComment(Long eventId, Long commentId, boolean published) {
                log.warn("Fallback patchComment: eventId={}, commentId={}, published={}, причина: {}",
                        eventId, commentId, published, cause.getMessage());

                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                        .body("{\"error\": \"Event service is temporarily unavailable. Please try again later.\"}");
//                return ResponseEntity.ok("{\"status\": \"PENDING\", \"message\": \"Изменение статуса комментария запланировано\"}");
            }

            @Override
            public ResponseEntity<String> getAllCommentsBy(Long userId, Long eventId) {
                log.warn("Fallback getAllCommentsBy: userId={}, eventId={}, причина: {}",
                        userId, eventId, cause.getMessage());

                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                        .body("{\"error\": \"Event service is temporarily unavailable. Please try again later.\"}");
//                return ResponseEntity.ok("{\"comments\": [], \"default\": true, \"message\": \"Сервис комментариев временно недоступен\"}");
            }

            @Override
            public ResponseEntity<String> getCommentByCommentId(Long userId, Long eventId, Long commentId) {
                log.warn("Fallback updateComment (GET): userId={}, eventId={}, commentId={}",
                        userId, eventId, commentId);

//                return ResponseEntity.ok("{\"comment\": null, \"default\": true}");
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                        .body("{\"error\": \"Event service is temporarily unavailable. Please try again later.\"}");
            }

            @Override
            public ResponseEntity<String> updateComment(Long userId, Long eventId, Long commentId, String body) {
                log.warn("Fallback updateComment (PATCH): userId={}, eventId={}, commentId={}",
                        userId, eventId, commentId);

                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                        .body("{\"error\": \"Event service is temporarily unavailable. Please try again later.\"}");
//                return ResponseEntity.status(HttpStatus.ACCEPTED)
//                        .body("{\"status\": \"QUEUED\", \"message\": \"Обновление комментария запланировано\"}");
            }

            @Override
            public ResponseEntity<String> deleteComment(Long userId, Long eventId, Long commentId) {
                log.warn("Fallback deleteComment: userId={}, eventId={}, commentId={}",
                        userId, eventId, commentId);

//                return ResponseEntity.accepted()
//                        .body("{\"status\": \"QUEUED\", \"message\": \"Удаление комментария запланировано\"}");
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                        .body("{\"error\": \"Event service is temporarily unavailable. Please try again later.\"}");
            }

            @Override
            public ResponseEntity<String> getComments(Long eventId) {
                log.warn("Fallback getComments: eventId={}, причина: {}", eventId, cause.getMessage());

//                return ResponseEntity.ok("{\"comments\": [], \"default\": true, \"eventId\": " + eventId + "}");
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                        .body("{\"error\": \"Event service is temporarily unavailable. Please try again later.\"}");
            }

            @Override
            public ResponseEntity<String> getCommentById(Long eventId, Long commentId) {
                log.warn("Fallback getCommentById: eventId={}, commentId={}", eventId, commentId);

//                return ResponseEntity.ok("{\"comment\": null, \"available\": false}");
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                        .body("{\"error\": \"Event service is temporarily unavailable. Please try again later.\"}");
            }
        };
    }
}