package ru.practicum.shareit.item.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemIMStorage;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.storage.UserIMStorage;

import java.util.List;

@Service("itemIMService")
@ConditionalOnProperty(name = "share.storage.type", havingValue = "memory")
@RequiredArgsConstructor
@Slf4j
public class ItemIMService implements ItemService {

    private final ItemIMStorage itemStorage;
    private final UserService userService;



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
        item.setOwnerId(ownerId);
        userService.validateUserId(item.getOwnerId());

        return ItemMapper.toDto(itemStorage.create(item));
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
        validateItemId(itemId);
        return ItemMapper.toDto(itemStorage.get(itemId));
    }

    /**
     * Удаление предмета по коду предмета
     *
     * @param itemId код предмета
     * @param ownerId код владельца предмета
     */
    @Override
    public void deleteItem(Long itemId, Long ownerId) {
        log.warn("deleteItem(Long {}, Long {})", itemId, ownerId);
        checkEqualOwners(itemId, ownerId);
        validateItemId(itemId);
        itemStorage.delete(itemId);
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
        validateItemId(itemId);
        checkEqualOwners(itemId, ownerId);
        Item item = ItemMapper.toEntity(itemDto);
        item.setOwnerId(ownerId);
        item.setId(itemId);
        return ItemMapper.toDto(itemStorage.update(itemId, item));
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
        return ItemMapper.toDto(itemStorage.getItemsListByOwner(ownerId));
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
        List<ItemDto> list = ItemMapper.toDto(itemStorage.searchAvailableByText(text));
        return list;
    }

    /**
     * @param itemId код предмета
     * @param ownerId код владельца предмета
     * @throws NotFoundException "Неверный код владельца предмета " + ownerId"
     */
    private void checkEqualOwners(Long itemId, Long ownerId) {
        if (!ownerId.equals(getItemByItemID(itemId).getOwnerId())) {
            throw new NotFoundException("Неверный код владельца предмета " + ownerId);
        }
    }

    /**
     * Проверка переданого в поиск кода предмета
     *
     * @param itemId код предмета
     * @throws IllegalArgumentException "ID предмета не может быть null"
     * @throws NotFoundException "Предмет с ID " + itemId + " не найден"
     */
    private void validateItemId(Long itemId) {
        // Проверка на null ID
        if (itemId == null) {
            throw new IllegalArgumentException("ID предмета не может быть null");
        }

        // Проверка существования пользователя
        if (itemStorage.get(itemId) == null) {
            throw new NotFoundException("Предмет с ID " + itemId + " не найден");
        }
    }


}