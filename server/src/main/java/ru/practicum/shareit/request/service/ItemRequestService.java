package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestShortDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestService {

    /**
     * добавить новый запрос вещи
     *
     * @param userId ID пользователя
     * @param dto Содержание запроса
     * @return ItemRequestDto запрос
     */
    ItemRequestDto createItemRequest(Long userId, ItemRequestShortDto dto);

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


    /**
     * данные о запросе
     *
     * @param requestId ID запроса
     * @return ItemRequestDto
     */
    ItemRequestDto getItemRequest(Long requestId);

    /**
     * Проверка переданого в поиск кода пользователя
     *
     * @param requestId ID запроса
     * @return ItemRequest
     */
    ItemRequest validateItemRequestExists(Long requestId);


}
