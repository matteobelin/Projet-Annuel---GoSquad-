package com.gosquad.usecase.price.impl;

import com.gosquad.domain.price.PriceEntity;
import com.gosquad.infrastructure.persistence.price.PriceRepository;
import com.gosquad.usecase.price.PriceMapper;
import com.gosquad.usecase.price.PriceService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PriceServiceImpl implements PriceService {

    private final PriceRepository priceRepository;
    private final PriceMapper priceMapper;
    private static final BigDecimal MAX_VAT_RATE = new BigDecimal("50");

    public PriceServiceImpl(PriceRepository priceRepository, PriceMapper priceMapper) {
        this.priceRepository = priceRepository;
        this.priceMapper = priceMapper;
    }
    public PriceEntity getPriceById(int id) throws Exception{
        return priceMapper.modelToEntity(priceRepository.getById(id));
    }

    public void createPrice(PriceEntity price) throws Exception{
        if(price.getVatRate().compareTo(MAX_VAT_RATE) > 0){
            throw new Exception("VAT rate cannot be greater than 50%");
        }else{
            priceRepository.createPrice(priceMapper.entityToModel(price));
        }
    }

    public void updatePrice(PriceEntity price) throws Exception{
        if(price.getVatRate().compareTo(MAX_VAT_RATE) > 0){
            throw new Exception("VAT rate cannot be greater than 50%");
        }else{
            priceRepository.updatePrice(priceMapper.entityToModel(price));
        }
    }

    public void deletePrice(int id) throws Exception{
        priceRepository.deletePrice(id);
    }

}
