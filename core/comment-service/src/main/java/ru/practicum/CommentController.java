package ru.practicum;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.function.Supplier;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentClient commentClient;

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
    public ResponseEntity<String> updateComment(@PathVariable Long userId,
                                                @PathVariable Long eventId,
                                                @PathVariable Long commentId) {
        return forward(() -> commentClient.updateComment(userId, eventId, commentId));
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