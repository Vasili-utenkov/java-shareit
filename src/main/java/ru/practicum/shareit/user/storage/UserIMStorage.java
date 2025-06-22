package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class UserIMStorage implements UserStorage {

    private final AtomicLong id = new AtomicLong(1);
    private final Map<Long, User> users = new HashMap<>();

    /**
     * Создание пользователя
     *
     * @param user данные о пользователе
     * @return User
     */
    @Override
    public User create(User user) {
        user.setId(id.getAndIncrement());
        users.put(user.getId(), user);
        return user;
    }

    /**
     * @param userId код пользователя
     * @param user   новые данные о пользователе
     * @return User
     */
    @Override
    public User update(Long userId, User user) {
        // Если существует
        if (!users.containsKey(userId)) {
            throw new NotFoundException("Пользователь не найден");
        }
        // Полная замена объекта
        users.put(userId, user);
        return user;
    }

    /**
     * @param userId код пользователя
     */
    @Override
    public void delete(Long userId) {
        // Если существует
        if (!users.containsKey(userId)) {
            throw new NotFoundException("Пользователь не найден");
        }
        users.remove(userId);
    }

    /**
     * @param userId код пользователя
     * @return Optional<User>
     */
    @Override
    public Optional<User> get(Long userId) {
        return Optional.ofNullable(users.get(userId));
    }

    /**
     * Получение списка пользователей
     *
     * @return List<User>
     */
    @Override
    public List<User> getAll() {
        return users == null ? Collections.emptyList()
                : new ArrayList<>(users.values());
    }
}
