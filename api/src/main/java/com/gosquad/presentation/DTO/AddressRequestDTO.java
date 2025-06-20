package com.gosquad.presentation.DTO;


public record AddressRequestDTO (
    String address_line,
    String cityName,
    String postalCode,
    String isoCode
    )
{}
