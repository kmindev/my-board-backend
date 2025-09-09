package org.kmin.board.api.comment.infrastructure.repository;

import org.kmin.board.api.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
