package com.back.article.presentation.controller;

import static com.back.common.config.TestSecurityUtil.boardUserDetails;
import static com.back.common.fixture.ArticleUpdateRequestFixture.createArticleUpdateRequest;
import static com.back.common.fixture.NewArticleRequestFixture.createDefaultNewArticleRequest;
import static com.back.common.fixture.ArticleWithCommentsWithHashtagsDtoFixture.createArticleWithCommentsWithHashtagsDto;
import static com.back.common.fixture.ArticleWithHashtagsDtoFixture.createArticleWithHashtagsDto;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.back.common.config.JsonDataEncoder;
import com.back.common.config.SecurityConfig;
import com.back.common.exception.GlobalExceptionRestAdvice;
import com.back.article.presentation.dto.request.ArticleUpdateRequest;
import com.back.article.presentation.dto.request.NewArticleRequest;
import com.back.user.domain.UserRoleType;
import com.back.article.domain.SearchType;
import com.back.article.domain.exception.ArticleNotFoundException;
import com.back.user.domain.exception.UserMismatchException;
import com.back.user.domain.exception.UserNotFoundException;
import com.back.auth.infrastructure.security.ApiAccessDeniedHandler;
import com.back.auth.infrastructure.security.ApiAuthenticationFailureHandler;
import com.back.auth.infrastructure.security.ApiAuthenticationSuccessHandler;
import com.back.auth.infrastructure.security.ApiLoginAuthenticationEntryPoint;
import com.back.auth.infrastructure.security.ApiLogoutSuccessHandler;
import com.back.article.application.ArticleService;
import com.back.article.application.dto.ArticleUpdateDto;
import com.back.article.application.dto.ArticleWithCommentsWithHashtagsDto;
import com.back.article.application.dto.ArticleWithHashtagsDto;
import com.back.article.application.dto.NewArticleRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;


