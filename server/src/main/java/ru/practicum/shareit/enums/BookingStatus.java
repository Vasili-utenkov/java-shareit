package ru.practicum.shareit.enums;

import java.util.Optional;

public enum BookingStatus {
    WAITING, // ОЖИДАНИЕ
    APPROVED, // ОДОБРЕНО
    REJECTED, // ОТКЛОНЕНО
    CANCELED, // ОТМЕНЕНО
    ENDED; // ЗАВЕРШЕНО

    public static Optional<BookingStatus> from(String stringState) {
        for (BookingStatus state : values()) {
            if (state.name().equalsIgnoreCase(stringState)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }

}
