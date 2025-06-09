package com.gosquad.presentation.DTO;

import lombok.Data;

@Data
public class AuthRequest {
    private String email;
    private String password;
    private String companyCode;
}
