package vn.edu.ut.Bao_mat_API.dto.request;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String email;
    private String password;
    // ⚠️ VULN: Mass Assignment
    // User tự gửi role=ADMIN khi register là được
    
    // Đã xóa biến role để client không thể tự phong ADMIN
    // private String role;
}