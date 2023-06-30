package com.sleepypoem.commerceapp.config.beans;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonProvider {

    private GsonProvider() {
    }

    private static final Gson gson = new GsonBuilder()
            .create();

    public static Gson getGson() {
        return gson;
    }

}
