package com.sleepypoem.commerceapp.annotations;

import com.sleepypoem.commerceapp.config.beans.ApplicationContextProvider;
import com.sleepypoem.commerceapp.domain.abstracts.AbstractEntity;
import com.sleepypoem.commerceapp.exceptions.MyValidableAnnotationException;
import com.sleepypoem.commerceapp.services.validators.DetailedValidator;
import com.sleepypoem.commerceapp.services.validators.IValidator;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ValidatorAdvisor {

    @Before("validableMethodPointcut() && validableMethodAnnotationPointcut()")
    public void validateMethod(JoinPoint joinPoint) {
        IValidator<?> validator = extractValidatorClass(joinPoint.getTarget().getClass());
        AbstractEntity<?> entity = extractEntityFromArgs(joinPoint.getArgs());
        @SuppressWarnings({"rawtypes", "unchecked"})
        DetailedValidator detailedValidatorClass = new DetailedValidator(validator, entity);
        try {
            detailedValidatorClass.validate();
        } catch (ClassCastException e) {
            throw new MyValidableAnnotationException("Error in validator, expected validator for: " + entity.getClass().getSimpleName()
                    + " in Validable annotation in  class: " + joinPoint.getTarget().getClass().getSimpleName()
                    , e);
        }

    }

    @Pointcut("execution( public * (@com.sleepypoem.commerceapp.annotations.Validable *)+.*(..))")
    public void validableMethodPointcut() {
    }

    @Pointcut("@annotation(com.sleepypoem.commerceapp.annotations.ValidableMethod)")
    public void validableMethodAnnotationPointcut() {
    }

    private IValidator<?> extractValidatorClass(Class<?> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("Class cannot be null.");
        }

        Validable annotation = clazz.getAnnotation(Validable.class);

        Class<? extends IValidator<?>> validatorClass = annotation.value();
        ApplicationContext applicationContext = ApplicationContextProvider.applicationContext;
        return applicationContext.getBean(validatorClass);
    }

    private AbstractEntity<?> extractEntityFromArgs(Object[] args) {
        AbstractEntity<?> entity = null;
        for (Object arg : args) {
            if (arg instanceof AbstractEntity<?>) {
                entity = (AbstractEntity<?>) arg;
            }
        }
        if (entity == null) {
            throw new MyValidableAnnotationException("Annotated method must have an argument of type AbstractEntity");
        }
        return entity;
    }

}
