package com.gosquad.presentation.DTO;
import lombok.Data;

@Data
public class CountryRequest {
    private String isoCode;
    private String countryName;
}