@DisplayName("컨트롤러 - 게시글")
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
@WebMvcTest(controllers = ArticleController.class)
class ArticleControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private JsonDataEncoder jsonDataEncoder;

    @MockitoSpyBean
    private GlobalExceptionRestAdvice globalExceptionRestAdvice;
    @MockitoBean
    private ArticleService articleService;

    @DisplayName("게시글 생성 요청 - 성공")
    @Test
    void givenNewArticleRequest_whenNewArticle_thenReturns200() throws Exception {
        // Given
        NewArticleRequest request = createDefaultNewArticleRequest();
        ArticleWithHashtagsDto articleWithHashtagsDto = createArticleWithHashtagsDto();
        given(articleService.newArticle(any(NewArticleRequestDto.class))).willReturn(articleWithHashtagsDto);

        // When & Then
        mvc.perform(post("/v1/articles")
                        .with(boardUserDetails("user1", UserRoleType.USER))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonDataEncoder.encode(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.message").isEmpty());
        then(articleService).should().newArticle(any(NewArticleRequestDto.class));
    }

    @DisplayName("게시글 생성 요청 - 실패(로그인 하지 않은 경우)")
    @Test
    void givenNewArticleRequestWithoutUser_whenNewArticle_thenReturns4xx() throws Exception {
        // Given
        NewArticleRequest request = createDefaultNewArticleRequest();

        // When & Then
        mvc.perform(post("/v1/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonDataEncoder.encode(request)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message").value("로그인이 필요합니다."));
    }

    @DisplayName("게시글 생성 요청 - 실패(유저를 찾을 수 없을 때)")
    @Test
    void givenNewArticleRequest_whenNewArticle_thenReturns4xx() throws Exception {
        // Given
        NewArticleRequest request = createDefaultNewArticleRequest();
        UserNotFoundException exception = new UserNotFoundException();
        given(articleService.newArticle(any(NewArticleRequestDto.class))).willThrow(exception);

        // When & Then
        mvc.perform(post("/v1/articles")
                        .with(boardUserDetails("user1", UserRoleType.USER))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonDataEncoder.encode(request)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message").value(exception.getMessage()));
        then(articleService).should().newArticle(any(NewArticleRequestDto.class));
        then(globalExceptionRestAdvice).should().applicationException(eq(exception));
    }

    @DisplayName("게시글 조건 검색 - 성공")
    @Test
    void givenSearchParams_whenGetArticles_thenReturns200() throws Exception {
        // Given
        String searchValue = "test1";
        SearchType searchType = SearchType.CONTENT;
        given(articleService.searchArticles(any(Pageable.class), eq(searchValue), eq(searchType)))
                .willReturn(Page.empty());

        // When & Then
        mvc.perform(get("/v1/articles")
                        .queryParam("searchValue", searchValue)
                        .queryParam("searchType", searchType.name()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.message").isEmpty());
        then(articleService).should().searchArticles(any(Pageable.class), eq(searchValue), eq(searchType));
    }

    @DisplayName("게시글 페이징, 정렬 검색 - 성공")
    @Test
    void givenPagingAndSortingParams_whenGetArticles_thenReturns200() throws Exception {
        // Given
        String sortName = "title";
        String direction = "desc";
        int pageNumber = 0;
        int pageSize = 5;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Order.desc(sortName)));
        given(articleService.searchArticles(pageable, null, null))
                .willReturn(Page.empty());

        // When & Then
        mvc.perform(get("/v1/articles")
                        .queryParam("page", String.valueOf(pageNumber))
                        .queryParam("size", String.valueOf(pageSize))
                        .queryParam("sort", sortName + "," + direction))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.message").isEmpty());
        then(articleService).should().searchArticles(pageable, null, null);
    }

    @DisplayName("게시글 검색 - 실패(잘못된 검색 타입)")
    @Test
    void givenNonExitingSearchType_whenNewArticle_thenReturns4xx() throws Exception {
        // Given
        String searchValue = "test1";
        String searchTypeStr = "잘못된 타입";

        // When & Then
        mvc.perform(get("/v1/articles")
                        .queryParam("searchValue", searchValue)
                        .queryParam("searchType", searchTypeStr))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message").isNotEmpty());
        then(globalExceptionRestAdvice).should().handleMethoArgumentTypeMismatchExceptions(
                any(MethodArgumentTypeMismatchException.class));
    }

    @DisplayName("게시글 상세 조회 - 성공")
    @Test
    void givenArticleId_whenGetArticleDetails_thenReturns200() throws Exception {
        // Given
        Long articleId = 1L;
        ArticleWithCommentsWithHashtagsDto dto = createArticleWithCommentsWithHashtagsDto();
        given(articleService.getArticleDetails(eq(articleId))).willReturn(dto);

        // When & Then
        mvc.perform(get("/v1/articles/{articleId}", articleId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.message").isEmpty());
        then(articleService).should().getArticleDetails(eq(articleId));
    }

    @DisplayName("게시글 상세 조회 - 실패(게시글을 찾을 수 없을 때)")
    @Test
    void givenNonExitingArticleId_whenGetArticleDetails_thenReturns4xx() throws Exception {
        // Given
        Long nonExitingArticleId = 100L;
        ArticleNotFoundException exception = new ArticleNotFoundException();
        given(articleService.getArticleDetails(eq(nonExitingArticleId))).willThrow(exception);

        // When & Then
        mvc.perform(get("/v1/articles/{articleId}", nonExitingArticleId))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message").value(exception.getMessage()));
        then(articleService).should().getArticleDetails(eq(nonExitingArticleId));
        then(globalExceptionRestAdvice).should().applicationException(eq(exception));
    }

    @DisplayName("게시글 수정 - 성공")
    @Test
    void givenArticleUpdateRequest_whenUpdateArticle_thenReturns200() throws Exception {
        // Given
        Long articleId = 1L;
        ArticleUpdateRequest request = createArticleUpdateRequest();
        ArticleWithHashtagsDto articleWithHashtagsDto = createArticleWithHashtagsDto();
        given(articleService.updateArticle(any(ArticleUpdateDto.class))).willReturn(articleWithHashtagsDto);

        // When & Then
        mvc.perform(patch("/v1/articles/{articleId}", articleId)
                        .with(boardUserDetails("user1", UserRoleType.USER))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonDataEncoder.encode(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.message").isEmpty());
        then(articleService).should().updateArticle(any(ArticleUpdateDto.class));
    }

    @DisplayName("게시글 수정 - 실패(로그인 하지 않았을 때)")
    @Test
    void givenArticleUpdateRequestWithoutUser_whenUpdateArticle_thenReturns4xx() throws Exception {
        // Given
        Long articleId = 1L;
        ArticleUpdateRequest request = createArticleUpdateRequest();

        // When & Then
        mvc.perform(patch("/v1/articles/{articleId}", articleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonDataEncoder.encode(request)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message").value("로그인이 필요합니다."));
    }

    @DisplayName("게시글 수정 - 실패(작성자가 일치하지 않을 때)")
    @Test
    void givenArticleUpdateRequestAndInvalidUser_whenUpdateArticle_thenReturns4xx() throws Exception {
        // Given
        Long articleId = 1L;
        ArticleUpdateRequest request = createArticleUpdateRequest();
        UserMismatchException exception = new UserMismatchException();
        given(articleService.updateArticle(any(ArticleUpdateDto.class))).willThrow(exception);

        // When & Then
        mvc.perform(patch("/v1/articles/{articleId}", articleId)
                        .with(boardUserDetails("user1", UserRoleType.USER))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonDataEncoder.encode(request)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message").value(exception.getMessage()));
        then(articleService).should().updateArticle(any(ArticleUpdateDto.class));
        then(globalExceptionRestAdvice).should().applicationException(eq(exception));
    }

    @DisplayName("게시글 삭제 - 성공")
    @Test
    void givenArticleIdAndUserId_whenDeleteArticle_thenReturns200() throws Exception {
        // Given
        Long articleId = 1L;
        willDoNothing().given(articleService).deleteArticle(any(), any());

        // When & Then
        mvc.perform(delete("/v1/articles/{articleId}", articleId)
                        .with(boardUserDetails("user1", UserRoleType.USER)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message").value("삭제 성공."));
        then(articleService).should().deleteArticle(any(), any());
    }

    @DisplayName("게시글 삭제 - 실패(로그인 하지 않았을 때)")
    @Test
    void givenArticleIdWithoutUser_whenDeleteArticle_thenReturns4xx() throws Exception {
        // Given
        Long articleId = 1L;

        // When & Then
        mvc.perform(delete("/v1/articles/{articleId}", articleId))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message").value("로그인이 필요합니다."));
    }

    @DisplayName("게시글 삭제 - 실패(작성자가 일치하지 않을 때)")
    @Test
    void givenArticleIdAndInvalidUser_whenDeleteArticle_thenReturns4xx() throws Exception {
        // Given
        Long articleId = 1L;
        UserMismatchException exception = new UserMismatchException();
        willThrow(exception).given(articleService).deleteArticle(any(), any());

        // When & Then
        mvc.perform(delete("/v1/articles/{articleId}", articleId)
                        .with(boardUserDetails("user1", UserRoleType.USER)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message").value(exception.getMessage()));
        then(articleService).should().deleteArticle(any(), any());
        then(globalExceptionRestAdvice).should().applicationException(eq(exception));
    }

}