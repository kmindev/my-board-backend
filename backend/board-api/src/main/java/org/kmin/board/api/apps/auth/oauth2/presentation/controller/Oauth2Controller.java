package org.kmin.board.api.apps.auth.oauth2.presentation.controller;

import org.kmin.board.api.common.presentation.controller.BaseController;
import org.kmin.board.api.common.presentation.dto.response.ApiResponse;
import org.kmin.board.api.apps.auth.oauth2.domain.Oauth2ProviderType;
import org.kmin.board.api.apps.auth.oauth2.application.Oauth2Service;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "OAuth2 API")
@Slf4j
@RequiredArgsConstructor
@RestController
public class Oauth2Controller extends BaseController {

    private final Oauth2Service oauth2Service;

    @Operation(
            summary = "OAuth2 로그인 요청 API (Swagger 테스트 금지)",
            description = "⚠️ 이 API는 브라우저 리다이렉트를 유도하는 API로, Swagger에서 호출해도 정상 작동하지 않습니다. 직접 브라우저를 통해 테스트하세요."
    )
    @GetMapping("/oauth2/authorization/{oauth2ProviderType}")
    public ResponseEntity<Void> authorize(
            @PathVariable("oauth2ProviderType")
            @Parameter(
                    name = "oauth2ProviderType",
                    description = "OAuth2 공급자 타입 (예: kakao, naver, google)",
                    required = true,
                    example = "kakao"
            )
            Oauth2ProviderType oauth2ProviderType,
            HttpServletRequest httpServletRequest
    ) {
        requestLog(log, httpServletRequest);
        ResponseEntity<Void> responseEntity = oauth2Service.authorizeRequest(oauth2ProviderType);
        responseLog(log, httpServletRequest);
        return responseEntity;
    }

    @Operation(
            summary = "OAuth2 로그인 콜백 API (Swagger 테스트 금지))",
            description = "⚠️ 이 API는 OAuth2에서 리다이렉트 할 때 사용하는 API 입니다. Swagger에서 호출해도 정상 작동하지 않습니다."
    )
    @Hidden
    @GetMapping("/login/oauth2/code/{oauth2ProviderType}")
    public ResponseEntity<ApiResponse<Void>> authorizationCallback(
            @PathVariable("oauth2ProviderType") Oauth2ProviderType oauth2ProviderType,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String error,
            @RequestParam(value = "error_description", required = false) String errorDescription,
            @RequestParam(required = false) String state,
            HttpServletRequest httpServletRequest
    ) {
        requestLog(log, httpServletRequest);
        oauth2Service.handleAuthorizationCallback(oauth2ProviderType, code, error, httpServletRequest);
        ApiResponse<Void> response = ApiResponse.okWithMessage("로그인 성공");
        responseLog(log, httpServletRequest, response);
        return ResponseEntity.ok(response);
    }

}
