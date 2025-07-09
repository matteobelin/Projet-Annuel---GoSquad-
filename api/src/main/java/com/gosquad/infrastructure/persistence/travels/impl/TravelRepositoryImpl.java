package com.gosquad.infrastructure.persistence.travels.impl;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.infrastructure.persistence.travels.TravelModel;
import com.gosquad.infrastructure.persistence.travels.TravelRepository;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class TravelRepositoryImpl implements TravelRepository {

    private final ConcurrentHashMap<Integer, TravelModel> travels = new ConcurrentHashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);

    @Override
    public TravelModel getById(Integer id) throws SQLException, NotFoundException {
        TravelModel travel = travels.get(id);
        if (travel == null) {
            throw new NotFoundException("Travel not found with id: " + id);
        }
        return travel;
    }

    @Override
    public List<TravelModel> getAllTravels() throws ConstraintViolationException {
        return new ArrayList<>(travels.values());
    }

    @Override
    public void addTravel(TravelModel travel) throws SQLException, ConstraintViolationException {
        if (travel.getId() == null) {
            travel.setId(idGenerator.getAndIncrement());
        }
        travels.put(travel.getId(), travel);
    }

    @Override
    public void updateTravel(TravelModel travel) throws SQLException, ConstraintViolationException {
        if (travel.getId() == null) {
            throw new ConstraintViolationException("Cannot update travel without ID");
        }
        if (!travels.containsKey(travel.getId())) {
            throw new ConstraintViolationException("Failed to update travel with id: " + travel.getId());
        }
        travels.put(travel.getId(), travel);
    }

    @Override
    public void deleteTravel(Integer id) throws SQLException {
        travels.remove(id);
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
}
