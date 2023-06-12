package com.sleepypoem.commerceapp.controllers.utils;

import com.sleepypoem.commerceapp.config.beans.SecurityContextProvider;
import com.sleepypoem.commerceapp.config.security.CustomPrincipal;

public class SecurityUtils {

    private SecurityUtils() {
    }

    public static String getCurrentLoggedUserId() {
        CustomPrincipal principal = (CustomPrincipal) SecurityContextProvider.getSecurityContext().getAuthentication().getPrincipal();
        return principal.getId();
    }
}
