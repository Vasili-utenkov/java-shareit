package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    /**
     * Создание предмета
     *
     * @param ownerId Код кладельца
     * @param itemDto данные по предмету
     * @return ItemDto
     */
    ItemDto createItem(Long ownerId, ItemDto itemDto);

    /**
     * получение предмета по коду предмета
     *
     * @param itemId код предмета
     * @return ItemDto
     */
    ItemDto getItemByItemID(Long itemId);

    /**
     * Удаление предмета по коду предмета
     *
     * @param itemId код предмета
     * @param ownerId код владелца предмета
     */
    void deleteItem(Long itemId, Long ownerId);


    /**
     * Изменения данных по предмету
     *
     * @param ownerId код владелца предмета
     * @param itemId код предмета
     * @param itemDto новые данные по предмету
     * @return ItemDto
     */
    ItemDto updateItemByItemID(Long ownerId, Long itemId, ItemDto itemDto);


    /**
     * Получение списка предметов по коду владельца
     *
     * @param ownerId код владелца предмета
     * @return List<ItemDto>
     */
    List<ItemDto> getItemsListByOwner(Long ownerId);

    /**
     * Получение списка предметов по тексту из названия или описания
     *
     * @param text текст из названия или описания предмета
     * @return List<ItemDto>
     */
    List<ItemDto> getItemsListByText(String text);


    /**
     * Проверка переданого в поиск кода предмета
     *
     * @param itemId код предмета
     * @return Item
     */
    Item validateItemExists(Long itemId);

}