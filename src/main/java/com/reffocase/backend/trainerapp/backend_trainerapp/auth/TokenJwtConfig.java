package com.reffocase.backend.trainerapp.backend_trainerapp.auth;

import javax.crypto.SecretKey;
import io.jsonwebtoken.security.Keys;

public class TokenJwtConfig {

    // La clave ahora se lee desde una variable de entorno
    public final static SecretKey SECRET_KEY = Keys.hmacShaKeyFor(
            System.getenv("JWT_SECRET").getBytes());

    public final static String PREFIX_TOKEN = "Bearer ";
    public final static String HEADER_AUTHORIZATION = "Authorization";
}
