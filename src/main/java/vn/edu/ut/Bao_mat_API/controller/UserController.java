package vn.edu.ut.Bao_mat_API.controller;

import vn.edu.ut.Bao_mat_API.entity.User;
import vn.edu.ut.Bao_mat_API.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    // Profile của chính mình
    @GetMapping("/api/me")
    public ResponseEntity<User> getMyProfile(@AuthenticationPrincipal String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));
        return ResponseEntity.ok(user);
    }

    // ADMIN: xem tất cả user
    @GetMapping("/api/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    // ADMIN: xóa user
    @DeleteMapping("/api/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return ResponseEntity.ok("Xóa user thành công");
    }

    // ADMIN: đổi role user
    @PutMapping("/api/users/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateRole(@PathVariable Long id,
                                              @RequestParam String role) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));
        user.setRole(role);
        userRepository.save(user);
        return ResponseEntity.ok("Cập nhật role thành công");
    }
}