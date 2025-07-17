package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.BookingClient;
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
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> createItem(
            @RequestHeader("X-Sharer-User-Id") long ownerId,
            @RequestBody @Valid ItemDto item) {
        log.warn("Добавление новой вещи. @PostMapping (/items) ");
        log.warn("createItem(@RequestHeader(X-Sharer-User-Id) Long {}, @RequestBody @Valid ItemDto {})",
                ownerId, item);
        ResponseEntity<Object> response = itemClient.createItem(ownerId, item);
        log.warn("ИТОГ: Создали предмет " + response);
        return response;
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItemByItemID(
            @RequestHeader("X-Sharer-User-Id") long ownerId,
            @PathVariable long itemId,
            @RequestBody ItemDto item
    ) {
        log.warn("Редактирование вещи. @PatchMapping (/items/{itemId}) ");
        log.warn("updateItemByItemID(@RequestHeader(X-Sharer-User-Id) Long {}, @PathVariable Long {}, @RequestBody ItemDto {})",
                ownerId, itemId, item);

        ResponseEntity<Object> response = itemClient.updateItemByItemID(ownerId, itemId, item);
        log.warn("ИТОГ: Изменили предмет на " + response);
        return response;
    }


    @DeleteMapping("/{itemId}")
    public ResponseEntity<Object> deleteItem(
            @PathVariable long itemId,
            @RequestHeader("X-Sharer-User-Id") long ownerId
    ) {
        log.warn("Удаление вещи. @DeleteMapping (/items/{itemId}) ");
        log.warn("deleteItem(@PathVariable Long {}, @RequestHeader(X-Sharer-User-Id) Long {})",
                itemId, ownerId);
        ResponseEntity<Object> response = itemClient.deleteItem(itemId, ownerId);
        return response;
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemByItemID(
            @PathVariable long itemId
    ) {
        log.warn("Просмотр информации о конкретной вещи по её идентификатору. @GetMapping (/items/{itemId}) ");
        log.warn("getItemByItemID(@PathVariable Long {})",
                itemId);
        ResponseEntity<Object> response = itemClient.getItemByItemID(itemId);
        log.warn("ИТОГ: Просмотр информации о конкретной вещи " + response);
        return response;
    }

    @GetMapping
    public ResponseEntity<Object> getItemsListByOwner(
            @RequestHeader("X-Sharer-User-Id") long ownerId
    ) {
        log.warn("Просмотр владельцем списка всех его вещей с указанием названия и описания для каждой из них. @GetMapping (/items)");
        log.warn("getItemsListByOwner(@RequestHeader(X-Sharer-User-Id) Long {}",
                ownerId);
        ResponseEntity<Object> response = itemClient.getItemsListByOwner(ownerId);
        return response;
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItemsByText(
            @RequestParam String text
    ) {
        log.warn("Поиск вещи потенциальным арендатором по имени или описанию. @GetMapping (/items/search) ");
        log.warn("searchItemsByText(@RequestParam String {})",
                text);
        ResponseEntity<Object> response = itemClient.getItemsByText(text);
        return response;
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object>  createComment(
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
        ResponseEntity<Object> response = itemClient.addCommentToItem(commentCreateDto, userId, itemId);
        log.warn("ИТОГ: Создали комментарий: " + response);
        return response;
    }

}
