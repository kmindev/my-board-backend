package org.kmin.board.api.article.presentation.dto.request;

import org.kmin.board.api.article.application.dto.ArticleUpdateDto;
import org.kmin.board.api.user.application.dto.UserAccountDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record ArticleUpdateRequest(
        @NotBlank(message = "제목을 입력하세요.")
        @Schema(description = "수정할 게시글 제목", example = "오늘 날씨 - 1")
        String title,
        @NotBlank(message = "내용을 입력하세요.")
        @Schema(description = "수정할 게시글 본문", example = "오늘 날씨 정말 좋아요~~~~")
        String content
) {

    public ArticleUpdateDto toDto(Long articleId, UserAccountDto userAccountDto) {
        return new ArticleUpdateDto(articleId, this.title, this.content, userAccountDto.userId());
    }

}
