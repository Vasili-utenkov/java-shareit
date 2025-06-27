package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DataConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.storage.UserIMStorage;

@Service("userIMService")
@ConditionalOnProperty(name = "share.storage.type", havingValue = "memory")
@RequiredArgsConstructor
@Slf4j
public class UserIMService implements UserService {

    private final UserIMStorage userStorage;

    /**
     * Создание пользователя
     *
     * @param userDto данные пользователя
     * @return UserDto
     */
    @Override
    public UserDto createUser(UserDto userDto) {
        log.warn("createUser(UserDto {})", userDto);
        validateEmail(UserMapper.toEntity(userDto).getEmail());
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
        log.warn("getUserByID(Long )", userId);
        validateUserId(userId);
        return UserMapper.toDto(userStorage.get(userId));
    }

    /**
     * Удаление пользователя по коду пользователя
     *
     * @param userId код пользователя
     */
    @Override
    public void deleteUserByID(Long userId) {
        log.warn("deleteUserByID(Long {})", userId);
        validateUserId(userId);
        userStorage.delete(userId);
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
        log.warn("updateUserByID(Long {}, UserDto {})", userId, userDto);
        validateUserId(userId);
        validateEmail(UserMapper.toEntity(userDto).getEmail());
        return UserMapper.toDto(userStorage.update(userId, UserMapper.toEntity(userDto)));
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
        log.warn("validateUserId(Long {})", userId);
        // Проверка на null ID
        if (userId == null) {
            throw new IllegalArgumentException("ID пользователя не может быть null");
        }

        // Проверка существования пользователя
        if (userStorage.get(userId) == null) {
            throw new NotFoundException("Пользователь с ID " + userId + " не найден");
        }
    }

    /**
     * Проверка существования email
     *
     * @param email электронная почта
     * @throws DataConflictException "Пользователь с email '" + email + "' уже существует"
     */
    public void validateEmail(String email) {
        log.warn("validateEmail(String {})", email);
        // Проверка существования email
        if (userStorage.existsEmail(email)) {
            throw new DataConflictException("Пользователь с email '" + email + "' уже существует");
        }

    }

}
