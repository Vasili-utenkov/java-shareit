package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.UserNotExistsException;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestShortDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserService userService;
    private final ItemService itemService;

    /**
     * добавить новый запрос вещи
     *
     * @param userId              ID пользователя
     * @param itemRequestShortDto Содержание запроса
     * @return ItemRequestDto
     */
    @Override
    public ItemRequestDto createItemRequest(Long userId, ItemRequestShortDto itemRequestShortDto) {
        log.warn("createItemRequest(Long {}, ItemRequestShortDto {})", userId, itemRequestShortDto);
        User user = userService.validateUserExists(userId);

        ItemRequest itemRequest = ItemRequestMapper.toEntity(itemRequestShortDto);
        itemRequest.setUser(user);
        return ItemRequestMapper.toDto(itemRequestRepository.save(itemRequest));
    }

    /**
     * список своих запросов вместе с данными об ответах на них
     *
     * @param userId ID пользователя
     * @return List<ItemRequestDto> список запросов
     */
    @Override
    public List<ItemRequestDto> findItemRequestsByUser(Long userId) {
        log.warn("findItemRequestsByUser(Long {})", userId);

        User user = userService.validateUserExists(userId);
        List<ItemRequest> list = itemRequestRepository.findItemRequestsByUser(userId);

        // вместе с данными об ответах на него
        return getRequestWithItemsList(list);
    }

    /**
     * список НЕ своих запросов вместе с данными об ответах на них
     *
     * @param userId ID пользователя
     * @return List<ItemRequestDto> список запросов
     */
    @Override
    public List<ItemRequestDto> findItemRequestsAllUser(Long userId) {
        log.warn("findItemRequestsAllUser(Long {})", userId);

        User user = userService.validateUserExists(userId);
        List<ItemRequest> list = itemRequestRepository.findItemRequestsAllUser(userId);

        // вместе с данными об ответах на него
        return getRequestWithItemsList(list);
    }


    /**
     * данные о запросе
     *
     * @param requestId ID запроса
     * @return ItemRequestDto
     */
    @Override
    public ItemRequestDto getItemRequest(Long requestId) {

        ItemRequestDto dto = ItemRequestMapper.toDto(validateItemRequestExists(requestId));

        // вместе с данными об ответах на него
        dto.setItems(ItemMapper.toDto(itemService.getItemsListByRequest(requestId)));

        return dto;

    }


    /**
     * Проверка переданного в поиск кода запроса
     *
     * @param requestId ID запроса
     */
    @Override
    public ItemRequest validateItemRequestExists(Long requestId) {
        log.warn("validateUserId(Long {})", requestId);
        // Проверка на null ID
        if (requestId == null) {
            throw new IllegalArgumentException("ID запроса не может быть null");
        }

        // Проверка существования пользователя
        return itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new UserNotExistsException("Запрос с ID " + requestId + " не найден"));
    }

    private List<ItemRequestDto> getRequestWithItemsList(List<ItemRequest> requestList) {
        log.warn("getRequestWithItemsList(List<ItemRequest> {})", requestList);

        // Список запросов пуст
        if (requestList.isEmpty()) {
            return Collections.emptyList();
        }

        // Список кодов запросов
        List<Long> requestIds = requestList.stream()
                .map(ItemRequest::getId)
                .toList();

        // Предметы по списку запросов
        Map<Long, List<Item>> itemsByRequest = itemService.getItemsByRequestIds(requestIds);

        return requestList.stream()
                .map(itemRequest -> {
                    ItemRequestDto dto = ItemRequestMapper.toDto(itemRequest);
                    List<Item> items = itemsByRequest.getOrDefault(itemRequest.getId(), Collections.emptyList());
                    dto.setItems(ItemMapper.toDto(items));
                    return dto;
                })
                .toList();
    }

}
