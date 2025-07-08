package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Repository;
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
     * @param userId ID пользователя
     * @param user   новые данные о пользователе
     * @return User
     */
    @Override
    public User update(Long userId, User user) {
        // Полная замена объекта
        user.setId(userId);
        users.put(userId, user);
        return user;
    }

    /**
     * @param userId ID пользователя
     */
    @Override
    public void delete(Long userId) {
        users.remove(userId);
    }

    /**
     * @param userId ID пользователя
     * @return User
     */
    @Override
    public User get(Long userId) {
        return users.get(userId);
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

    /**
     * Проверка дублирования электронной почты
     *
     * @param email электронная почта
     */
    @Override
    public boolean existsEmail(String email) {
        // Проверка на дубликат через count()
        long count = users.values().stream()
                .map(User::getEmail)
                .filter(Objects::nonNull) // Игнорируем null email
                .map(String::trim) // Удаляем пробелы
                .filter(e -> e.equalsIgnoreCase(email))
                .count();

        return count > 0;

    }
}
