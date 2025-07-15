package com.back.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_CLASS;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_CLASS;

import com.back.config.TestJpaConfig;
import com.back.domain.Hashtag;
import com.back.domain.HashtagMockDataFactory;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

@DisplayName("Repository - 해시태그")
@Sql(scripts = "/sql/data.sql", executionPhase = BEFORE_TEST_CLASS)
@Import(TestJpaConfig.class)
@DirtiesContext(classMode = BEFORE_CLASS)
@DataJpaTest
class HashtagRepositoryTest {

    @Autowired
    private HashtagRepository sut;

    @DisplayName("해시태그명을 전달하면, 해당되는 해시태그 엔티티를 조회할 수 있다")
    @Test
    void givenHashtagNames_whenFindByHashtagNameIn_thenReturnsMatchingHashtags() {
        // Given
        Set<String> hashtagNames = Set.of("java", "jpa");

        // When
        Set<Hashtag> result = sut.findByHashtagNameIn(hashtagNames);

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(2);
        assertThat(result)
                .extracting(Hashtag::getHashtagName)
                .anyMatch(hashtagNames::contains);
    }

    @DisplayName("N개의 해시태그엔티티를 전달하면, DB에 저장된다.")
    @Test
    void givenHashtags_whenSaveAll_thenHashtagIsSaved() {
        // Given
        Set<String> hashtagNames = Set.of("test1", "test2");
        Set<Hashtag> hashtags = hashtagNames.stream()
                .map(HashtagMockDataFactory::createHashtagFromHashtagName)
                .collect(Collectors.toSet());

        // When
        sut.saveAll(hashtags);

        // Then
        Set<Hashtag> savedHashtags = sut.findByHashtagNameIn(hashtagNames);
        assertThat(savedHashtags).isNotEmpty();
        assertThat(savedHashtags).hasSize(2);
        assertThat(savedHashtags)
                .extracting(Hashtag::getHashtagName)
                .anyMatch(hashtagNames::contains);
    }

}