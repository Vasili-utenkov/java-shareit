package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.validators.StartBeforeEnd;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@StartBeforeEnd
public class BookingShortDto {
    private Long itemId;
    @NotNull(message = "Дата начала аренды должна быть")
    private LocalDateTime start;
    @NotNull(message = "Дата окончания аренды должна быть")
    private LocalDateTime end;
}