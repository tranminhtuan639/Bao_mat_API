package vn.edu.ut.Bao_mat_API.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("📚 Book Review API - Security Demo")
                        .description("""
                                ## Thực hành tấn công và bảo mật API
                                
                                ### JWT Vulnerabilities (có chủ đích):
                                - ⚠️ **IDOR** → Xóa/sửa comment của người khác
                                
                                ### OAuth2 Vulnerabilities (có chủ đích):
                                - ⚠️ **Token Exposed in URL** → Token lộ trên thanh địa chỉ
                                
                                ### Cách dùng:
                                1. Register hoặc Login để lấy token
                                2. Bấm nút **Authorize 🔒** bên phải
                                3. Nhập token vào ô **bearerAuth**
                                4. Gọi API bình thường
                                
                                ### OAuth2 Login Google:
                                Mở tab mới: [http://localhost:8080/oauth2/authorization/google](http://localhost:8080/oauth2/authorization/google)
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("UT Security Team")
                                .email("security@ut.edu.vn"))
                )
                // Thêm nút Authorize với Bearer Token
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .name("bearerAuth")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Paste JWT token vào đây (không cần thêm 'Bearer ')")
                        )
                );
    }
}