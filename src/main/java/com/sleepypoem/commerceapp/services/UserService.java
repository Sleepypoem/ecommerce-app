package com.sleepypoem.commerceapp.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sleepypoem.commerceapp.domain.dto.AuthServerResponseDto;
import com.sleepypoem.commerceapp.domain.dto.UserDto;
import com.sleepypoem.commerceapp.exceptions.MyBadRequestException;
import com.sleepypoem.commerceapp.exceptions.MyUserNameAlreadyUsedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class UserService {

    @Autowired
    RestTemplate restTemplate;

    ObjectMapper mapper;
    @Value("${auth-server.token-endpoint}")
    static String tokenEndpoint;

    AuthServerResponseDto serverResponse;

    @Value("${auth-server.prefix}")
    static String uriPrefix;

    @Value("${auth-server.realm-name}")
    static String realmName;

    public UserService(ObjectMapper mapper) throws Exception {
        this.mapper = mapper;
        serverResponse = obtainAccessToken();
    }

    public UserDto getUserByUserName(String userName) throws Exception {
        HttpHeaders headers = createHeaders(MediaType.APPLICATION_JSON, serverResponse.getAccessToken());

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = makeRequest(uriPrefix + realmName + "/users?username=" + userName, HttpMethod.GET, entity);
        if(response.getBody() == null){
          return null;
        }
        String responseBody = trimFirstAndLastChar(response.getBody());
        return mapper.readValue(responseBody, UserDto.class);
    }

    public UserDto getUserById(String id) throws Exception {
        HttpHeaders headers = createHeaders(MediaType.APPLICATION_JSON, serverResponse.getAccessToken());

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = makeRequest(uriPrefix + realmName + "/users/" + id, HttpMethod.GET, entity);
        if(response.getBody() == null){
            return null;
        }
        return mapper.readValue(response.getBody(), UserDto.class);
    }

    public String addUser(UserDto user) throws Exception {
        HttpHeaders headers = createHeaders(MediaType.APPLICATION_JSON, serverResponse.getAccessToken());

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("firstName", user.getFirstName());
        requestBody.add("email", user.getEmail());
        requestBody.add("LastName", user.getLastName());
        requestBody.add("username", user.getUserName());
        requestBody.add("enabled", "true");
        requestBody.add("credentials", "[{\"type\" : \"password\", \"value\" : \"" + user.getPassword() + "\", \"temporary\" : \"false\"}]" );

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(requestBody, headers);
        makeRequest(tokenEndpoint, HttpMethod.POST, request);
        return getUserByUserName(user.getUserName()).getId();
    }

    public AuthServerResponseDto obtainAccessToken() throws Exception {
        HttpHeaders headers = createHeaders(MediaType.APPLICATION_FORM_URLENCODED, null);

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("client_id", "ecommerce");
        requestBody.add("username", "sleepypoem");
        requestBody.add("password", "admin123");
        requestBody.add("grant_type", "password");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = makeRequest(tokenEndpoint, HttpMethod.POST, request);
        return mapper.readValue(response.getBody(), AuthServerResponseDto.class);
    }

    private String trimFirstAndLastChar(String string) {
        if(string == null){
            return null;
        }
        return string.substring(1, string.length() -1);
    }

    private HttpHeaders createHeaders(MediaType mediaType, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        if (token != null) {
            headers.add("Authorization", "Bearer " + serverResponse.getAccessToken());
        }

        return headers;
    }

    private ResponseEntity<String> makeRequest(String url, HttpMethod method, HttpEntity<?> entity) throws Exception {
        ResponseEntity<String> response = restTemplate.exchange(url, method, entity, String.class);

        if(response.getStatusCode() == HttpStatus.BAD_REQUEST){
            throw new MyBadRequestException(response.getBody());
        }

        if(response.getStatusCode() == HttpStatus.CONFLICT){
            throw new MyUserNameAlreadyUsedException(response.getBody());
        }

        return response;
    }
}
