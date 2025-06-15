package com.back.controler;

import com.back.controler.dto.reponse.ApiResponse;
import com.back.controler.dto.reponse.ArticleDetailsResponse;
import com.back.controler.dto.request.NewCommentRequest;
import com.back.secuirty.BoardUserDetails;
import com.back.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1/comments")
@RestController
public class CommentController extends BaseController {

    private final CommentService commentService;

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

    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @PathVariable Long commentId,
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
