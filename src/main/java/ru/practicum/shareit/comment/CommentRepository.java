package ru.practicum.shareit.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.comment.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByItemId(Long id);

    @Query("SELECT c FROM Comment c JOIN FETCH c.item WHERE c.item.id IN :listItemId")
    List<Comment> findAllByListItemId(@Param("itemIds") List<Long> listItemId);

}
