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
     * Проверка переданого в поиск кода пользователя
     *
     * @param userId код пользователя
     */
    void validateUserId(Long userId);


    /**
     * Проверка дублирования электронной почты
     *
     * @param email электронная почта
     */
    void validateEmail(String email);

}
