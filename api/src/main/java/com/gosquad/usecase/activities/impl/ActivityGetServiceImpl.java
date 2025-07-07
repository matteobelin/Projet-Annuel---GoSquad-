package com.gosquad.usecase.activities.impl;
import com.gosquad.domain.activities.ActivityEntity;
import com.gosquad.domain.addresses.AddressEntity;
import com.gosquad.domain.cities.CityEntity;
import com.gosquad.domain.company.CompanyEntity;
import com.gosquad.domain.countries.CountryEntity;
import com.gosquad.domain.price.PriceEntity;
import com.gosquad.presentation.DTO.activities.ActivityResponseDTO;
import com.gosquad.usecase.activities.ActivityGetService;
import com.gosquad.usecase.activities.ActivityService;
import com.gosquad.usecase.addresses.AddressService;
import com.gosquad.usecase.categories.CategoryService;
import com.gosquad.usecase.cities.CityService;
import com.gosquad.usecase.company.utils.GetCompany;
import com.gosquad.usecase.countries.CountryService;
import com.gosquad.usecase.price.PriceService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;


@Service
public class ActivityGetServiceImpl implements ActivityGetService {

    private final ActivityService activityService;
    private final CategoryService categoryService;
    private final PriceService priceService;
    private final GetCompany getCompany;
    private final AddressService addressService;
    private final CityService cityService;
    private final CountryService countryService;

    public ActivityGetServiceImpl(ActivityService activityService, CategoryService categoryService, PriceService priceService, GetCompany getCompany, AddressService addressService, CityService cityService, CountryService countryService) {
        this.activityService = activityService;
        this.categoryService = categoryService;
        this.priceService = priceService;
        this.getCompany = getCompany;
        this.addressService = addressService;
        this.cityService = cityService;
        this.countryService = countryService;
    }


    public List<ActivityResponseDTO> getAllActivities(HttpServletRequest request)throws Exception{
        CompanyEntity company = getCompany.getCompanyFromRequest(request);
        List<ActivityEntity> activities = activityService.getAllByCompanyId(company.getId());

        List<ActivityResponseDTO> result = new ArrayList<>();
        for (ActivityEntity activity : activities) {
            result.add(buildActivityDTO(activity));
        }
        return result;
    }

    public List<ActivityResponseDTO> getAllActivitiesByCategory(HttpServletRequest request) throws Exception{
        CompanyEntity company = getCompany.getCompanyFromRequest(request);
        int categoryId = categoryService.getCategoryByNameAndCompanyId(request.getParameter("categoryName"), company.getId()).getId();
        List<ActivityEntity> activities = activityService.getAllActivitiesByCategory(categoryId);
        List<ActivityResponseDTO> result = new ArrayList<>();
        for (ActivityEntity activity : activities) {
            result.add(buildActivityDTO(activity));
        }
        return result;
    }

    public ActivityResponseDTO getActivityById(HttpServletRequest request)throws Exception{
        int activityId = Integer.parseInt(request.getParameter("id"));
        ActivityEntity activity = activityService.getByIdAndCompanyId(activityId, getCompany.getCompanyFromRequest(request).getId());
        return buildActivityDTO(activity);
    }


    private ActivityResponseDTO buildActivityDTO(ActivityEntity activity) throws Exception {
        PriceEntity price = priceService.getPriceById(activity.getPriceId());
        String categoryName = categoryService.getCategoryById(activity.getCategoryId()).getName();
        AddressEntity address = addressService.getAddressByID(activity.getAddressId());
        CityEntity city = cityService.getCityById(address.getCityId());
        CountryEntity country = countryService.getCountryById(city.getCountryId());

        return new ActivityResponseDTO(
                activity.getId(),
                activity.getName(),
                activity.getDescription(),
                address.getAddressLine(),
                city.getCityName(),
                country.getCountryName(),
                categoryName,
                price.getNetPrice(),
                price.getVatRate(),
                price.getGrossPrice()
        );
    }

}
