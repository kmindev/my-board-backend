package com.back.controler.dto.request;

import com.back.service.dto.NewCommentRequestDto;
import com.back.service.dto.UserAccountDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NewCommentRequest(
        @NotNull(message = "게시글 ID는 필수입니다.")
        @Schema(description = "게시글 ID", example = "1")
        Long articleId,
        @NotBlank(message = "댓글 내용을 입력하세요.")
        @Schema(description = "댓글 내용", example = "게시글 잘봤습니다!")
        String content
) {
    public NewCommentRequestDto toDto(UserAccountDto userAccountDto) {
        return new NewCommentRequestDto(this.articleId, this.content, userAccountDto.userId());
    }
}
