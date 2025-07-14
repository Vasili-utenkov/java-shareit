package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.enums.BookingState;
import ru.practicum.shareit.enums.BookingStatus;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.NotAvailableForOrderException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemService itemService;

    /**
     * Создание нового бронирования
     *
     * @param bookingDto данные бронирования (start, end, itemId)
     * @param bookerId   идентификатор пользователя, создающего бронирование
     * @return BookingDto созданное бронирование со статусом WAITING
     * @throws NotAvailableForOrderException Предмет недоступен к заказу.
     * @throws IllegalArgumentException      Владелец не может бронировать свою вещь.
     */
    @Override
    public BookingDto createBooking(Long bookerId, BookingShortDto bookingDto) {
        log.warn("createBooking(Long {}, BookingDto {})", bookerId, bookingDto);
        User booker = userService.validateUserExists(bookerId);
        Item item = itemService.validateItemExists(bookingDto.getItemId());

        // Вещь в доступе к заказу
        if (!item.getAvailable()) {
            throw new NotAvailableForOrderException("Предмет недоступен к заказу.");
        }

        // Владелец не может бронировать свою вещь
        if (bookerId.equals(item.getOwner().getId())) {
            throw new IllegalArgumentException("Владелец не может бронировать свою вещь.");
        }
        Booking booking = bookingRepository.save(BookingMapper.toEntity(bookingDto, booker, item));
        return BookingMapper.toDto(bookingRepository.save(booking));
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
    public BookingDto approveBooking(Long ownerId, Long bookingId, Boolean approved) {
        log.warn("updateBookingStatus(Long ownerId, Long bookingId, Boolean approved)");
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
            existingBooking.setItem(existingItem);
        }

        return BookingMapper.toDto(bookingRepository.save(existingBooking));
    }

    /**
     * Получение информации о бронировании по ID
     *
     * @param userId    ID пользователя (автор или владелец)
     * @param bookingId ID заказа
     * @return BookingDto данные бронирования
     * @throws AccessDeniedException Информацию о брони может смотреть только владелец вещи или автор бронирования.
     */
    @Override
    public BookingDto getBookingById(Long userId, Long bookingId) {
        log.warn("getBookingById(Long {}, Long {})", userId, bookingId);
        Booking existingBooking = validateBookingExists(bookingId);

//  Может быть выполнено либо автором бронирования, либо владельцем вещи, к которой относится бронирование.
        Item item = existingBooking.getItem();
// арендатор
        User booker = existingBooking.getBooker();
// Владелец вещи
        User owner = item.getOwner();

        if (!userId.equals(booker.getId()) && !userId.equals(owner.getId())) {
            throw new AccessDeniedException("Информацию о брони может смотреть только владелец вещи или автор бронирования.");
        }
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
    public List<BookingDto> getUserBookings(Long userId, BookingState state) {
        log.warn("getUserBookings(Long {}, BookingStatus {})", userId, state);
        userService.validateUserExists(userId);
        List<Booking> bookings = bookingRepository.findUserBookingsByState(userId, state.toString());

        return bookings.stream()
                .map(BookingMapper::toDto)
                .collect(Collectors.toList());

    }

    /**
     * Получение списка бронирований для всех вещей владельца
     *
     * @param ownerId ID владельца вещей
     * @param state   статус бронирования (ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED)
     * @return List<BookingDto> список бронирований, отсортированный по дате начала (новые → старые)
     */
    @Override
    public List<BookingDto> getOwnerBookings(Long ownerId, BookingState state) {
        log.warn("getOwnerBookings(Long {}, BookingStatus {})", ownerId, state);
        userService.validateUserExists(ownerId);
        List<Booking> bookings = bookingRepository.findOwnerBookingsByState(ownerId, state.toString());

        return bookings.stream()
                .map(BookingMapper::toDto)
                .collect(Collectors.toList());
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


    /**
     * Прользователь пользовался в аренде вещью на момент bookerEndTime
     *
     * @param userId        ID пользователя
     * @param itemId        ID предмета
     * @param bookerEndTime дата запроса аренды вещи
     * @throws NotAvailableForOrderException Пользователь не брал этот предмет в аренду или аренда еще не завершена
     */
    @Override
    public void validateUserBookedItem(Long userId, Long itemId, LocalDateTime bookerEndTime) {

        List<Booking> userBookings = bookingRepository.findCompletedBookingsByUserAndItem(
                userId,
                itemId,
                bookerEndTime
        );
        if (userBookings.isEmpty()) {
            throw new NotAvailableForOrderException("Пользователь не брал этот предмет в аренду или аренда еще не завершена");
        }
    }
}
