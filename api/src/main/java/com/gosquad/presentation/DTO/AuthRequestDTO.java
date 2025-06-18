package com.gosquad.presentation.DTO;

public record AuthRequestDTO (
    String email,
    String password,
    String companyCode
)
{}
