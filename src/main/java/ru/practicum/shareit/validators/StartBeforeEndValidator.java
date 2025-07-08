package ru.practicum.shareit.validators;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.practicum.shareit.booking.dto.BookingShortDto;

public class StartBeforeEndValidator implements ConstraintValidator<StartBeforeEnd, BookingShortDto> {

    @Override
    public void initialize(StartBeforeEnd constraintAnnotation) {
    }

    @Override
    public boolean isValid(BookingShortDto bookingDto, ConstraintValidatorContext context) {

        if (bookingDto.getStart() == null || bookingDto.getEnd() == null) {
            return true; // null-значения обрабатываются другими аннотациями
        }

        return bookingDto.getEnd().isAfter(bookingDto.getStart());
    }

}