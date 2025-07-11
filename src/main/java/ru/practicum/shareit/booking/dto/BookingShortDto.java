package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
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
@StartBeforeEnd(startField = "start", endField = "end")
public class BookingShortDto {
    @NotNull(message = "Должен присутствовать предмет  аренды")
    private Long itemId;
    @FutureOrPresent(message = "Дата начала аренды должна быть в будущем")
    @NotNull(message = "Дата начала аренды должна быть")
    private LocalDateTime start;
    @Future(message = "Дата начала аренды должна быть в будущем")
    @NotNull(message = "Дата окончания аренды должна быть")
    private LocalDateTime end;
}