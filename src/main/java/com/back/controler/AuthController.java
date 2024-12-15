package com.back.controler;

import com.back.controler.dto.reponse.ApiResponse;
import com.back.secuirty.BoardUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/v1/auth")
@RestController
public class AuthController {

    @GetMapping("/admin-test")
    public ResponseEntity<ApiResponse<String>> adminTest(@AuthenticationPrincipal BoardUserDetails boardUserDetails) {
        return ResponseEntity.ok().body(ApiResponse.okWithMessageAndData("admin test api", "hello: " + boardUserDetails));
    }

    @GetMapping("/user-test")
    public ResponseEntity<ApiResponse<String>> userTest(@AuthenticationPrincipal BoardUserDetails boardUserDetails) {
        return ResponseEntity.ok().body(ApiResponse.okWithMessageAndData("user test api", "hello: " + boardUserDetails));
    }

}
