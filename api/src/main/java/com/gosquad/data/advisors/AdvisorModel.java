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
    private Integer compagnyId;
    private String password;

    public AdvisorModel(Integer id, String firstname, String lastname, String email, String phone, Integer compagnyId, String password) {
        super(id);
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phone = phone;
        this.compagnyId = compagnyId;
        this.password = password;
    }

    public AdvisorModel(Integer id, String email, String password, Integer compagnyId) {
        super(id);
        this.email = email;
        this.password = password;
        this.compagnyId = compagnyId;
    }

}
