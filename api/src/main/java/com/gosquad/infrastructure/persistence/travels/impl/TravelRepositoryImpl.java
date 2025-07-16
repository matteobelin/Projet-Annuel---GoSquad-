package com.gosquad.infrastructure.persistence.travels.impl;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.infrastructure.persistence.travels.TravelModel;
import com.gosquad.infrastructure.persistence.travels.TravelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Repository
public class TravelRepositoryImpl implements TravelRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TravelRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public TravelModel getById(Integer id) throws SQLException, NotFoundException {
        String sql = "SELECT t.id, t.title, t.destination, t.start_date, t.end_date, t.budget, t.description, t.group_id, 1 as company_id " +
                "FROM travel_information t " +
                "WHERE t.id = ?";
        List<TravelModel> results = jdbcTemplate.query(sql, new TravelRowMapper(), id);
        if (results.isEmpty()) {
            throw new NotFoundException("Travel not found with id: " + id);
        }
        return results.get(0);
    }

    @Override
    public List<TravelModel> getAllTravels() throws ConstraintViolationException {
        String sql = "SELECT t.id, t.title, t.destination, t.start_date, t.end_date, t.budget, t.description, t.group_id, 1 as company_id " +
                "FROM travel_information t";
        return jdbcTemplate.query(sql, new TravelRowMapper());
    }

    @Override
    public void addTravel(TravelModel travel) throws SQLException, ConstraintViolationException {
        try {
            String sql = "INSERT INTO travel_information (title, description, start_date, end_date, budget, group_id, destination) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id";
            // Vérification group_id
            String groupSql = "SELECT COUNT(*) FROM groups WHERE id = ?";
            Integer groupExists = jdbcTemplate.queryForObject(groupSql, Integer.class, travel.getGroupId());
            if (groupExists == null || groupExists == 0) {
                throw new ConstraintViolationException("Groupe inexistant (id=" + travel.getGroupId() + ")");
            }
            // Conversion explicite des dates pour compatibilité SQL
            Date startDateSql = travel.getStartDate();
            Date endDateSql = travel.getEndDate();
            Integer id = null;
            try {
                id = jdbcTemplate.queryForObject(
                    sql,
                    Integer.class,
                    travel.getTitle(),
                    travel.getDescription(),
                    startDateSql,
                    endDateSql,
                    travel.getBudget(),
                    travel.getGroupId(),
                    travel.getDestination()
                );
            } catch (Exception ex) {
                throw new ConstraintViolationException("Erreur lors de l'insertion du voyage (vérifie les contraintes et les clés étrangères) : " + ex.getMessage());
            }
            if (id == null) {
                throw new ConstraintViolationException("L'insertion du voyage n'a retourné aucun id (vérifie les contraintes et les clés étrangères)");
            }
            travel.setId(id);
        } catch (ConstraintViolationException e) {
            throw e;
        } catch (Exception e) {
            throw new ConstraintViolationException("Erreur lors de l'insertion du voyage : " + e.getMessage());
        }
    }

    @Override
    public void updateTravel(TravelModel travel) throws SQLException, ConstraintViolationException {
        try {
            String sql = "UPDATE travel_information SET title = ?, description = ?, start_date = ?, end_date = ?, budget = ?, group_id = ?, destination = ? WHERE id = ?";
            int updated = jdbcTemplate.update(
                sql,
                travel.getTitle(),
                travel.getDescription(),
                travel.getStartDate(),
                travel.getEndDate(),
                travel.getBudget(),
                travel.getGroupId(),
                travel.getDestination(),
                travel.getId()
            );
            if (updated == 0) {
                throw new ConstraintViolationException("Aucun voyage mis à jour (id non trouvé)");
            }
        } catch (Exception e) {
            throw new ConstraintViolationException("Erreur lors de la mise à jour du voyage : " + e.getMessage());
        }
    }

    @Override

    public void deleteTravel(Integer id) throws SQLException {
        String sql = "DELETE FROM travel_information WHERE id = ?";
        int deleted = jdbcTemplate.update(sql, id);
        if (deleted == 0) {
            throw new SQLException("Aucun voyage supprimé (id non trouvé)");
        }
    }

    @Override
    public TravelModel save(TravelModel travel) throws SQLException, ConstraintViolationException {
        if (travel.getId() == null) {
            addTravel(travel);
        } else {
            updateTravel(travel);
        }
        return travel;
    }

    private static class TravelRowMapper implements RowMapper<TravelModel> {
        @Override
        public TravelModel mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
            TravelModel travel = new TravelModel();
            travel.setId(rs.getInt("id"));
            travel.setTitle(rs.getString("title"));
            travel.setDestination(rs.getString("destination"));
            travel.setStartDate(rs.getDate("start_date"));
            travel.setEndDate(rs.getDate("end_date"));
            travel.setBudget(rs.getDouble("budget"));
            travel.setDescription(rs.getString("description"));
            travel.setGroupId(rs.getInt("group_id"));
            travel.setCompanyId(rs.getInt("company_id"));
            return travel;
        }
    }
}
