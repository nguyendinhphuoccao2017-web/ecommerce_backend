package com.nguyendinhphuoccao.ecommerce.service;

import com.nguyendinhphuoccao.ecommerce.entity.Notification;
import java.util.List;
import java.util.UUID;

public interface NotificationService {
    Notification create(Notification entity);
    Notification update(UUID id, Notification entity);
    void delete(UUID id);
    Notification getById(UUID id);
    List<Notification> getAll();
    void sendOrderConfirmationEmail(String email, String orderId);
}
