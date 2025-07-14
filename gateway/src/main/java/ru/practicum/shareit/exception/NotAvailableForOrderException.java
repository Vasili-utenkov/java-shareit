package ru.practicum.shareit.exception;


public class NotAvailableForOrderException extends RuntimeException {
    public NotAvailableForOrderException(String message) {
        super(message);
    }
}