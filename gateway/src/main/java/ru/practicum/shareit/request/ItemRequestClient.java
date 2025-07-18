package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.request.dto.ItemRequestShortDto;


@Service
@Slf4j
public class ItemRequestClient extends BaseClient {

    private static final String API_PREFIX = "/requests2";

    @Autowired
    public ItemRequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    ResponseEntity<Object> createItemRequest(long userId, ItemRequestShortDto dto) {
        log.warn("ItemRequestClient:: Добавление нового запроса. @PostMapping (/requests): " +
                 "createItemRequest(long {}, ItemRequestShortDto {})", userId, dto);

        return post("", userId, dto);
    }


    ResponseEntity<Object> getItemRequestListByUser(long userId) {
        log.warn("ItemRequestClient:: список запросов пользователя и ответы на них. @GetMapping (/requests): " +
                 "getItemRequestList(long {})", userId);

        return get("", userId);
    }


    ResponseEntity<Object> getItemRequestListAllUser(long userId) {
        log.warn("ItemRequestClient:: список запросов всех пользователя и ответы на них. " +
                 "@GetMapping (/requests/all): getItemRequestList(long {})", userId);

        return get("/all", userId);
    }


    ResponseEntity<Object> getItemRequestById(long userId, long requestId) {
        log.warn("ItemRequestClient:: данные о запросе вместе с данными об ответах на него. " +
                 "@GetMapping (/requests/{requestId}): getItemRequestList(long {}, long {})",
                userId, requestId);

        return get("/" + requestId, userId);
    }


}
