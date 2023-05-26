package com.sleepypoem.commerceapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MyValidationException extends RuntimeException {

    private Map<String, String> errors;

    public MyValidationException(String message) {
        super(message);
    }

    public MyValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyValidationException(Map<String, String> errors) {
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }

    @Override
    public String getMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("The following errors were found during validation : {\n");
        for (Map.Entry<String, String> entry : errors.entrySet()) {
            sb.append("Field: ");
            sb.append(entry.getKey());
            sb.append(" || Error: ");
            sb.append(entry.getValue());
            sb.append(", \n");

        }
        sb.delete(sb.length() - 3, sb.length());
        sb.append("\n}");
        return sb.toString();
    }
}
