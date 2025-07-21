package com.gosquad.infrastructure.persistence.categories;
import com.gosquad.infrastructure.persistence.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CategoryModel extends Model {
    String name;
    Integer companyId;

    public CategoryModel(Integer id, String name, Integer companyId) {
        super(id);
        this.name = name;
        this.companyId = companyId;
    }
}
