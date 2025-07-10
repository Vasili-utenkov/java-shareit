package ru.practicum.shareit.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

public class StartBeforeEndValidator implements ConstraintValidator<StartBeforeEnd, Object> {

    private String startFieldName;
    private String endFieldName;
    private String message;

    @Override
    public void initialize(StartBeforeEnd constraintAnnotation) {
        this.startFieldName = constraintAnnotation.startField();
        this.endFieldName = constraintAnnotation.endField();
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        try {
            Field startField = object.getClass().getDeclaredField(startFieldName);
            Field endField = object.getClass().getDeclaredField(endFieldName);

            startField.setAccessible(true);
            endField.setAccessible(true);

            LocalDateTime start = (LocalDateTime) startField.get(object);
            LocalDateTime end = (LocalDateTime) endField.get(object);

            if (start == null || end == null) {
                return true;
            }

            if (!end.isAfter(start)) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(message)
                        .addPropertyNode(startFieldName)
                        .addConstraintViolation();
                context.buildConstraintViolationWithTemplate(message)
                        .addPropertyNode(endFieldName)
                        .addConstraintViolation();

                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}