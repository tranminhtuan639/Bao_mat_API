package vn.edu.ut.Bao_mat_API.dto.request;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String email;
    private String password;
    // ⚠️ VULN: Mass Assignment
    // User tự gửi role=ADMIN khi register là được
    private String role;
}