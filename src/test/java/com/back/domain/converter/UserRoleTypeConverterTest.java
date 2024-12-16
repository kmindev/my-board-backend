package com.back.domain.converter;

import com.back.domain.UserRoleType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DisplayName("UserRoleTypeConverter 테스트")
class UserRoleTypeConverterTest {

    private final UserRoleTypeConverter converter = new UserRoleTypeConverter();

    @Test
    @DisplayName("UserRoleType을 Database 컬럼 값으로 변환 테스트")
    void convertToDatabaseColumn_shouldReturnUserRoleTypeName() {
        // Given
        UserRoleType userRole = UserRoleType.USER;
        UserRoleType adminRole = UserRoleType.ADMIN;

        // When
        String userColumnValue = converter.convertToDatabaseColumn(userRole);
        String adminColumnValue = converter.convertToDatabaseColumn(adminRole);
        String nullColumnValue = converter.convertToDatabaseColumn(null);

        // Then
        assertThat(userColumnValue).isEqualTo("ROLE_USER");
        assertThat(adminColumnValue).isEqualTo("ROLE_ADMIN");
        assertThat(nullColumnValue).isNull();
    }

    @Test
    @DisplayName("Database 컬럼 값을 UserRoleType으로 변환 테스트")
    void convertToEntityAttribute_shouldReturnUserRoleType() {
        // Given
        String userColumnValue = "ROLE_USER";
        String adminColumnValue = "ROLE_ADMIN";

        // When
        UserRoleType userResult = converter.convertToEntityAttribute(userColumnValue);
        UserRoleType adminResult = converter.convertToEntityAttribute(adminColumnValue);
        UserRoleType nullResult = converter.convertToEntityAttribute(null);

        // Then
        assertThat(userResult).isEqualTo(UserRoleType.USER);
        assertThat(adminResult).isEqualTo(UserRoleType.ADMIN);
        assertThat(nullResult).isNull();
    }

    @Test
    @DisplayName("잘못된 데이터베이스 컬럼 값을 UserRoleType으로 변환 시 예외 테스트")
    void convertToEntityAttribute_withInvalidValue_shouldThrowException() {
        // Given
        String invalidValue = "ROLE_NONE";

        // When & Then
        assertThatThrownBy(() -> converter.convertToEntityAttribute(invalidValue))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("잘못된 유저 권한 타입입니다 : " + invalidValue);
    }

}