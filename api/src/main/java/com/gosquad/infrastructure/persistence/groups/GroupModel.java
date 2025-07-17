package com.gosquad.infrastructure.persistence.groups;

import com.gosquad.infrastructure.persistence.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class GroupModel extends Model {
    private String name;
    private Boolean visible = true;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer companyId;

    public GroupModel() {}

    public GroupModel(Integer id, String name, Boolean visible, LocalDateTime createdAt, LocalDateTime updatedAt, Integer companyId) {
        super(id);
        this.name = name;
        this.visible = visible;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.companyId = companyId;
    }
}
