package ru.practicum.shareit.validators;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

public class StartBeforeEndValidator implements ConstraintValidator<StartBeforeEnd, Object> {

    private String startFieldName;
    private String endFieldName;

    @Override
    public void initialize(StartBeforeEnd constraintAnnotation) {
        this.startFieldName = constraintAnnotation.startField();
        this.endFieldName = constraintAnnotation.endField();
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

            return end.isAfter(start);
        } catch (Exception e) {
            return false;
        }
    }
}