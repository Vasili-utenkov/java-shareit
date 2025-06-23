package ru.practicum.shareit.item.storage;


import ru.practicum.shareit.item.model.Item;
import java.util.List;


public interface ItemStorage {

    /**
     * Создание предмета
     *
     * @param item описание предмета
     * @return Item
     */
    Item create(Item item);

    /**
     * изменение данных предмета
     *
     * @param itemId код предмета
     * @param item новое описание предмета
     * @return Item
     */
    Item update(Long itemId, Item item);

    /**
     * удаление предмета
     *
     * @param itemId код предмета
     */
    void delete(Long itemId);

    /**
     * получение данных о предмете по коду предмета
     *
     * @param itemId  код предмета
     * @return Item
     */
    Item get(Long itemId);

    /**
     * Получение списка предметов у вадельца с заданным кодом
     *
     * @param ownerId код владельца предмета
     * @return List<Item>
     */
    List<Item> getItemsListByOwner(Long ownerId);

    /**
     * получение списка предмета, содержащих текст в наименовании или описании
     *
     * @param text Текст для поиска
     * @return List<Item>
     */
    List<Item> searchAvailableByText(String text);

    /**
     * Удаление предметов владельца
     *
     * @param ownerId Код владельца
     */
    void deleteItemsByOwnerId(Long ownerId);

    /**
     * Проверка переданого в поиск кода предмета
     *
     * @param itemId код предмета
     */
    void validateItemId(Long itemId);


}
