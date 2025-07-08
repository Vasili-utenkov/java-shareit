package ru.practicum.shareit.item.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserServiceImpl userService;

    /**
     * Создание предмета
     *
     * @param ownerId код владельца предмета
     * @param itemDto данные по предмету
     * @return ItemDto
     */
    @Override
    public ItemDto createItem(Long ownerId, ItemDto itemDto) {
        log.warn("createItem(Long {}, ItemDto {})", ownerId, itemDto);
        Item item = ItemMapper.toEntity(itemDto);
        // Проверка
        item.setOwner(userService.validateUserId(ownerId));
        return ItemMapper.toDto(itemRepository.save(item));
    }

    /**
     * получение предмета по коду предмета
     *
     * @param itemId код предмета
     * @return ItemDto
     */
    @Override
    public ItemDto getItemByItemID(Long itemId) {
        log.warn("getItemByItemID(Long {})", itemId);
        return ItemMapper.toDto(validateItemExists(itemId));
    }

    /**
     * Удаление предмета по коду предмета
     *
     * @param itemId  код предмета
     * @param ownerId код владельца предмета
     */
    @Override
    public void deleteItem(Long itemId, Long ownerId) {
        log.warn("deleteItem(Long {}, Long {})", itemId, ownerId);
        checkEqualOwners(itemId, ownerId);
        itemRepository.delete(validateItemExists(itemId));
    }

    /**
     * Изменения данных по предмету
     *
     * @param ownerId код владельца предмета
     * @param itemId  код предмета
     * @param itemDto новые данные по предмету
     * @return ItemDto
     */
    @Override
    public ItemDto updateItemByItemID(Long ownerId, Long itemId, ItemDto itemDto) {
        log.warn("updateItemByItemID(Long {}, Long {}, ItemDto {})", ownerId, itemId, itemDto);
        Item exitingItem = validateItemExists(itemId);
        checkEqualOwners(itemId, ownerId);
        exitingItem.setOwner(userService.validateUserId(ownerId));
        exitingItem.setId(itemId);
        ItemMapper.updateItemFromDto(itemDto, exitingItem);
        return ItemMapper.toDto(exitingItem);
    }

    /**
     * Получение списка предметов по коду владельца
     *
     * @param ownerId код владельца предмета
     * @return List<ItemDto>
     */
    @Override
    public List<ItemDto> getItemsListByOwner(Long ownerId) {
        log.warn("getItemsListByOwner(Long {})", ownerId);
        userService.validateUserId(ownerId);
        List<ItemDto> list = ItemMapper.toDto(itemRepository.findAllByOwnerId(ownerId));
        return list;
    }

    /**
     * Получение списка предметов по тексту из названия или описания
     *
     * @param text текст из названия или описания предмета
     * @return List<ItemDto>
     */
    @Override
    public List<ItemDto> getItemsListByText(String text) {
        log.warn("getItemsListByText(String {})", text);
        if (text == null || text.isBlank()) {
            return List.of();
        }
        List<ItemDto> list = ItemMapper.toDto(itemRepository.findAvailableByText(text.toLowerCase(Locale.ROOT)));
        return list;
    }

    /**
     * @param itemId  код предмета
     * @param ownerId код владельца предмета
     * @throws NotFoundException "Неверный код владельца предмета " + ownerId"
     */
    private void checkEqualOwners(Long itemId, Long ownerId) {
        if (!ownerId.equals(getItemByItemID(itemId).getOwner().getId())) {
            throw new NotFoundException("Неверный код владельца предмета " + ownerId);
        }
    }

    /**
     * Проверка переданого в поиск кода предмета
     *
     * @param itemId код предмета
     * @throws IllegalArgumentException "ID предмета не может быть null"
     * @throws NotFoundException        "Предмет с ID " + itemId + " не найден"
     */
    @Override
    public Item validateItemExists(Long itemId) {
        // Проверка на null ID
        if (itemId == null) {
            throw new IllegalArgumentException("ID предмета не может быть null");
        }

        // Проверка существования предмета
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Предмет с ID " + itemId + " не найден"));
    }
}