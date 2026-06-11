package vn.edu.ut.Bao_mat_API.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import vn.edu.ut.Bao_mat_API.entity.User;
import vn.edu.ut.Bao_mat_API.repository.UserRepository;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        
        var oauth2User = (OAuth2User) authentication.getPrincipal();
        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");

        // ✅ TẠO/TÌM USER TỪ OAUTH2 VÀ LƯU VÀO DATABASE
        User user = findOrCreateOAuth2User(email, name);

        String token = jwtUtils.generateToken(user.getUsername(), "USER");
        String encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8);

        Cookie jwtCookie = new Cookie("JWT_TOKEN", token);
        jwtCookie.setHttpOnly(false);  // ✅ Set false để JavaScript có thể đọc cookie
        jwtCookie.setSecure(false);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(24 * 60 * 60);
        response.addCookie(jwtCookie);
        
        Cookie nameCookie = new Cookie("USER_NAME", encodedName);
        nameCookie.setHttpOnly(false);
        nameCookie.setPath("/");
        nameCookie.setMaxAge(24 * 60 * 60);
        response.addCookie(nameCookie);

        // ✅ Redirect đến trang callback để auto-inject token vào Swagger UI
        response.sendRedirect("/oauth2-callback.html");
    }

    private User findOrCreateOAuth2User(String email, String name) {
        // Nếu user đã tồn tại bằng email, trả về user đó
        var existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            return existingUser.get();
        }

        // Tạo username từ email (lấy phần trước @)
        String username = email.split("@")[0];

        // Kiểm tra nếu username đã tồn tại, thêm UUID để tránh trùng
        if (userRepository.existsByUsername(username)) {
            username = username + "_" + System.currentTimeMillis();
        }

        // Tạo user mới cho OAuth2
        User user = User.builder()
                .username(username)
                .email(email)
                .password("OAUTH2_USER")  // Không dùng password cho OAuth2
                .role("USER")
                .build();

        return userRepository.save(user);
    }
}
