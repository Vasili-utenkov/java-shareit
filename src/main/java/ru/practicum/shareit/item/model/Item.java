package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    private Long id;
    private String name;
    private String description;
    private Boolean available; // Доступна для аренды?
    private Long ownerId; // ID владельца (User)
    private Long requestId; // ID запроса (ItemRequest), если вещь создана в ответ на него
}
