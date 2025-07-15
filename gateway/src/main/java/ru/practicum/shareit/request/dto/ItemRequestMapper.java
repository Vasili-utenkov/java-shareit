package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserMapper;


import java.time.LocalDateTime;

public class ItemRequestMapper {

    public static ItemRequestDto toDto(ItemRequest request) {
        return ItemRequestDto.builder()
                .id(request.getId())
                .description(request.getDescription())
                .requester(UserMapper.toDto(request.getUser()))
                .created(request.getCreated() != null ? request.getCreated() : LocalDateTime.now())
                .build();
    }




    public static ItemRequest toEntity(ItemRequestDto dto) {
        return ItemRequest.builder()
                .id(dto.getId())
                .description(dto.getDescription())
                .created(dto.getCreated() != null ? dto.getCreated() : LocalDateTime.now())
                // user устанавливается отдельно!
                .build();
    }

}
