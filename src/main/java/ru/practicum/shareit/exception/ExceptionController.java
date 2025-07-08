package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ExceptionController {

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DataConflictException.class)
    public Map<String, String> handleEmailExists(DataConflictException e) {
        log.warn("ПРОВЕРКА: ExceptionController: handleEmailExists: " + e.getMessage());
        return Map.of("error", e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND) // 404
    @ExceptionHandler(NotFoundException.class)
    public Map<String, String> handleNotFound(NotFoundException e) {
        log.warn("ПРОВЕРКА: ExceptionController: handleNotFound: " + e.getMessage());
        return Map.of("error", e.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(IllegalArgumentException.class)
    public Map<String, String> handleIllegalArgument(IllegalArgumentException e) {
        log.warn("ПРОВЕРКА: ExceptionController: handleIllegalArgument: " + e.getMessage());
        return Map.of("error", e.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ItemNotAvailableException.class)
    public Map<String, String> ItemNotAvailableException(ItemNotAvailableException e) {
        log.warn("ПРОВЕРКА: ExceptionController: ItemNotAvailableException: " + e.getMessage());
        return Map.of("error", e.getMessage());
    }

//    @ResponseStatus(HttpStatus.FORBIDDEN) // 403
    @ResponseStatus(HttpStatus.NOT_FOUND) // 404
    @ExceptionHandler(UserNotExistsException.class)
    public Map<String, String> UserNotExistsException(UserNotExistsException e) {
        log.warn("ПРОВЕРКА: ExceptionController: UserNotExistsException: " + e.getMessage());
        return Map.of("error", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    @ExceptionHandler(NotAvailableForOrderException.class)
    public Map<String, String> NotAvailableForOrderException(NotAvailableForOrderException e) {
        log.warn("ПРОВЕРКА: ExceptionController: NotAvailableForOrderException: " + e.getMessage());
        return Map.of("error", e.getMessage());
    }

}