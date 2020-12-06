package ru.guteam.customer_service.controllers;
// https://sysout.ru/testirovanie-spring-boot-prilozheniya-s-testresttemplate/

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import ru.guteam.customer_service.entities.utils.RegistrationResponse;
import ru.guteam.customer_service.entities.utils.SystemUser;
import ru.guteam.customer_service.entities.utils.validation.ValidationErrorDTO;
import ru.guteam.customer_service.util.TestUtils;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RegistrationControllerIntegrationTests {
    private final String ENDPOINT = "/reg";
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    TestUtils utils;


    @Test
    public void whenUserRegistered_thenStatus200() throws IOException, URISyntaxException {
        SystemUser user = utils.readJson("systemUser.json", SystemUser.class);
        ResponseEntity<RegistrationResponse> response = restTemplate.postForEntity(ENDPOINT, new HttpEntity<>(user), RegistrationResponse.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assert response.getBody() != null;
        assertThat(response.getBody(), is(notNullValue()));
    }

    @Test
    public void whenUserWithNull_thenStatus200AndErrors() throws IOException, URISyntaxException {
        SystemUser user = utils.readJson("userWithNull.json", SystemUser.class);
        ResponseEntity<ValidationErrorDTO> response = restTemplate.postForEntity(ENDPOINT, new HttpEntity<>(user), ValidationErrorDTO.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assert response.getBody() != null;
        assertThat(response.getBody(), is(notNullValue()));
        assertThat(response.getBody(), instanceOf(ValidationErrorDTO.class));
    }

    @Test
    public void whenUsernameAlreadyExists_thenStatus409() throws IOException, URISyntaxException {
        SystemUser user = utils.readJson("userWithConflict.json", SystemUser.class);
        ResponseEntity<String> response = restTemplate.postForEntity(ENDPOINT, new HttpEntity<>(user), String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.CONFLICT));
        assert response.getBody() != null;
        assertThat(response.getBody(), is(notNullValue()));
    }

}
