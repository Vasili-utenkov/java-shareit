package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.user.dto.UserDto;

@Service
@Slf4j
public class UserClient extends BaseClient {

    private static final String API_PREFIX = "/users2";

    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> createUser(UserDto userDto) {
        log.warn("UserClient:: Создание пользователя. @PostMapping (/users): createUser(UserDto {})", userDto);
        return post("", userDto);
    }

    public ResponseEntity<Object> getUserByID(long userId) {
        log.warn("UserClient:: Получение пользователя по коду пользователя. @GetMapping (/users/{userId}): " +
                "getUserByID(long {})", userId);
        return get("/" + userId);
    }

    public ResponseEntity<Object> deleteUserByID(long userId) {
        log.warn("UserClient:: Удаление пользователя по коду пользователя. @DeleteMapping (/users/{userId}): " +
                "deleteUserByID(long {})", userId);
        return delete("/" + userId);
    }

    public ResponseEntity<Object> updateUserByID(long userId, UserDto userDto) {
        log.warn("UserClient:: Изменение пользователя по коду пользователя. @PatchMapping (/users/{userId}): " +
                "updateUserByID(long {}, UserDto {})", userId, userDto);
        return patch("/" + userId, userDto);
    }


}
