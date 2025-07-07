package com.gosquad.infrastructure.persistence.price.impl;

import com.gosquad.infrastructure.persistence.Repository;
import com.gosquad.infrastructure.persistence.price.PriceModel;
import com.gosquad.infrastructure.persistence.price.PriceRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@org.springframework.stereotype.Repository
public class PriceRepositoryImpl extends Repository<PriceModel> implements PriceRepository {

    public static final String TABLE_NAME = "price";

    public PriceRepositoryImpl() throws SQLException {
        super(TABLE_NAME);
    }

    protected PriceModel mapResultSetToEntity(ResultSet rs) throws SQLException {
        return new PriceModel(
                rs.getInt("id"),
                rs.getBigDecimal("net_price"),
                rs.getBigDecimal("vat_rate"),
                rs.getBigDecimal("vat_amount"),
                rs.getBigDecimal("gross_price")
        );
    }

    public void createPrice(PriceModel price) throws SQLException {
        Map<String, Object> values = new HashMap<>();
        values.put("net_price", price.getNetPrice());
        values.put("vat_rate", price.getVatRate());
        price.setId(insert(values));
    }

    public void updatePrice(PriceModel price) throws SQLException {
        Map<String, Object> values = new HashMap<>();
        values.put("net_price", price.getNetPrice());
        values.put("vat_rate", price.getVatRate());
        updateBy("id", price.getId(), values);
    }

    public void deletePrice(int id) throws SQLException {
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("id", id);
        deleteBy(conditions);
    }
}
