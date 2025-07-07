package com.gosquad.usecase.price;

import com.gosquad.domain.price.PriceEntity;

public interface PriceService {
    PriceEntity getPriceById(int id) throws Exception;
    void createPrice(PriceEntity price) throws Exception;
    void updatePrice(PriceEntity price) throws Exception;
    void deletePrice(int id) throws Exception;
}
