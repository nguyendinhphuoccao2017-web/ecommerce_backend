package com.nguyendinhphuoccao.ecommerce.service.impl;

import com.nguyendinhphuoccao.ecommerce.dto.product.ProductCategoryResponseDTO;
import com.nguyendinhphuoccao.ecommerce.entity.Customer;
import com.nguyendinhphuoccao.ecommerce.entity.CustomerFavorite;
import com.nguyendinhphuoccao.ecommerce.entity.Product;
import com.nguyendinhphuoccao.ecommerce.exception.ResourceNotFoundException;
import com.nguyendinhphuoccao.ecommerce.repository.CustomerFavoriteRepository;
import com.nguyendinhphuoccao.ecommerce.repository.ProductRepository;
import com.nguyendinhphuoccao.ecommerce.security.CustomUserDetails;
import com.nguyendinhphuoccao.ecommerce.service.CustomerFavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerFavoriteServiceImpl implements CustomerFavoriteService {

    private final CustomerFavoriteRepository customerFavoriteRepository;
    private final ProductRepository productRepository;

    private Customer getCurrentCustomer() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof CustomUserDetails) {
            Customer customer = ((CustomUserDetails) auth.getPrincipal()).getCustomer();
            if (customer != null) {
                return customer;
            }
        }
        throw new AccessDeniedException("Unauthorized: Only Customer Account can perform this action.");
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductCategoryResponseDTO> getFavoriteProducts() {
        Customer customer = getCurrentCustomer();
        return productRepository.findFavoriteProductsByCustomerId(customer.getId());
    }

    @Override
    @Transactional
    public void toggleFavorite(UUID productId) {
        Customer customer = getCurrentCustomer();
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        Optional<CustomerFavorite> existingFavorite = customerFavoriteRepository.findByCustomerIdAndProductId(customer.getId(), productId);
        
        if (existingFavorite.isPresent()) {
            customerFavoriteRepository.delete(existingFavorite.get());
        } else {
            CustomerFavorite newFavorite = new CustomerFavorite();
            newFavorite.setCustomer(customer);
            newFavorite.setProduct(product);
            customerFavoriteRepository.save(newFavorite);
        }
    }
}
