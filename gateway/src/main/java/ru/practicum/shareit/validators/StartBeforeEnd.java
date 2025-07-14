package ru.practicum.shareit.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StartBeforeEndValidator.class)
public @interface StartBeforeEnd {
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String startField() default "start";  // имя поля с датой начала
    String endField() default "end";      // имя поля с датой окончания
    String message() default "{validators.StartBeforeEnd.message}";
}
