package ru.practicum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CommentClientFallback implements CommentClient {

    @Override
    public ResponseEntity<String> addComment(String body, Long eventId, Long userId) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("{\"error\": \"Event service is temporarily unavailable. Please try again later.\"}");
    }

    @Override
    public ResponseEntity<String> patchComment(Long eventId, Long commentId, boolean published) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("{\"error\": \"Event service is temporarily unavailable. Please try again later.\"}");
    }

    @Override
    public ResponseEntity<String> getAllCommentsBy(Long userId, Long eventId) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("{\"error\": \"Event service is temporarily unavailable. Please try again later.\"}");
    }

    @Override
    public ResponseEntity<String> getCommentByCommentId(Long userId, Long eventId, Long commentId) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("{\"error\": \"Event service is temporarily unavailable. Please try again later.\"}");
    }

    @Override
    public ResponseEntity<String> updateComment(Long userId, Long eventId, Long commentId, String body) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("{\"error\": \"Event service is temporarily unavailable. Please try again later.\"}");
    }

    @Override
    public ResponseEntity<String> deleteComment(Long userId, Long eventId, Long commentId) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("{\"error\": \"Event service is temporarily unavailable. Please try again later.\"}");
    }

    @Override
    public ResponseEntity<String> getComments(Long eventId) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("{\"error\": \"Event service is temporarily unavailable. Please try again later.\"}");
    }

    @Override
    public ResponseEntity<String> getCommentById(Long eventId, Long commentId) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("{\"error\": \"Event service is temporarily unavailable. Please try again later.\"}");
    }
}