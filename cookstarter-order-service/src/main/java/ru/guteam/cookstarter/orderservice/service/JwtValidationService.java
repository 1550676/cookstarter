package ru.guteam.cookstarter.orderservice.service;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

@Service
@RequiredArgsConstructor
public class JwtValidationService {

    @Value("${app.jwt.secret-key}")
    private String secretKey;

    public void checkToken(String token) {
        Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token);
    }
}
