package vn.edu.ut.Bao_mat_API.repository;

import vn.edu.ut.Bao_mat_API.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByBookIdAndParentIsNull(Long bookId);
}