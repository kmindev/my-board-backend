package org.kmin.board.api.apps.comment.presentation.controller;

import org.kmin.board.api.common.presentation.controller.BaseController;
import org.kmin.board.api.common.presentation.dto.response.ApiResponse;
import org.kmin.board.api.apps.article.presentation.dto.response.ArticleDetailsResponse;
import org.kmin.board.api.apps.comment.presentation.dto.request.NewCommentRequest;
import org.kmin.board.api.apps.auth.basic.domain.BoardUserDetails;
import org.kmin.board.api.apps.comment.application.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "댓글 API")
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1/comments")
@RestController
public class CommentController extends BaseController {

    private final CommentService commentService;

    @Operation(summary = "댓글 등록 API")
    @PostMapping
    public ResponseEntity<ApiResponse<ArticleDetailsResponse>> newComment(
            @RequestBody @Valid NewCommentRequest request,
            @AuthenticationPrincipal BoardUserDetails boardUserDetails,
            HttpServletRequest httpServletRequest
    ) {
        requestLog(log, httpServletRequest, request);
        ApiResponse<ArticleDetailsResponse> response = ApiResponse.okWithData(
                ArticleDetailsResponse.from(
                        commentService.newComment(request.toDto(boardUserDetails.toDto()))
                )
        );
        responseLog(log, httpServletRequest, response);
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "댓글 삭제 API")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @PathVariable
            @Parameter(name = "commentId", description = "댓글 ID", required = true, example = "1")
            Long commentId,
            @AuthenticationPrincipal BoardUserDetails boardUserDetails,
            HttpServletRequest httpServletRequest
    ) {
        requestLog(log, httpServletRequest);
        commentService.deleteComment(commentId, boardUserDetails.userId());
        ApiResponse<Void> response = ApiResponse.okWithMessage("삭제 성공.");
        responseLog(log, httpServletRequest, response);
        return ResponseEntity.ok().body(response);
    }

}
