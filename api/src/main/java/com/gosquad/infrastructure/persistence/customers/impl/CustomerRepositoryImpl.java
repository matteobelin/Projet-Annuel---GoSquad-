package com.gosquad.infrastructure.persistence.customers.impl;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.infrastructure.persistence.Repository;
import com.gosquad.infrastructure.persistence.customers.CustomerModel;
import com.gosquad.infrastructure.persistence.customers.CustomerRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@org.springframework.stereotype.Repository
public class CustomerRepositoryImpl extends Repository<CustomerModel> implements CustomerRepository{

    public static final String TABLE_NAME = "customer";

    public CustomerRepositoryImpl() throws SQLException {
        super(TABLE_NAME);
    }

    protected CustomerModel mapResultSetToEntity(ResultSet rs) throws SQLException {
        return new CustomerModel(
                rs.getInt("id"),
                rs.getString("firstname"),
                rs.getString("lastname"),
                rs.getString("email"),
                rs.getString("phone_number"),
                rs.getDate("birth_date"),
                rs.getString("id_card_number"),
                rs.getDate("id_card_expiration_date"),
                rs.getString("id_card_copy_url"),
                rs.getString("passport_number"),
                rs.getDate("passport_expiration_date"),
                rs.getString("passport_copy_url"),
                rs.getInt("country_id"),
                rs.getInt("address_id"),
                rs.getInt("billing_address_id"),
                rs.getInt("company_id")
        );
    }

    @Override
    public CustomerModel getByIdAndCompanyId(int id,int companyId) throws SQLException, ConstraintViolationException {
        try{
            Map<String, Object> conditions = new HashMap<>();
            conditions.put("id", id);
            conditions.put("company_id", companyId);
            return findByMultiple(conditions);
        }catch(NotFoundException e){
            throw new ConstraintViolationException(e);
        }
    }

    @Override
    public List<CustomerModel> getAllCustomers(int companyId) throws ConstraintViolationException {
        try {
            return findAllBy("company_id", companyId);
        }catch(SQLException e){
            throw new ConstraintViolationException(e);
        }
    }

    @Override
    public void addCustomer(CustomerModel customer) throws ConstraintViolationException {
        try{
            Map<String, Object> conditions = new HashMap<>();
            conditions.put("firstname", customer.getFirstname());
            conditions.put("lastname", customer.getLastname());
            conditions.put("email", customer.getEmail());
            conditions.put("phone_number", customer.getPhoneNumber());
            conditions.put("birth_date", customer.getBirthDate());
            conditions.put("id_card_number", customer.getIdCardNumber());
            conditions.put("id_card_expiration_date", customer.getIdCardExpirationDate());
            conditions.put("id_card_copy_url", customer.getIdCardCopyUrl());
            conditions.put("passport_number", customer.getPassportNumber());
            conditions.put("passport_expiration_date", customer.getPassportExpirationDate());
            conditions.put("passport_copy_url", customer.getPassportCopyUrl());
            conditions.put("country_id", customer.getCountryId());
            conditions.put("address_id", customer.getAddressId());
            conditions.put("billing_address_id", customer.getBillingAddressId());
            conditions.put("company_id", customer.getCompanyId());
            customer.setId(insert(conditions));
        }catch(SQLException e){
            throw new ConstraintViolationException(e);
        }
    }

    @Override
    public void updateCustomerPassport(CustomerModel customer) throws ConstraintViolationException {
        try{
            Map<String, Object> updates = new HashMap<>();
            updates.put("passport_number", customer.getPassportNumber());
            updates.put("passport_expiration_date", customer.getPassportExpirationDate());
            updates.put("passport_copy_url", customer.getPassportCopyUrl());

            updateBy("id",customer.getId(),updates);
        } catch (SQLException e) {
            throw new ConstraintViolationException(e);
        }
    }

    public void updateCustomerIdCard(CustomerModel customer) throws ConstraintViolationException{
        try{
            Map<String, Object> updates = new HashMap<>();
            updates.put("id_card_number", customer.getIdCardNumber());
            updates.put("id_card_expiration_date", customer.getIdCardExpirationDate());
            updates.put("id_card_copy_url", customer.getIdCardCopyUrl());

            updateBy("id",customer.getId(),updates);
        }catch (SQLException e) {
            throw new ConstraintViolationException(e);
        }
    }

    public void updateEmail(CustomerModel customer) throws ConstraintViolationException{
        try {
            Map<String, Object> updates = new HashMap<>();
            updates.put("email", customer.getEmail());

            updateBy("id",customer.getId(),updates);
        }catch (SQLException e) {
            throw new ConstraintViolationException(e);
        }
    }

    public void updatePhone(CustomerModel customer) throws ConstraintViolationException{
        try {
            Map<String, Object> updates = new HashMap<>();
            updates.put("phone_number", customer.getPhoneNumber());

            updateBy("id",customer.getId(),updates);
        }catch (SQLException e) {
            throw new ConstraintViolationException(e);
        }
    }

}
