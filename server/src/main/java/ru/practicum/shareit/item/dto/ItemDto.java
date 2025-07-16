package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available; // Доступна для аренды?
    private UserDto owner; // владелец (User)
    private ItemRequestDto request; // запрос (ItemRequest), если вещь создана в ответ на него
    private List<CommentDto> comments;

    private BookingDto lastBooking;
    private BookingDto nextBooking;
}

