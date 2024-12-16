package com.back.controler.dto.request;

public record LoginRequest(
        String username,
        String password
) {
}
