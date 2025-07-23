package com.back.controler.dto.request;

import com.back.service.dto.NewArticleRequestDto;
import com.back.service.dto.UserAccountDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record NewArticleRequest(
        @Schema(description = "게시글 제목", example = "오늘 날씨 - 1")
        @NotBlank(message = "제목을 입력하세요.")
        String title,
        @Schema(description = "게시글 본문", example = "오늘 날씨 정말 좋아요~~~~")
        @NotBlank(message = "내용을 입력하세요.")
        String content
) {
    public NewArticleRequestDto toDto(UserAccountDto userAccountDto) {
        return new NewArticleRequestDto(this.title, this.content, userAccountDto.userId());
    }

}
