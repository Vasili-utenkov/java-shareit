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

    @PostMapping
    public UserDto createUser(@Valid @RequestBody UserDto userDto) {
        log.warn("Создание пользователя. @PostMapping (/users) ");
        log.warn("createUser(@Valid @RequestBody UserDto {})", userDto);
        UserDto dto = userService.createUser(userDto);
        return dto;
    }

    @GetMapping("/{userId}")
    public UserDto getUserByID(@PathVariable Long userId) {
        log.warn("Получение пользователя по коду пользователя. @GetMapping (/users/{userId}) ");
        log.warn("getUserByID(@PathVariable Long {})", userId);
        UserDto dto = userService.getUserByID(userId);
        return dto;
    }

    @DeleteMapping("/{userId}")
    public void deleteUserByID(@PathVariable Long userId) {
        log.warn("Удаление пользователя по коду пользователя. @DeleteMapping (/users/{userId}) ");
        log.warn("deleteUserByID(@PathVariable Long {})", userId);
        userService.deleteUserByID(userId);
    }

    @PatchMapping("/{userId}")
    public UserDto updateUserByID(@PathVariable Long userId, @RequestBody UserDto userDto) {
        log.warn("Изменение пользователя по коду пользователя. @PatchMapping (/users/{userId}) ");
        log.warn("updateUserByID(@PathVariable Long {}, @RequestBody UserDto {})", userId, userDto);
        UserDto dto = userService.updateUserByID(userId, userDto);
        return dto;
    }
}