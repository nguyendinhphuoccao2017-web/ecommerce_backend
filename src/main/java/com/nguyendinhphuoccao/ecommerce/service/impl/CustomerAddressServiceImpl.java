package com.nguyendinhphuoccao.ecommerce.service.impl;

import com.nguyendinhphuoccao.ecommerce.entity.CustomerAddress;
import com.nguyendinhphuoccao.ecommerce.repository.CustomerAddressRepository;
import com.nguyendinhphuoccao.ecommerce.service.CustomerAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerAddressServiceImpl implements CustomerAddressService {

    private final CustomerAddressRepository repository;

    @Override
    public CustomerAddress create(CustomerAddress entity) {
        return repository.save(entity);
    }

    @Override
    public CustomerAddress update(UUID id, CustomerAddress entity) {
        if(repository.existsById(id)) {
            entity.setId(id);
            return repository.save(entity);
        }
        return null;
    }

    @Override
    public void delete(UUID id) {
        CustomerAddress address = repository.findById(id).orElse(null);
        if (address != null) {
            address.setIsActive(false);
            repository.save(address);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerAddress getById(UUID id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerAddress> getAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerAddress> getMyAddresses(UUID customerId) {
        return repository.findByCustomerIdAndIsActiveTrue(customerId);
    }
}
