package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.enums.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {
    private Long id;
    private ItemDto item;
    private UserDto booker;
    @NotNull(message = "Дата начала аренды должна быть")
    @FutureOrPresent(message = "Дата начала аренды не должна быть в прошлом")
    private LocalDateTime start;
    @NotNull(message = "Дата окончания аренды должна быть")
    @FutureOrPresent(message = "Дата окончания аренды не должна быть в прошлом")
    private LocalDateTime end;
    private BookingStatus status;
}