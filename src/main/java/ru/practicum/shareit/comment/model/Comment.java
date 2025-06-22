package ru.practicum.shareit.comment.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    private Long id;
    private String text;
    private Long itemId; // ID вещи
    private Long authorId; // ID автора (User)
    private LocalDateTime created;
}
