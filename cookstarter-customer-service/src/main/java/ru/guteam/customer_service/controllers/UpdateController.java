package ru.guteam.customer_service.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.guteam.customer_service.entities.User;
import ru.guteam.customer_service.entities.utils.RestaurantInfo;
import ru.guteam.customer_service.entities.utils.validation.ValidationErrorDTO;
import ru.guteam.customer_service.services.UsersService;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@CrossOrigin("*")
@RestController
@RequestMapping("/update")
@AllArgsConstructor
@Api("Set of endpoints for updating information about users")
public class UpdateController {
    private final UsersService usersService;


    @ApiOperation("Updates restaurant's id and user's authority by his id if this user was previously registered as a representative of the restaurant.")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateRestaurantId (@RequestBody @ApiParam("Cannot be empty") @Valid RestaurantInfo info) {
        Optional<User> user = usersService.findOptionalById(info.getUserId());
        if (!user.isPresent()) {
            return new ResponseEntity<>("User with id: " + info.getUserId() + " is not found", HttpStatus.NOT_FOUND);
        } else {
            usersService.updateRestaurantId (info);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String processValidationError() {
        return "Field error in object 'restaurantInfo'. Not all fields were filled in before the request.";
    }

}