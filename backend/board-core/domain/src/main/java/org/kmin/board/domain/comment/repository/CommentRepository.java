package org.kmin.board.domain.comment.repository;

import org.kmin.board.domain.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
