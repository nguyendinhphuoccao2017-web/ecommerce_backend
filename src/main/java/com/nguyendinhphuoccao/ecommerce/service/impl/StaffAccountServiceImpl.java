package com.nguyendinhphuoccao.ecommerce.service.impl;

import com.nguyendinhphuoccao.ecommerce.entity.StaffAccount;
import com.nguyendinhphuoccao.ecommerce.repository.StaffAccountRepository;
import com.nguyendinhphuoccao.ecommerce.service.StaffAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class StaffAccountServiceImpl implements StaffAccountService {

    private final StaffAccountRepository repository;

    @Override
    public StaffAccount create(StaffAccount entity) {
        return repository.save(entity);
    }

    @Override
    public StaffAccount update(UUID id, StaffAccount entity) {
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
    public StaffAccount getById(UUID id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StaffAccount> getAll() {
        return repository.findAll();
    }
}
