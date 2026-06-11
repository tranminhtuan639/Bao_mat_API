package vn.edu.ut.Bao_mat_API.dto.request;

import lombok.Data;

@Data
public class BookRequest {
    private String title;
    private String author;
    private String imageUrl;
    private String description;
}