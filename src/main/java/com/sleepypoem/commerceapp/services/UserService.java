package com.sleepypoem.commerceapp.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sleepypoem.commerceapp.domain.dto.AuthServerResponseDto;
import com.sleepypoem.commerceapp.domain.dto.UserDto;
import com.sleepypoem.commerceapp.domain.dto.UserRepresentationDto;
import com.sleepypoem.commerceapp.domain.entities.AddressEntity;
import com.sleepypoem.commerceapp.domain.entities.CheckoutEntity;
import com.sleepypoem.commerceapp.domain.entities.PaymentMethodEntity;
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

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class UserService {


    RestTemplate restTemplate = new RestTemplate();

    ObjectMapper mapper;
    private final String tokenEndpoint;

    AuthServerResponseDto serverResponse;

    private final String uriPrefix;

    private final String realmName;

    @Autowired
    private CheckoutService checkoutService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private PaymentMethodService paymentMethodService;

    public UserService(ObjectMapper mapper, @Value("${auth-server.admin-rest-prefix}") String uriPrefix,
                       @Value("${auth-server.realm-name}") String realmName,
                       @Value("${auth-server.token-endpoint}") String tokenEndpoint) throws Exception {
        this.mapper = mapper;
        this.uriPrefix = uriPrefix;
        this.realmName = realmName;
        this.tokenEndpoint = tokenEndpoint;
        serverResponse = obtainAccessToken();

    }

    public UserDto getUserByUserName(String userName) throws Exception {
        HttpHeaders headers = createHeaders(MediaType.APPLICATION_JSON, serverResponse.getAccessToken());

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = makeRequest(uriPrefix + realmName + "/users?username=" + userName, HttpMethod.GET, entity);

        String responseBody = trimFirstAndLastChar(response.getBody());
        UserDto user = mapper.readValue(responseBody, UserDto.class);
        attachAddresses(user);
        attachCheckout(user);
        attachPaymentMethods(user);
        return user;
    }

    public UserDto getUserById(String id) throws Exception {
        HttpHeaders headers = createHeaders(MediaType.APPLICATION_JSON, serverResponse.getAccessToken());
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = makeRequest(uriPrefix + realmName + "/users/" + id, HttpMethod.GET, entity);

        UserDto user = mapper.readValue(response.getBody(), UserDto.class);
        attachAddresses(user);
        attachCheckout(user);
        attachPaymentMethods(user);

        return user;
    }

    public String addUser(UserRepresentationDto user) throws Exception {
        HttpHeaders headers = createHeaders(MediaType.APPLICATION_JSON, serverResponse.getAccessToken());

        HttpEntity<String> request = new HttpEntity<>(mapper.writeValueAsString(user), headers);
        makeRequest(uriPrefix + realmName + "/users/", HttpMethod.POST, request);
        return getUserByUserName(user.getUsername()).getId();
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
        if (string == null) {
            return null;
        }
        return string.substring(1, string.length() - 1);
    }

    private HttpHeaders createHeaders(MediaType mediaType, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        if (token != null) {
            headers.add("Authorization", "Bearer " + serverResponse.getAccessToken());
        }

        return headers;
    }

    public String testMaps(UserRepresentationDto user) throws JsonProcessingException {
        return mapper.writeValueAsString(user);
    }

    private ResponseEntity<String> makeRequest(String url, HttpMethod method, HttpEntity<?> entity) throws Exception {
        ResponseEntity<String> response = restTemplate.exchange(url, method, entity, String.class);

        if (response.getStatusCode() == HttpStatus.BAD_REQUEST) {
            throw new MyBadRequestException(response.getBody());
        }

        if (response.getStatusCode() == HttpStatus.CONFLICT) {
            throw new MyUserNameAlreadyUsedException(response.getBody());
        }

        return response;
    }

    private UserDto attachCheckout(UserDto user) {
        CheckoutEntity checkout = checkoutService.getByUserId(user.getId());
        user.setCheckouts(checkout);
        return user;
    }

    private UserDto attachAddresses(UserDto user) {
        List<AddressEntity> addresses = addressService.findByUserId(user.getId());
        user.setAddresses(addresses);
        return user;
    }

    private UserDto attachPaymentMethods(UserDto user) {
        List<PaymentMethodEntity> paymentMethods = paymentMethodService.findByUserId(user.getId());
        user.setPaymentMethods(paymentMethods);
        return user;
    }

}
