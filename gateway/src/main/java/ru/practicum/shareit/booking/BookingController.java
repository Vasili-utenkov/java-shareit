package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.enums.BookingState;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> createBooking(@RequestHeader("X-Sharer-User-Id") Long bookerId,
                                                @RequestBody @Valid BookingShortDto bookingDto) {
        log.warn("GATEWAY:: Добавление бронирования. @PostMapping (/bookings) ");
        log.warn("createBooking( @RequestHeader(X-Sharer-User-Id) Long {}, @Valid @RequestBody BookingShortDto {} )",
                bookerId, bookingDto);

        ResponseEntity<Object> response = bookingClient.createBooking(bookerId, bookingDto);
        log.warn("GATEWAY:: ИТОГ: Создали бронирование " + response);
        return response;
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approveBooking(
            @RequestHeader("X-Sharer-User-Id") long bookerId,
            @PathVariable long bookingId,
            @RequestParam Boolean approved
    ) {
        log.warn("GATEWAY:: Подтверждение/отклонение бронирования. @PatchMapping (/bookings/{bookingId})");
        log.warn("approveBooking(" +
                "@RequestHeader(X-Sharer-User-Id) Long {}," +
                "@PathVariable Long {}," +
                "@RequestParam Boolean {}" +
                ")", bookerId, bookingId, approved);

        ResponseEntity<Object> response = bookingClient.approveBooking(bookerId, bookingId, approved);
        return response;
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingById(@RequestHeader("X-Sharer-User-Id") long bookerId,
                                                 @PathVariable long bookingId) {
        log.warn("GATEWAY:: Получение данных о бронировании по ID брони. @GetMapping (/bookings/{bookingId})");
        log.warn("getBookingById(@RequestHeader(X-Sharer-User-Id) Long {}, @PathVariable Long {})",
                bookerId, bookingId);

        ResponseEntity<Object> response = bookingClient.getBookingById(bookerId, bookingId);
        return response;
    }

    @GetMapping
    public ResponseEntity<Object> getUserBookings(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam(name = "state", defaultValue = "all") String stateParam) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        log.warn("GATEWAY:: Получение списка бронирований для пользователя. @GetMapping (/bookings)");
        log.warn("getUserBookings(@RequestHeader(X-Sharer-User-Id) Long {}," +
                        " @RequestParam(name = state, defaultValue = ALL) BookingState {})",
                userId, state);

        ResponseEntity<Object> response = bookingClient.getUserBookings(userId, state);
        return response;
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getOwnerBookings(
            @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @RequestParam(name = "state", defaultValue = "all") String stateParam) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        log.warn("GATEWAY:: Получение списка бронирований всех вещей по владельцу вещи. @GetMapping (/bookings/owner)");
        log.warn("getOwnerBookings(@RequestHeader(X-Sharer-User-Id) Long {}," +
                        " @RequestParam(name = state, defaultValue = ALL) BookingState {})",
                ownerId, state);

        ResponseEntity<Object> response = bookingClient.getOwnerBookings(ownerId, state);
        return response;
    }
}
