package com.nguyendinhphuoccao.ecommerce.service;

import com.nguyendinhphuoccao.ecommerce.entity.Customer;
import java.util.List;
import java.util.UUID;

public interface CustomerService {
    Customer create(Customer entity);
    Customer update(UUID id, Customer entity);
    void delete(UUID id);
    Customer getById(UUID id);
    List<Customer> getAll();
}
