package org.kmin.board.api.comment.application.dto;

public record NewCommentRequestDto(
        Long articleId,
        String content,
        String userId
) {
}
