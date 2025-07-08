package com.gosquad.usecase.activities.impl;
import com.gosquad.domain.activities.ActivityEntity;
import com.gosquad.domain.addresses.AddressEntity;
import com.gosquad.domain.categories.CategoryEntity;
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
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


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

        return getActivitiesByIds(activities);
    }

    public List<ActivityResponseDTO> getAllActivitiesByCategory(HttpServletRequest request) throws Exception{
        CompanyEntity company = getCompany.getCompanyFromRequest(request);
        int categoryId = categoryService.getCategoryByNameAndCompanyId(request.getParameter("categoryName"), company.getId()).getId();
        List<ActivityEntity> activities = activityService.getAllActivitiesByCategory(categoryId);
        return getActivitiesByIds(activities);
    }

    public ActivityResponseDTO getActivityById(HttpServletRequest request)throws Exception{
        int activityId = Integer.parseInt(request.getParameter("id"));
        ActivityEntity activity = activityService.getByIdAndCompanyId(activityId, getCompany.getCompanyFromRequest(request).getId());
        return buildActivityDTO(activity);
    }

    private List<ActivityResponseDTO> getActivitiesByIds(List<ActivityEntity> activities) throws Exception {
        Set<Integer> priceIds = activities.stream().map(ActivityEntity::getPriceId).collect(Collectors.toSet());
        Set<Integer> categoryIds = activities.stream().map(ActivityEntity::getCategoryId).collect(Collectors.toSet());
        Set<Integer> addressIds = activities.stream().map(ActivityEntity::getAddressId).collect(Collectors.toSet());

        List<Integer> priceIdsList = new ArrayList<>(priceIds);
        List<PriceEntity> priceList = priceService.findByIds(priceIdsList);
        Map<Integer, PriceEntity> prices = priceList.stream()
                .collect(Collectors.toMap(PriceEntity::getId, price -> price));

        List<Integer> categoryIdsList = new ArrayList<>(categoryIds);
        List<CategoryEntity> categoryList = categoryService.findByIds(categoryIdsList);
        Map<Integer, CategoryEntity> categories = categoryList.stream()
                .collect(Collectors.toMap(CategoryEntity::getId, category -> category));

        List<Integer> addressIdsList = new ArrayList<>(addressIds);
        List<AddressEntity> addressList = addressService.findByIds(addressIdsList);
        Map<Integer, AddressEntity> addresses = addressList.stream()
                .collect(Collectors.toMap(AddressEntity::getId, address -> address));


        Set<Integer> cityIds = addresses.values().stream().map(AddressEntity::getCityId).collect(Collectors.toSet());
        List<CityEntity> cityList = cityService.findByIds(new ArrayList<>(cityIds));
        Map<Integer, CityEntity> cities = cityList.stream()
                .collect(Collectors.toMap(CityEntity::getId, city -> city));

        Set<Integer> countryIds = cities.values().stream().map(CityEntity::getCountryId).collect(Collectors.toSet());
        List<CountryEntity> countryList = countryService.findByIds(new ArrayList<>(countryIds));
        Map<Integer, CountryEntity> countries = countryList.stream()
                .collect(Collectors.toMap(CountryEntity::getId, country -> country));

        List<ActivityResponseDTO> result = new ArrayList<>();
        for (ActivityEntity activity : activities) {
            AddressEntity address = addresses.get(activity.getAddressId());
            CityEntity city = cities.get(address.getCityId());
            CountryEntity country = countries.get(city.getCountryId());
            CategoryEntity category = categories.get(activity.getCategoryId());
            PriceEntity price = prices.get(activity.getPriceId());

            result.add(new ActivityResponseDTO(
                    activity.getId(),
                    activity.getName(),
                    activity.getDescription(),
                    address.getAddressLine(),
                    city.getCityName(),
                    country.getCountryName(),
                    category.getName(),
                    price.getNetPrice(),
                    price.getVatRate(),
                    price.getGrossPrice()
            ));
        }
        return result;
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
