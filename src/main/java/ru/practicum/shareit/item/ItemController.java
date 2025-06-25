package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.service.ItemServiceFactory;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemServiceFactory factory;
    private final ItemService itemService;

    @Autowired
    public ItemController(ItemServiceFactory factory) {
        this.factory = factory;
        this.itemService = factory.getItemService();
    }

    // Добавление новой вещи
    @PostMapping
    public ItemDto createItem(
            @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @RequestBody @Valid ItemDto item) {
        ItemDto dto = itemService.createItem(ownerId, item);
        return dto;
    }

    // Редактирование вещи
    @PatchMapping("/{itemId}")
    public ItemDto updateItemByItemID(
            @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @PathVariable Long itemId,
            @RequestBody ItemDto item
    ) {
        ItemDto dto = itemService.updateItemByItemID(ownerId, itemId, item);
        return dto;
    }

    // Удаление вещи
    @DeleteMapping("/{itemId}")
    public void deleteItem(
            @PathVariable Long itemId,
            @RequestHeader("X-Sharer-User-Id") Long ownerId
    ) {
        itemService.deleteItem(itemId, ownerId);
    }

    // Просмотр информации о конкретной вещи по её идентификатору
    @GetMapping("{itemId}")
    public ItemDto getItemByItemID(
            @PathVariable Long itemId
    ) {
        return itemService.getItemByItemID(itemId);
    }

    // Просмотр владельцем списка всех его вещей с указанием названия и описания для каждой из них.
    @GetMapping
    public List<ItemDto> getItemsListByOwner(
            @RequestHeader("X-Sharer-User-Id") Long ownerId
    ) {
        return itemService.getItemsListByOwner(ownerId);
    }

    // Поиск вещи потенциальным арендатором по имени или описанию.
    @GetMapping("/search")
    public List<ItemDto> searchItemsByText(
            @RequestParam String text
    ) {
        List<ItemDto> list = itemService.getItemsListByText(text);
        return list;
    }

}
