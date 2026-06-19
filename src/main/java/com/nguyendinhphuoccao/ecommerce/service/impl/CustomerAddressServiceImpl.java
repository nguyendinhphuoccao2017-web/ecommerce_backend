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
        CustomerAddress existing = repository.findById(id).orElse(null);
        if(existing != null) {
            existing.setFullName(entity.getFullName());
            existing.setAddressLine1(entity.getAddressLine1());
            existing.setAddressLine2(entity.getAddressLine2());
            existing.setPhoneNumber(entity.getPhoneNumber());
            existing.setDialCode(entity.getDialCode());
            existing.setCountry(entity.getCountry());
            existing.setPostalCode(entity.getPostalCode());
            existing.setCity(entity.getCity());
            existing.setState(entity.getState());
            return repository.save(existing);
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
