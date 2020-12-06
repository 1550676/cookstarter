package ru.guteam.customer_service.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import ru.guteam.customer_service.entities.dtos.UserInfoDTO;
import ru.guteam.customer_service.entities.utils.TokenRequest;
import ru.guteam.customer_service.entities.utils.TokenResponse;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UsersInfoControllerIntegrationTests {
    private final String ENDPOINT = "/users/info/";

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void whenUsersInfoProvided_thenStatus200AndInfo() {
        ResponseEntity<UserInfoDTO> responseEntity = restTemplate.getForEntity(ENDPOINT + "/1", UserInfoDTO.class);
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        UserInfoDTO response = responseEntity.getBody();
        assert responseEntity.getBody() != null;
        assertThat(response, is(instanceOf(UserInfoDTO.class)));
        assertThat(response.getFirstName(), is(notNullValue()));
        assertThat(response.getLastName(), is(notNullValue()));
        assertThat(response.getEmail(), is(notNullValue()));
    }

    @Test
    public void whenUserNotFound_thenStatus404AndError() {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(ENDPOINT + "/100", String.class);
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.NOT_FOUND));
        String response = responseEntity.getBody();
        assert responseEntity.getBody() != null;
        assertThat(response, is(instanceOf(String.class)));
    }
}
