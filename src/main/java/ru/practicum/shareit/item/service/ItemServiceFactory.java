package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemServiceFactory {
    private final Environment environment;
    private final ApplicationContext applicationContext;

    public ItemService getItemService() {
        String storageType = environment.getProperty("share.storage.type", "memory");

        try {
            return switch (storageType.toLowerCase()) {
                default -> applicationContext.getBean("itemIMService", ItemService.class);
            };
        } catch (BeansException ex) {
            throw new IllegalStateException("No available share service implementation for type: " + storageType, ex);
        }
    }
}