package vn.edu.ut.Bao_mat_API.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OAuth2CallbackController {

    @GetMapping("/oauth2/callback")
    public String oauth2Callback(HttpServletRequest request, Model model) {
        String token = null;
        String userName = null;

        // Lấy token từ cookie
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("JWT_TOKEN".equals(cookie.getName())) {
                    token = cookie.getValue();
                }
                if ("USER_NAME".equals(cookie.getName())) {
                    userName = cookie.getValue();
                }
            }
        }

        // Truyền token và userName sang Thymeleaf template
        model.addAttribute("token", token);
        model.addAttribute("userName", userName);

        return "oauth2-callback";
    }
}
