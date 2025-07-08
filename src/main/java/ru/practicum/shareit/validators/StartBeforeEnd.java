package ru.practicum.shareit.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StartBeforeEndValidator.class)
@Documented
public @interface StartBeforeEnd {
    String message() default "Дата начала должна быть раньше даты окончания";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
