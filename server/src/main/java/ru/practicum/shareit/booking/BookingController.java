package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.enums.BookingState;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingDto createBooking(
            @RequestHeader("X-Sharer-User-Id") Long bookerId,
            @RequestBody BookingShortDto bookingDto
    ) {
        log.warn("Добавление бронирования. @PostMapping (/bookings) ");
        log.warn("createBooking( @RequestHeader(X-Sharer-User-Id) Long {}, @Valid @RequestBody BookingShortDto {} )",
                bookerId, bookingDto);
        BookingDto dto = bookingService.createBooking(bookerId, bookingDto);
        log.warn("ИТОГ: Создали бронь " + dto);
        return dto;
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approveBooking(
            @RequestHeader("X-Sharer-User-Id") Long bookerId,
            @PathVariable Long bookingId,
            @RequestParam Boolean approved
    ) {
        log.warn("Подтверждение/отклонение бронирования. @PatchMapping (/bookings/{bookingId})");
        log.warn("approveBooking(" +
                "@RequestHeader(X-Sharer-User-Id) Long {}," +
                "@PathVariable Long {}," +
                "@RequestParam Boolean {}" +
                ")", bookerId, bookingId, approved);
        BookingDto dto = bookingService.approveBooking(bookerId, bookingId, approved);
        return dto;
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(
            @RequestHeader("X-Sharer-User-Id") Long bookerId,
            @PathVariable Long bookingId
    ) {
        log.warn("Получение данных о бронировании по ID брони. @GetMapping (/bookings/{bookingId})");
        log.warn("getBookingById(@RequestHeader(X-Sharer-User-Id) Long {}, @PathVariable Long {})",
                bookerId, bookingId);
        BookingDto dto = bookingService.getBookingById(bookerId, bookingId);
        return dto;
    }

    @GetMapping
    public List<BookingDto> getUserBookings(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(name = "state", defaultValue = "ALL") BookingState state
    ) {
        log.warn("Получение списка бронирований для пользователя. @GetMapping (/bookings)");
        log.warn("getUserBookings(@RequestHeader(X-Sharer-User-Id) Long {}," +
                        " @RequestParam(name = state, defaultValue = ALL) BookingState {})",
                userId, state);
        List<BookingDto> bookingDtoList = bookingService.getUserBookings(userId, state);
        return bookingDtoList;
    }

    @GetMapping("/owner")
    public List<BookingDto> getOwnerBookings(
            @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @RequestParam(name = "state", defaultValue = "ALL") BookingState state
    ) {
        log.warn("Получение списка бронирований всех вещей по владельцу вещи. @GetMapping (/bookings/owner)");
        log.warn("getOwnerBookings(@RequestHeader(X-Sharer-User-Id) Long {}," +
                        " @RequestParam(name = state, defaultValue = ALL) BookingState {})",
                ownerId, state);
        List<BookingDto> bookingDtoList = bookingService.getOwnerBookings(ownerId, state);
        return bookingDtoList;
    }
}
