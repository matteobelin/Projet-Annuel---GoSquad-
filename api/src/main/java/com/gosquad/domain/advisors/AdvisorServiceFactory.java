package com.gosquad.domain.advisors;

import com.gosquad.data.advisors.AdvisorRepository;
import com.gosquad.data.advisors.AdvisorRepositoryFactory;
import com.gosquad.domain.advisors.impl.AdvisorServiceImpl;

import java.sql.SQLException;

public class AdvisorServiceFactory {
    private static AdvisorService advisorService;

    public AdvisorService getAdvisorService() throws SQLException {
        if (advisorService == null) {
            advisorService = makeAdvisorService();
        }
        return advisorService;
    }

    private AdvisorService makeAdvisorService() throws SQLException {
        AdvisorRepository advisorRepository = AdvisorRepositoryFactory.makeAdvisorRepository();
        AdvisorMapper advisorMapper = new AdvisorMapper();
        return new AdvisorServiceImpl(advisorRepository, advisorMapper);
    }
}
