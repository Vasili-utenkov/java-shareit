package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {

    /**
     * добавить новый запрос вещи
     *
     * @param userId ID пользователя
     * @param description Содержание запроса
     * @return ItemRequestDto запрос
     */
    ItemRequestDto createItemRequest(Long userId, String description);


    /**
     * список своих запросов вместе с данными об ответах на них
     *
     * @param userId ID пользователя
     * @return List<ItemRequestDto> список запросов
     */
    List<ItemRequestDto> findItemRequestsByUser(Long userId);

    /**
     * список НЕ своих запросов вместе с данными об ответах на них
     *
     * @param userId ID пользователя
     * @return List<ItemRequestDto> список запросов
     */
    List<ItemRequestDto> findItemRequestsAllUser(Long userId);
}
