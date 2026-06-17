package com.nguyendinhphuoccao.ecommerce.service.impl;

import com.nguyendinhphuoccao.ecommerce.entity.Notification;
import com.nguyendinhphuoccao.ecommerce.repository.NotificationRepository;
import com.nguyendinhphuoccao.ecommerce.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository repository;

    @Override
    public Notification create(Notification entity) {
        return repository.save(entity);
    }

    @Override
    public Notification update(UUID id, Notification entity) {
        if(repository.existsById(id)) {
            entity.setId(id);
            return repository.save(entity);
        }
        return null;
    }

    @Override
    public void delete(UUID id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Notification getById(UUID id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notification> getAll() {
        return repository.findAll();
    }

    @Override
    @Async
    public void sendOrderConfirmationEmail(String email, String orderId) {
        log.info("START ASYNC TASK: Sending order confirmation email to {} for order ID: {}", email, orderId);
        
        try {
            // Simulate network delay for email sending
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        log.info("END ASYNC TASK: Order confirmation email successfully sent to {}", email);
    }
}
