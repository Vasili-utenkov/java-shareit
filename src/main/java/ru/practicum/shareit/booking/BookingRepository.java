package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Booking;

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


}
