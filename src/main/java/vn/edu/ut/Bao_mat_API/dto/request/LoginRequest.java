package vn.edu.ut.Bao_mat_API.dto.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}