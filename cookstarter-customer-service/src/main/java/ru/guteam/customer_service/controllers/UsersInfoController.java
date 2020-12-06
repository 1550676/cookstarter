package ru.guteam.customer_service.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.guteam.customer_service.entities.User;
import ru.guteam.customer_service.entities.dtos.UserInfoDTO;
import ru.guteam.customer_service.services.UsersInfoService;
import ru.guteam.customer_service.services.UsersService;

import java.util.Optional;

@Slf4j
@CrossOrigin("*")
@RestController
@RequestMapping("/users")
@AllArgsConstructor
@Api("Set of endpoints for requesting and providing user information")
public class UsersInfoController {
    private final UsersService usersService;
    private final UsersInfoService usersInfoService;



    @ApiOperation("Returns firstname, lastname and email for the user by his id.")
    @GetMapping(value = "info/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserInfo(@PathVariable @ApiParam("Cannot be empty") Long userId) {
        Optional<User> user = usersService.findOptionalById(userId);
        if (!user.isPresent()) {
            return new ResponseEntity<>("User with id: " + userId + " is not found", HttpStatus.NOT_FOUND);
        }
        else
            return new ResponseEntity<>(new UserInfoDTO(user.get().getUsersInfo()), HttpStatus.OK);
    }

}
