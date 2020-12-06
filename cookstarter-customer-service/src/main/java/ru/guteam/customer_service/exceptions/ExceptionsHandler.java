package ru.guteam.customer_service.exceptions;

import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<?> JWTSignatureException(Exception e) {
        log.error("The authenticity of the token has not been confirmed", e);
        return new ResponseEntity<>("The authenticity of the token has not been confirmed", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> resourceNotReadableException(Exception e) {
        log.error("Data in request was incorrect \n" + e);
        String[] exceptionCause = e.getCause().toString().split("\n");
        return new ResponseEntity<>("Data in request was incorrect. " + exceptionCause[0], HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<?> exception(Exception e) {
        log.error("Something went wrong. Please contact technical support", e);
        return new ResponseEntity<>("Something went wrong. Please contact technical support", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
