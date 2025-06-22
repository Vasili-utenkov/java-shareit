package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class ItemIMStorage implements ItemStorage{

    private final AtomicLong id = new AtomicLong(1);
    private final Map<Long, Item> items = new HashMap<>();


    /**
     * Создание предмета
     *
     * @param item описание предмета
     * @return Item
     */
    @Override
    public Item create(Item item) {
        return null;
    }

    /**
     * изменение данных предмета
     *
     * @param itemId код предмета
     * @param item   описание предмета
     * @return Item
     */
    @Override
    public Item update(Long itemId, Item item) {
        return null;
    }

    /**
     * удаление предмета
     *
     * @param itemId код предмета
     */
    @Override
    public void delete(Long itemId) {
    }

    /**
     * получение данных о предмете по коду предмета
     *
     * @param itemId код предмета
     * @return Optional<Item>
     */
    @Override
    public Optional<Item> get(Long itemId) {
        return Optional.empty();
    }

    /**
     * Получение списка предметов у вадельца с заданным кодом
     *
     * @param ownerId код владельца предмета
     * @return List<Item>
     */
    @Override
    public List<Item> findAllByOwnerId(Long ownerId) {
        return null;
    }

    /**
     * получение списка предмета, содержащих текст в наименовании или описании
     *
     * @param text Текст для поиска
     * @return List<Item>
     */
    @Override
    public List<Item> searchAvailableByText(String text) {
        return null;
    }

    /**
     * Удаление предметов у пользователя
     *
     * @param userId Код пользователя
     */
    @Override
    public void deleteItemsByUserId(Long userId) {

    }
}
