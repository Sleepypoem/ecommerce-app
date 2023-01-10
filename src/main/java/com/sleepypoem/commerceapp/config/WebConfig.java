package com.sleepypoem.commerceapp.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.Optional;

@Configuration
@EnableWebMvc
@ComponentScan("com.sleepypoem.signinapp.controllers")
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        Optional<HttpMessageConverter<?>> converterFound = converters.stream().filter(AbstractJackson2HttpMessageConverter.class::isInstance).findFirst();
        if (converterFound.isPresent()) {
            AbstractJackson2HttpMessageConverter converter = (AbstractJackson2HttpMessageConverter) converterFound.get();
            converter.getObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
            converter.getObjectMapper().enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        }
    }

    @Bean
    public MappingJackson2HttpMessageConverter jackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        Jackson2ObjectMapperBuilder builder = this.jacksonBuilder();
        converter.setObjectMapper(builder.build());

        return converter;
    }

    @Bean
    public Jackson2ObjectMapperBuilder jacksonBuilder() {
        return new Jackson2ObjectMapperBuilder();
    }

    @Bean
    public com.fasterxml.jackson.databind.ObjectMapper objectMapper(){
        return jacksonBuilder().build();
    }

}
