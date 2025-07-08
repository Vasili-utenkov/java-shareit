package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.enums.BookingStatus;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemStorage itemStorage;
    private final ItemService itemService;

    /**
     * Создание нового бронирования
     *
     * @param bookingDto данные бронирования (start, end, itemId)
     * @param bookerId   идентификатор пользователя, создающего бронирование
     * @return BookingDto созданное бронирование со статусом WAITING
     * @throws IllegalArgumentException Предмет недоступен к заказу.
     * @throws IllegalArgumentException Владелец не может бронировать свою вещь.
     */
    @Override
//    public BookingDto createBooking(BookingShortDto bookingDto, Long bookerId) {
    public BookingDto createBooking(BookingDto bookingDto, Long bookerId) {
        log.warn("createBooking(BookingDto {}, Long {})", bookingDto, bookerId);
        User booker = userService.validateUserId(bookerId);
        ItemDto itemDto = bookingDto.getItem();
        if (itemDto != null) {

            Item item = ItemMapper.toEntity(bookingDto.getItem());

            // Вещь в доступе к заказу
            if (!item.getAvailable()) {
                throw new IllegalArgumentException("Предмет недоступен к заказу.");
            }

            // Владелец не может бронировать свою вещь
            if (bookerId.equals(item.getOwner().getId())) {
                throw new IllegalArgumentException("Владелец не может бронировать свою вещь.");
            }
        }

        return BookingMapper.toDto(bookingRepository.save(BookingMapper.toEntity(bookingDto)));
    }

    /**
     * Подтверждение или отклонение бронирования владельцем вещи
     *
     * @param ownerId   идентификатор владельца вещи
     * @param bookingId ID заказа
     * @param approved  true - подтверждение (APPROVED), false - отклонение (REJECTED)
     * @return BookingDto обновлённое бронирование
     * @throws IllegalArgumentException Изменить статус может только владелец вещи.
     * @throws IllegalArgumentException Параметр approved не может быть null.
     * @throws IllegalArgumentException Статус можно изменить только из состояния WAITING.
     */
    @Override
    public BookingDto updateBookingStatus(Long ownerId, Long bookingId, Boolean approved) {
        log.warn("updateBookingStatus(Long ownerId, Long bookingId, Boolean approved)");
        userService.validateUserId(ownerId);

        // Проверяем, что параметр approved не null
        if (approved == null) {
            throw new IllegalArgumentException("Параметр approved не может быть null.");
        }

        Booking existingBooking = validateBookingExists(bookingId);
        Item existingItem = itemService.validateItemExists(existingBooking.getItem().getId());

        // Изменить статус может только владелец вещи
        if (!ownerId.equals(existingItem.getOwner().getId())) {
            throw new IllegalArgumentException("Изменить статус может только владелец вещи.");
        }

        // Проверяем, что статус можно изменить (только из WAITING)
        if (existingBooking.getStatus() != BookingStatus.WAITING) {
            throw new IllegalArgumentException("Статус можно изменить только из состояния WAITING.");
        }

        // Обновляем статус в зависимости от approved
        existingBooking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);

        // обновление статуса доступности вещи к заказу, если бронь подтверждена
        if (existingBooking.getStatus().equals(BookingStatus.APPROVED)) {
            existingItem.setAvailable(false);
            itemStorage.update(existingItem.getId(), existingItem);
        }

        return BookingMapper.toDto(bookingRepository.save(existingBooking));
    }

    /**
     * Получение информации о бронировании по ID
     *
     * @param userId    ID пользователя (автор или владелец)
     * @param bookingId ID заказа
     * @return BookingDto данные бронирования
     */
    @Override
    public BookingDto getBookingById(Long userId, Long bookingId) {
        log.warn("getBookingById(Long {}, Long {})", userId, bookingId);
        User user = userService.validateUserId(userId);
        Booking existingBooking = validateBookingExists(bookingId);
        return BookingMapper.toDto(existingBooking);
    }

    /**
     * Получение списка бронирований пользователя с фильтром по статусу
     *
     * @param userId ID пользователя
     * @param state  статус бронирования (ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED)
     * @return List<BookingDto> список бронирований, отсортированный по дате начала (новые → старые)
     */
    @Override
    public List<BookingDto> getUserBookings(Long userId, BookingStatus state) {
        log.warn("");
        return null;
    }

    /**
     * Получение списка бронирований для всех вещей владельца
     *
     * @param ownerId ID владельца вещей
     * @param state   статус бронирования (ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED)
     * @return List<BookingDto> список бронирований, отсортированный по дате начала (новые → старые)
     */
    @Override
    public List<BookingDto> getOwnerBookings(Long ownerId, BookingStatus state) {
        log.warn("");
        return null;
    }


    /**
     * @param bookingId ID заказа
     * @return Booking
     */
    public Booking validateBookingExists(Long bookingId) {
        log.warn("validateBookingId(Long {})", bookingId);
        // Проверка на null ID
        if (bookingId == null) {
            throw new IllegalArgumentException("ID заказа не может быть null");
        }

        // Проверка существования заказа
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Заказ с ID " + bookingId + " не найден"));
    }

}
