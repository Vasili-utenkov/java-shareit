package ru.practicum.shareit.item.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.comment.CommentRepository;
import ru.practicum.shareit.comment.dto.CommentCreateDto;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentMapper;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.exception.NotAvailableForOrderException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserServiceImpl userService;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    /**
     * Создание предмета
     *
     * @param ownerId код владельца предмета
     * @param itemDto данные по предмету
     * @return ItemDto
     */
    @Override
    public ItemDto createItem(Long ownerId, ItemDto itemDto) {
        log.warn("createItem(Long {}, ItemDto {})", ownerId, itemDto);
        Item item = ItemMapper.toEntity(itemDto);
        // Проверка
        item.setOwner(userService.validateUserExists(ownerId));
        return ItemMapper.toDto(itemRepository.save(item));
    }

    /**
     * получение предмета по коду предмета
     *
     * @param itemId код предмета
     * @return ItemDto
     */
    @Override
    public ItemDto getItemByItemID(Long itemId) {
        log.warn("getItemByItemID(Long {})", itemId);
        ItemDto dto = ItemMapper.toDto(validateItemExists(itemId));
        // Добавить комменты
        dto.setComments(CommentMapper.toDto(commentRepository.findAllByItemId(itemId)));
        return dto;
    }

    /**
     * Удаление предмета по коду предмета
     *
     * @param itemId  код предмета
     * @param ownerId код владельца предмета
     */
    @Override
    public void deleteItem(Long itemId, Long ownerId) {
        log.warn("deleteItem(Long {}, Long {})", itemId, ownerId);
        checkEqualOwners(itemId, ownerId);
        itemRepository.delete(validateItemExists(itemId));
    }

    /**
     * Изменения данных по предмету
     *
     * @param ownerId код владельца предмета
     * @param itemId  код предмета
     * @param itemDto новые данные по предмету
     * @return ItemDto
     */
    @Override
    public ItemDto updateItemByItemID(Long ownerId, Long itemId, ItemDto itemDto) {
        log.warn("updateItemByItemID(Long {}, Long {}, ItemDto {})", ownerId, itemId, itemDto);
        Item exitingItem = validateItemExists(itemId);
        checkEqualOwners(itemId, ownerId);
        exitingItem.setOwner(userService.validateUserExists(ownerId));
        exitingItem.setId(itemId);
        ItemMapper.updateItemFromDto(itemDto, exitingItem);
        return ItemMapper.toDto(exitingItem);
    }

    /**
     * Получение списка предметов по коду владельца
     *
     * @param ownerId код владельца предмета
     * @return List<ItemDto>
     */
    @Override
    public List<ItemDto> getItemsListByOwner(Long ownerId) {
        log.warn("getItemsListByOwner(Long {})", ownerId);
        userService.validateUserExists(ownerId);
        List<ItemDto> list = ItemMapper.toDto(itemRepository.findAllByOwnerId(ownerId));
        return list;
    }

    /**
     * Получение списка предметов по тексту из названия или описания
     *
     * @param text текст из названия или описания предмета
     * @return List<ItemDto>
     */
    @Override
    public List<ItemDto> getItemsListByText(String text) {
        log.warn("getItemsListByText(String {})", text);
        if (text == null || text.isBlank()) {
            return List.of();
        }
        List<ItemDto> list = ItemMapper.toDto(itemRepository.findAvailableByText(text.toLowerCase(Locale.ROOT)));
        return list;
    }

    /**
     * @param itemId  код предмета
     * @param ownerId код владельца предмета
     * @throws NotFoundException "Неверный код владельца предмета " + ownerId"
     */
    private void checkEqualOwners(Long itemId, Long ownerId) {
        log.warn("checkEqualOwners(Long {}, Long {});", itemId, ownerId);
        if (!ownerId.equals(getItemByItemID(itemId).getOwner().getId())) {
            throw new NotFoundException("Неверный код владельца предмета " + ownerId);
        }
    }

    /**
     * Проверка переданого в поиск кода предмета
     *
     * @param itemId код предмета
     * @throws IllegalArgumentException "ID предмета не может быть null"
     * @throws NotFoundException        "Предмет с ID " + itemId + " не найден"
     */
    @Override
    public Item validateItemExists(Long itemId) {
        log.warn("validateItemExists(Long {});", itemId);
        // Проверка на null ID
        if (itemId == null) {
            throw new IllegalArgumentException("ID предмета не может быть null");
        }

        // Проверка существования предмета
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Предмет с ID " + itemId + " не найден"));
    }


    /**
     * Добавить комментарий в предмет
     *
     * @param commentCreateDto Комментарий
     * @param userId           ID пользователя
     * @param itemId           ID предмета
     * @return CommentDto
     */
    @Override
    public CommentDto addCommentToItem(CommentCreateDto commentCreateDto, Long userId, Long itemId) {
        log.warn("addCommentToItem(CommentCreateDto {}, Long {}, Long {}})", commentCreateDto, userId, itemId);

        Item existingItem = validateItemExists(itemId);
        User author = userService.validateUserExists(userId);
        Comment comment = commentRepository.save(CommentMapper.toEntity(commentCreateDto, author, existingItem));

        // Проверка бронирований через репозиторий
        List<Booking> bookings = bookingRepository.findCompletedBookingsByUserAndItem(
                userId,
                itemId,
                LocalDateTime.now()
        );

        if (bookings.isEmpty()) {
            throw new NotAvailableForOrderException("Пользователь не брал вещь в аренду");
        }

        // Добавить коментарий в предмет
        ItemDto itemDto = ItemMapper.toDto(existingItem);
        List<CommentDto> dtoList = itemDto.getComments();
        if (dtoList == null) {
            dtoList = new ArrayList<>();
        }
        dtoList.add(CommentMapper.toDto(comment));
        itemDto.setComments(dtoList);

        return CommentMapper.toDto(commentRepository.save(comment));
    }
}