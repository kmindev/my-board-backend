package com.back.controler;

import com.back.controler.dto.reponse.ApiResponse;
import com.back.controler.dto.reponse.ArticleDetailsResponse;
import com.back.controler.dto.reponse.ArticleWithHashtagsResponse;
import com.back.controler.dto.reponse.SearchArticleResponse;
import com.back.controler.dto.request.ArticleUpdateRequest;
import com.back.controler.dto.request.NewArticleRequest;
import com.back.domain.constant.SearchType;
import com.back.secuirty.BoardUserDetails;
import com.back.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "게시글 API")
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1/articles")
@RestController
public class ArticleController extends BaseController {

    private final ArticleService articleService;

    @Operation(summary = "게시글 등록 API")
    @PostMapping
    public ResponseEntity<ApiResponse<ArticleWithHashtagsResponse>> newArticle(
            @RequestBody @Valid NewArticleRequest request,
            @AuthenticationPrincipal BoardUserDetails boardUserDetails,
            HttpServletRequest httpServletRequest
    ) {
        requestLog(log, httpServletRequest, request);
        ApiResponse<ArticleWithHashtagsResponse> response = ApiResponse.okWithData(
                ArticleWithHashtagsResponse.from(
                        articleService.newArticle(request.toDto(boardUserDetails.toDto()))
                )
        );
        responseLog(log, httpServletRequest, response);
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "게시글 검색 API")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<SearchArticleResponse>>> getArticles(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            @ParameterObject
            Pageable pageable,
            @RequestParam(required = false)
            @Parameter(name = "searchValue", description = "키워드", example = "jpa")
            String searchValue,
            @RequestParam(required = false)
            @Parameter(name = "searchType", description = "검색 항목", example = "TITLE")
            SearchType searchType,
            HttpServletRequest httpServletRequest
    ) {
        requestLog(log, httpServletRequest);
        ApiResponse<Page<SearchArticleResponse>> response = ApiResponse.okWithData(
                articleService.searchArticles(pageable, searchValue, searchType)
                        .map(SearchArticleResponse::from)
        );
        responseLog(log, httpServletRequest, response);
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "게시글 상세 조회 API")
    @GetMapping("/{articleId}")
    public ResponseEntity<ApiResponse<ArticleDetailsResponse>> getArticleDetails(
            @PathVariable
            @Parameter(name = "articleId", description = "게시글 ID", required = true, example = "1")
            Long articleId,
            HttpServletRequest httpServletRequest
    ) {
        requestLog(log, httpServletRequest);
        ApiResponse<ArticleDetailsResponse> response = ApiResponse.okWithData(
                ArticleDetailsResponse.from(articleService.getArticleDetails(articleId))
        );
        responseLog(log, httpServletRequest, response);
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "게시글 수정 API")
    @PatchMapping("/{articleId}")
    public ResponseEntity<ApiResponse<ArticleWithHashtagsResponse>> updateArticle(
            @PathVariable
            @Parameter(name = "articleId", description = "게시글 ID", required = true, example = "1")
            Long articleId,
            @RequestBody @Valid ArticleUpdateRequest request,
            @AuthenticationPrincipal BoardUserDetails boardUserDetails,
            HttpServletRequest httpServletRequest
    ) {
        requestLog(log, httpServletRequest, request);
        ApiResponse<ArticleWithHashtagsResponse> response = ApiResponse.okWithData(
                ArticleWithHashtagsResponse.from(
                        articleService.updateArticle(request.toDto(articleId, boardUserDetails.toDto()))
                )
        );
        responseLog(log, httpServletRequest, response);
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "게시글 삭제 API")
    @DeleteMapping("/{articleId}")
    public ResponseEntity<ApiResponse<Void>> updateArticle(
            @PathVariable
            @Parameter(name = "articleId", description = "게시글 ID", required = true, example = "1")
            Long articleId,
            @AuthenticationPrincipal BoardUserDetails boardUserDetails,
            HttpServletRequest httpServletRequest
    ) {
        requestLog(log, httpServletRequest);
        articleService.deleteArticle(articleId, boardUserDetails.userId());
        ApiResponse<Void> response = ApiResponse.okWithMessage("삭제 성공.");
        responseLog(log, httpServletRequest, response);
        return ResponseEntity.ok().body(response);
    }

}
