package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserStorage {

    /**
     * Создание пользователя
     *
     * @param user данные о пользователе
     * @return User
     */
    User create(User user);

    /**
     * Изменение данных по пользователю
     *
     * @param userId код пользователя
     * @param user новые данные о пользователе
     * @return User
     */
    User update(Long userId, User user);

    /**
     * Удаление пользователя
     *
     * @param userId код пользователя
     */
    void delete(Long userId);

    /**
     * получение данных по пользователю
     *
     * @param userId код пользователя
     * @return User
     */
    User get(Long userId);

    /**
     * Получение списка пользователей
     *
     * @return List<User>
     */
    List<User> getAll();


    /**
     * Проверка существования электронной почты
     *
     * @param email электронная почта
     * @return boolean
     */
    boolean existsEmail(String email);

}
