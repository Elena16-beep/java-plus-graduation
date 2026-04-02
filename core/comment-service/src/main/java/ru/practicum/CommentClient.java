package ru.practicum;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "event-service")
public interface CommentClient {

    @PostMapping(value = "/admin/events/{eventId}/comments/{commentId}", consumes = "application/json")
    ResponseEntity<String> addComment(@PathVariable Long userId,
                                      @PathVariable Long eventId,
                                      @RequestBody String body);

    @PatchMapping(value = "/admin/events/{eventId}/comments/{commentId}", consumes = "application/json")
    ResponseEntity<String> patchComment(@PathVariable Long eventId,
                                        @PathVariable Long commentId,
                                        @RequestParam boolean published);

    @GetMapping("/users/{userId}/events/{eventId}/comments")
    ResponseEntity<String> getAllCommentsBy(@PathVariable Long userId,
                                            @PathVariable Long eventId);

    @GetMapping("/users/{userId}/events/{eventId}/comments/{commentId}")
    ResponseEntity<String> updateComment(@PathVariable Long userId,
                                         @PathVariable Long eventId,
                                         @PathVariable Long commentId);

    @PatchMapping(value = "/users/{userId}/events/{eventId}/comments/{commentId}", consumes = "application/json")
    ResponseEntity<String> updateComment(@PathVariable Long userId,
                                         @PathVariable Long eventId,
                                         @PathVariable Long commentId,
                                         @RequestBody String body);

    @DeleteMapping("/users/{userId}/events/{eventId}/comments/{commentId}")
    ResponseEntity<String> deleteComment(@PathVariable Long userId,
                                         @PathVariable Long eventId,
                                         @PathVariable Long commentId);

    @GetMapping("/events/{eventId}/comments")
    ResponseEntity<String> getComments(@PathVariable Long eventId);

    @GetMapping("/events/{eventId}/comments/{commentId}")
    ResponseEntity<String> getCommentById(@PathVariable Long eventId,
                                          @PathVariable Long commentId);
}