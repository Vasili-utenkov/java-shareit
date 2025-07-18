package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ItemIMStorage implements ItemStorage {

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
        item.setId(id.getAndIncrement());
        items.put(item.getId(), item);
        return item;
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
        Item existingItem = items.get(itemId);

        if (item.getName() != null) {
            existingItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            existingItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            existingItem.setAvailable(item.getAvailable());
        }
        if (item.getOwner() != null) {
            existingItem.setOwner(item.getOwner());
        }
        if (item.getRequest() != null) {
            existingItem.setRequest(item.getRequest());
        }

        items.put(itemId, existingItem);
        return item;
    }

    /**
     * удаление предмета
     *
     * @param itemId код предмета
     */
    @Override
    public void delete(Long itemId) {
        items.remove(itemId);
    }

    /**
     * получение данных о предмете по коду предмета
     *
     * @param itemId код предмета
     * @return Item
     */
    @Override
    public Item get(Long itemId) {
        return items.get(itemId);
    }

    /**
     * Получение списка предметов у вадельца с заданным кодом
     *
     * @param ownerId код владельца предмета
     * @return List<Item>
     */
    @Override
    public List<Item> getItemsListByOwner(Long ownerId) {
        return items.values().stream()
                .filter(item -> Objects.equals(item.getOwner().getId(), ownerId))
                .toList();
    }

    /**
     * получение списка предмета, содержащих текст в наименовании или описании
     *
     * @param text Текст для поиска
     * @return List<Item>
     */
    @Override
    public List<Item> searchAvailableByText(String text) {

        final String searchText = text.trim().toLowerCase();

        List<Item> list = new ArrayList<>();

        list = items.values().stream()
                .filter(Item::getAvailable)
                .filter(item -> (item.getName().toLowerCase().contains(searchText))
                || item.getDescription().toLowerCase().contains(searchText))
                .toList();

        return list;
    }

}

