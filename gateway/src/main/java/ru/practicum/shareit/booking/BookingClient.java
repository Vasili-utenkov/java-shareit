package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookingShortDtoGW;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.enums.BookingState;

import java.util.Map;

@Service
@Slf4j
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings2";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> createBooking(long bookerId, BookingShortDtoGW requestDto) {
        log.warn("BookingClient:: Добавление бронирования. @PostMapping: " +
                        "bookItem(long {}, BookingShortDtoGW {}) ",
                bookerId, requestDto);
        return post("", bookerId, requestDto);
    }

    public ResponseEntity<Object> approveBooking(long bookerId, long bookingId, boolean approved) {
        Map<String, Object> parameters = Map.of(
                "approved", approved
        );
        log.warn("BookingClient::Подтверждение/отклонение бронирования. @PatchMapping: " +
                        "approveBooking(long {}, long {}, boolean {})",
                bookerId, bookingId, approved);
        return patch("/" + bookingId + "?approved=" + approved, bookerId, parameters, null);
    }

    public ResponseEntity<Object> getBookingById(long bookerId, long bookingId) {
        log.warn("BookingClient::Получение данных о бронировании по ID брони. @GetMapping: " +
                        "getBookingById(long {}, long {}))",
                bookerId, bookingId);
        return get("/" + bookingId, bookerId);
    }


    public ResponseEntity<Object> getUserBookings(long userId, BookingState stateParam) {
        log.warn("BookingClient::Получение списка бронирований для пользователя. @GetMapping: " +
                        "getUserBookings(long {}, BookingState {})",
                userId, stateParam);
        Map<String, Object> parameters = Map.of(
                "state", stateParam
        );
        return get("", userId, parameters);
    }

    public ResponseEntity<Object> getOwnerBookings(long bookerId, BookingState stateParam) {
        log.warn("BookingClient::Получение списка бронирований всех вещей по владельцу вещи. @GetMapping:" +
                        " getOwnerBookings(long {}, BookingState {})",
                bookerId, stateParam);
        Map<String, Object> parameters = Map.of(
                "state", stateParam
        );
        return get("/owner", bookerId, parameters);
    }


}