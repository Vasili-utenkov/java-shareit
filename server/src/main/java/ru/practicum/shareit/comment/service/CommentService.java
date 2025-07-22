package ru.practicum.shareit.comment.service;

import ru.practicum.shareit.comment.model.Comment;

public interface CommentService {

    /**
     * Проверка переданого в поиск кода коментария
     *
     * @param commentId ID комментария
     */
    Comment validateCommentExists(Long commentId);

}
