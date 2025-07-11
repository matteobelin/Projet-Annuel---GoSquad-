package com.gosquad.infrastructure.persistence.travels;

import com.gosquad.infrastructure.persistence.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class TravelModel extends Model {
    
    private String title;
    private String destination;
    private Date startDate;
    private Date endDate;
    private Double budget;
    private String description;
    private Integer groupId;
    private Integer companyId;
    
    public TravelModel() {
        super();
    }
    
    public TravelModel(Integer id, String title, String destination, Date startDate, Date endDate, 
                      Double budget, String description, Integer groupId, Integer companyId) {
        super(id);
        this.title = title;
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.budget = budget;
        this.description = description;
        this.groupId = groupId;
        this.companyId = companyId;
    }
}
