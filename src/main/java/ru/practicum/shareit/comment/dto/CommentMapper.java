package ru.practicum.shareit.comment.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.user.dto.UserMapper;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CommentMapper {

    public CommentDto toDto(Comment comment) {

        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .item(ItemMapper.toDto(comment.getItem()))
                .author(UserMapper.toDto(comment.getAuthor()))
                .created(comment.getCreated())
                .build();
    }

    public Comment toEntity(CommentDto commentDto) {
        return Comment.builder()
                .id(commentDto.getId())
                .text(commentDto.getText())
                .created(commentDto.getCreated() != null ? commentDto.getCreated() : LocalDateTime.now())
                .build();
    }
}

