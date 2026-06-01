package vn.edu.ut.Bao_mat_API.service;

import vn.edu.ut.Bao_mat_API.dto.request.CommentRequest;
import vn.edu.ut.Bao_mat_API.dto.response.CommentResponse;
import vn.edu.ut.Bao_mat_API.entity.Book;
import vn.edu.ut.Bao_mat_API.entity.Comment;
import vn.edu.ut.Bao_mat_API.entity.User;
import vn.edu.ut.Bao_mat_API.repository.BookRepository;
import vn.edu.ut.Bao_mat_API.repository.CommentRepository;
import vn.edu.ut.Bao_mat_API.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public List<CommentResponse> getCommentsByBook(Long bookId) {
        return commentRepository.findByBookIdAndParentIsNull(bookId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public CommentResponse addComment(Long bookId, String username, CommentRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sách id: " + bookId));

        Comment parent = null;
        if (request.getParentId() != null) {
            parent = commentRepository.findById(request.getParentId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy comment id: " + request.getParentId()));
        }

        Comment comment = Comment.builder()
                .content(request.getContent())
                .user(user)
                .book(book)
                .parent(parent)
                .build();

        return toResponse(commentRepository.save(comment));
    }

    public CommentResponse updateComment(Long commentId, String username, CommentRequest request) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy comment id: " + commentId));
                
        // ✅ ĐÃ VÁ LỖI IDOR: Kiểm tra xem người sửa có phải là chủ nhân comment không
        if (!comment.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Lỗi IDOR: Bạn không có quyền sửa bình luận của người khác!");
        }

        // ⚠️ VULN: IDOR - không kiểm tra comment có thuộc về user này không
        // Bất kỳ user nào cũng sửa được comment của người khác
        comment.setContent(request.getContent());
        return toResponse(commentRepository.save(comment));
    }

    public void deleteComment(Long commentId, String username) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy comment id: " + commentId));
        // ✅ ĐÃ VÁ LỖI IDOR: Kiểm tra xem người xóa có phải là chủ nhân comment không
        if (!comment.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Lỗi IDOR: Bạn không có quyền xóa bình luận của người khác!");
        }
        // ⚠️ VULN: IDOR - không kiểm tra ownership
        // Bất kỳ user nào cũng xóa được comment của người khác
        commentRepository.delete(comment);
    }

    private CommentResponse toResponse(Comment comment) {
        List<CommentResponse> replies = comment.getReplies() != null
                ? comment.getReplies().stream()
                    .map(this::toResponse)
                    .collect(Collectors.toList())
                : Collections.emptyList();

        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .username(comment.getUser().getUsername())
                .userId(comment.getUser().getId())
                .createdAt(comment.getCreatedAt())
                .replies(replies)
                .build();
    }
}