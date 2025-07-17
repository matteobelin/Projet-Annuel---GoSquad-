package com.gosquad.usecase.groups.impl;
import com.gosquad.domain.groups.GroupEntity;
import com.gosquad.infrastructure.persistence.groups.GroupModel;
import com.gosquad.infrastructure.persistence.groups.GroupRepository;
import com.gosquad.usecase.groups.GroupMapper;
import com.gosquad.usecase.groups.GroupService;
import com.gosquad.usecase.company.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;
    private final CompanyService companyService;
    private final com.gosquad.infrastructure.jwt.JWTInterceptor jwtInterceptor;

    @Autowired
    public GroupServiceImpl(GroupRepository groupRepository, GroupMapper groupMapper, CompanyService companyService, com.gosquad.infrastructure.jwt.JWTInterceptor jwtInterceptor) {
        this.groupRepository = groupRepository;
        this.groupMapper = groupMapper;
        this.companyService = companyService;
        this.jwtInterceptor = jwtInterceptor;
    }

    @Override
    public GroupEntity getGroupByName(String name) {
        try {
            return groupMapper.modelToEntity(groupRepository.getByName(name));
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public GroupEntity getGroupById(int id) {
        try {
            return groupMapper.modelToEntity(groupRepository.getById(id));
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<GroupEntity> getAllGroups(jakarta.servlet.http.HttpServletRequest request) {
        try {
            String authHeader = request.getHeader("Authorization");
            String token = authHeader.substring(7);
            java.util.Map<String, Object> tokenInfo = jwtInterceptor.extractTokenInfo(token);
            String companyCode = tokenInfo.get("companyCode").toString();
            int companyId = companyService.getCompanyByCode(companyCode).getId();
            return groupMapper.modelToEntity(groupRepository.getAllGroups(companyId));
        } catch (Exception e) {
            return List.of();
        }
    }

    @Override
    public void addGroup(GroupEntity group) {
        try {
            // companyId doit déjà être renseigné dans l'entité (par le contrôleur)
            GroupModel groupModel = groupMapper.entityToModel(group);
            groupRepository.addGroup(groupModel);
            group.setId(groupModel.getId());
            group.setCreatedAt(groupModel.getCreatedAt());
            group.setUpdatedAt(groupModel.getUpdatedAt());
        } catch (Exception ignored) {}
    }

    @Override
    public void updateGroup(GroupEntity group) {
        try {
            GroupModel groupModel = groupMapper.entityToModel(group);
            groupRepository.updateGroup(groupModel);
            group.setUpdatedAt(groupModel.getUpdatedAt());
        } catch (Exception ignored) {}
    }

    @Override
    public void deleteGroup(int id) throws SQLException {
        groupRepository.deleteGroup(id);
    }
}
