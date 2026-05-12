package vn.edu.ut.Bao_mat_API.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class BookResponse {
    private Long id;
    private String title;
    private String author;
    private String imageUrl;
    private String description;
    private LocalDateTime createdAt;
}