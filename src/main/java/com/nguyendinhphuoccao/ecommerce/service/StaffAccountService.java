package com.nguyendinhphuoccao.ecommerce.service;

import com.nguyendinhphuoccao.ecommerce.entity.StaffAccount;
import java.util.List;
import java.util.UUID;

public interface StaffAccountService {
    StaffAccount create(StaffAccount entity);
    StaffAccount update(UUID id, StaffAccount entity);
    void delete(UUID id);
    StaffAccount getById(UUID id);
    List<StaffAccount> getAll();
}
