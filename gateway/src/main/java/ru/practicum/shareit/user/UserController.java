package ru.practicum.shareit.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDtoGW;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> createUser(
            @Valid @RequestBody UserDtoGW userDto
    ) {
        log.warn("GATEWAY:: Создание пользователя. @PostMapping (/users) ");
        log.warn("createUser(@Valid @RequestBody UserDtoGW {})", userDto);
        ResponseEntity<Object> response = userClient.createUser(userDto);

        // Логируем тело ответа
        log.warn("GATEWAY:: Тело ответа: {}", response.getBody());

        log.warn("GATEWAY:: ИТОГ: Создали пользователя " + response);
        return response;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserByID(
            @PathVariable long userId
    ) {
        log.warn("GATEWAY:: Получение пользователя по коду пользователя. @GetMapping (/users/{userId}) ");
        log.warn("getUserByID(@PathVariable Long {})", userId);
        ResponseEntity<Object> response = userClient.getUserByID(userId);
        return response;
    }

    @DeleteMapping("/{userId}")
    public void deleteUserByID(
            @PathVariable long userId
    ) {
        log.warn("GATEWAY:: Удаление пользователя по коду пользователя. @DeleteMapping (/users/{userId}) ");
        log.warn("deleteUserByID(@PathVariable Long {})", userId);
        ResponseEntity<Object> response = userClient.deleteUserByID(userId);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUserByID(
            @PathVariable long userId,
            @Valid @RequestBody UserDtoGW userDto) {
        log.warn("GATEWAY:: Изменение пользователя по коду пользователя. @PatchMapping (/users/{userId}) ");
        log.warn("updateUserByID(@PathVariable Long {}, @RequestBody UserDtoGW {})", userId, userDto);
        ResponseEntity<Object> response = userClient.updateUserByID(userId, userDto);
        log.warn("GATEWAY:: ИТОГ Изменили пользователя на " + response);
        return response;
    }
}