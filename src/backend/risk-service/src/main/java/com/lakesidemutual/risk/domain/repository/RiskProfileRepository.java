package com.lakesidemutual.risk.domain.repository;

import com.lakesidemutual.risk.domain.model.RiskProfile;
import com.lakesidemutual.risk.domain.model.RiskProfileType;

import java.util.List;
import java.util.Optional;

/**
 * Domain repository interface for RiskProfile aggregate.
 * This interface defines the contract for persistence operations
 * without exposing infrastructure details.
 */
public interface RiskProfileRepository {
    
    /**
     * Save a risk profile
     */
    RiskProfile save(RiskProfile riskProfile);
    
    /**
     * Find a risk profile by its ID
     */
    Optional<RiskProfile> findById(String profileId);
    
    /**
     * Find risk profiles by customer ID
     */
    List<RiskProfile> findByCustomerId(String customerId);
    
    /**
     * Find risk profiles by customer ID and profile type
     */
    Optional<RiskProfile> findByCustomerIdAndProfileType(String customerId, RiskProfileType profileType);
    
    /**
     * Find all risk profiles of a specific type
     */
    List<RiskProfile> findByProfileType(RiskProfileType profileType);
    
    /**
     * Find high-risk profiles (for monitoring and review)
     */
    List<RiskProfile> findHighRiskProfiles();
    
    /**
     * Delete a risk profile
     */
    void delete(RiskProfile riskProfile);
    
    /**
     * Check if a risk profile exists for a customer and profile type
     */
    boolean existsByCustomerIdAndProfileType(String customerId, RiskProfileType profileType);
    
    /**
     * Find all risk profiles (with pagination support if needed)
     */
    List<RiskProfile> findAll();
}
