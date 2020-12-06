package ru.guteam.customer_service.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ru.guteam.customer_service.entities.User;
import ru.guteam.customer_service.entities.utils.RegistrationResponse;
import ru.guteam.customer_service.entities.utils.SystemUser;
import ru.guteam.customer_service.entities.utils.validation.ValidationErrorDTO;
import ru.guteam.customer_service.services.UsersInfoService;
import ru.guteam.customer_service.services.UsersService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@CrossOrigin("*")
@RestController
@RequestMapping("/reg")
@AllArgsConstructor
@Api("Set of endpoints for registration")
public class RegistrationController {
    private final UsersService usersService;
    private final UsersInfoService usersInfoService;


    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @ApiOperation("Returns HttpStatus and user's id of trying registration procedure for users. Inside the object of SystemUser type is data about them.")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> userRegistration(@RequestBody @ApiParam("Cannot be empty") @Valid SystemUser systemUser) {
        String username = systemUser.getUsername();
        if (usersService.existsByUsername(username)) {
            return new ResponseEntity<>("Registration is not possible. User with login: " + username + " already exists", HttpStatus.CONFLICT);
        }
        usersInfoService.saveBySystemUser(systemUser);
        User user = usersService.findByUsername(username);
        return new ResponseEntity<>(new RegistrationResponse(user.getId()), HttpStatus.OK);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.OK)
    public ValidationErrorDTO processValidationError(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        return processFieldErrors(fieldErrors);
    }

    private ValidationErrorDTO processFieldErrors(List<FieldError> fieldErrors) {
        ValidationErrorDTO dto = new ValidationErrorDTO();
        for (FieldError fieldError : fieldErrors) {
            dto.addFieldError(fieldError.getField(), fieldError.getDefaultMessage());
            log.info("Error filling in the input field " + fieldError.getField() +
                    ": " + fieldError.getDefaultMessage());
        }
        return dto;
    }

}
