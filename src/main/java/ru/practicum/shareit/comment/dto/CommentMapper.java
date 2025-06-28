package ru.practicum.shareit.comment.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.comment.model.Comment;

@Component
@RequiredArgsConstructor
public class CommentMapper {

    public CommentDto toDto(Comment comment) {

        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .itemId(comment.getItemId())
                .authorId(comment.getAuthorId())
                .created(comment.getCreated())
                .build();
    }

    public Comment toEntity(CommentDto commentDto) {
        return Comment.builder()
                .id(commentDto.getId())
                .text(commentDto.getText())
                .itemId(commentDto.getItemId())
                .authorId(commentDto.getAuthorId())
                .created(commentDto.getCreated())
                .build();
    }
}

