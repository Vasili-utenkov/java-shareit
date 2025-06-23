package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.DataConflictException;
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
        validateEmail(user.getEmail());
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
        validateUserId(userId);
        validateEmail(user.getEmail());
        // Полная замена объекта
        user.setId(userId);
        users.put(userId, user);
        return user;
    }

    /**
     * @param userId код пользователя
     */
    @Override
    public void delete(Long userId) {
        validateUserId(userId);
        users.remove(userId);
    }

    /**
     * @param userId код пользователя
     * @return User
     */
    @Override
    public User get(Long userId) {
        validateUserId(userId);
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
     * Проверка переданого в поиск кода пользователя
     *
     * @param userId код пользователя
     * @throws IllegalArgumentException "ID пользователя не может быть null"
     * @throws NotFoundException "Пользователь с ID " + userId + " не найден"
     */
    @Override
    public void validateUserId(Long userId) {
        // Проверка на null ID
        if (userId == null) {
            throw new IllegalArgumentException("ID пользователя не может быть null");
        }

        // Проверка существования пользователя
        if (!users.containsKey(userId)) {
            throw new NotFoundException("Пользователь с ID " + userId + " не найден");
        }
    }

    /**
     * Проверка дублирования электронной почты
     *
     * @param email электронная почта
     */
    @Override
    public void validateEmail(String email) {
        // Проверка на дубликат через count()
        long count = users.values().stream()
                .map(User::getEmail)
                .filter(Objects::nonNull) // Игнорируем null email
                .map(String::trim) // Удаляем пробелы
                .filter(e -> e.equalsIgnoreCase(email))
                .count();

        if (count > 0) {
            throw new DataConflictException("Пользователь с email '" + email + "' уже существует");
        }
    }
}
