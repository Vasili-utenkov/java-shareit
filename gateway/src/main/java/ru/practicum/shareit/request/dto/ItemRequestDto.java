package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDto {
    private Long id;
    @NotBlank(message = "Описание не может быть пустым")
    private String description;
    @NotNull(message = "Пользователь не может быть null")
    private UserDto requester;
    @FutureOrPresent(message = "Дата создания не должна быть в прошлом")
    private LocalDateTime created;
}
