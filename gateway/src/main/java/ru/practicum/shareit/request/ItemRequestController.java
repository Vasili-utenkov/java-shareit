package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.dto.ItemRequestShortDto;


@RestController
@Slf4j
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> createItemRequest(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @Valid @RequestBody ItemRequestShortDto itemRequestShortDto
    ) {
        log.warn("GATEWAY:: Добавление нового запроса. @PostMapping (/requests) ");
        log.warn("ItemRequestDto createItemRequest(@RequestHeader(X-Sharer-User-Id) Long {}, @RequestBody ItemRequestShortDto {}",
                userId, itemRequestShortDto);
        ResponseEntity<Object> response = itemRequestClient.createItemRequest(userId, itemRequestShortDto);
        log.warn("GATEWAY:: ИТОГ: Создали запрос " + response);
        return response;
    }

    @GetMapping
    public ResponseEntity<Object> getItemRequestListByUser(
            @RequestHeader("X-Sharer-User-Id") long userId
    ) {
        log.warn("GATEWAY:: список запросов пользователя и ответы на них. @GetMapping (/requests) ");
        log.warn("List<ItemRequestDto> getItemRequestList (@RequestHeader(X-Sharer-User-Id) Long {}", userId);

        ResponseEntity<Object> response = itemRequestClient.getItemRequestListByUser(userId);
        return response;
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getItemRequestListAllUser(
            @RequestHeader("X-Sharer-User-Id") long userId
    ) {
        log.warn("GATEWAY:: список запросов всех пользователя и ответы на них. @GetMapping (/requests/all) ");
        log.warn("List<ItemRequestDto> getItemRequestList (@RequestHeader(X-Sharer-User-Id) Long {}", userId);

        ResponseEntity<Object> response = itemRequestClient.getItemRequestListAllUser(userId);
        return response;
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequestById(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @PathVariable long requestId
    ) {
        log.warn("GATEWAY:: данные о запросе вместе с данными об ответах на него. @GetMapping (/requests/{requestId}) ");
        log.warn("List<ItemRequestDto> getItemRequestList (@RequestHeader(X-Sharer-User-Id) Long {}, @PathVariable Long {})",
                userId, requestId);

        ResponseEntity<Object> response = itemRequestClient.getItemRequestById(userId, requestId);
        return response;
    }


}
