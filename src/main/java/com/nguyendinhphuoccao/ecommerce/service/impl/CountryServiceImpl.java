package com.nguyendinhphuoccao.ecommerce.service.impl;

import com.nguyendinhphuoccao.ecommerce.entity.Country;
import com.nguyendinhphuoccao.ecommerce.repository.CountryRepository;
import com.nguyendinhphuoccao.ecommerce.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CountryServiceImpl implements CountryService {

    private final CountryRepository repository;

    @Override
    public Country create(Country entity) {
        return repository.save(entity);
    }

    @Override
    public Country update(Integer id, Country entity) {
        if(repository.existsById(id)) {
            entity.setId(id);
            return repository.save(entity);
        }
        return null;
    }

    @Override
    public void delete(Integer id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Country getById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Country> getAll() {
        return repository.findAll();
    }
}
