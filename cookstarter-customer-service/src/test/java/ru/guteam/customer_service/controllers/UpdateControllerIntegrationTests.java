package ru.guteam.customer_service.controllers;
// https://sysout.ru/testirovanie-spring-boot-prilozheniya-s-testresttemplate/

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cache.support.NullValue;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import ru.guteam.customer_service.entities.utils.RegistrationResponse;
import ru.guteam.customer_service.entities.utils.RestaurantInfo;
import ru.guteam.customer_service.entities.utils.SystemUser;
import ru.guteam.customer_service.entities.utils.validation.ValidationErrorDTO;
import ru.guteam.customer_service.util.TestUtils;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UpdateControllerIntegrationTests {
    private final String ENDPOINT = "/update";
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    TestUtils utils;


    @Test
    public void whenRestaurantIdUpdated_thenStatus200() throws IOException, URISyntaxException {
        RestaurantInfo info = utils.readJson("restaurantIdUpdating.json", RestaurantInfo.class);
        ResponseEntity<RegistrationResponse> response = restTemplate.postForEntity(ENDPOINT, new HttpEntity<>(info), RegistrationResponse.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assert response.getBody() == null;
        assertThat(response.getBody(), is(nullValue()));
    }

    @Test
    public void whenRestaurantInfoWithWrongRole_thenStatus500() throws IOException, URISyntaxException {
        RestaurantInfo info = utils.readJson("restaurantIdUpdatingWithWrongRole.json", RestaurantInfo.class);
        ResponseEntity<String> response = restTemplate.postForEntity(ENDPOINT, new HttpEntity<>(info), String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.INTERNAL_SERVER_ERROR));
        assert response.getBody() != null;
        assertThat(response.getBody(), is(notNullValue()));
        assertThat(response.getBody(), instanceOf(String.class));
    }

    @Test
    public void whenRestaurantInfoWithNull_thenStatus400AndError() throws IOException, URISyntaxException {
        RestaurantInfo info = utils.readJson("restaurantIdUpdatingWithNull.json", RestaurantInfo.class);
        ResponseEntity<String> response = restTemplate.postForEntity(ENDPOINT, new HttpEntity<>(info), String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        assert response.getBody() != null;
        assertThat(response.getBody(), is(notNullValue()));
        assertThat(response.getBody(), instanceOf(String.class));
    }


}
