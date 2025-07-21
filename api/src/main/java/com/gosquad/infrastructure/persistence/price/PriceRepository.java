package com.gosquad.infrastructure.persistence.price;

import java.sql.SQLException;
import java.util.List;

public interface PriceRepository {
    PriceModel getById(int id) throws Exception;
    List<PriceModel> findByIds(List<Integer> ids) throws SQLException;
    void createPrice(PriceModel price) throws Exception;
    void updatePrice(PriceModel price) throws Exception;
    void deletePrice(int id) throws Exception;
}
