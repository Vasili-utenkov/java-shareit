package ru.practicum.shareit.item.service;


import ru.practicum.shareit.item.dto.ItemDto;
import java.util.List;

public interface ItemService {

    ItemDto createItem(ItemDto itemDto);
    ItemDto updateItem(Long ownerId, Long itemId, ItemDto itemDto);
    ItemDto getItemById(Long itemId);
    List<ItemDto> getAllItemsByOwner(Long ownerId);
    List<ItemDto> searchAvailableItems(String text);

}