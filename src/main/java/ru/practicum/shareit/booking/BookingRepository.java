package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Booking;
import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id = :bookerId " +
            "AND (:state = 'ALL' OR " +
            "     (:state = 'CURRENT' AND b.start <= CURRENT_TIMESTAMP AND b.end >= CURRENT_TIMESTAMP) OR " +
            "     (:state = 'PAST' AND b.end < CURRENT_TIMESTAMP) OR " +
            "     (:state = 'FUTURE' AND b.start > CURRENT_TIMESTAMP) OR " +
            "     (:state = 'WAITING' AND b.status = 'WAITING') OR " +
            "     (:state = 'REJECTED' AND b.status = 'REJECTED')) " +
            "ORDER BY b.start DESC")
    List<Booking> findUserBookingsByState(@Param("bookerId") Long bookerId,
                                          @Param("state") String state);


    // Бронирования для всех вещей владельца (с фильтрацией по статусу)
    @Query("SELECT b FROM Booking b " +
            "JOIN b.item i " +
            "WHERE i.owner.id = :ownerId " +
            "AND (:state = 'ALL' OR " +
            "     (:state = 'CURRENT' AND b.start <= CURRENT_TIMESTAMP AND b.end >= CURRENT_TIMESTAMP) OR " +
            "     (:state = 'PAST' AND b.end < CURRENT_TIMESTAMP) OR " +
            "     (:state = 'FUTURE' AND b.start > CURRENT_TIMESTAMP) OR " +
            "     (:state = 'WAITING' AND b.status = 'WAITING') OR " +
            "     (:state = 'REJECTED' AND b.status = 'REJECTED')) " +
            "ORDER BY b.start DESC")
    List<Booking> findOwnerBookingsByState(@Param("ownerId") Long ownerId,
                                           @Param("state") String state);


    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id = :bookerId " +
            "AND b.item.id = :itemId " +
            "AND b.end < :currentTime " +
            "AND b.status IN (ru.practicum.shareit.enums.BookingStatus.APPROVED, ru.practicum.shareit.enums.BookingStatus.ENDED) " +
            "ORDER BY b.end DESC")
    List<Booking> findCompletedBookingsByUserAndItem(
            @Param("bookerId") Long bookerId,
            @Param("itemId") Long itemId,
            @Param("currentTime") LocalDateTime currentTime);


    @Query("SELECT b FROM Booking b " +
            "JOIN FETCH b.item i " +
            "JOIN FETCH b.booker " +
            "WHERE i.id IN :itemIds")
    List<Booking> findAllByItemIdIn(
            @Param("itemIds") List<Long> itemIds);

}
