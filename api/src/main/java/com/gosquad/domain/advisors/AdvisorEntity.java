package com.gosquad.domain.advisors;

import com.gosquad.domain.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AdvisorEntity extends Entity {
    private String firstname;
    private String lastname;
    private String email;
    private String phone;

    public AdvisorEntity(Integer id,String firstname, String lastname, String email, String phone) {
        super(id);
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phone = phone;
    }
}
