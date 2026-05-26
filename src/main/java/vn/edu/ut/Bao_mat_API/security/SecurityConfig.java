package vn.edu.ut.Bao_mat_API.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final JwtUtils jwtUtils;

    @Bean
    @Order(1)
    public SecurityFilterChain swaggerFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher(
                "/swagger-ui.html",
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/v3/api-docs.yaml",
                "/webjars/**"
            )
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            // Fix: OAuth2 cần session, chỉ STATELESS cho API thôi
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            )
            .authorizeHttpRequests(auth -> auth
                // Public
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/oauth2/**").permitAll()
                .requestMatchers("/login/oauth2/**").permitAll()
                .requestMatchers("/error").permitAll()

                // Books: USER và ADMIN xem được
                .requestMatchers(HttpMethod.GET, "/api/books/**").hasAnyRole("USER", "ADMIN")

                // Books CRUD: chỉ ADMIN
                .requestMatchers(HttpMethod.POST, "/api/books/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/books/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/books/**").hasRole("ADMIN")

                // Comments: USER và ADMIN
                .requestMatchers("/api/comments/**").hasAnyRole("USER", "ADMIN")

                // Profile
                .requestMatchers("/api/me/**").hasAnyRole("USER", "ADMIN")

                // Users management: chỉ ADMIN
                .requestMatchers("/api/users/**").hasRole("ADMIN")

                .anyRequest().authenticated()
            )
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setStatus(401);
                    response.getWriter().write("Unauthorized");
                })
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.setStatus(403);
                    response.getWriter().write("Forbidden");
                })
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .oauth2Login(oauth2 -> oauth2
                .successHandler((request, response, authentication) -> {
                    var oauth2User = (OAuth2User) authentication.getPrincipal();
                    String email = oauth2User.getAttribute("email");
                    String name = oauth2User.getAttribute("name");

                    String token = jwtUtils.generateToken(email, "USER");
                    String encodedName = java.net.URLEncoder.encode(name, "UTF-8");

                    // ⚠️ VULN: token lộ trên URL
                    response.sendRedirect("/oauth2/success?token=" + token + "&name=" + encodedName);
                })
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}