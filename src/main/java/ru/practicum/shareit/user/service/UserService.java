package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

public interface UserService {

    /**
     * Создание пользователя
     *
     * @param userDto данные пользователя
     * @return UserDto
     */
    UserDto createUser(UserDto userDto);

    /**
     * Получение пользователя по коду пользователя
     *
     * @param userId ID пользователя
     * @return UserDto
     */
    UserDto getUserByID(Long userId);

    /**
     * Удаление пользователя по коду пользователя
     *
     * @param userId ID пользователя
     */

    void deleteUserByID(Long userId);

    /**
     * Изменение пользователя по коду пользователя
     *
     * @param userId ID пользователя
     * @param userDto новые данные пользователя
     * @return UserDto
     */
    UserDto updateUserByID(Long userId, UserDto userDto);

    /**
     * Проверка переданого в поиск кода пользователя
     *
     * @param userId ID пользователя
     */
    User validateUserId(Long userId);

    /**
     * Проверка существования email
     *
     * @param email электронная почта
     */
    void validateEmail(String email);

}
