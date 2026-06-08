package com.nguyendinhphuoccao.ecommerce.service.impl;

import com.nguyendinhphuoccao.ecommerce.entity.Slideshow;
import com.nguyendinhphuoccao.ecommerce.entity.StaffAccount;
import com.nguyendinhphuoccao.ecommerce.repository.SlideshowRepository;
import com.nguyendinhphuoccao.ecommerce.security.CustomUserDetails;
import com.nguyendinhphuoccao.ecommerce.service.SlideshowService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class SlideshowServiceImpl implements SlideshowService {

    private final SlideshowRepository repository;

    private StaffAccount getCurrentStaff() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof CustomUserDetails) {
            StaffAccount staff = ((CustomUserDetails) auth.getPrincipal()).getStaffAccount();
            if (staff != null) {
                return staff;
            }
        }
        throw new org.springframework.security.access.AccessDeniedException("Unauthorized: Only Staff Account can perform this action.");
    }

    @Override
    public Slideshow create(Slideshow entity) {
        entity.setCreatedBy(getCurrentStaff());
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());
        
        if (entity.getClicks() == null) {
            entity.setClicks(0);
        }
        if (entity.getPublished() == null) {
            entity.setPublished(true);
        }
        
        return repository.save(entity);
    }

    @Override
    public Slideshow update(UUID id, Slideshow entity) {
        return repository.findById(id).map(existing -> {
            if (entity.getTitle() != null) existing.setTitle(entity.getTitle());
            if (entity.getDestinationUrl() != null) existing.setDestinationUrl(entity.getDestinationUrl());
            if (entity.getImage() != null) existing.setImage(entity.getImage());
            if (entity.getPlaceholder() != null) existing.setPlaceholder(entity.getPlaceholder());
            if (entity.getDescription() != null) existing.setDescription(entity.getDescription());
            if (entity.getBmLabel() != null) existing.setBmLabel(entity.getBmLabel());
            if (entity.getDisplayOrder() != null) existing.setDisplayOrder(entity.getDisplayOrder());
            if (entity.getPublished() != null) existing.setPublished(entity.getPublished());
            if (entity.getClicks() != null) existing.setClicks(entity.getClicks());
            if (entity.getStyles() != null) existing.setStyles(entity.getStyles());
            
            existing.setUpdatedBy(getCurrentStaff());
            existing.setUpdatedAt(OffsetDateTime.now());
            
            return repository.save(existing);
        }).orElse(null);
    }

    @Override
    public void delete(UUID id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Slideshow getById(UUID id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Slideshow> getAll() {
        return repository.findAll();
    }
}
