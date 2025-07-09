package com.gosquad.presentation.DTO.activities;

import java.math.BigDecimal;

public record ActivityResponseDTO(
        int id,
        String name,
        String description,
        String address,
        String city,
        String country,
        String postalCode,
        String isoCode,
        String categoryName,
        BigDecimal netPrice,
        BigDecimal vatRate,
        BigDecimal gross_price
) {
}
