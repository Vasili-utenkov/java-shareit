package ru.practicum.shareit.request.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ItemRequestMapper {
    private final ItemMapper itemMapper;

    public ItemRequestDto toDto(ItemRequest request, List<Item> items) {
        return ItemRequestDto.builder()
                .id(request.getId())
                .description(request.getDescription())
                .requesterId(request.getRequesterId())
                .created(request.getCreated())
                .build();
    }

    public ItemRequest toEntity(ItemRequestDto requestDto) {
        return ItemRequest.builder()
                .description(requestDto.getDescription())
                .requesterId(requestDto.getRequesterId())
                .created(requestDto.getCreated())
                .build();
    }
}
