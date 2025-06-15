package com.gosquad.presentation.DTO;

public record CityRequestDTO (
    String cityName,
    String postalCode,
    String isoCode
){}
