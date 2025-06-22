package ru.practicum.shareit.booking.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserMapper;

@Component
@RequiredArgsConstructor
public class BookingMapper {
    private final UserMapper userMapper;

    public BookingDto toDto(Booking booking, User booker, Item item) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .itemId(booking.getItemId())
                .bookerId(booking.getBookerId())
                .booker(userMapper.toDto(booker))
                .build();
    }

    public Booking toEntity(BookingDto bookingDto) {
        return Booking.builder()
                .id(bookingDto.getId())
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .status(bookingDto.getStatus())
                .itemId(bookingDto.getItemId())
                .bookerId(bookingDto.getBookerId())
                .build();
    }
}
