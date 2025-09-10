package org.kmin.board.api.apps.comment.presentation.controller;

import static org.kmin.board.api.common.config.TestSecurityUtil.boardUserDetails;
import static org.kmin.board.api.common.fixture.NewCommentRequestFixture.createNewCommentRequest;
import static org.kmin.board.api.common.fixture.ArticleWithCommentsWithHashtagsDtoFixture.createArticleWithCommentsWithHashtagsDto;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.kmin.board.api.common.config.JsonDataEncoder;
import org.kmin.board.api.common.config.SecurityConfig;
import org.kmin.board.api.common.exception.GlobalExceptionRestAdvice;
import org.kmin.board.api.apps.comment.presentation.dto.request.NewCommentRequest;
import org.kmin.board.domain.user.UserRoleType;
import org.kmin.board.api.apps.article.application.exception.ArticleNotFoundException;
import org.kmin.board.api.apps.comment.application.exception.CommentNotFoundException;
import org.kmin.board.api.apps.auth.common.ApiAccessDeniedHandler;
import org.kmin.board.api.apps.auth.basic.presentation.ApiAuthenticationFailureHandler;
import org.kmin.board.api.apps.auth.basic.presentation.ApiAuthenticationSuccessHandler;
import org.kmin.board.api.apps.auth.common.ApiLoginAuthenticationEntryPoint;
import org.kmin.board.api.apps.auth.common.ApiLogoutSuccessHandler;
import org.kmin.board.api.apps.comment.application.CommentService;
import org.kmin.board.api.apps.article.application.dto.ArticleWithCommentsWithHashtagsDto;
import org.kmin.board.api.apps.comment.application.dto.NewCommentRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;

@DisplayName("컨트롤러 - 댓글")
@Import({
        JsonDataEncoder.class,
        SecurityConfig.class,
        AuthenticationConfiguration.class,
        ApiAuthenticationSuccessHandler.class,
        ApiAuthenticationFailureHandler.class,
        ApiAccessDeniedHandler.class,
        ApiLoginAuthenticationEntryPoint.class,
        ApiLogoutSuccessHandler.class
})
@WebMvcTest(controllers = CommentController.class)
public class CommentControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private JsonDataEncoder jsonDataEncoder;

    @MockitoBean
    private CommentService commentService;
    @MockitoSpyBean
    private GlobalExceptionRestAdvice globalExceptionRestAdvice;

    @DisplayName("댓글 등록 요청 - 성공")
    @Test
    void givenNewCommentRequest_whenNewComment_thenReturns200() throws Exception {
        // Given
        NewCommentRequest request = createNewCommentRequest();
        ArticleWithCommentsWithHashtagsDto dto = createArticleWithCommentsWithHashtagsDto();
        given(commentService.newComment(any(NewCommentRequestDto.class))).willReturn(dto);

        // When & Then
        mvc.perform(post("/v1/comments")
                        .with(boardUserDetails("user1", UserRoleType.USER))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonDataEncoder.encode(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.message").isEmpty());
        then(commentService).should().newComment(any(NewCommentRequestDto.class));
    }

    @DisplayName("댓글 등록 요청 - 실패(로그인을 안한 경우)")
    @Test
    void givenNewCommentRequestWithoutUser_whenNewComment_thenReturns4xx() throws Exception {
        // Given
        NewCommentRequest request = createNewCommentRequest();

        // When & Then
        mvc.perform(post("/v1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonDataEncoder.encode(request)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message").value("로그인이 필요합니다."));
    }

    @DisplayName("댓글 등록 요청 - 실패(게시글을 찾을 수 없을 때)")
    @Test
    void givenNewCommentRequestAndNotExistingArticleId_whenNewComment_thenReturns4xx() throws Exception {
        // Given
        NewCommentRequest request = createNewCommentRequest();
        ArticleNotFoundException exception = new ArticleNotFoundException();
        given(commentService.newComment(any(NewCommentRequestDto.class))).willThrow(exception);

        // When & Then
        mvc.perform(post("/v1/comments")
                        .with(boardUserDetails("user1", UserRoleType.USER))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonDataEncoder.encode(request)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message").value(exception.getMessage()));
        then(commentService).should().newComment(any(NewCommentRequestDto.class));
        then(globalExceptionRestAdvice).should().applicationException(eq(exception));
    }

    @DisplayName("댓글 삭제 요청 - 성공")
    @Test
    void givenCommentId_whenDeleteComment_thenReturns200() throws Exception {
        // Given
        Long commentId = 1L;
        willDoNothing().given(commentService).deleteComment(any(), any());

        // When & Then
        mvc.perform(delete("/v1/comments/{commentId}", commentId)
                        .with(boardUserDetails("user1", UserRoleType.USER)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message").value("삭제 성공."));
        then(commentService).should().deleteComment(any(), any());
    }

    @DisplayName("댓글 삭제 요청 - 실패(로그인을 안한 경우)")
    @Test
    void givenCommentIdWithoutUser_whenDeleteComment_thenReturns4xx() throws Exception {
        // Given
        Long commentId = 1L;

        // When & Then
        mvc.perform(delete("/v1/comments/{commentId}", commentId))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message").value("로그인이 필요합니다."));
    }

    @DisplayName("댓글 삭제 요청 - 실패(댓글을 찾을 수 없을 때)")
    @Test
    void givenNotExistingCommentId_whenDeleteComment_thenReturns4xx() throws Exception {
        // Given
        Long commentId = 1L;
        CommentNotFoundException exception = new CommentNotFoundException();
        willThrow(exception).given(commentService).deleteComment(any(), any());

        // When & Then
        mvc.perform(delete("/v1/comments/{commentId}", commentId)
                        .with(boardUserDetails("user1", UserRoleType.USER)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message").value(exception.getMessage()));
        then(commentService).should().deleteComment(any(), any());
        then(globalExceptionRestAdvice).should().applicationException(eq(exception));
    }

}
