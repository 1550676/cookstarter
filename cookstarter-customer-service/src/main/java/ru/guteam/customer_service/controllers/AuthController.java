package ru.guteam.customer_service.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.guteam.customer_service.controllers.utils.JwtTokenUtil;
import ru.guteam.customer_service.entities.utils.TokenResponse;
import ru.guteam.customer_service.entities.utils.TokenRequest;
import ru.guteam.customer_service.entities.User;
import ru.guteam.customer_service.services.UsersService;

@Slf4j
@CrossOrigin("*")
@RestController
@RequestMapping("/auth")
@AllArgsConstructor
@Api("Set of endpoints for authentication")
public class AuthController {
    private final UsersService usersService;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    @ApiOperation("Returns JWT Token for the user by his username, password and email inside an object of TokenRequest type.")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createAuthToken(@RequestBody @ApiParam("Cannot be empty") TokenRequest authRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>("Invalid login or password", HttpStatus.UNAUTHORIZED);
        }
        UserDetails userDetails = usersService.loadUserByUsername(authRequest.getUsername());
        String token = jwtTokenUtil.generateToken(userDetails);
        User user = usersService.findByUsername(authRequest.getUsername());
        return new ResponseEntity<>(new TokenResponse(token, user.getId(), user.getRestaurantId()), HttpStatus.OK);
    }

}