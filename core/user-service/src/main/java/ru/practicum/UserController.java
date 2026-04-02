package ru.practicum;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.function.Supplier;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class UserController {
    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<String> getUsers(@RequestParam(required = false) List<Long> ids,
                                           @RequestParam(required = false) Integer from,
                                           @RequestParam(required = false) Integer size) {
        return forward(() -> userClient.getUsers(ids, from, size));
    }

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody(required = false) String body) {
        return forward(() -> userClient.createUser(body));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        return forward(() -> userClient.deleteUser(userId));
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