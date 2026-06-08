package com.nguyendinhphuoccao.ecommerce.repository;

import com.nguyendinhphuoccao.ecommerce.entity.StaffAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

import java.util.Optional;

@Repository
public interface StaffAccountRepository extends JpaRepository<StaffAccount, UUID> {
    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = {"role"})
    Optional<StaffAccount> findByEmail(String email);
}
