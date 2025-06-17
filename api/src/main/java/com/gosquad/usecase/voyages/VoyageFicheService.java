package com.gosquad.usecase.voyages;

import com.gosquad.core.exceptions.NotFoundException;

import java.sql.SQLException;

public interface VoyageFicheService {
    byte[] generateFichePDF(int voyageId) throws SQLException, NotFoundException;
    String generateFicheHTML(int voyageId) throws SQLException, NotFoundException;
}