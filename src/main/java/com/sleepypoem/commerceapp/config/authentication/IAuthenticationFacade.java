package com.sleepypoem.commerceapp.config.authentication;

import com.sleepypoem.commerceapp.domain.dto.UserDto;
import org.springframework.security.core.Authentication;

public interface IAuthenticationFacade {

    Authentication getAuthentication();

    Object getPrincipal();
}