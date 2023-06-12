package com.sleepypoem.commerceapp.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sleepypoem.commerceapp.config.beans.ApplicationContextProvider;
import com.sleepypoem.commerceapp.exceptions.MyInternalException;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {

    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private boolean enabled;
    private String createdTimestamp;

    public String toJsonString() {
        return "{" +
                "\"id\":\"" + id + '\"' +
                ", \"username\":\"" + username + '\"' +
                ", \"firstName\":\"" + firstName + '\"' +
                ", \"lastName\":\"" + lastName + '\"' +
                ", \"email\":\"" + email + '\"' +
                ", \"enabled\":\"" + enabled + '\"' +
                ", \"createdTimeStamp\"" + createdTimestamp + '\"' +
                '}';
    }

    public static UserDto fromJsonString(String jsonString) {
        ObjectMapper mapper = ApplicationContextProvider.applicationContext.getBean(ObjectMapper.class);
        try {
            return mapper.readValue(jsonString, UserDto.class);
        } catch (JsonProcessingException e) {
            throw new MyInternalException("Error mapping JSON String to UserDto.", e);
        }
    }
}
