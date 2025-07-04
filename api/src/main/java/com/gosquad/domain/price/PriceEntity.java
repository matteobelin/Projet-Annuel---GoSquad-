package com.gosquad.domain.price;

import com.gosquad.domain.Entity;
import lombok.Data;

import java.math.BigDecimal;


@Data
public class PriceEntity extends Entity {
    private BigDecimal netPrice;
    private BigDecimal vatRate;
    private BigDecimal vatAmount;
    private BigDecimal grossPrice;

    public PriceEntity(Integer id, BigDecimal netPrice, BigDecimal vatRate, BigDecimal vatAmount, BigDecimal grossPrice) {
        super(id);
        this.netPrice = netPrice;
        this.vatRate = vatRate;
    }
}
