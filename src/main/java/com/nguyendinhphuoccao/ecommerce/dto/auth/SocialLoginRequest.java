package com.nguyendinhphuoccao.ecommerce.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SocialLoginRequest {
    private String provider;
    private String idToken;
    private String email;
    private String firstName;
    private String lastName;
    private String avatar;
}
