package com.sleepypoem.commerceapp.config.exception;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.sleepypoem.commerceapp.domain.dto.errors.ApiError;
import com.sleepypoem.commerceapp.domain.dto.errors.RequestFieldError;
import com.sleepypoem.commerceapp.exceptions.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@ControllerAdvice
@Slf4j
public class RestResponseEntityExceptionHandling extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.info(ex.getLocalizedMessage());
        String msg = null;
        Throwable cause = ex.getCause();

        if (cause instanceof JsonParseException) {
            JsonParseException jpe = (JsonParseException) cause;
            msg = jpe.getOriginalMessage();
        }

        // special case of JsonMappingException below, too much class detail in error messages
        else if (cause instanceof MismatchedInputException) {
            MismatchedInputException mie = (MismatchedInputException) cause;
            if (mie.getPath() != null && mie.getPath().size() > 0) {
                msg = "Invalid request field: " + mie.getPath().get(0).getFieldName();
            }

            // just in case, haven't seen this condition
            else {
                msg = "Invalid request message";
            }
        }

        else if (cause instanceof JsonMappingException) {
            JsonMappingException jme = (JsonMappingException) cause;
            msg = jme.getOriginalMessage();
            if (jme.getPath() != null && jme.getPath().size() > 0) {
                msg = "Invalid request field: " + jme.getPath().get(0).getFieldName() +
                        ": " + msg;
            }
        }
        return handleExceptionInternal(ex, message(HttpStatus.BAD_REQUEST, msg, ex),headers, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = { MyBadRequestException.class,
                                MyEntityNotFoundException.class,
                                MyResourceNotFoundException.class,
                                MyValidationException.class,
                                MyUserNameAlreadyUsedException.class,
                                MyUserNotFoundException.class
                              })
    public ResponseEntity<Object> handleBadRequest(Exception ex, WebRequest request){
        return handleExceptionInternal(ex, message(HttpStatus.BAD_REQUEST, ex),new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = { ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintException(ConstraintViolationException ex, WebRequest request){
        Set<ConstraintViolation<?>> constraintViolationsSet = ex.getConstraintViolations();

       Map<String, String> errors = new HashMap<>();

        for (ConstraintViolation<?> violation : constraintViolationsSet) {
            errors.put(String.valueOf(violation.getPropertyPath()), violation.getMessage());
        }
        return handleExceptionInternal(ex, fields(HttpStatus.BAD_REQUEST, errors),new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    private ApiError message(HttpStatus status, Exception ex){
        final String message = ex.getMessage() == null ? ex.getClass().getSimpleName() : ex.getMessage();
        final String devMessage = ExceptionUtils.getMessage(ex);

        return new ApiError(status.value(), message, devMessage);
    }

    private ApiError message(HttpStatus status, String message, Exception ex){
        final String devMessage = ExceptionUtils.getMessage(ex);

        return new ApiError(status.value(), message, devMessage);
    }

    private RequestFieldError fields(HttpStatus status, Map<String, String> errors){
        final String message = "There are some errors in the following fields";
        return new RequestFieldError(status, message, errors);
    }
}
