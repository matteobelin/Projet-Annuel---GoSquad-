package com.gosquad.presentation.DTO.activities;

import java.math.BigDecimal;

public record ActivityRequestDTO(
        int id,
        String name,
        String description,
        String address,
        String city,
        String postalCode,
        String isoCode,
        String countryName,
        String categoryName,
        BigDecimal netPrice,
        BigDecimal vatRate
) {
}
