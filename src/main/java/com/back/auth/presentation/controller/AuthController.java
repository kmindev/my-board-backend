package com.back.auth.presentation.controller;

import com.back.common.presentation.dto.response.ApiResponse;
import com.back.auth.presentation.dto.request.LoginRequest;
import com.back.auth.domain.BoardUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "인증 API")
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
@RestController
public class AuthController {

    @Operation(summary = "로그인 API")
    @PostMapping("/login")
    public void login(@RequestBody LoginRequest loginRequest) {
        // Swagger 문서화를 위한 용도입니다.
        // 실제 로직은 SecurityFilter에서 처리되므로 여기는 빈 껍데기입니다.
        throw new IllegalStateException("Security Filter에서 처리합니다.");
    }

    @Operation(summary = "로그아웃 API")
    @PostMapping("/logout")
    public void logout() {
        // Swagger 문서화를 위한 용도입니다.
        // 실제 로직은 SecurityFilter에서 처리되므로 여기는 빈 껍데기입니다.
        throw new IllegalStateException("Security Filter에서 처리합니다.");
    }

    @Operation(summary = "관리자 권한 확인 API")
    @GetMapping("/admin-test")
    public ResponseEntity<ApiResponse<String>> adminTest(@AuthenticationPrincipal BoardUserDetails boardUserDetails) {
        return ResponseEntity.ok()
                .body(ApiResponse.okWithMessageAndData("admin test api", "hello: " + boardUserDetails));
    }

    @Operation(summary = "사용자 권한 확인 API")
    @GetMapping("/user-test")
    public ResponseEntity<ApiResponse<String>> userTest(@AuthenticationPrincipal BoardUserDetails boardUserDetails) {
        return ResponseEntity.ok()
                .body(ApiResponse.okWithMessageAndData("user test api", "hello: " + boardUserDetails));
    }

}
