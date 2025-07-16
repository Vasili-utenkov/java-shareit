package ru.practicum.shareit.item.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.ArrayList;
import java.util.List;

@Component
public class ItemMapper {

    public static Item toEntity(ItemDto itemDto) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .owner(itemDto.getOwner() != null ?
                        UserMapper.toEntity(itemDto.getOwner()) : null)
                .request(itemDto.getRequest() != null ?
                        ItemRequestMapper.toEntity(itemDto.getRequest()) : null)
                .build();
    }

    public static ItemDto toDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(item.getOwner() != null ?
                        UserMapper.toDto(item.getOwner()) : null)
                .request(item.getRequest() != null ?
                        ItemRequestMapper.toDto(item.getRequest()) : null)
                .comments(new ArrayList<>()) // или заполнить из сущности, если нужно
                .build();
    }


    public static List<ItemDto> toDto(List<Item> items) {
        return items.stream().map(ItemMapper::toDto).toList();
    }


    public static void updateItemFromDto(ItemDto itemDto, Item item) {
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
    }
}
