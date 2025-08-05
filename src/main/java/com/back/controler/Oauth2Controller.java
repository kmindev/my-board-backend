package com.back.controler;

import com.back.controler.dto.reponse.ApiResponse;
import com.back.secuirty.coustomoauth.Oauth2ProviderType;
import com.back.service.Oauth2Service;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class Oauth2Controller extends BaseController {

    private final Oauth2Service oauth2Service;

    @GetMapping("/oauth2/authorization/{oauth2ProviderType}")
    public ResponseEntity<Void> authorize(
            @PathVariable("oauth2ProviderType") Oauth2ProviderType oauth2ProviderType,
            HttpServletRequest httpServletRequest
    ) {
        requestLog(log, httpServletRequest);
        ResponseEntity<Void> responseEntity = oauth2Service.authorizeRequest(oauth2ProviderType);
        responseLog(log, httpServletRequest);
        return responseEntity;
    }

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
