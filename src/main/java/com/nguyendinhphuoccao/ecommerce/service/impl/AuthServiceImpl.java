package com.nguyendinhphuoccao.ecommerce.service.impl;

import com.nguyendinhphuoccao.ecommerce.dto.auth.*;
import com.nguyendinhphuoccao.ecommerce.entity.Customer;
import com.nguyendinhphuoccao.ecommerce.repository.CustomerRepository;
import com.nguyendinhphuoccao.ecommerce.security.CustomUserDetails;
import com.nguyendinhphuoccao.ecommerce.security.CustomUserDetailsService;
import com.nguyendinhphuoccao.ecommerce.security.JwtService;
import com.nguyendinhphuoccao.ecommerce.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final CustomerRepository customerRepository;
    private final com.nguyendinhphuoccao.ecommerce.repository.StaffAccountRepository staffAccountRepository;
    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final JavaMailSender mailSender;

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (customerRepository.findByEmail(request.getEmail()).isPresent()) {
            return AuthResponse.builder().error("Email already exists").build();
        }

        Customer customer = Customer.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .active(true)
                .registeredAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();

        customerRepository.save(customer);

        String jwtToken = jwtService.generateToken(new CustomUserDetails(customer));
        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Override
    public AuthResponse registerStaff(RegisterRequest request) {
        if (staffAccountRepository.findByEmail(request.getEmail()).isPresent()) {
            return AuthResponse.builder().error("Staff Email already exists").build();
        }

        com.nguyendinhphuoccao.ecommerce.entity.StaffAccount staff = com.nguyendinhphuoccao.ecommerce.entity.StaffAccount.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .active(true)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();

        staffAccountRepository.save(staff);

        String jwtToken = jwtService.generateToken(new CustomUserDetails(staff));
        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (Exception e) {
            return AuthResponse.builder().error("Doesn’t exist this email. Please Register!").build();
        }

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(request.getEmail());
        
        String jwtToken = jwtService.generateToken(userDetails);
        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Override
    public AuthResponse socialLogin(SocialLoginRequest request) {
        Optional<Customer> existingCustomer = customerRepository.findByEmail(request.getEmail());
        Customer customer;
        
        if (existingCustomer.isPresent()) {
            customer = existingCustomer.get();
        } else {
            // Create new customer if not exists
            customer = Customer.builder()
                    .firstName(request.getFirstName() != null ? request.getFirstName() : "")
                    .lastName(request.getLastName() != null ? request.getLastName() : "")
                    .email(request.getEmail())
                    .passwordHash(passwordEncoder.encode("SOCIAL_LOGIN_SECRET")) // random or strong dummy password
                    .active(true)
                    .registeredAt(OffsetDateTime.now())
                    .updatedAt(OffsetDateTime.now())
                    .build();
            customer = customerRepository.save(customer);
        }

        String jwtToken = jwtService.generateToken(new CustomUserDetails(customer));
        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Override
    public void forgotPassword(ForgotPasswordRequest request) {
        Optional<Customer> customerOpt = customerRepository.findByEmail(request.getEmail());
        if (customerOpt.isEmpty()) {
            // Don't leak if email exists or not
            return;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(request.getEmail());
        message.setSubject("Reset your password");
        message.setText("Click the link to reset your password: http://localhost:3000/reset-password"); // dummy link
        
        mailSender.send(message);
    }
}
