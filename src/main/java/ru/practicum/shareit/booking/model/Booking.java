package ru.practicum.shareit.booking.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.enums.BookingStatus;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Booking {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private BookingStatus status; // WAITING, APPROVED, REJECTED, CANCELED
    private Long itemId; // ID вещи
    private Long bookerId; // ID арендатора (User)
}
