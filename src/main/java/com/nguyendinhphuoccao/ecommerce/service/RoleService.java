package com.nguyendinhphuoccao.ecommerce.service;

import com.nguyendinhphuoccao.ecommerce.entity.Role;
import java.util.List;
import java.util.UUID;

public interface RoleService {
    Role create(Role entity);
    Role update(UUID id, Role entity);
    void delete(UUID id);
    Role getById(UUID id);
    List<Role> getAll();
}
