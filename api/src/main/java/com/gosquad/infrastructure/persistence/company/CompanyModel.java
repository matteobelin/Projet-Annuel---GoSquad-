package com.gosquad.infrastructure.persistence.company;

import com.gosquad.infrastructure.persistence.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CompanyModel extends Model {
    private String code;
    private String name;

    public CompanyModel(Integer id,String code, String name) {
        super(id);
        this.code = code;
        this.name = name;
    }
}
