package com.back.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.back.config.TestJpaConfig;
import com.back.domain.UserAccount;
import com.back.domain.UserAccountMockDataFactory;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;


@DisplayName("Repository - 회원")
@Sql(scripts = "/sql/data.sql")
@Import(TestJpaConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DataJpaTest
class UserAccountRepositoryTest {

    @Autowired
    private UserAccountRepository sut;

    @DisplayName("회원 ID를 전달하면, 해당되는 회원 엔티티를 조회할 수 있다")
    @Test
    void givenUserId_whenFindById_thenReturnsMatchingUser() {
        // Given
        String userId = "user1";

        // When
        Optional<UserAccount> result = sut.findById(userId);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getUserId()).isEqualTo(userId);
    }

    @DisplayName("회원엔티티를 전달하면, DB에 저장한다.")
    @Test
    void givenUser_whenSave_thenUserIsSaved() {
        // Given
        String userId = "test-user1";
        UserAccount userAccount = UserAccountMockDataFactory.createDBUserAccountFromUserId(userId);

        // When
        UserAccount result = sut.save(userAccount);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(userAccount);
        assertThat(sut.findById(result.getUserId())).contains(result);
    }

}