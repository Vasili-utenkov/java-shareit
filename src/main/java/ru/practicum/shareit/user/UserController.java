package ru.practicum.shareit.user;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    // Создание пользователя
    @PostMapping
    public UserDto createUser(@Valid @RequestBody UserDto userDto) {
        return userService.createUser(userDto);
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
        return userService.updateUserByID(userId, userDto);
    }
}