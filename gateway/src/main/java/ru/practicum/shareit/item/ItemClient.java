package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.comment.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Map;

@Service
@Slf4j
public class ItemClient extends BaseClient {

    private static final String API_PREFIX = "/items2";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }


    public ResponseEntity<Object> createItem(long userId, ItemDto requestDto) {
        log.warn("ItemClient:: Добавление новой вещи. @PostMapping (/items):" +
                        " createItem(long {}, ItemDto {})",
                userId, requestDto);
        return post("", userId, requestDto);
    }

    public ResponseEntity<Object> updateItemByItemID(long ownerId, long itemId, ItemDto item) {
        log.warn("ItemClient:: Редактирование вещи. @PatchMapping (/items/{itemId}): " +
                        "updateItemByItemID(long {}, long {}, ItemDto {})",
                ownerId, itemId, item);
        return patch("/" + itemId, ownerId, item);
    }

    public ResponseEntity<Object> deleteItem(long itemId, long ownerId) {
        log.warn("ItemClient:: Удаление вещи. @DeleteMapping (/items/{itemId}): " +
                        "deleteItem(long {}, long {})",
                itemId, ownerId);
        return delete("/" + itemId, ownerId);
    }

    public ResponseEntity<Object> getItemByItemID(long itemId) {
        log.warn("ItemClient:: Просмотр информации о конкретной вещи по её идентификатору. " +
                "@GetMapping (/items/{itemId}): getItemByItemID(long {})", itemId);
        return get("/" + itemId);
    }

    public ResponseEntity<Object> getItemsListByOwner(long ownerId) {
        log.warn("ItemClient:: Просмотр владельцем списка всех его вещей с указанием названия " +
                        "и описания для каждой из них. @GetMapping (/items): " +
                        "getItemsListByOwner(long {}", ownerId);
        return get("", ownerId);
    }

    public ResponseEntity<Object> getItemsByText(String text) {
        log.warn("ItemClient:: Поиск вещи потенциальным арендатором по имени или описанию. " +
                "@GetMapping (/items/search): searchItemsByText(String {})", text);
        Map<String, Object> parameters = Map.of(
                "text", text
        );
        return get("/search", parameters);
    }

    public ResponseEntity<Object> addCommentToItem(CommentCreateDto dto, long userId, long itemId) {
        log.warn("ItemClient:: Добавить коментарий по вещи. @PostMapping(/items/{itemId}/comment): createComment(" +
                        "CommentCreateDto {}, long {}, long {})",
                        dto, userId, itemId);
        return post("/" + itemId + "/comment)", userId, dto);
    }

}
