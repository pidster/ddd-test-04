package com.lakesidemutual.risk.infrastructure.repository;

import com.lakesidemutual.risk.domain.model.AssessmentStatus;
import com.lakesidemutual.risk.domain.model.RiskAssessment;
import com.lakesidemutual.risk.domain.model.RiskProfileType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for RiskAssessment entities.
 */
@Repository
public interface JpaRiskAssessmentRepository extends JpaRepository<RiskAssessment, String> {
    
    /**
     * Find risk assessments by profile ID
     */
    List<RiskAssessment> findByProfileId(String profileId);
    
    /**
     * Find the most recent risk assessment for a profile
     */
    @Query("SELECT ra FROM RiskAssessment ra WHERE ra.profileId = :profileId ORDER BY ra.createdAt DESC LIMIT 1")
    Optional<RiskAssessment> findMostRecentByProfileId(@Param("profileId") String profileId);
    
    /**
     * Find risk assessments by status
     */
    List<RiskAssessment> findByStatus(AssessmentStatus status);
    
    /**
     * Find risk assessments by policy type
     */
    List<RiskAssessment> findByPolicyType(RiskProfileType policyType);
    
    /**
     * Find risk assessments by assessor
     */
    List<RiskAssessment> findByAssessorId(String assessorId);
    
    /**
     * Find risk assessments created within a date range
     */
    List<RiskAssessment> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Find completed risk assessments within a date range
     */
    @Query("SELECT ra FROM RiskAssessment ra WHERE ra.status = 'COMPLETED' AND ra.completedAt BETWEEN :startDate AND :endDate")
    List<RiskAssessment> findCompletedAssessmentsBetween(@Param("startDate") LocalDateTime startDate, 
                                                        @Param("endDate") LocalDateTime endDate);
    
    /**
     * Find pending/in-progress assessments older than specified date
     */
    @Query("SELECT ra FROM RiskAssessment ra WHERE ra.status = 'IN_PROGRESS' AND ra.createdAt < :cutoffDate")
    List<RiskAssessment> findPendingAssessmentsOlderThan(@Param("cutoffDate") LocalDateTime cutoffDate);
    
    /**
     * Count assessments by status
     */
    long countByStatus(AssessmentStatus status);
    
    /**
     * Find assessments by risk score range
     */
    @Query("SELECT ra FROM RiskAssessment ra WHERE ra.calculatedRiskScore.value BETWEEN :minScore AND :maxScore")
    List<RiskAssessment> findByRiskScoreRange(@Param("minScore") Integer minScore, @Param("maxScore") Integer maxScore);
    
    /**
     * Find high-premium assessments
     */
    @Query("SELECT ra FROM RiskAssessment ra WHERE ra.finalPremium >= :minimumPremium ORDER BY ra.finalPremium DESC")
    List<RiskAssessment> findHighPremiumAssessments(@Param("minimumPremium") double minimumPremium);
}
