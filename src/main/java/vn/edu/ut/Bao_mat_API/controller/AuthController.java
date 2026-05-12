package vn.edu.ut.Bao_mat_API.controller;

import vn.edu.ut.Bao_mat_API.dto.request.LoginRequest;
import vn.edu.ut.Bao_mat_API.dto.request.RegisterRequest;
import vn.edu.ut.Bao_mat_API.dto.response.AuthResponse;
import vn.edu.ut.Bao_mat_API.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        System.out.println("=== REGISTER CALLED ===");
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    // ⚠️ VULN: logout không blacklist token
    // Client xóa token nhưng token vẫn hợp lệ đến khi hết hạn
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok("Đăng xuất thành công");
    }
}