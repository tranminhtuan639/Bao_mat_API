package vn.edu.ut.Bao_mat_API.dto.request;

import lombok.Data;

@Data
public class CommentRequest {
    private String content;
    // null = comment gốc, có giá trị = reply
    private Long parentId;
}