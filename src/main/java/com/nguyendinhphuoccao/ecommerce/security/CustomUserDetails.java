package com.nguyendinhphuoccao.ecommerce.security;

import com.nguyendinhphuoccao.ecommerce.entity.Customer;
import com.nguyendinhphuoccao.ecommerce.entity.StaffAccount;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private Customer customer;
    private StaffAccount staffAccount;

    public CustomUserDetails(Customer customer) {
        this.customer = customer;
    }

    public CustomUserDetails(StaffAccount staffAccount) {
        this.staffAccount = staffAccount;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (staffAccount != null) {
            return Collections.singletonList(new SimpleGrantedAuthority("ROLE_STAFF"));
        }
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_CUSTOMER"));
    }

    @Override
    public String getPassword() {
        if (staffAccount != null) {
            return staffAccount.getPasswordHash();
        }
        return customer != null ? customer.getPasswordHash() : null;
    }

    @Override
    public String getUsername() {
        if (staffAccount != null) {
            return staffAccount.getEmail();
        }
        return customer != null ? customer.getEmail() : null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        if (staffAccount != null) {
            return staffAccount.getActive() != null ? staffAccount.getActive() : true;
        }
        return customer != null ? (customer.getActive() != null ? customer.getActive() : true) : true;
    }

    public Customer getCustomer() {
        return customer;
    }

    public StaffAccount getStaffAccount() {
        return staffAccount;
    }
}
