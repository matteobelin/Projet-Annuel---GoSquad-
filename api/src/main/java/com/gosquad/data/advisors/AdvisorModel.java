package com.gosquad.data.advisors;

import com.gosquad.data.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AdvisorModel extends Model {
    private String firstname;
    private String lastname;
    private String email;
    private String phone;

    public AdvisorModel(Integer id,String firstname, String lastname, String email, String phone) {
        super(id);
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phone = phone;
    }
}
