package com.lakesidemutual.risk.infrastructure.repository;

import com.lakesidemutual.risk.domain.model.AssessmentStatus;
import com.lakesidemutual.risk.domain.model.RiskAssessment;
import com.lakesidemutual.risk.domain.model.RiskProfileType;
import com.lakesidemutual.risk.domain.repository.RiskAssessmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of the domain RiskAssessmentRepository using Spring Data JPA.
 */
@Component
public class RiskAssessmentRepositoryImpl implements RiskAssessmentRepository {
    
    private final JpaRiskAssessmentRepository jpaRepository;
    
    @Autowired
    public RiskAssessmentRepositoryImpl(JpaRiskAssessmentRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }
    
    @Override
    public RiskAssessment save(RiskAssessment riskAssessment) {
        return jpaRepository.save(riskAssessment);
    }
    
    @Override
    public Optional<RiskAssessment> findById(String assessmentId) {
        return jpaRepository.findById(assessmentId);
    }
    
    @Override
    public List<RiskAssessment> findByProfileId(String profileId) {
        return jpaRepository.findByProfileId(profileId);
    }
    
    @Override
    public Optional<RiskAssessment> findMostRecentByProfileId(String profileId) {
        return jpaRepository.findMostRecentByProfileId(profileId);
    }
    
    @Override
    public List<RiskAssessment> findByStatus(AssessmentStatus status) {
        return jpaRepository.findByStatus(status);
    }
    
    @Override
    public List<RiskAssessment> findByPolicyType(RiskProfileType policyType) {
        return jpaRepository.findByPolicyType(policyType);
    }
    
    @Override
    public List<RiskAssessment> findByAssessorId(String assessorId) {
        return jpaRepository.findByAssessorId(assessorId);
    }
    
    @Override
    public List<RiskAssessment> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return jpaRepository.findByCreatedAtBetween(startDate, endDate);
    }
    
    @Override
    public List<RiskAssessment> findCompletedAssessmentsBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return jpaRepository.findCompletedAssessmentsBetween(startDate, endDate);
    }
    
    @Override
    public List<RiskAssessment> findPendingAssessmentsOlderThan(LocalDateTime cutoffDate) {
        return jpaRepository.findPendingAssessmentsOlderThan(cutoffDate);
    }
    
    @Override
    public void delete(RiskAssessment riskAssessment) {
        jpaRepository.delete(riskAssessment);
    }
    
    @Override
    public long countByStatus(AssessmentStatus status) {
        return jpaRepository.countByStatus(status);
    }
    
    @Override
    public List<RiskAssessment> findAll() {
        return jpaRepository.findAll();
    }
}
