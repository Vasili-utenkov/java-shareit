package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DataConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UserNotExistsException;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    /**
     * Создание пользователя
     *
     * @param userDto данные пользователя
     * @return UserDto
     */
    @Override
    public UserDto createUser(UserDto userDto) {
        log.warn("createUser(UserDto {})", userDto);
        validateEmail(userDto.getEmail());
        return UserMapper.toDto(userRepository.save(UserMapper.toEntity(userDto)));
    }

    /**
     * Получение пользователя по коду пользователя
     *
     * @param userId ID пользователя
     * @return UserDto
     */
    @Override
    public UserDto getUserByID(Long userId) {
        log.warn("getUserByID(Long {})", userId);
        return UserMapper.toDto(validateUserExists(userId));
    }

    /**
     * Удаление пользователя по коду пользователя
     *
     * @param userId ID пользователя
     */
    @Override
    public void deleteUserByID(Long userId) {
        log.warn("deleteUserByID(Long {})", userId);
        User user = validateUserExists(userId);
        userRepository.delete(user);
    }

    /**
     * Изменение пользователя по коду пользователя
     *
     * @param userId  ID пользователя
     * @param userDto данные пользователя
     * @return UserDto
     */
    @Override
    public UserDto updateUserByID(Long userId, UserDto userDto) {
        log.warn("updateUserByID(Long {}, UserDto {})", userId, userDto);
        User existingUser = validateUserExists(userId);
        validateEmail(userDto.getEmail());
        // Обновление полей
        if (userDto.getEmail() != null && !userDto.getEmail().equals(existingUser.getEmail())) {
            existingUser.setEmail(userDto.getEmail());
        }

        if (userDto.getName() != null) {
            existingUser.setName(userDto.getName());
        }

        return UserMapper.toDto(userRepository.save(existingUser));
    }

    /**
     * Проверка переданого в поиск кода пользователя
     *
     * @param userId ID пользователя
     * @throws IllegalArgumentException "ID пользователя не может быть null"
     * @throws NotFoundException "Пользователь с ID " + userId + " не найден"
     */
    @Override
    public User validateUserExists(Long userId) {
        log.warn("validateUserId(Long {})", userId);
        // Проверка на null ID
        if (userId == null) {
            throw new IllegalArgumentException("ID пользователя не может быть null");
        }

        // Проверка существования пользователя
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotExistsException("Пользователь с ID " + userId + " не найден"));
    }

    /**
     * Проверка существования ( пользователя с ) email
     *
     * @param email электронная почта
     * @throws DataConflictException "Пользователь с email '" + email + "' уже существует"
     */
    public void validateEmail(String email) {
        log.warn("validateEmail(String {})", email);

        // Проверка существования пользователя с email
        if (userRepository.existsByEmail(email)) {
            throw new DataConflictException("Пользователь с email '" + email + "' уже существует");
        }
    }

}
