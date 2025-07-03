package com.gosquad.infrastructure.persistence.categories;
import com.gosquad.infrastructure.persistence.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CategoryModel extends Model {
    String name;

    public CategoryModel(Integer id, String name) {
        super(id);
        this.name = name;
    }
}
