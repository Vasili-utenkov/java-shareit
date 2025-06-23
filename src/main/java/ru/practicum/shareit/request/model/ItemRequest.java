package ru.practicum.shareit.request.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class ItemRequest {
//    private Long id;
//    private String description; // Что ищет пользователь
//    private Long requesterId; // ID создателя запроса (User)
//    private LocalDateTime created; // Дата создания
}
