package ru.practicum.shareit.comment.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemDtoGW;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDtoGW {
    private Long id;
    @NotBlank(message = "Текст не может быть пустым")
    private String text;
    private ItemDtoGW item;
    private String authorName;
    @FutureOrPresent(message = "Дата нового комментария не может быть в прошлом")
    private LocalDateTime created;
}
