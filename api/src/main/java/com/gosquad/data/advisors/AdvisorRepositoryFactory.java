package com.gosquad.data.advisors;

import com.gosquad.data.advisors.impl.AdvisorRepositoryImpl;

import java.sql.SQLException;

public class AdvisorRepositoryFactory {
    public static AdvisorRepository makeAdvisorRepository() throws SQLException {
        return new AdvisorRepositoryImpl();
    }
}
