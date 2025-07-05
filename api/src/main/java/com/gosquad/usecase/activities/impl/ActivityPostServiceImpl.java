package com.gosquad.usecase.activities.impl;

import com.gosquad.domain.activities.ActivityEntity;
import com.gosquad.domain.addresses.AddressEntity;
import com.gosquad.domain.categories.CategoryEntity;
import com.gosquad.domain.cities.CityEntity;
import com.gosquad.domain.company.CompanyEntity;
import com.gosquad.domain.countries.CountryEntity;
import com.gosquad.domain.price.PriceEntity;
import com.gosquad.presentation.DTO.activities.ActivityRequestDTO;
import com.gosquad.usecase.activities.ActivityPostService;
import com.gosquad.usecase.activities.ActivityService;
import com.gosquad.usecase.addresses.AddressService;
import com.gosquad.usecase.categories.CategoryService;
import com.gosquad.usecase.cities.CityService;
import com.gosquad.usecase.company.utils.GetCompany;
import com.gosquad.usecase.countries.CountryService;
import com.gosquad.usecase.price.PriceService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;


@Service
public class ActivityPostServiceImpl implements ActivityPostService {

    private final ActivityService activityService;
    private final CategoryService categoryService;
    private final PriceService priceService;
    private final GetCompany getCompany;
    private final AddressService addressService;
    private final CityService cityService;
    private final CountryService countryService;

    public ActivityPostServiceImpl(ActivityService activityService, CategoryService categoryService, PriceService priceService, GetCompany getCompany, AddressService addressService, CityService cityService, CountryService countryService) {
        this.activityService = activityService;
        this.categoryService = categoryService;
        this.priceService = priceService;
        this.getCompany = getCompany;
        this.addressService = addressService;
        this.cityService = cityService;
        this.countryService = countryService;
    }

    public void createActivity(HttpServletRequest request, ActivityRequestDTO dto) throws Exception {
        CompanyEntity company = getCompany.getCompanyFromRequest(request);

        CategoryEntity category = categoryService.getCategoryByNameAndCompanyId(dto.categoryName(), company.getId());

        CountryEntity country;
        try {
            country = countryService.getCountryByIsoCode(dto.isoCode());
        } catch (Exception e) {
            country = new CountryEntity(null, dto.countryName(), dto.isoCode());
            countryService.addCountry(country);
            country = countryService.getCountryByIsoCode(dto.isoCode());
        }


        CityEntity city;
        try {
            city = cityService.getCityByNameByPostalCodeByCountry(dto.city(), dto.postalCode(), country.getId());
        } catch (Exception e) {
            city = new CityEntity(null, dto.city(), dto.postalCode(), country.getId());
            cityService.addCity(city);
            city = cityService.getCityByNameByPostalCodeByCountry(dto.city(), dto.postalCode(), country.getId());
        }


        AddressEntity address;
        try {
            address = addressService.getOrCreateAddress(dto.address(), city.getId());
        } catch (Exception e) {
            address = new AddressEntity(null, dto.address(), city.getId());
            addressService.addAddress(address);
            address = addressService.getOrCreateAddress(dto.address(), city.getId());
        }

        PriceEntity price = new PriceEntity(
                null,
                dto.netPrice(),
                dto.vatRate(),
                null,
                null
        );
        priceService.createPrice(price);

        ActivityEntity activity = new ActivityEntity(
                null,
                dto.name(),
                dto.description(),
                address.getId(),
                price.getId(),
                category.getId(),
                company.getId()
        );

        activityService.createActivity(activity);


    }
}
