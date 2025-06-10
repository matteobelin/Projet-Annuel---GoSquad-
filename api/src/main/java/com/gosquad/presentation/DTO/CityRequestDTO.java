package com.gosquad.presentation.DTO;

import lombok.Data;

@Data
public class CityRequestDTO {
    private String cityName;
    private String postalCode;
    private String isoCode;
}
