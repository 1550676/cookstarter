package ru.guteam.customer_service.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import ru.guteam.customer_service.entities.utils.TokenResponse;
import ru.guteam.customer_service.entities.utils.TokenRequest;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerIntegrationTests {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void whenUserAuthenticated_thenStatus200() {
        TokenRequest request = new TokenRequest();
        request.setUsername("100");
        request.setPassword("100");
        ResponseEntity<TokenResponse> responseEntity = restTemplate.postForEntity("/auth", new HttpEntity<>(request), TokenResponse.class);
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        TokenResponse response = responseEntity.getBody();
        assert responseEntity.getBody() != null;
        assertThat(response, is(instanceOf(TokenResponse.class)));
        assertThat(response.getToken(), is(notNullValue()));
        assertThat(response.getUserId(), is(notNullValue()));
    }


    @Test
    public void whenUserNotAuthenticated_thenStatus401() {
        TokenRequest request = new TokenRequest();
        request.setUsername("100");
        request.setPassword("200");
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("/auth", new HttpEntity<>(request), String.class);
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
        assert responseEntity.getBody() != null;
        assertThat(responseEntity.getBody(), is(notNullValue()));
    }
}
