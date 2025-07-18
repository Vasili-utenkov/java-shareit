package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemDtoGW;
import ru.practicum.shareit.user.dto.UserDtoGW;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDtoGW {
    private Long id;
    @NotBlank(message = "Описание не может быть пустым")
    private String description;
    @NotNull(message = "Пользователь не может быть null")
    private UserDtoGW requester;
    @FutureOrPresent(message = "Дата создания не должна быть в прошлом")
    private LocalDateTime created;
    private List<ItemDtoGW> items;
}
