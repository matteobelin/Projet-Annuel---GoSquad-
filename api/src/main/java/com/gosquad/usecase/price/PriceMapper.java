package com.gosquad.usecase.price;

import com.gosquad.domain.price.PriceEntity;
import com.gosquad.infrastructure.persistence.price.PriceModel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PriceMapper {
    public PriceEntity modelToEntity(PriceModel priceModel) {
        return new PriceEntity(
                priceModel.getId(),
                priceModel.getNetPrice(),
                priceModel.getVatRate(),
                priceModel.getVatAmount(),
                priceModel.getGrossPrice()
        );
    }

    public PriceModel entityToModel(PriceEntity priceEntity) {
        return new PriceModel(
                priceEntity.getId(),
                priceEntity.getNetPrice(),
                priceEntity.getVatRate(),
                priceEntity.getVatAmount(),
                priceEntity.getGrossPrice()
        );
    }

    public List<PriceEntity> modelsToEntities(List<PriceModel> byIds) {
        return byIds.stream()
                .map(this::modelToEntity)
                .toList();
    }
}
