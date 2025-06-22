package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.storage.UserIMStorage;

@Service("userIMService")
@ConditionalOnProperty(name = "share.storage.type", havingValue = "memory")
@RequiredArgsConstructor
public class UserIMService implements UserService{

    private final UserIMStorage userStorage;

    /**
     * Создание пользователя
     *
     * @param userDto данные пользователя
     * @return UserDto
     */
    @Override
    public UserDto createUser(UserDto userDto) {
        return UserMapper.toDto(userStorage.create(UserMapper.toEntity(userDto)));
    }

    /**
     * Получение пользователя по коду пользователя
     *
     * @param userId код пользователя
     * @return UserDto
     */
    @Override
    public UserDto getUserByID(Long userId) {
        return null;
    }

    /**
     * Удаление пользователя по коду пользователя
     *
     * @param userId код пользователя
     */
    @Override
    public void deleteUserByID(Long userId) {

    }

    /**
     * Изменение пользователя по коду пользователя
     *
     * @param userId  код пользователя
     * @param userDto данные пользователя
     * @return UserDto
     */
    @Override
    public UserDto updateUserByID(Long userId, UserDto userDto) {
        return null;
    }
}
