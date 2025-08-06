package com.back.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

import com.back.domain.UserRoleType;
import com.back.secuirty.BoardUserDetails;
import com.back.service.dto.UserAccountDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

@DisplayName("서비스 로직 - 로그인")
@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @InjectMocks
    private LoginService sut;

    @DisplayName("UserAccountDto로 로그인하면, 인증 객체가 SecurityContext와 세션에 저장된다.")
    @Test
    void givenUserAccountDto_whenLogin_thenAuthenticationStoredInSessionAndContext() {
        // given
        UserAccountDto userAccountDto = UserAccountDto.of(
                "user123",
                "encoded-password",
                "test@email.com",
                "nickname",
                null,
                "kakao",
                "123456789",
                UserRoleType.USER
        );
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        given(request.getSession(true)).willReturn(session);

        // when
        sut.login(userAccountDto, request);

        // then
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertThat(auth).isNotNull();
        assertThat(auth.isAuthenticated()).isTrue();

        assertThat(auth.getPrincipal()).isInstanceOf(BoardUserDetails.class);
        BoardUserDetails principal = (BoardUserDetails) auth.getPrincipal();
        assertThat(principal.getUsername()).isEqualTo(userAccountDto.userId());
        assertThat(principal.getAuthorities()).extracting(GrantedAuthority::getAuthority)
                .containsExactly(userAccountDto.role().getName());
        then(session).should().setAttribute(
                eq(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY),
                any(SecurityContext.class)
        );
        SecurityContextHolder.clearContext();
    }

}