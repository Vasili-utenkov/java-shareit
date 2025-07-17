package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

@RestController
@RequestMapping("/users2")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDto createUser( @RequestBody UserDto userDto) {
        log.warn("Создание пользователя. @PostMapping (/users) ");
        log.warn("createUser(@Valid @RequestBody UserDto {})", userDto);
        UserDto dto = userService.createUser(userDto);
        log.warn("ИТОГ: Создали пользователя " + dto);
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
        log.warn("ИТОГ Изменили пользователя на " + dto);
        return dto;
    }
}