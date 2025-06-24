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
import ru.practicum.shareit.user.storage.UserIMStorage;

import java.util.List;

@Service("itemIMService")
@ConditionalOnProperty(name = "share.storage.type", havingValue = "memory")
@RequiredArgsConstructor
@Slf4j
public class ItemIMService implements ItemService {

    private final ItemIMStorage itemStorage;
    private final UserIMStorage userStorage;

    /**
     * Создание предмета
     *
     * @param ownerId Код кладельца
     * @param itemDto данные по предмету
     * @return ItemDto
     */
    @Override
    public ItemDto createItem(Long ownerId, ItemDto itemDto) {
        Item item = ItemMapper.toEntity(itemDto);
        // Проверка
        item.setOwnerId(ownerId);
        userStorage.validateUserId(item.getOwnerId());
        itemStorage.validateOwnerId(ownerId);

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
        return ItemMapper.toDto(itemStorage.get(itemId));
    }

    /**
     * Удаление предмета по коду предмета
     *
     * @param itemId код предмета
     * @param ownerId код владелца предмета
     * @throws IllegalArgumentException "Неверный код владелца предмета " + ownerId
     */
    @Override
    public void deleteItem(Long itemId, Long ownerId) {
        if (!ownerId.equals(getItemByItemID(itemId).getOwnerId())) {
            throw new IllegalArgumentException("Неверный код владелца предмета " + ownerId);
        }
        itemStorage.delete(itemId);
    }

    /**
     * Изменения данных по предмету
     *
     * @param ownerId код владелца предмета
     * @param itemId  код предмета
     * @param itemDto новые данные по предмету
     * @return ItemDto
     */
    @Override
    public ItemDto updateItemByItemID(Long ownerId, Long itemId, ItemDto itemDto) {
        if (!ownerId.equals(getItemByItemID(itemId).getOwnerId())) {
            throw new NotFoundException("Неверный код владелца предмета " + ownerId);
        }
        Item item = ItemMapper.toEntity(itemDto);
        item.setOwnerId(ownerId);
        item.setId(itemId);
        return ItemMapper.toDto(itemStorage.update(itemId, item));
    }

    /**
     * Получение списка предметов по коду владельца
     *
     * @param ownerId код владелца предмета
     * @return List<ItemDto>
     */
    @Override
    public List<ItemDto> getItemsListByOwner(Long ownerId) {
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

        log.warn("ПРОВЕРКА: ");
        log.warn("ПРОВЕРКА: " + "getItemsListByText(String {})", text);


        if (text == null || text.isBlank()) {
            return List.of();
        }

        List<ItemDto> list = ItemMapper.toDto(itemStorage.searchAvailableByText(text));
        log.warn("ПРОВЕРКА: " + "List<ItemDto> = " + list);
        return list;
    }
}