package com.gosquad.infrastructure.persistence.price;

public interface PriceRepository {
    PriceModel getById(int id) throws Exception;
    void createPrice(PriceModel price) throws Exception;
    void updatePrice(PriceModel price) throws Exception;
    void deletePrice(int id) throws Exception;
}
