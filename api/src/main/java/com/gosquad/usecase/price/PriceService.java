package com.gosquad.usecase.price;

import com.gosquad.domain.price.PriceEntity;

import java.sql.SQLException;
import java.util.List;

public interface PriceService {
    PriceEntity getPriceById(int id) throws Exception;
    void createPrice(PriceEntity price) throws Exception;
    List<PriceEntity> findByIds(List<Integer> ids) throws Exception;
    void updatePrice(PriceEntity price) throws Exception;
    void deletePrice(int id) throws Exception;
}
