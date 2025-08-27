package com.back.article.application.dto;

import com.back.comment.domain.Comment;

import com.back.user.application.dto.UserAccountDto;
import java.time.LocalDateTime;

public record CommentDto(
    Long id, // id
    Long articleId, // 게시글 id
    UserAccountDto userAccountDto, // 작성자
    String content, // 댓글 내용
    LocalDateTime createdAt, // 작성일시
    String createdBy, // 작성자
    LocalDateTime modifiedAt, // 수정일시
    String modifiedBy // 수정자
) {
    public static CommentDto from(Comment entity) {
        return new CommentDto(
                entity.getId(),
                entity.getArticle().getId(),
                UserAccountDto.from(entity.getUserAccount()),
                entity.getContent(),
                entity.getCreatedAt(),
                entity.getCreatedBy(),
                entity.getModifiedAt(),
                entity.getModifiedBy()
        );
    }
}
