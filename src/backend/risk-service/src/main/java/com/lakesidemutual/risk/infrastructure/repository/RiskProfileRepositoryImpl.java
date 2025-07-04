package com.lakesidemutual.risk.infrastructure.repository;

import com.lakesidemutual.risk.domain.model.RiskProfile;
import com.lakesidemutual.risk.domain.model.RiskProfileType;
import com.lakesidemutual.risk.domain.repository.RiskProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the domain RiskProfileRepository using Spring Data JPA.
 */
@Component
public class RiskProfileRepositoryImpl implements RiskProfileRepository {
    
    private final JpaRiskProfileRepository jpaRepository;
    
    @Autowired
    public RiskProfileRepositoryImpl(JpaRiskProfileRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }
    
    @Override
    public RiskProfile save(RiskProfile riskProfile) {
        return jpaRepository.save(riskProfile);
    }
    
    @Override
    public Optional<RiskProfile> findById(String profileId) {
        return jpaRepository.findById(profileId);
    }
    
    @Override
    public List<RiskProfile> findByCustomerId(String customerId) {
        return jpaRepository.findByCustomerId(customerId);
    }
    
    @Override
    public Optional<RiskProfile> findByCustomerIdAndProfileType(String customerId, RiskProfileType profileType) {
        return jpaRepository.findByCustomerIdAndProfileType(customerId, profileType);
    }
    
    @Override
    public List<RiskProfile> findByProfileType(RiskProfileType profileType) {
        return jpaRepository.findByProfileType(profileType);
    }
    
    @Override
    public List<RiskProfile> findHighRiskProfiles() {
        return jpaRepository.findHighRiskProfiles();
    }
    
    @Override
    public void delete(RiskProfile riskProfile) {
        jpaRepository.delete(riskProfile);
    }
    
    @Override
    public boolean existsByCustomerIdAndProfileType(String customerId, RiskProfileType profileType) {
        return jpaRepository.existsByCustomerIdAndProfileType(customerId, profileType);
    }
    
    @Override
    public List<RiskProfile> findAll() {
        return jpaRepository.findAll();
    }
}
