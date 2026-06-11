package vn.edu.ut.Bao_mat_API.controller;

import vn.edu.ut.Bao_mat_API.dto.request.LoginRequest;
import vn.edu.ut.Bao_mat_API.dto.request.RegisterRequest;
import vn.edu.ut.Bao_mat_API.dto.response.AuthResponse;
import vn.edu.ut.Bao_mat_API.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/api/auth/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/api/auth/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    // ⚠️ VULN: logout không blacklist token
    @PostMapping("/api/auth/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok("Đăng xuất thành công");
    }

    // OAuth2 success - token lộ trên URL ⚠️ VULN
    @GetMapping("/oauth2/success")
    public ResponseEntity<AuthResponse> oauth2Success(
            @RequestParam String token,
            @RequestParam String name) {
        return ResponseEntity.ok(AuthResponse.builder()
                .token(token)
                .username(name)
                .role("USER")
                .build());
    }
}