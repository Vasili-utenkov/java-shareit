package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestShortDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserService userService;

    /**
     * добавить новый запрос вещи
     *
     * @param userId      ID пользователя
     * @param itemRequestShortDto Содержание запроса
     * @return ItemRequestDto
     */
    @Override
    public ItemRequestDto createItemRequest(Long userId, ItemRequestShortDto itemRequestShortDto) {
        log.warn("createItemRequest(Long {}, ItemRequestShortDto {})", userId, itemRequestShortDto);
        User user = userService.validateUserExists(userId);
        ItemRequest itemRequest = ItemRequest.builder()
                .user(user)
                .description(itemRequestShortDto.getDescription())
                .created(LocalDateTime.now())
                .build();
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

        return list.stream().map(ItemRequestMapper::toDto).collect(Collectors.toList());
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

        return list.stream().map(ItemRequestMapper::toDto).collect(Collectors.toList());
    }
}
