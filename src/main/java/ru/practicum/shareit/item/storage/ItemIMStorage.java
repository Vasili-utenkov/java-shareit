package ru.practicum.shareit.item.storage;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
@Slf4j
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
    public Item create(@Valid Item item) {
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
    public Item update(Long itemId, @Valid Item item) {
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

        final String searchText = text.trim().toLowerCase();

        List<Item> list = new ArrayList<>();

        if (searchText == null || searchText.isBlank()) {
            return list;
        }

        list = items.values().stream()
                .filter(item -> {
                    // Явная проверка available != null
                    Boolean available = item.getAvailable();
                    return available != null && available;
                })
                .filter(item ->
                        (item.getName() != null && !item.getName().isBlank()
                                && item.getName().toLowerCase().contains(searchText))
                        || (item.getDescription() != null && !item.getDescription().isBlank()
                                && item.getDescription().toLowerCase().contains(searchText)))
                .collect(Collectors.toList());

        return list;
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

    /**
     * Проверка кода владельца
     *
     * @param ownerId Код владельца
     */
    @Override
    public void validateOwnerId(Long ownerId) {
        // Проверка на null ID
        if (ownerId == null) {
            throw new IllegalArgumentException("ID владельца не может быть null");
        }
    }
}

