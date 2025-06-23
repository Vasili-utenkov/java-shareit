package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.UserServiceFactory;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserServiceFactory factory;
    private final UserService userService;

    @Autowired
    public UserController(UserServiceFactory factory) {
        this.factory = factory;
        this.userService = factory.getUserService();
    }

    // Создание пользователя
    @PostMapping
    public UserDto createUser(@Valid @RequestBody UserDto userDto) {
        UserDto dto = userService.createUser(userDto);
        log.warn("ПРОВЕРКА: ");
        log.warn("ПРОВЕРКА: " + "createUser(@Valid @RequestBody UserDto {})", userDto);
        log.warn("ПРОВЕРКА: " + "dto = " + dto);

        return dto;
    }

    // Получение пользователя по коду пользователя
    @GetMapping("{userId}")
    public UserDto getUserByID(@PathVariable Long userId) {
        return userService.getUserByID(userId);
    }

    // Удаление пользователя по коду пользователя
    @DeleteMapping("{userId}")
    public void deleteUserByID(@PathVariable Long userId) {
        userService.deleteUserByID(userId);
    }

    // Изменение пользователя по коду пользователя
    @PatchMapping("{userId}")
    public UserDto updateUserByID(@PathVariable Long userId, @RequestBody UserDto userDto) {
        UserDto dto = userService.updateUserByID(userId, userDto);

        log.warn("ПРОВЕРКА: ");
        log.warn("ПРОВЕРКА: " + "updateUserByID(@PathVariable Long {}, @RequestBody UserDto {})", userId, userDto);
        log.warn("ПРОВЕРКА: " + "dto = " + dto);

        return dto;
    }
}