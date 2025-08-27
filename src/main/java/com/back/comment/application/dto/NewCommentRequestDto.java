package com.back.comment.application.dto;

public record NewCommentRequestDto(
        Long articleId,
        String content,
        String userId
) {
}
