package com.sleepypoem.commerceapp.config.keycloak;

import com.sleepypoem.commerceapp.exceptions.*;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RequestHelperTest {

    public static MockWebServer mockKeycloakServer;

    RequestHelper requestHelper;

    @BeforeEach
    void init() throws IOException {
        mockKeycloakServer = new MockWebServer();
        mockKeycloakServer.start();
        WebClient webClient = WebClient.builder()
                .build();
        requestHelper = new RequestHelper(webClient);
    }

    @AfterEach
    void reset() throws IOException {
        mockKeycloakServer.shutdown();
    }

    @Test
    @DisplayName("Test make request without a body")
    void testMakeRequestWithoutBody() throws InterruptedException {
        // Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        var httpEntity = new HttpEntity<>(null, headers);
        mockKeycloakServer.enqueue(new MockResponse()
                .setBody("{}")
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json"));
        // Act
        var result = requestHelper.makeRequest("http://localhost:" + mockKeycloakServer.getPort(), HttpMethod.GET, httpEntity);
        // Assert
        RecordedRequest recordedRequest = mockKeycloakServer.takeRequest();

        assertThat(recordedRequest.getMethod(), is("GET"));
        assertThat(recordedRequest.getPath(), is("/"));
        assertThat(recordedRequest.getHeader("Content-Type"), is("application/json"));
    }

    @Test
    @DisplayName("Test create headers without auth token")
    void testCreateHeadersWithoutAuthToken() {
        // Arrange
        // Act
        var result = requestHelper.createHeaders(MediaType.APPLICATION_JSON, null);
        // Assert
        assertThat(result, is(notNullValue()));
        assertThat(result.getContentType(), is(MediaType.APPLICATION_JSON));
        assertThat(result.get("Authorization"), is(nullValue()));
        assertThat(result, is(instanceOf(org.springframework.http.HttpHeaders.class)));
    }

    @Test
    @DisplayName("Test create headers with auth token")
    void testCreateHeadersWithAuthToken() {
        //arrange
        String token = "testToken";
        //act
        var result = requestHelper.createHeaders(MediaType.APPLICATION_JSON, token);
        //assert
        assertThat(result, is(notNullValue()));
        assertThat(result.getContentType(), is(MediaType.APPLICATION_JSON));
        assertThat(result.get("Authorization"), is(List.of("Bearer " + token)));
        assertThat(result, is(instanceOf(org.springframework.http.HttpHeaders.class)));
    }

    @Test
    @DisplayName("Test make request with body")
    void testMakeRequestWithBody() throws InterruptedException {
        // Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        var httpEntity = new HttpEntity<>("{\"test\":\"test\"}", headers);
        mockKeycloakServer.enqueue(new MockResponse()
                .setBody("{}")
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json"));
        // Act
        var result = requestHelper.makeRequest("http://localhost:" + mockKeycloakServer.getPort(), HttpMethod.POST, httpEntity);
        // Assert
        RecordedRequest recordedRequest = mockKeycloakServer.takeRequest();

        assertThat(recordedRequest.getMethod(), is("POST"));
        assertThat(recordedRequest.getPath(), is("/"));
        assertThat(recordedRequest.getHeader("Content-Type"), is("application/json"));
        assertThat(recordedRequest.getBody().readUtf8(), is("{\"test\":\"test\"}"));
    }

    @Test
    @DisplayName("Test create HttpEntity without a body")
    void testCreateHttpEntityWithoutBody() {
        // Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // Act
        var result = requestHelper.createEmptyHttpEntity(headers);
        // Assert
        assertThat(result, is(notNullValue()));
        assertThat(result.getBody(), is(nullValue()));
        assertThat(result.getHeaders(), is(headers));
    }

    @Test
    @DisplayName("Test create HttpEntity with a body")
    void testCreateHttpEntityWithBody() {
        // Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // Act
        var result = requestHelper.createHttpEntity("{\"test\":\"test\"}", headers);
        // Assert
        assertThat(result, is(notNullValue()));
        assertThat(result.getBody(), is("{\"test\":\"test\"}"));
        assertThat(result.getHeaders(), is(headers));
    }

    @Test
    @DisplayName("Test evaluate response when response is 404 and error is not user not found")
    void testEvaluateResponseWhenResponseIsNotFound() {
        // Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        var httpEntity = new HttpEntity<>("{\"test\":\"test\"}", headers);
        int port = mockKeycloakServer.getPort();
        mockKeycloakServer.enqueue(new MockResponse()
                .setBody("{\"error\":\"testError\",\"error_description\":\"testErrorDescription\"}")
                .setResponseCode(404)
                .addHeader("Content-Type", "application/json"));
        // Act
        var ex = assertThrows(MyAuthServerException.class, () -> requestHelper.makeRequest("http://localhost:" + port, HttpMethod.POST, httpEntity));
        // Assert
        assertThat(ex.getMessage(), is("Error in auth server url."));
        assertThat(ex.getErrorDto().getError(), is("testError"));
        assertThat(ex.getErrorDto().getError_description(), is("testErrorDescription"));
    }

    @Test
    @DisplayName("Test evaluate response when response is 404 and error is user not found")
    void testEvaluateResponseWhenResponseIsNotFoundAndErrorIsUserNotFound() {
        // Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        var httpEntity = new HttpEntity<>("{\"test\":\"test\"}", headers);
        int port = mockKeycloakServer.getPort();
        mockKeycloakServer.enqueue(new MockResponse()
                .setBody("{\"error\":\"User not found\",\"error_description\":\"\"}")
                .setResponseCode(404)
                .addHeader("Content-Type", "application/json"));
        // Act
        var ex = assertThrows(MyUserNotFoundException.class, () -> requestHelper.makeRequest("http://localhost:" + port, HttpMethod.POST, httpEntity));
        // Assert
        assertThat(ex.getMessage(), is("User not found."));
    }

    @Test
    @DisplayName("Test evaluate response when response is 401")
    void testEvaluateResponseWhenResponseIsUnauthorized() {
        // Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        var httpEntity = new HttpEntity<>("{\"test\":\"test\"}", headers);
        int port = mockKeycloakServer.getPort();
        mockKeycloakServer.enqueue(new MockResponse()
                .setBody("")
                .setResponseCode(401)
                .addHeader("Content-Type", "application/json"));
        // Act
        var ex = assertThrows(MyAuthServerException.class, () -> requestHelper.makeRequest("http://localhost:" + port, HttpMethod.POST, httpEntity));
        // Assert
        assertThat(ex.getMessage(), is("Unauthorized, check your credentials."));
    }

    @Test
    @DisplayName("Test evaluate response when response is 500")
    void testEvaluateResponseWhenResponseIsInternalServerError() {
        // Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        var httpEntity = new HttpEntity<>("{\"test\":\"test\"}", headers);
        int port = mockKeycloakServer.getPort();
        mockKeycloakServer.enqueue(new MockResponse()
                .setBody("")
                .setResponseCode(500)
                .addHeader("Content-Type", "application/json"));
        // Act
        var ex = assertThrows(MyAuthServerException.class, () -> requestHelper.makeRequest("http://localhost:" + port, HttpMethod.POST, httpEntity));
        // Assert
        assertThat(ex.getMessage(), is("Error while connecting with auth server."));
    }

    @Test
    @DisplayName("Test evaluate response when response is 400")
    void testEvaluateResponseWhenResponseIsBadRequest() {
        // Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        var httpEntity = new HttpEntity<>("{\"test\":\"test\"}", headers);
        int port = mockKeycloakServer.getPort();
        mockKeycloakServer.enqueue(new MockResponse()
                .setBody("")
                .setResponseCode(400)
                .addHeader("Content-Type", "application/json"));
        // Act
        var ex = assertThrows(MyBadRequestException.class, () -> requestHelper.makeRequest("http://localhost:" + port, HttpMethod.POST, httpEntity));
        // Assert
        assertThat(ex.getMessage(), is("Bad request, check your request body."));
    }

    @Test
    @DisplayName("Test evaluate response when response is 403")
    void testEvaluateResponseWhenResponseIsForbidden() {
        // Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        var httpEntity = new HttpEntity<>("{\"test\":\"test\"}", headers);
        int port = mockKeycloakServer.getPort();
        mockKeycloakServer.enqueue(new MockResponse()
                .setBody("")
                .setResponseCode(403)
                .addHeader("Content-Type", "application/json"));
        // Act
        var ex = assertThrows(MyForbiddenException.class, () -> requestHelper.makeRequest("http://localhost:" + port, HttpMethod.POST, httpEntity));
        // Assert
        assertThat(ex.getMessage(), is("Forbidden, check your permissions, does the user has realm management role?"));
    }

    @Test
    @DisplayName("Test evaluate response when response is 409")
    void testEvaluateResponseWhenResponseIsConflict() {
        // Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        var httpEntity = new HttpEntity<>("{\"test\":\"test\"}", headers);
        int port = mockKeycloakServer.getPort();
        mockKeycloakServer.enqueue(new MockResponse()
                .setBody("")
                .setResponseCode(409)
                .addHeader("Content-Type", "application/json"));
        // Act
        var ex = assertThrows(MyUserNameAlreadyUsedException.class, () -> requestHelper.makeRequest("http://localhost:" + port, HttpMethod.POST, httpEntity));
        // Assert
        assertThat(ex.getMessage(), is("Username already used."));
    }

}