package ru.practicum.shareit.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.comment.CommentRepository;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.exception.NotFoundException;


@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    /**
     * Проверка переданого в поиск кода коментария
     *
     * @param commentId ID комментария
     */
    @Override
    public Comment validateCommentExists(Long commentId) {
        // Проверка на null ID
        if (commentId == null) {
            throw new IllegalArgumentException("ID предмета не может быть null");
        }

        // Проверка существования комментария
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Предмет с ID " + commentId + " не найден"));
    }
}
