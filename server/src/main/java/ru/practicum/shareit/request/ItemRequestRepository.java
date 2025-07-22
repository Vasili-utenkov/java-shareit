package ru.practicum.shareit.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    @Query("SELECT r FROM ItemRequest r WHERE r.user.id = :userID ORDER BY r.created DESC")
    List<ItemRequest> findItemRequestsByUser(@Param("userID") Long userId);

    @Query("SELECT r FROM ItemRequest r WHERE r.user.id <> :userID ORDER BY r.created DESC")
    List<ItemRequest> findItemRequestsAllUser(@Param("userID") Long userId);

}
