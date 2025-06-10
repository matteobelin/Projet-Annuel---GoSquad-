package com.gosquad.presentation.DTO;

import lombok.Data;

@Data
public class AddressRequestDTO {
    private String address_line;
    private String cityName;
    private String postalCode;
    private String isoCode;
}
