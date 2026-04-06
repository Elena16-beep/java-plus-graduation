package ru.practicum;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentClient commentClient;

    @PatchMapping("/admin/events/{eventId}/comments/{commentId}")
    @Retry(name = "event-service-retry")
    @CircuitBreaker(name = "event-service-cb")
    public ResponseEntity<String> patchComment(@PathVariable Long eventId,
                                               @PathVariable Long commentId,
                                               @RequestParam boolean published) {
        log.info("Patching comment: eventId={}, commentId={}, published={}", eventId, commentId, published);

        return commentClient.patchComment(eventId, commentId, published);
    }

    @PostMapping("/users/{userId}/events/{eventId}/comments")
    @Retry(name = "event-service-retry")
    @CircuitBreaker(name = "event-service-cb")
    public ResponseEntity<String> addComment(@PathVariable Long userId,
                                             @PathVariable Long eventId,
                                             @RequestBody(required = false) String body) {
        log.info("Adding comment: userId={}, eventId={}, body={}", userId, eventId, body);

        return commentClient.addComment(body, eventId, userId);
    }

    @GetMapping("/users/{userId}/events/{eventId}/comments")
    @Retry(name = "event-service-retry")
    @CircuitBreaker(name = "event-service-cb")
    public ResponseEntity<String> getAllCommentsBy(@PathVariable Long userId,
                                                   @PathVariable Long eventId) {
        log.info("Getting all comments: userId={}, eventId={}", userId, eventId);

        return commentClient.getAllCommentsBy(userId, eventId);
    }

    @GetMapping("/users/{userId}/events/{eventId}/comments/{commentId}")
    @Retry(name = "event-service-retry")
    @CircuitBreaker(name = "event-service-cb")
    public ResponseEntity<String> getCommentByCommentId(@PathVariable Long userId,
                                                        @PathVariable Long eventId,
                                                        @PathVariable Long commentId) {
        log.info("Getting comment by id: userId={}, eventId={}, commentId={}", userId, eventId, commentId);

        return commentClient.getCommentByCommentId(userId, eventId, commentId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}/comments/{commentId}")
    @Retry(name = "event-service-retry")
    @CircuitBreaker(name = "event-service-cb")
    public ResponseEntity<String> updateComment(@PathVariable Long userId,
                                                @PathVariable Long eventId,
                                                @PathVariable Long commentId,
                                                @RequestBody(required = false) String body) {
        log.info("Updating comment: userId={}, eventId={}, commentId={}, body={}", userId, eventId, commentId, body);

        return commentClient.updateComment(userId, eventId, commentId, body);
    }

    @DeleteMapping("/users/{userId}/events/{eventId}/comments/{commentId}")
    @Retry(name = "event-service-retry")
    @CircuitBreaker(name = "event-service-cb")
    public ResponseEntity<String> deleteComment(@PathVariable Long userId,
                                                @PathVariable Long eventId,
                                                @PathVariable Long commentId) {
        log.info("Deleting comment: userId={}, eventId={}, commentId={}", userId, eventId, commentId);

        return commentClient.deleteComment(userId, eventId, commentId);
    }

    @GetMapping("/events/{eventId}/comments")
    @Retry(name = "event-service-retry")
    @CircuitBreaker(name = "event-service-cb")
    public ResponseEntity<String> getComments(@PathVariable Long eventId) {
        log.info("Getting comments for event: eventId={}", eventId);

        return commentClient.getComments(eventId);
    }

    @GetMapping("/events/{eventId}/comments/{commentId}")
    @Retry(name = "event-service-retry")
    @CircuitBreaker(name = "event-service-cb")
    public ResponseEntity<String> getCommentById(@PathVariable Long eventId,
                                                 @PathVariable Long commentId) {
        log.info("Getting comment by id: eventId={}, commentId={}", eventId, commentId);

        return commentClient.getCommentById(eventId, commentId);
    }
}