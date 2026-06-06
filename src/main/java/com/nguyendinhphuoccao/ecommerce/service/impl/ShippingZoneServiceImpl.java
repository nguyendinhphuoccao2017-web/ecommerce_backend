package com.nguyendinhphuoccao.ecommerce.service.impl;

import com.nguyendinhphuoccao.ecommerce.entity.ShippingZone;
import com.nguyendinhphuoccao.ecommerce.repository.ShippingZoneRepository;
import com.nguyendinhphuoccao.ecommerce.service.ShippingZoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ShippingZoneServiceImpl implements ShippingZoneService {

    private final ShippingZoneRepository repository;

    @Override
    public ShippingZone create(ShippingZone entity) {
        return repository.save(entity);
    }

    @Override
    public ShippingZone update(UUID id, ShippingZone entity) {
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
    public ShippingZone getById(UUID id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShippingZone> getAll() {
        return repository.findAll();
    }
}
