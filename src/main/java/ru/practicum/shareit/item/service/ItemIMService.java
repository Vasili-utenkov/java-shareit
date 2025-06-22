package ru.practicum.shareit.item.service;


import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

@Service("itemIMService")
@ConditionalOnProperty(name = "share.storage.type", havingValue = "memory")
public class ItemIMService implements ItemService {

    @Override
    public ItemDto createItem(ItemDto itemDto) {
        ItemDto dto = new ItemDto();
        return dto;
    }

    @Override
    public ItemDto updateItem(Long ownerId, Long itemId, ItemDto itemDto) {
        return null;
    }

    @Override
    public ItemDto getItemById(Long itemId) {
        return null;
    }

    @Override
    public List<ItemDto> getAllItemsByOwner(Long ownerId) {
        return null;
    }

    @Override
    public List<ItemDto> searchAvailableItems(String text) {
        return null;
    }
}