package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceFactory {
    private final Environment environment;
    private final ApplicationContext applicationContext;

    public UserService getUserService() {
        String storageType = environment.getProperty("share.storage.type", "memory");

        try {
            return switch (storageType.toLowerCase()) {
                default -> applicationContext.getBean("userIMService", UserIMService.class);
            };
        } catch (BeansException ex) {
            throw new IllegalStateException("No available share service implementation for type: " + storageType, ex);
        }
    }
}