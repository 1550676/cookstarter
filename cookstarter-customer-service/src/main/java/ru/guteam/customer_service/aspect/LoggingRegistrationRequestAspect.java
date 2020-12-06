package ru.guteam.customer_service.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.guteam.customer_service.entities.utils.RegistrationResponse;
import ru.guteam.customer_service.entities.utils.SystemUser;
import ru.guteam.customer_service.entities.utils.TokenRequest;
import ru.guteam.customer_service.entities.utils.TokenResponse;

import java.util.Objects;

@Slf4j
@Aspect
@Configuration
public class LoggingRegistrationRequestAspect {

    // logging user's registration attempt
    @Before("execution(* ru.guteam.customer_service.controllers.RegistrationController.userRegistration(..))")
    public void logAuthRequest(JoinPoint joinPoint) {
        SystemUser request = (SystemUser) joinPoint.getArgs()[0];
        log.info("Request for user's registration with login: " + request.getUsername() +
                " and password: " + request.getPassword());
    }

    // logging user's registration results
    @AfterReturning(pointcut = "execution(* ru.guteam.customer_service.controllers.RegistrationController.userRegistration(..))", returning = "result")
    public void logAuthResponse(JoinPoint joinPoint, Object result) {
        ResponseEntity response = (ResponseEntity) result;
        if (response.getStatusCode().equals(HttpStatus.CONFLICT)) {
            log.info("Attempt of registration was failed. " + response.getBody().toString());
            return;
        }
        if (response.getStatusCode().equals(HttpStatus.OK) && Objects.requireNonNull(response.getBody()).getClass() != null) {
            SystemUser request = (SystemUser) joinPoint.getArgs()[0];
                log.info("The user with login: " + request.getUsername() + " was successfully registered.");
        }
    }

}
