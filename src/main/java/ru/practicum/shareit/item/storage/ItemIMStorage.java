package ru.practicum.shareit.item.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

@Repository
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
        validateItemId(itemId);
        items.put(itemId, item);
        return item;
    }

    /**
     * удаление предмета
     *
     * @param itemId код предмета
     */
    @Override
    public void delete(Long itemId) {
        validateItemId(itemId);
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
        validateItemId(itemId);
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
                .filter(item -> Objects.equals(item.getOwnerId(), ownerId))
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


        return null;
    }

    /**
     * Удаление предметов владельца
     *
     * @param ownerId Код владельца
     */
    @Override
    public void deleteItemsByOwnerId(Long ownerId) {

    }

    /**
     * Проверка переданого в поиск кода предмета
     *
     * @param itemId код предмета
     */
    @Override
    public void validateItemId(Long itemId) {
        // Проверка на null ID
        if (itemId == null) {
            throw new IllegalArgumentException("ID пользователя не может быть null");
        }

        // Проверка существования пользователя
        if (!items.containsKey(itemId)) {
            throw new NotFoundException("Предмет с ID " + itemId + " не найден");
        }
    }

}

