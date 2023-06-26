package com.sleepypoem.commerceapp.config.beans;

import com.google.gson.Gson;

public class GsonProvider {

    private GsonProvider() {
    }

    private static final Gson gson = new Gson();

    public static Gson getGson() {
        return gson;
    }

}
