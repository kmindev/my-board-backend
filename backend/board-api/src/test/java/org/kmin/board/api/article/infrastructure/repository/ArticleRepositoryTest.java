package org.kmin.board.api.article.infrastructure.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_CLASS;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_CLASS;

import org.kmin.board.api.common.config.TestJpaConfig;
import org.kmin.board.api.article.domain.Article;
import org.kmin.board.api.common.fixture.ArticleFixture;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

@DisplayName("Repository - 게시글")
@Sql(scripts = "/sql/data.sql", executionPhase = BEFORE_TEST_CLASS)
@Import(TestJpaConfig.class)
@DirtiesContext(classMode = BEFORE_CLASS)
@DataJpaTest
class ArticleRepositoryTest {

    @Autowired
    private ArticleRepository sut;

    @DisplayName("게시글 ID를 전달하면, 해당되는 게시글을 조회할 수 있다")
    @Test
    void givenArticleId_whenFindById_thenReturnsMatchingArticle() {
        // Given
        Long articleId = 1L;

        // When
        Optional<Article> result = sut.findById(articleId);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(articleId);
    }

    @DisplayName("존재하지 않는 게시글 ID를 전달하면, 빈 Optional을 반환한다")
    @Test
    void givenNotExistArticleId_whenFindById_thenReturnsEmptyOptional() {
        // Given
        Long articleId = -1L;

        // When
        Optional<Article> result = sut.findById(articleId);

        // Then
        assertThat(result).isNotPresent();
    }

    @DisplayName("페이징 정보를 전달하면, 게시글을 페이지 단위로 조회할 수 있다")
    @Test
    void givenPageable_whenFindAll_thenReturnsMatchingArticles() {
        // Given
        int pageSize = 10;
        Pageable pageable = PageRequest.of(0, pageSize);

        // When
        Page<Article> result = sut.findAll(pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(pageSize);
    }

    @DisplayName("검색어와 페이징 정보를 전달하면, 제목에 검색어가 포함된 게시글을 페이지 단위로 조회할 수 있다")
    @Test
    void givenSearchValueAndPageable_whenFindByTitleContaining_thenReturnsMatchingArticles() {
        // Given
        String searchValue = "우승";
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Article> result = sut.findByTitleContaining(searchValue, pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(3);
        assertThat(result.getContent())
                .extracting(Article::getTitle)
                .allMatch(title -> title.contains(searchValue));
    }

    @DisplayName("검색어와 페이징 정보를 전달하면, 본문에 검색어가 포함된 게시글을 페이지 단위로 조회할 수 있다")
    @Test
    void givenSearchValueAndPageable_whenFindByContentContaining_thenReturnsMatchingArticles() {
        // Given
        String searchValue = "야구";
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Article> result = sut.findByContentContaining(searchValue, pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(3);
        assertThat(result.getContent())
                .extracting(Article::getContent)
                .allMatch(content -> content.contains(searchValue));
    }

    @DisplayName("게시글 작성자 ID와 페이징 정보를 전달하면, 작성자 ID 해당되는 게시글을 페이지 단위로 조회할 수 있다")
    @Test
    void givenSearchValueAndPageable_whenFindByUserIdContaining_thenReturnsMatchingArticles() {
        // Given
        String searchValue = "user1";
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Article> result = sut.findByUserAccount_UserIdContaining(searchValue, pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent())
                .extracting(article -> article.getUserAccount().getUserId())
                .allMatch(userId -> userId.contains(searchValue));
    }

    @DisplayName("게시글 작성자 닉네임과 페이징 정보를 전달하면, 작성자 닉네임에 해당되는 게시글을 페이지 단위로 조회할 수 있다")
    @Test
    void givenSearchValueAndPageable_whenFindByUserNicknameContaining_thenReturnsMatchingArticles() {
        // Given
        String searchValue = "별빛1";
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Article> result = sut.findByUserAccount_NicknameContaining(searchValue, pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent())
                .extracting(article -> article.getUserAccount().getNickname())
                .allMatch(nickname -> nickname.contains(searchValue));
    }

    @DisplayName("해시태그명들과 페이징 정보를 전달하면, 해시태그명에 해당되는 게시글을 페이지 단위로 조회할 수 있다")
    @Test
    void givenSearchValueAndPageable_whenFindByHashtagNames_thenReturnsMatchingArticles() {
        // Given
        Set<String> hashtags = Set.of("java");
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Article> result = sut.findByHashtagNames(hashtags, pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent())
                .flatExtracting(Article::getArticleHashtags)
                .extracting(articleHashtag -> articleHashtag.getHashtag().getHashtagName())
                .anyMatch(hashtags::contains);
    }

    @DisplayName("게시글을 전달하면, DB에 저장한다")
    @Test
    void givenArticle_whenSave_thenReturnsSavedArticle() {
        // Given
        Article article = ArticleFixture.createDBArticle();

        // When
        Article result = sut.save(article);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(article);
        assertThat(sut.findById(result.getId())).contains(result);
    }

    @DisplayName("게시글 ID를 전달하면, DB에 삭제한다")
    @Test
    void givenArticleId_whenDeleteById_thenArticleIsDeleted() {
        // Given
        Long articleId = 1L;

        // When
        sut.deleteById(articleId);

        // Then
        assertThat(sut.findById(articleId)).isNotPresent();
    }

}