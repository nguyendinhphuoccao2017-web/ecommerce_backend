package com.nguyendinhphuoccao.ecommerce.service;

import com.nguyendinhphuoccao.ecommerce.entity.Country;
import java.util.List;

public interface CountryService {
    Country create(Country entity);
    Country update(Integer id, Country entity);
    void delete(Integer id);
    Country getById(Integer id);
    List<Country> getAll();
}
