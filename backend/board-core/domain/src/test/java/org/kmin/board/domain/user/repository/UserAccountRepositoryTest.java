package org.kmin.board.domain.user.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_CLASS;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_CLASS;

import org.kmin.board.domain.config.TestJpaAuditingConfig;
import org.kmin.board.domain.fixture.UserAccountFixture;
import org.kmin.board.domain.user.UserAccount;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;


@DisplayName("Repository - 회원")
@Sql(scripts = "/sql/data.sql", executionPhase = BEFORE_TEST_CLASS)
@Import({TestJpaAuditingConfig.class, org.kmin.board.domain.config.TestJpaConfig.class})
@DirtiesContext(classMode = BEFORE_CLASS)
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
        UserAccount userAccount = UserAccountFixture.createDBUserAccountFromUserId(userId);

        // When
        UserAccount result = sut.save(userAccount);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(userAccount);
        assertThat(sut.findById(result.getUserId())).contains(result);
    }

}