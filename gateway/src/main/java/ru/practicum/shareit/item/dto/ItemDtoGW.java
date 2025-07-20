package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDtoGW;
import ru.practicum.shareit.comment.dto.CommentDtoGW;
import ru.practicum.shareit.request.dto.ItemRequestDtoGW;
import ru.practicum.shareit.user.dto.UserDtoGW;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemDtoGW {
    private Long id;
    @NotBlank(message = "Наименование не может быть пустым")
    private String name;
    @NotBlank(message = "Описание не может быть пустым")
    private String description;
    @NotNull(message = "Статус доступности аренды не может быть null")
    private Boolean available; // Доступна для аренды?
    private UserDtoGW owner; // владелец (User)
    private ItemRequestDtoGW request; // запрос (ItemRequest), если вещь создана в ответ на него
    private List<CommentDtoGW> comments;

    private BookingDtoGW lastBooking;
    private BookingDtoGW nextBooking;
}

