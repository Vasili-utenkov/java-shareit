package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestShortDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping(path = "/requests2")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto createItemRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @RequestBody ItemRequestShortDto itemRequestShortDto
    ) {
        log.warn("SERVER:: Добавление нового запроса. @PostMapping (/requests) ");
        log.warn("ItemRequestDto createItemRequest(@RequestHeader(X-Sharer-User-Id) Long {}, @RequestBody ItemRequestShortDto {}",
                userId, itemRequestShortDto);
        ItemRequestDto dto = itemRequestService.createItemRequest(userId, itemRequestShortDto);
        log.warn("SERVER:: ИТОГ: Создали запрос " + dto);
        return dto;
    }

    @GetMapping
    public List<ItemRequestDto> getItemRequestListByUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.warn("SERVER:: список запросов пользователя и ответы на них. @GetMapping (/requests) ");
        log.warn("List<ItemRequestDto> getItemRequestList (@RequestHeader(X-Sharer-User-Id) Long {}", userId);

        List<ItemRequestDto> dtoList = itemRequestService.findItemRequestsByUser(userId);
        return dtoList;
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getItemRequestListAllUser(
            @RequestHeader("X-Sharer-User-Id") long userId
    ) {
        log.warn("SERVER:: список запросов всех пользователя и ответы на них. @GetMapping (/requests/all) ");
        log.warn("List<ItemRequestDto> getItemRequestList (@RequestHeader(X-Sharer-User-Id) Long {}", userId);

        List<ItemRequestDto> dtoList = itemRequestService.findItemRequestsAllUser(userId);
        return dtoList;
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getItemRequestById(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @PathVariable long requestId
    ) {
        log.warn("SERVER:: данные о запросе вместе с данными об ответах на него. @GetMapping (/requests/{requestId}) ");
        log.warn("List<ItemRequestDto> getItemRequestList (@RequestHeader(X-Sharer-User-Id) Long {}, @PathVariable Long {})",
                userId, requestId);

        ItemRequestDto dto = itemRequestService.getItemRequest(requestId);
        return dto;
    }

}
