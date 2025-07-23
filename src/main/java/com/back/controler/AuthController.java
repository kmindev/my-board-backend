package com.back.controler;

import com.back.controler.dto.reponse.ApiResponse;
import com.back.secuirty.BoardUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "인증 API")
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
@RestController
public class AuthController {

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
