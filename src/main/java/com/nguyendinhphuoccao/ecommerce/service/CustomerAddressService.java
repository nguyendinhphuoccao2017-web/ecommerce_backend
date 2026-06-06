package com.nguyendinhphuoccao.ecommerce.service;

import com.nguyendinhphuoccao.ecommerce.entity.CustomerAddress;
import java.util.List;
import java.util.UUID;

public interface CustomerAddressService {
    CustomerAddress create(CustomerAddress entity);
    CustomerAddress update(UUID id, CustomerAddress entity);
    void delete(UUID id);
    CustomerAddress getById(UUID id);
    List<CustomerAddress> getAll();
}
