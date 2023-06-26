package com.sleepypoem.commerceapp.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sleepypoem.commerceapp.config.beans.GsonProvider;
import lombok.*;

import java.util.List;
import java.util.Objects;

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
    private boolean emailVerified;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<CredentialsDto> credentials;

    public String toJsonString() {
        return GsonProvider.getGson().toJson(this);
    }

    public static UserDto fromJsonString(String jsonString) {
        return GsonProvider.getGson().fromJson(jsonString, UserDto.class);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto userDto = (UserDto) o;
        return Objects.equals(id, userDto.id) && Objects.equals(username, userDto.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }
}
