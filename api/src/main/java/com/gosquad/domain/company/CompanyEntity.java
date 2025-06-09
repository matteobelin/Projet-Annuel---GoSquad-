package com.gosquad.domain.company;
import com.gosquad.domain.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CompanyEntity extends Entity {
    private String code;
    private String name;

    public CompanyEntity(int id,String code, String name) {
        super(id);
        this.code = code;
        this.name = name;
    }
}
