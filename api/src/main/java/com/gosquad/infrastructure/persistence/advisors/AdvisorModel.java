package com.gosquad.infrastructure.persistence.advisors;

import com.gosquad.infrastructure.persistence.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AdvisorModel extends Model {
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private Integer compagnyId;
    private String password;
    private String role;

    public AdvisorModel(Integer id, String firstname, String lastname, String email, String phone, Integer compagnyId, String password, String role) {
        super(id);
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phone = phone;
        this.compagnyId = compagnyId;
        this.password = password;
        this.role = role;
    }

    public AdvisorModel(Integer id, String email, String password, Integer compagnyId,String role) {
        super(id);
        this.email = email;
        this.password = password;
        this.compagnyId = compagnyId;
        this.role = role;
    }


}
