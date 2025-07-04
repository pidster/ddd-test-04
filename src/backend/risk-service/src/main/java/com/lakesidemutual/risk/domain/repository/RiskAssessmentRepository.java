package com.lakesidemutual.risk.domain.repository;

import com.lakesidemutual.risk.domain.model.AssessmentStatus;
import com.lakesidemutual.risk.domain.model.RiskAssessment;
import com.lakesidemutual.risk.domain.model.RiskProfileType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Domain repository interface for RiskAssessment aggregate.
 * This interface defines the contract for persistence operations
 * without exposing infrastructure details.
 */
public interface RiskAssessmentRepository {
    
    /**
     * Save a risk assessment
     */
    RiskAssessment save(RiskAssessment riskAssessment);
    
    /**
     * Find a risk assessment by its ID
     */
    Optional<RiskAssessment> findById(String assessmentId);
    
    /**
     * Find risk assessments by profile ID
     */
    List<RiskAssessment> findByProfileId(String profileId);
    
    /**
     * Find the most recent risk assessment for a profile
     */
    Optional<RiskAssessment> findMostRecentByProfileId(String profileId);
    
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
    List<RiskAssessment> findCompletedAssessmentsBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Find pending/in-progress assessments older than specified date
     */
    List<RiskAssessment> findPendingAssessmentsOlderThan(LocalDateTime cutoffDate);
    
    /**
     * Delete a risk assessment
     */
    void delete(RiskAssessment riskAssessment);
    
    /**
     * Count assessments by status
     */
    long countByStatus(AssessmentStatus status);
    
    /**
     * Find all risk assessments (with pagination support if needed)
     */
    List<RiskAssessment> findAll();
}
