package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.user.dto.UserMapper;

public class BookingMapper {
    private final UserMapper userMapper;

    public BookingMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public static BookingDto toDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .itemId(booking.getItemId())
                .bookerId(booking.getBookerId())
                .build();
    }

    public static Booking toEntity(BookingDto bookingDto) {
        return Booking.builder()
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .status(bookingDto.getStatus())
                .itemId(bookingDto.getItemId())
                .bookerId(bookingDto.getBookerId())
                .build();
    }
}
