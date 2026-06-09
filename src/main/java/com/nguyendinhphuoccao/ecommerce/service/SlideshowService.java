package com.nguyendinhphuoccao.ecommerce.service;

import com.nguyendinhphuoccao.ecommerce.entity.Slideshow;
import java.util.List;
import java.util.UUID;

public interface SlideshowService {
    Slideshow create(Slideshow entity);
    Slideshow update(UUID id, Slideshow entity);
    void delete(UUID id);
    Slideshow getById(UUID id);
    List<Slideshow> getAll();
    List<Slideshow> getHomeSlideshows();
}
