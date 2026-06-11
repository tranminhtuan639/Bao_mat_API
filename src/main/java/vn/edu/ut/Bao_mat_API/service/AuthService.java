package vn.edu.ut.Bao_mat_API.service;

import vn.edu.ut.Bao_mat_API.dto.request.LoginRequest;
import vn.edu.ut.Bao_mat_API.dto.request.RegisterRequest;
import vn.edu.ut.Bao_mat_API.dto.response.AuthResponse;
import vn.edu.ut.Bao_mat_API.entity.User;
import vn.edu.ut.Bao_mat_API.repository.UserRepository;
import vn.edu.ut.Bao_mat_API.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username đã tồn tại");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email đã tồn tại");
        }

        // ⚠️ VULN: Mass Assignment
        // Lấy role trực tiếp từ request, user tự set role=ADMIN được
        // String role = request.getRole() != null ? request.getRole() : "USER";
        
        // ✅ ĐÃ VÁ LỖI: Gán cứng quyền USER cho mọi tài khoản mới
        String role = "USER";
        
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .build();

        userRepository.save(user);

        String token = jwtUtils.generateToken(user.getUsername(), user.getRole());
        return AuthResponse.builder()
                .token(token)
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Sai mật khẩu");
        }

        String token = jwtUtils.generateToken(user.getUsername(), user.getRole());
        return AuthResponse.builder()
                .token(token)
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }
}