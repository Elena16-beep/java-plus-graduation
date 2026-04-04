package ru.practicum;

import feign.FeignException;
//import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
//import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.function.Supplier;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentClient commentClient;

//    @PatchMapping("/admin/events/{eventId}/comments/{commentId}")
//    @Retry(name = "event-service-retry", fallbackMethod = "fallbackPatchComment")
//    @CircuitBreaker(name = "event-service-cb", fallbackMethod = "fallbackPatchComment")
//    public ResponseEntity<String> patchComment(@PathVariable Long eventId,
//                                               @PathVariable Long commentId,
//                                               @RequestParam boolean published) {
//        log.info("Patching comment: eventId={}, commentId={}, published={}", eventId, commentId, published);
//
//        return commentClient.patchComment(eventId, commentId, published);
//    }
//
//    @PostMapping("/users/{userId}/events/{eventId}/comments")
//    @Retry(name = "event-service-retry", fallbackMethod = "fallbackAddComment")
//    @CircuitBreaker(name = "event-service-cb", fallbackMethod = "fallbackAddComment")
//    public ResponseEntity<String> addComment(@PathVariable Long userId,
//                                             @PathVariable Long eventId,
//                                             @RequestBody(required = false) String body) {
//        log.info("Adding comment: userId={}, eventId={}", userId, eventId);
//
//        return commentClient.addComment(body, eventId, userId);
//    }
//
//    @GetMapping("/users/{userId}/events/{eventId}/comments")
//    @Retry(name = "event-service-retry", fallbackMethod = "fallbackGetAllComments")
//    @CircuitBreaker(name = "event-service-cb", fallbackMethod = "fallbackGetAllComments")
//    public ResponseEntity<String> getAllCommentsBy(@PathVariable Long userId,
//                                                   @PathVariable Long eventId) {
//        return commentClient.getAllCommentsBy(userId, eventId);
//    }
//
//    @GetMapping("/users/{userId}/events/{eventId}/comments/{commentId}")
//    @CircuitBreaker(name = "event-service-cb", fallbackMethod = "fallbackGetCommentById")
//    public ResponseEntity<String> getCommentByCommentId(@PathVariable Long userId,
//                                                @PathVariable Long eventId,
//                                                @PathVariable Long commentId) {
//        return commentClient.getCommentByCommentId(userId, eventId, commentId);
//    }
//
//    @PatchMapping("/users/{userId}/events/{eventId}/comments/{commentId}")
//    @Retry(name = "event-service-retry", fallbackMethod = "fallbackUpdateComment")
//    @CircuitBreaker(name = "event-service-cb", fallbackMethod = "fallbackUpdateComment")
//    public ResponseEntity<String> updateComment(@PathVariable Long userId,
//                                                @PathVariable Long eventId,
//                                                @PathVariable Long commentId,
//                                                @RequestBody(required = false) String body) {
//        return commentClient.updateComment(userId, eventId, commentId, body);
//    }
//
//    @DeleteMapping("/users/{userId}/events/{eventId}/comments/{commentId}")
//    @Retry(name = "event-service-retry", fallbackMethod = "fallbackDeleteComment")
//    @CircuitBreaker(name = "event-service-cb", fallbackMethod = "fallbackDeleteComment")
//    public ResponseEntity<String> deleteComment(@PathVariable Long userId,
//                                                @PathVariable Long eventId,
//                                                @PathVariable Long commentId) {
//        return commentClient.deleteComment(userId, eventId, commentId);
//    }
//
//    @GetMapping("/events/{eventId}/comments")
//    @CircuitBreaker(name = "event-service-cb", fallbackMethod = "fallbackGetComments")
//    public ResponseEntity<String> getComments(@PathVariable Long eventId) {
//        return commentClient.getComments(eventId);
//    }
//
//    @GetMapping("/events/{eventId}/comments/{commentId}")
//    @CircuitBreaker(name = "event-service-cb", fallbackMethod = "fallbackGetCommentById")
//    public ResponseEntity<String> getCommentById(@PathVariable Long eventId,
//                                                 @PathVariable Long commentId) {
//        return commentClient.getCommentById(eventId, commentId);
//    }
//
//    // Fallback методы
//    private ResponseEntity<String> fallbackPatchComment(Long eventId, Long commentId, Boolean published, Exception ex) {
//        log.warn("Fallback patchComment: {}", ex.getMessage());
//
////        return ResponseEntity.ok("{\"status\": \"PENDING\", \"message\": \"Изменение статуса запланировано\"}");
//        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
//                .body("{\"error\": \"Event service is temporarily unavailable. Please try again later.\"}");
//    }
//
//    private ResponseEntity<String> fallbackAddComment(Long userId, Long eventId, String body, Exception ex) {
//        log.warn("Fallback addComment: {}", ex.getMessage());
//
////        return ResponseEntity.status(HttpStatus.ACCEPTED)
////                .body("{\"status\": \"QUEUED\", \"message\": \"Комментарий будет добавлен позже\"}");
//        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
//                .body("{\"error\": \"Event service is temporarily unavailable. Please try again later.\"}");
//    }
//
//    private ResponseEntity<String> fallbackGetAllComments(Long userId, Long eventId, Exception ex) {
//        log.warn("Fallback getAllComments: {}", ex.getMessage());
//
////        return ResponseEntity.ok("{\"comments\": [], \"default\": true}");
//        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
//                .body("{\"error\": \"Event service is temporarily unavailable. Please try again later.\"}");
//    }
//
//    private ResponseEntity<String> fallbackGetCommentById(Long eventId, Long commentId, Exception ex) {
//        log.warn("Fallback getCommentById: {}", ex.getMessage());
//
////        return ResponseEntity.ok("{\"comment\": null, \"available\": false}");
//        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
//                .body("{\"error\": \"Event service is temporarily unavailable. Please try again later.\"}");
//    }
//
//    private ResponseEntity<String> fallbackUpdateComment(Long userId, Long eventId, Long commentId, String body, Exception ex) {
//        log.warn("Fallback updateComment: {}", ex.getMessage());
//
////        return ResponseEntity.status(HttpStatus.ACCEPTED)
////                .body("{\"status\": \"QUEUED\", \"message\": \"Обновление комментария запланировано\"}");
//        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
//                .body("{\"error\": \"Event service is temporarily unavailable. Please try again later.\"}");
//    }
//
//    private ResponseEntity<String> fallbackDeleteComment(Long userId, Long eventId, Long commentId, Exception ex) {
//        log.warn("Fallback deleteComment: {}", ex.getMessage());
//
////        return ResponseEntity.accepted()
////                .body("{\"status\": \"QUEUED\", \"message\": \"Удаление комментария запланировано\"}");
//        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
//                .body("{\"error\": \"Event service is temporarily unavailable. Please try again later.\"}");
//    }
//
//    private ResponseEntity<String> fallbackGetComments(Long eventId, Exception ex) {
//        log.warn("Fallback getComments: {}", ex.getMessage());
//
////        return ResponseEntity.ok("{\"comments\": [], \"default\": true}");
//        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
//                .body("{\"error\": \"Event service is temporarily unavailable. Please try again later.\"}");
//    }

    @PatchMapping("/admin/events/{eventId}/comments/{commentId}")
    public ResponseEntity<String> patchComment(@PathVariable Long eventId,
                                               @PathVariable Long commentId,
                                               @RequestParam boolean published) {
        return forward(() -> commentClient.patchComment(eventId, commentId, published));
    }

    @PostMapping("/users/{userId}/events/{eventId}/comments")
    public ResponseEntity<String> addComment(@PathVariable Long userId,
                                             @PathVariable Long eventId,
                                             @RequestBody(required = false) String body) {
        return forward(() -> commentClient.addComment(body, eventId, userId));
    }

    @GetMapping("/users/{userId}/events/{eventId}/comments")
    public ResponseEntity<String> getAllCommentsBy(@PathVariable Long userId,
                                                   @PathVariable Long eventId) {
        return forward(() -> commentClient.getAllCommentsBy(userId, eventId));
    }

    @GetMapping("/users/{userId}/events/{eventId}/comments/{commentId}")
    public ResponseEntity<String> getCommentByCommentId(@PathVariable Long userId,
                                                @PathVariable Long eventId,
                                                @PathVariable Long commentId) {
        return forward(() -> commentClient.getCommentByCommentId(userId, eventId, commentId));
    }

    @PatchMapping("/users/{userId}/events/{eventId}/comments/{commentId}")
    public ResponseEntity<String> updateComment(@PathVariable Long userId,
                                                @PathVariable Long eventId,
                                                @PathVariable Long commentId,
                                                @RequestBody(required = false) String body) {
        return forward(() -> commentClient.updateComment(userId, eventId, commentId, body));
    }

    @DeleteMapping("/users/{userId}/events/{eventId}/comments/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long userId,
                                                @PathVariable Long eventId,
                                                @PathVariable Long commentId) {
        return forward(() -> commentClient.deleteComment(userId, eventId, commentId));
    }

    @GetMapping("/events/{eventId}/comments")
    public ResponseEntity<String> getComments(@PathVariable Long eventId) {
        return forward(() -> commentClient.getComments(eventId));
    }

    @GetMapping("/events/{eventId}/comments/{commentId}")
    public ResponseEntity<String> getCommentById(@PathVariable Long eventId,
                                                 @PathVariable Long commentId) {
        return forward(() -> commentClient.getCommentById(eventId, commentId));
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