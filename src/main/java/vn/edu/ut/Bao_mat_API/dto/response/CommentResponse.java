package vn.edu.ut.Bao_mat_API.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class CommentResponse {
    private Long id;
    private String content;
    private String username;
    private Long userId;
    private LocalDateTime createdAt;
    // Nested replies
    private List<CommentResponse> replies;
}