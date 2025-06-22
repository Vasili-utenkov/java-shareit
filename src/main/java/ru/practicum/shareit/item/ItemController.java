package ru.practicum.shareit.item;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    // Добавление новой вещи
    @PostMapping
    public ItemDto createItem(
            @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @RequestBody @Valid ItemDto item) {
        return itemService.createItem(ownerId, item);
    }

    // Редактирование вещи
    @PatchMapping("/{itemId}")
    public ItemDto updateItemByItemID(
            @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @PathVariable Long itemId,
            @RequestBody ItemDto item
    ) {
        return itemService.updateItemByItemID(ownerId, itemId, item);
    }

    // Удаление вещи
    @DeleteMapping("/{itemId}")
    public void deleteItem(
            @PathVariable Long itemId,
            @RequestHeader("X-Sharer-User-Id") Long ownerId
    ) {
        itemService.deleteItem(itemId,ownerId);
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
    public List<ItemDto> searchItemByText(
            @RequestParam String text
    ) {
        return itemService.searchItemByText(text);
    }

}
