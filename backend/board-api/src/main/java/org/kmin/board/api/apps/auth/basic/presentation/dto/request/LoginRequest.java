package org.kmin.board.api.apps.auth.basic.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginRequest(
        @Schema(description = "username", example = "user1")
        String username,
        @Schema(description = "password", example = "qwer1234")
        String password
) {
}
