package ru.practicum.shareit.item.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.comment.CommentRepository;
import ru.practicum.shareit.comment.dto.CommentShortDto;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentMapper;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.exception.NotAvailableForOrderException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserServiceImpl userService;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemRequestService itemRequestService;

    @Autowired
    public ItemServiceImpl(
            ItemRepository itemRepository,
            UserServiceImpl userService,
            BookingRepository bookingRepository,
            CommentRepository commentRepository,
            @Lazy ItemRequestService itemRequestService  // Lazy применяется здесь
    ) {
        this.itemRepository = itemRepository;
        this.userService = userService;
        this.bookingRepository = bookingRepository;
        this.commentRepository = commentRepository;
        this.itemRequestService = itemRequestService;
    }

    /**
     * Создание предмета
     *
     * @param ownerId код владельца предмета
     * @param itemDto данные по предмету
     * @return ItemDto
     */
    @Override
    public ItemDto createItem(Long ownerId, ItemShortDto itemDto) {
        log.warn("createItem(Long {}, ItemShortDtoGW {})", ownerId, itemDto);
        Item item = ItemMapper.toEntity(itemDto);
        // Проверка
        item.setOwner(userService.validateUserExists(ownerId));


        Long requestId = itemDto.getRequestId();
        if (requestId != null) {
            item.setRequest(itemRequestService.validateItemRequestExists(requestId));
        }

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
        dto.setComments(CommentMapper.toDtoList(commentRepository.findAllByItemId(itemId)));
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
        List<Item> ownerItemList = itemRepository.findAllByOwnerId(ownerId);

        List<Long> ownerItemListId = ownerItemList.stream()
                .map(Item::getId)
                .toList();

        // Получаем комментарии для каждой вещи
        List<Comment> commentsAllList = commentRepository.findAllByListItemId(ownerItemListId);
        Map<Long, List<Comment>> commentsByItem = commentsAllList.stream()
                .collect(Collectors.groupingBy(comment -> comment.getItem().getId()));


        // Получаем бронирования для каждой вещи
        List<Booking> bookings = bookingRepository.findAllByItemId(ownerItemListId);
        Map<Long, BookingDto> lastBookings = new HashMap<>();
        Map<Long, BookingDto> nextBookings = new HashMap<>();

        for (Booking booking : bookings) {
            BookingDto bookingDto = BookingMapper.toDto(booking);
            Long itemId = booking.getItem().getId();

            // Определяем, является ли это последним или следующим бронированием
            if (booking.getStart().isBefore(LocalDateTime.now())) {
                lastBookings.put(itemId, bookingDto);
            } else {
                nextBookings.put(itemId, bookingDto);
            }
        }

        return ownerItemList.stream().map(
                item -> {
                    ItemDto itemDto = ItemMapper.toDto(item);

                    itemDto.setComments(commentsByItem.getOrDefault(itemDto.getId(), List.of()).stream()
                            .map(CommentMapper::toDto)
                            .toList());

                    itemDto.setLastBooking(lastBookings.get(itemDto.getId()));
                    itemDto.setNextBooking(nextBookings.get(itemDto.getId()));
                    return itemDto;
                }
        ).toList();
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
     * @param commentShortDto Комментарий
     * @param userId           ID пользователя
     * @param itemId           ID предмета
     * @return CommentDto
     */
    @Override
    public CommentDto addCommentToItem(CommentShortDto commentShortDto, Long userId, Long itemId) {
        log.warn("addCommentToItem(CommentShortDto {}, Long {}, Long {}})", commentShortDto, userId, itemId);

        Item existingItem = validateItemExists(itemId);
        User author = userService.validateUserExists(userId);
        Comment comment = commentRepository.save(CommentMapper.toEntity(commentShortDto, author, existingItem));

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


    /**
     * Получение списка предметов по коду запроса
     *
     * @param requestId код запроса
     * @return List<Item>
     */
    @Override
    public List<Item> getItemsListByRequest(Long requestId) {

        List<Item> list = itemRepository.findAllByRequestId(requestId);

        log.warn("ПРОВЕРКА:: getItemsListByRequest(Long {})", requestId);
        log.warn("ПРОВЕРКА:: getItemsListByRequest list = {}", list);

        List<Item> listAll = itemRepository.findAll();
        log.warn("ПРОВЕРКА:: getItemsListByRequest listAll = {}", listAll);

        return listAll;
    }
}