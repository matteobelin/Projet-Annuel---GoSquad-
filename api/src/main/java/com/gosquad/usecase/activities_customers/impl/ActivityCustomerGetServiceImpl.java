package com.gosquad.usecase.activities_customers.impl;

import com.gosquad.domain.activities.ActivityEntity;
import com.gosquad.domain.activities_customers.ActivityCustomerEntity;
import com.gosquad.domain.addresses.AddressEntity;
import com.gosquad.domain.cities.CityEntity;
import com.gosquad.domain.company.CompanyEntity;
import com.gosquad.domain.countries.CountryEntity;
import com.gosquad.domain.price.PriceEntity;
import com.gosquad.presentation.DTO.activities_customers.ActivityCustomerResponseDTO;
import com.gosquad.usecase.activities.ActivityService;
import com.gosquad.usecase.activities_customers.ActivityCustomerGetService;
import com.gosquad.usecase.activities_customers.ActivityCustomerService;
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
public class ActivityCustomerGetServiceImpl implements ActivityCustomerGetService {

    private final ActivityCustomerService activityCustomerService;
    private final GetCompany getCompany;
    private final ActivityService activityService;
    private final CategoryService categoryService;
    private final PriceService priceService;
    private final AddressService addressService;
    private final CityService cityService;
    private final CountryService countryService;

    public ActivityCustomerGetServiceImpl(ActivityCustomerService activityCustomerService, GetCompany getCompany, ActivityService activityService, CategoryService categoryService, PriceService priceService, AddressService addressService, CityService cityService, CountryService countryService) {
        this.activityCustomerService = activityCustomerService;
        this.getCompany = getCompany;
        this.activityService = activityService;
        this.categoryService = categoryService;
        this.priceService = priceService;
        this.addressService = addressService;
        this.cityService = cityService;
        this.countryService = countryService;
    }


    public List<ActivityCustomerResponseDTO> getActivitiesByGroupIdWhereParticipation(HttpServletRequest request) throws Exception{
        CompanyEntity company = getCompany.getCompanyFromRequest(request);
        int groupId = Integer.parseInt(request.getParameter("groupeId"));
        List<ActivityCustomerEntity> activityCustomers = activityCustomerService.getActivitiesByGroupIdWhereParticipation(groupId, true);
        List<ActivityCustomerResponseDTO> result = new ArrayList<>();
        for (ActivityCustomerEntity activityCustomerEntity : activityCustomers) {
            result.add(buildActivityDTO(company, activityCustomerEntity));
        }
        return result;
    }

    public List<ActivityCustomerResponseDTO> getActivityByCustomerId(HttpServletRequest request) throws Exception{
        CompanyEntity company = getCompany.getCompanyFromRequest(request);
        int customerId = getCompany.GetCustomerIdByCompany(request.getParameter("customerId"), company);

        List<ActivityCustomerEntity> activityCustomers = activityCustomerService.getActivityByCustomerId(customerId);
        List<ActivityCustomerResponseDTO> result = new ArrayList<>();
        for (ActivityCustomerEntity activityCustomerEntity : activityCustomers) {
            result.add(buildActivityDTO(company, activityCustomerEntity));
        }
        return result;
    }

    public List<ActivityCustomerResponseDTO> getActivityByCustomerWhereParticipation(HttpServletRequest request) throws Exception{
        CompanyEntity company = getCompany.getCompanyFromRequest(request);
        int customerId = getCompany.GetCustomerIdByCompany(request.getParameter("customerId"), company);
        int groupId = Integer.parseInt(request.getParameter("groupeId"));

        List<ActivityCustomerEntity> activityCustomers = activityCustomerService.getActivityByCustomerWhereParticipation(customerId, true, groupId);
        List<ActivityCustomerResponseDTO> result = new ArrayList<>();
        for (ActivityCustomerEntity activityCustomerEntity : activityCustomers) {
            result.add(buildActivityDTO(company, activityCustomerEntity));
        }
        return result;
    }


    public ActivityCustomerResponseDTO getActivityById(HttpServletRequest request) throws Exception{
        CompanyEntity company = getCompany.getCompanyFromRequest(request);
        int activityCustomerId = Integer.parseInt(request.getParameter("activityId"));
        int customerId = getCompany.GetCustomerIdByCompany(request.getParameter("customerId"), company);
        int groupId = Integer.parseInt(request.getParameter("groupeId"));
        ActivityCustomerEntity activityCustomerEntity = activityCustomerService.getActivityById(activityCustomerId, customerId, groupId);

        return buildActivityDTO(company, activityCustomerEntity);
    }

    public List<ActivityCustomerResponseDTO> getActivityByCustomerIdAndGroupIdWhereParticipation(HttpServletRequest request) throws Exception{
        CompanyEntity company = getCompany.getCompanyFromRequest(request);
        int customerId = getCompany.GetCustomerIdByCompany(request.getParameter("customerId"), company);
        int groupId = Integer.parseInt(request.getParameter("groupeId"));

        List<ActivityCustomerEntity> activityCustomers = activityCustomerService.getActivityByCustomerIdAndGroupIdWhereParticipation(customerId, groupId, true);
        List<ActivityCustomerResponseDTO> result = new ArrayList<>();
        for (ActivityCustomerEntity activityCustomerEntity : activityCustomers) {
            result.add(buildActivityDTO(company, activityCustomerEntity));
        }
        return result;
    }

    public List<ActivityCustomerResponseDTO> getCustomersByActivityIdAndGroupId(HttpServletRequest request) throws Exception{
        CompanyEntity company = getCompany.getCompanyFromRequest(request);
        int activityId = Integer.parseInt(request.getParameter("activityId"));
        int groupId = Integer.parseInt(request.getParameter("groupeId"));

        List<ActivityCustomerEntity> activityCustomers = activityCustomerService.getCustomersByActivityIdAndGroupId(activityId, groupId);
        List<ActivityCustomerResponseDTO> result = new ArrayList<>();
        for (ActivityCustomerEntity activityCustomerEntity : activityCustomers) {
            result.add(buildActivityDTO(company, activityCustomerEntity));
        }
        return result;
    }




    private ActivityCustomerResponseDTO buildActivityDTO(CompanyEntity company, ActivityCustomerEntity activityCustomerEntity) throws Exception {
        ActivityEntity activity = activityService.getByIdAndCompanyId(activityCustomerEntity.getActivityId(), company.getId());
        PriceEntity price = priceService.getPriceById(activity.getPriceId());
        String categoryName = categoryService.getCategoryById(activity.getCategoryId()).getName();
        AddressEntity address = addressService.getAddressByID(activity.getAddressId());
        CityEntity city = cityService.getCityById(address.getCityId());
        CountryEntity country = countryService.getCountryById(city.getCountryId());

        return new ActivityCustomerResponseDTO(
                company.getCode()+activityCustomerEntity.getCustomerId(),
                activityCustomerEntity.getParticipation(),
                activityCustomerEntity.getStartDate(),
                activityCustomerEntity.getEndDate(),
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
