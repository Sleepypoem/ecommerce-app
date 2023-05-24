package com.sleepypoem.commerceapp.annotations;

import com.sleepypoem.commerceapp.domain.abstracts.AbstractEntity;
import com.sleepypoem.commerceapp.services.validators.IValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Validable {

    Class<? extends IValidator<?>> value();
}
