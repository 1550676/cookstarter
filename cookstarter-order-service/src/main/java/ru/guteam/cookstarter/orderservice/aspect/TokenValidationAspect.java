package ru.guteam.cookstarter.orderservice.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import ru.guteam.cookstarter.orderservice.exception.AuthorizationException;
import ru.guteam.cookstarter.orderservice.service.JwtValidationService;

@Slf4j
@Aspect
@Configuration
@RequiredArgsConstructor
public class TokenValidationAspect {

    @Value("${app.auth-type}")
    private String authType;

    private final JwtValidationService jwtValidationService;

    @Before("execution(* ru.guteam.cookstarter.orderservice.controller.OrderController.* (*, java.lang.String))")
    public void tokenCheck(JoinPoint joinPoint) {
        String token = (String) joinPoint.getArgs()[1];
        log.info("Проверка токена");
        if (token.startsWith(authType)) {
            token = token.substring(authType.length() + 1);
            jwtValidationService.checkToken(token);
        } else {
            throw new AuthorizationException("Неподдерживаемый тип авторизации, либо тип не указан");
        }
    }
}
