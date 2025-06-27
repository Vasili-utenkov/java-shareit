package ru.practicum.shareit.user.service;

import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

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
     * @param userId код пользователя
     * @return UserDto
     */
    UserDto getUserByID(Long userId);

    /**
     * Удаление пользователя по коду пользователя
     *
     * @param userId код пользователя
     */

    void deleteUserByID(Long userId);

    /**
     * Изменение пользователя по коду пользователя
     *
     * @param userId код пользователя
     * @param userDto новые данные пользователя
     * @return UserDto
     */
    UserDto updateUserByID(Long userId, UserDto userDto);


    /**
     * Проверка переданого в поиск кода пользователя
     *
     * @param userId код пользователя
     */
    void validateUserId(Long userId);

    /**
     * Проверка существования email
     *
     * @param email электронная почта
     */
    void validateEmail(String email);

}
