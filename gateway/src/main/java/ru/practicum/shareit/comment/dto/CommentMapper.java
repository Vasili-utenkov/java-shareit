package ru.practicum.shareit.comment.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CommentMapper {

    public static CommentDto toDto(Comment comment) {

        return CommentDto.builder()
                .id(comment.getId())
                .authorName(comment.getAuthor().getName()).text(comment.getText())
                .created(comment.getCreated()).build();
    }

    public static Comment toEntity(CommentCreateDto dto, User user, Item item) {
        return Comment.builder()
                .author(user)
                .text(dto.getText())
                .item(item)
                .created(LocalDateTime.now())
                .build();
    }

    public static List<CommentDto> toDtoList(List<Comment> comments) {
        return comments.stream().map(CommentMapper::toDto).toList();
    }

}

