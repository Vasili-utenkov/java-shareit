package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@Slf4j
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    ItemRequestDto createItemRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @RequestBody String description
    ) {
        log.warn("Добавление нового запроса. @PostMapping (/requests) ");
        log.warn("ItemRequestDto createItemRequest(@RequestHeader(X-Sharer-User-Id) Long {}, @RequestBody String {}",
                userId, description);
        ItemRequestDto dto = itemRequestService.createItemRequest(userId, description);
        log.warn("ИТОГ: Создали запрос " + dto);
        return dto;
    }

    @GetMapping
    List<ItemRequestDto> getItemRequestListByUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.warn("список запросов пользователя и ответы на них. @GetMapping (/requests) ");
        log.warn("List<ItemRequestDto> getItemRequestList (@RequestHeader(X-Sharer-User-Id) Long {}", userId);


        List<ItemRequestDto> dtoList = itemRequestService.findItemRequestsByUser(userId);

        return dtoList;

    }


}
