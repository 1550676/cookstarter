package ru.guteam.customer_service.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.guteam.customer_service.entities.utils.RestaurantInfo;
import ru.guteam.customer_service.entities.utils.SystemUser;
import ru.guteam.customer_service.entities.utils.TokenRequest;
import ru.guteam.customer_service.entities.utils.TokenResponse;

import java.util.Objects;

@Slf4j
@Aspect
@Configuration
public class LoggingOtherRequestAspect {

    // logging update of restaurant's id and user's authority by his id
    @AfterReturning(pointcut = "execution(* ru.guteam.customer_service.controllers.UpdateController.updateRestaurantId(..))", returning = "result")
    public void logUpdateRestaurantId(JoinPoint joinPoint, Object result) {
        ResponseEntity response = (ResponseEntity) result;
        if (response.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
            log.info("Attempt of restaurant's id update was failed. " + response.getBody().toString());
            return;
        }
        if (response.getStatusCode().equals(HttpStatus.OK)) {
            RestaurantInfo request = (RestaurantInfo) joinPoint.getArgs()[0];
            log.info("Restaurant's id: " + request.getRestaurantId() +
                    " was successfully set for user with id: " + request.getUserId());
        }
    }

    // logging providing information about user by his id
    @AfterReturning(pointcut = "execution(* ru.guteam.customer_service.controllers.UsersInfoController.getUserInfo(..))", returning = "result")
    public void logUserInfoRequest(JoinPoint joinPoint, Object result) {
        ResponseEntity response = (ResponseEntity) result;
        if (response.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
            log.info("Attempt providing information about user was failed. " + response.getBody().toString());
            return;
        }
        if (response.getStatusCode().equals(HttpStatus.OK) && Objects.requireNonNull(response.getBody()).getClass() != null) {
            Long request = (Long) joinPoint.getArgs()[0];
            log.info("The request to provide information about user with id: " + request + " was completed successfully.");

        }
    }

}
