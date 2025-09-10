package org.kmin.board.api.apps.article.presentation.dto.response;

import org.kmin.board.api.apps.article.application.dto.ArticleWithHashtagsDto;
import org.kmin.board.api.apps.article.application.dto.HashtagDto;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public record ArticleWithHashtagsResponse(
        Long id,
        String title,
        String content,
        String userId,
        Set<String> hashtagNames,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime modifiedAt,
        String modifiedBy
) {
    public static ArticleWithHashtagsResponse from(ArticleWithHashtagsDto articleWithHashtagsDto) {
        return new ArticleWithHashtagsResponse(
                articleWithHashtagsDto.id(),
                articleWithHashtagsDto.title(),
                articleWithHashtagsDto.content(),
                articleWithHashtagsDto.userAccountDto().userId(),
                articleWithHashtagsDto.hashtagDtos().stream()
                        .map(HashtagDto::hashtagName)
                        .collect(Collectors.toCollection(LinkedHashSet::new)),
                articleWithHashtagsDto.createdAt(),
                articleWithHashtagsDto.createdBy(),
                articleWithHashtagsDto.modifiedAt(),
                articleWithHashtagsDto.modifiedBy()
        );
    }
}
