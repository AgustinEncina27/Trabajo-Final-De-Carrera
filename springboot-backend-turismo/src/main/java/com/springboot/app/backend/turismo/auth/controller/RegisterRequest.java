package com.springboot.app.backend.turismo.auth.controller;

public record RegisterRequest(
        String name,
        String email,
        String password,
        String celular
) {
}
