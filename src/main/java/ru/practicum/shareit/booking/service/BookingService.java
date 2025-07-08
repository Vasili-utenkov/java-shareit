package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.enums.BookingStatus;

import java.util.List;

public interface BookingService {

    /**
     * Создание нового бронирования
     *
     * @param bookingDto данные бронирования (start, end, itemId)
     * @param bookerId идентификатор пользователя, создающего бронирование
     * @return BookingDto созданное бронирование со статусом WAITING
     */
    BookingDto createBooking(BookingDto bookingDto, Long bookerId);

    /**
     * Подтверждение или отклонение бронирования владельцем вещи
     *
     * @param ownerId идентификатор владельца вещи
     * @param bookingId ID заказа
     * @param approved true - подтверждение (APPROVED), false - отклонение (REJECTED)
     * @return BookingDto обновлённое бронирование
     */
    BookingDto updateBookingStatus(Long ownerId, Long bookingId, Boolean approved);

    /**
     * Получение информации о бронировании по ID
     *
     * @param userId идентификатор пользователя (автор или владелец)
     * @param bookingId ID заказа
     * @return BookingDto данные бронирования
     */
    BookingDto getBookingById(Long userId, Long bookingId);

    /**
     * Получение списка бронирований пользователя с фильтром по статусу
     *
     * @param userId ID пользователя
     * @param state статус бронирования (ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED)
     * @return List<BookingDto> список бронирований, отсортированный по дате начала (новые → старые)
     */
    List<BookingDto> getUserBookings(Long userId, BookingStatus state);

    /**
     * Получение списка бронирований для всех вещей владельца
     *
     * @param ownerId идентификатор владельца вещей
     * @param state статус бронирования (ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED)
     * @return List<BookingDto> список бронирований, отсортированный по дате начала (новые → старые)
     */
    List<BookingDto> getOwnerBookings(Long ownerId, BookingStatus state);
}
