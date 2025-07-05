package com.gosquad.infrastructure.persistence.price;

import com.gosquad.infrastructure.persistence.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
public class PriceModel extends Model {
    private BigDecimal netPrice;
    private BigDecimal vatRate;
    private BigDecimal vatAmount;
    private BigDecimal grossPrice;

    public PriceModel(Integer id, BigDecimal netPrice, BigDecimal vatRate, BigDecimal vatAmount, BigDecimal grossPrice) {
        super(id);
        this.netPrice = netPrice;
        this.vatRate = vatRate;
        this.vatAmount = vatAmount;
        this.grossPrice = grossPrice;
    }
}
