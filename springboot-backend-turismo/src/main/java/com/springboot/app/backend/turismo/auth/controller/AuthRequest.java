package com.springboot.app.backend.turismo.auth.controller;

public record AuthRequest(
        String email,
        String password
) {
}
