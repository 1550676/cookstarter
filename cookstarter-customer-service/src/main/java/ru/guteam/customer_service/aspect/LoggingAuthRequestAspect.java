package ru.guteam.customer_service.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.guteam.customer_service.entities.utils.TokenResponse;
import ru.guteam.customer_service.entities.utils.TokenRequest;

import java.util.Objects;

@Slf4j
@Aspect
@Configuration
public class LoggingAuthRequestAspect {

//    // logging user's authentication attempt
//    @Before("execution(* ru.guteam.customer_service.controllers.AuthController.* (..))")
//    public void logAuthRequest(JoinPoint joinPoint) {
//        TokenRequest request = (TokenRequest) joinPoint.getArgs()[0];
//        log.info("Request for user's authentication with login: " + request.getUsername() +
//                " and password: " + request.getPassword());
//    }

    // logging user's authentication results
    @AfterReturning(pointcut = "execution(* ru.guteam.customer_service.controllers.AuthController.* (..))", returning = "result")
    public void logAuthResponse(JoinPoint joinPoint, Object result) {
        ResponseEntity response = (ResponseEntity) result;
        TokenRequest authRequest = (TokenRequest) joinPoint.getArgs()[0];
        if (response.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
            log.info("Attempt of authentication was failed. User with login: " + authRequest.getUsername() +
                    " and password: " + authRequest.getPassword() + " was not found.");
            return;
        }
        if (response.getStatusCode().equals(HttpStatus.OK) && Objects.requireNonNull(response.getBody()).getClass() != null) {
                TokenResponse customerResponse = (TokenResponse) response.getBody();
                log.info("Token: " + customerResponse.getToken() + " was generated for user with login: " + authRequest.getUsername() +
                        " and password: " + authRequest.getPassword());
        }
    }

}
