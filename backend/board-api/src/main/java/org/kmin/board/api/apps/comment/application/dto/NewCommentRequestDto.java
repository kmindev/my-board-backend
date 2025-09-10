package org.kmin.board.api.apps.comment.application.dto;

public record NewCommentRequestDto(
        Long articleId,
        String content,
        String userId
) {
}
