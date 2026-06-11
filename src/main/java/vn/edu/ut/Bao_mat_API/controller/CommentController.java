package vn.edu.ut.Bao_mat_API.controller;

import vn.edu.ut.Bao_mat_API.dto.request.CommentRequest;
import vn.edu.ut.Bao_mat_API.dto.response.CommentResponse;
import vn.edu.ut.Bao_mat_API.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // Xem tất cả comment của 1 cuốn sách
    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<CommentResponse>> getCommentsByBook(@PathVariable Long bookId) {
        return ResponseEntity.ok(commentService.getCommentsByBook(bookId));
    }

    // Thêm comment vào sách
    @PostMapping("/book/{bookId}")
    public ResponseEntity<CommentResponse> addComment(
            @PathVariable Long bookId,
            @AuthenticationPrincipal String username,
            @RequestBody CommentRequest request) {
        return ResponseEntity.ok(commentService.addComment(bookId, username, request));
    }

    // ⚠️ VULN: IDOR - user nào cũng sửa được comment của người khác
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal String username,
            @RequestBody CommentRequest request) {
        return ResponseEntity.ok(commentService.updateComment(commentId, username, request));
    }

    // ⚠️ VULN: IDOR - user nào cũng xóa được comment của người khác
    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal String username) {
        commentService.deleteComment(commentId, username);
        return ResponseEntity.ok("Xóa comment thành công");
    }
}