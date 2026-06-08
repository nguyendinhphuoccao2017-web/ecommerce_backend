package com.nguyendinhphuoccao.ecommerce.security;

import com.nguyendinhphuoccao.ecommerce.entity.Customer;
import com.nguyendinhphuoccao.ecommerce.entity.StaffAccount;
import com.nguyendinhphuoccao.ecommerce.repository.CustomerRepository;
import com.nguyendinhphuoccao.ecommerce.repository.StaffAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomUserDetailsService implements UserDetailsService {

    private final CustomerRepository customerRepository;
    private final StaffAccountRepository staffAccountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<StaffAccount> staffOpt = staffAccountRepository.findByEmail(username);
        if (staffOpt.isPresent()) {
            return new CustomUserDetails(staffOpt.get());
        }

        Optional<Customer> customerOpt = customerRepository.findByEmail(username);
        if (customerOpt.isPresent()) {
            return new CustomUserDetails(customerOpt.get());
        }
        
        throw new UsernameNotFoundException("User not found with email: " + username);
    }
}
