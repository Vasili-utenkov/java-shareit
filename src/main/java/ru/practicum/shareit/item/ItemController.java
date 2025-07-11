package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentCreateDto;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemDto createItem(
            @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @RequestBody @Valid ItemDto item) {
        log.warn("Добавление новой вещи. @PostMapping (/items) ");
        log.warn("createItem(@RequestHeader(X-Sharer-User-Id) Long {}, @RequestBody @Valid ItemDto {})",
                ownerId, item);
        ItemDto dto = itemService.createItem(ownerId, item);
        log.warn("ИТОГ: Создали предмет " + dto);
        return dto;
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItemByItemID(
            @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @PathVariable Long itemId,
            @RequestBody ItemDto item
    ) {
        log.warn("Редактирование вещи. @PatchMapping (/items/{itemId}) ");
        log.warn("updateItemByItemID(@RequestHeader(X-Sharer-User-Id) Long {}, @PathVariable Long {}, @RequestBody ItemDto {})",
                ownerId, itemId, item);
        ItemDto dto = itemService.updateItemByItemID(ownerId, itemId, item);
        log.warn("ИТОГ: Изменили предмет на " + dto);
        return dto;
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(
            @PathVariable Long itemId,
            @RequestHeader("X-Sharer-User-Id") Long ownerId
    ) {
        log.warn("Удаление вещи. @DeleteMapping (/items/{itemId}) ");
        log.warn("deleteItem(@PathVariable Long {}, @RequestHeader(X-Sharer-User-Id) Long {})",
                itemId, ownerId);
        itemService.deleteItem(itemId, ownerId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemByItemID(
            @PathVariable Long itemId
    ) {
        log.warn("Просмотр информации о конкретной вещи по её идентификатору. @GetMapping (/items/{itemId}) ");
        log.warn("getItemByItemID(@PathVariable Long {})",
                itemId);
        ItemDto dto = itemService.getItemByItemID(itemId);
        log.warn("ИТОГ: Просмотр информации о конкретной вещи " + dto);
        return dto;
    }

    @GetMapping
    public List<ItemDto> getItemsListByOwner(
            @RequestHeader("X-Sharer-User-Id") Long ownerId
    ) {
        log.warn("Просмотр владельцем списка всех его вещей с указанием названия и описания для каждой из них. @GetMapping (/items)");
        log.warn("getItemsListByOwner(@RequestHeader(X-Sharer-User-Id) Long {}",
                ownerId);
        List<ItemDto> list = itemService.getItemsListByOwner(ownerId);
        return list;
    }

    @GetMapping("/search")
    public List<ItemDto> searchItemsByText(
            @RequestParam String text
    ) {
        log.warn("Поиск вещи потенциальным арендатором по имени или описанию. @GetMapping (/items/search) ");
        log.warn("searchItemsByText(@RequestParam String {})",
                text);
        List<ItemDto> list = itemService.getItemsListByText(text);
        return list;
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(
            @Valid @RequestBody CommentCreateDto commentCreateDto,
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long itemId
    ) {
        log.warn("Добавить коментарий по вещи. @PostMapping(/items/{itemId}/comment) ");
        log.warn("createComment(" +
                "@Valid @RequestBody CommentCreateDto {}," +
                "@RequestHeader(X-Sharer-User-Id) Long {}," +
                "@PathVariable Long {})",
                commentCreateDto, userId, itemId);
        CommentDto dto = itemService.addCommentToItem(commentCreateDto, userId, itemId);
        log.warn("ИТОГ: Создали комментарий: " + dto);
        return dto;
    }

}
