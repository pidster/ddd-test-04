package com.lakesidemutual.risk.infrastructure.repository;

import com.lakesidemutual.risk.domain.model.RiskProfile;
import com.lakesidemutual.risk.domain.model.RiskProfileType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for RiskProfile entities.
 */
@Repository
public interface JpaRiskProfileRepository extends JpaRepository<RiskProfile, String> {
    
    /**
     * Find risk profiles by customer ID
     */
    List<RiskProfile> findByCustomerId(String customerId);
    
    /**
     * Find risk profile by customer ID and profile type
     */
    Optional<RiskProfile> findByCustomerIdAndProfileType(String customerId, RiskProfileType profileType);
    
    /**
     * Find all risk profiles of a specific type
     */
    List<RiskProfile> findByProfileType(RiskProfileType profileType);
    
    /**
     * Find high-risk profiles based on risk score
     */
    @Query("SELECT rp FROM RiskProfile rp WHERE rp.currentRiskScore.value >= 600")
    List<RiskProfile> findHighRiskProfiles();
    
    /**
     * Check if a risk profile exists for a customer and profile type
     */
    boolean existsByCustomerIdAndProfileType(String customerId, RiskProfileType profileType);
    
    /**
     * Find profiles by age range
     */
    @Query("SELECT rp FROM RiskProfile rp WHERE rp.age BETWEEN :minAge AND :maxAge")
    List<RiskProfile> findByAgeRange(@Param("minAge") Integer minAge, @Param("maxAge") Integer maxAge);
    
    /**
     * Find profiles by occupation
     */
    List<RiskProfile> findByOccupationContainingIgnoreCase(String occupation);
    
    /**
     * Find profiles by state
     */
    @Query("SELECT rp FROM RiskProfile rp WHERE rp.address.state = :state")
    List<RiskProfile> findByState(@Param("state") String state);
}
