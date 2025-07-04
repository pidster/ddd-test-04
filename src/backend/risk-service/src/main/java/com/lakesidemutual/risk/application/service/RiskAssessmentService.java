package com.lakesidemutual.risk.application.service;

import com.lakesidemutual.risk.domain.model.*;
import com.lakesidemutual.risk.domain.repository.RiskAssessmentRepository;
import com.lakesidemutual.risk.domain.repository.RiskProfileRepository;
import com.lakesidemutual.risk.domain.service.PricingService;
import com.lakesidemutual.risk.domain.service.RiskCalculationService;
import com.lakesidemutual.risk.interfaces.dto.CoverageRequest;
import com.lakesidemutual.risk.interfaces.dto.RiskAssessmentRequest;
import com.lakesidemutual.risk.interfaces.dto.RiskAssessmentResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Application service for managing risk assessments.
 * Orchestrates the complete risk assessment process.
 */
@Service
@Transactional
public class RiskAssessmentService {
    
    private final RiskAssessmentRepository riskAssessmentRepository;
    private final RiskProfileRepository riskProfileRepository;
    private final RiskCalculationService riskCalculationService;
    private final PricingService pricingService;
    
    public RiskAssessmentService(RiskAssessmentRepository riskAssessmentRepository,
                                RiskProfileRepository riskProfileRepository,
                                RiskCalculationService riskCalculationService,
                                PricingService pricingService) {
        this.riskAssessmentRepository = riskAssessmentRepository;
        this.riskProfileRepository = riskProfileRepository;
        this.riskCalculationService = riskCalculationService;
        this.pricingService = pricingService;
    }
    
    /**
     * Start a new risk assessment
     */
    public RiskAssessment startAssessment(String profileId, RiskProfileType policyType, 
                                         String assessorId) {
        
        // Verify profile exists
        RiskProfile profile = riskProfileRepository.findById(profileId)
            .orElseThrow(() -> new IllegalArgumentException("Risk profile not found: " + profileId));
        
        // Get base premium for policy type
        BigDecimal basePremium = pricingService.getBasePremium(policyType);
        
        // Create new assessment
        RiskAssessment assessment = new RiskAssessment(profileId, policyType, basePremium, assessorId);
        
        return riskAssessmentRepository.save(assessment);
    }
    
    /**
     * Complete a risk assessment with calculated results
     */
    public RiskAssessment completeAssessment(String assessmentId, String notes) {
        
        RiskAssessment assessment = riskAssessmentRepository.findById(assessmentId)
            .orElseThrow(() -> new IllegalArgumentException("Risk assessment not found: " + assessmentId));
        
        // Get the associated risk profile
        RiskProfile profile = riskProfileRepository.findById(assessment.getProfileId())
            .orElseThrow(() -> new IllegalArgumentException("Risk profile not found: " + assessment.getProfileId()));
        
        // Calculate risk score and factors
        RiskScore calculatedScore = riskCalculationService.calculateRiskScore(profile);
        Set<RiskFactor> riskFactors = riskCalculationService.calculateRiskFactors(profile);
        
        // Check if profile is insurable
        if (!pricingService.isInsurable(calculatedScore, riskFactors)) {
            assessment.rejectAssessment("Profile does not meet insurability criteria");
            return riskAssessmentRepository.save(assessment);
        }
        
        // Calculate pricing
        BigDecimal riskMultiplier = pricingService.calculateRiskMultiplier(calculatedScore, riskFactors);
        
        // Complete the assessment
        assessment.completeAssessment(calculatedScore, riskFactors, riskMultiplier, notes);
        
        return riskAssessmentRepository.save(assessment);
    }
    
    /**
     * Reject a risk assessment
     */
    public RiskAssessment rejectAssessment(String assessmentId, String rejectionReason) {
        
        RiskAssessment assessment = riskAssessmentRepository.findById(assessmentId)
            .orElseThrow(() -> new IllegalArgumentException("Risk assessment not found: " + assessmentId));
        
        assessment.rejectAssessment(rejectionReason);
        
        return riskAssessmentRepository.save(assessment);
    }
    
    /**
     * Get risk assessment by ID
     */
    @Transactional(readOnly = true)
    public Optional<RiskAssessment> getAssessment(String assessmentId) {
        return riskAssessmentRepository.findById(assessmentId);
    }
    
    /**
     * Get all assessments for a risk profile
     */
    @Transactional(readOnly = true)
    public List<RiskAssessment> getAssessmentsForProfile(String profileId) {
        return riskAssessmentRepository.findByProfileId(profileId);
    }
    
    /**
     * Get the most recent assessment for a profile
     */
    @Transactional(readOnly = true)
    public Optional<RiskAssessment> getMostRecentAssessment(String profileId) {
        return riskAssessmentRepository.findMostRecentByProfileId(profileId);
    }
    
    /**
     * Get assessments by status
     */
    @Transactional(readOnly = true)
    public List<RiskAssessment> getAssessmentsByStatus(AssessmentStatus status) {
        return riskAssessmentRepository.findByStatus(status);
    }
    
    /**
     * Get assessments by assessor
     */
    @Transactional(readOnly = true)
    public List<RiskAssessment> getAssessmentsByAssessor(String assessorId) {
        return riskAssessmentRepository.findByAssessorId(assessorId);
    }
    
    /**
     * Get assessments within a date range
     */
    @Transactional(readOnly = true)
    public List<RiskAssessment> getAssessmentsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return riskAssessmentRepository.findByCreatedAtBetween(startDate, endDate);
    }
    
    /**
     * Get pending assessments that are overdue
     */
    @Transactional(readOnly = true)
    public List<RiskAssessment> getOverdueAssessments(int daysOverdue) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(daysOverdue);
        return riskAssessmentRepository.findPendingAssessmentsOlderThan(cutoffDate);
    }
    
    /**
     * Get assessment statistics
     */
    @Transactional(readOnly = true)
    public AssessmentStatistics getAssessmentStatistics() {
        long totalAssessments = riskAssessmentRepository.findAll().size();
        long completedAssessments = riskAssessmentRepository.countByStatus(AssessmentStatus.COMPLETED);
        long rejectedAssessments = riskAssessmentRepository.countByStatus(AssessmentStatus.REJECTED);
        long pendingAssessments = riskAssessmentRepository.countByStatus(AssessmentStatus.IN_PROGRESS);
        
        return new AssessmentStatistics(totalAssessments, completedAssessments, 
                                      rejectedAssessments, pendingAssessments);
    }
    
    /**
     * Auto-complete assessments based on profile data
     */
    public RiskAssessment autoCompleteAssessment(String assessmentId) {
        return completeAssessment(assessmentId, "Auto-completed by system");
    }
    
    /**
     * Calculate premium estimate for a profile without creating formal assessment
     */
    @Transactional(readOnly = true)
    public PremiumEstimate calculatePremiumEstimate(String profileId, RiskProfileType policyType) {
        
        RiskProfile profile = riskProfileRepository.findById(profileId)
            .orElseThrow(() -> new IllegalArgumentException("Risk profile not found: " + profileId));
        
        // Calculate risk assessment components
        RiskScore calculatedScore = riskCalculationService.calculateRiskScore(profile);
        Set<RiskFactor> riskFactors = riskCalculationService.calculateRiskFactors(profile);
        
        // Check insurability
        boolean isInsurable = pricingService.isInsurable(calculatedScore, riskFactors);
        
        if (!isInsurable) {
            return new PremiumEstimate(profileId, policyType, false, 
                                     BigDecimal.ZERO, BigDecimal.ZERO, calculatedScore);
        }
        
        // Calculate pricing
        BigDecimal basePremium = pricingService.getBasePremium(policyType);
        BigDecimal riskMultiplier = pricingService.calculateRiskMultiplier(calculatedScore, riskFactors);
        BigDecimal finalPremium = pricingService.calculateFinalPremium(basePremium, riskMultiplier);
        
        return new PremiumEstimate(profileId, policyType, true, 
                                 basePremium, finalPremium, calculatedScore);
    }
    
    /**
     * Perform risk assessment from request DTO
     */
    public RiskAssessment performAssessment(RiskAssessmentRequest request) {
        // Find or create a risk profile for the customer
        List<RiskProfile> profiles = riskProfileRepository.findByCustomerId(request.customerId());
        RiskProfile profile = profiles.isEmpty() ? 
            createBasicRiskProfile(request.customerId()) : profiles.get(0);
        
        // Calculate base premium from coverage requests
        BigDecimal basePremium = request.requestedCoverages().stream()
            .map(CoverageRequest::coverageAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        if (basePremium.compareTo(BigDecimal.ZERO) <= 0) {
            basePremium = new BigDecimal("1000"); // Default base premium
        }
        
        // Create assessment with correct constructor
        RiskAssessment assessment = new RiskAssessment(
            profile.getProfileId(),
            RiskProfileType.valueOf(request.policyType()),
            basePremium,
            "SYSTEM"
        );
        
        // Perform the assessment calculations
        Set<RiskFactor> factors = riskCalculationService.calculateRiskFactors(profile);
        RiskScore score = riskCalculationService.calculateRiskScore(profile);
        
        // Calculate risk multiplier based on score
        BigDecimal riskMultiplier = pricingService.calculateRiskMultiplier(score, factors);
        
        // Complete the assessment
        assessment.completeAssessment(score, factors, riskMultiplier, "Automated assessment");
        
        return riskAssessmentRepository.save(assessment);
    }
    
    /**
     * Get assessment history for a customer
     */
    @Transactional(readOnly = true)
    public List<RiskAssessment> getAssessmentHistory(String customerId) {
        // Get risk profiles for customer first
        List<RiskProfile> profiles = riskProfileRepository.findByCustomerId(customerId);
        
        // Get assessments for each profile
        return profiles.stream()
            .flatMap(profile -> riskAssessmentRepository.findByProfileId(profile.getProfileId()).stream())
            .sorted((a1, a2) -> a2.getCreatedAt().compareTo(a1.getCreatedAt())) // Newest first
            .toList();
    }
    
    /**
     * Recalculate an existing assessment
     */
    public Optional<RiskAssessment> recalculateAssessment(String assessmentId) {
        Optional<RiskAssessment> assessmentOpt = riskAssessmentRepository.findById(assessmentId);
        if (assessmentOpt.isEmpty()) {
            return Optional.empty();
        }
        
        RiskAssessment assessment = assessmentOpt.get();
        
        // Find the associated risk profile
        Optional<RiskProfile> profileOpt = riskProfileRepository.findById(assessment.getProfileId());
        if (profileOpt.isEmpty()) {
            return Optional.empty();
        }
        
        RiskProfile profile = profileOpt.get();
        
        // Create a new assessment with recalculated values
        Set<RiskFactor> newFactors = riskCalculationService.calculateRiskFactors(profile);
        RiskScore newScore = riskCalculationService.calculateRiskScore(profile);
        BigDecimal newMultiplier = pricingService.calculateRiskMultiplier(newScore, newFactors);
        
        RiskAssessment newAssessment = new RiskAssessment(
            profile.getProfileId(),
            assessment.getPolicyType(),
            assessment.getBasePremium(),
            "SYSTEM"
        );
        
        newAssessment.completeAssessment(newScore, newFactors, newMultiplier, "Recalculated assessment");
        
        return Optional.of(riskAssessmentRepository.save(newAssessment));
    }
    
    /**
     * Convert domain model to response DTO
     */
    public RiskAssessmentResponse convertToResponse(RiskAssessment assessment) {
        Map<String, Object> details = new HashMap<>();
        details.put("factors", assessment.getAssessedRiskFactors());
        details.put("notes", assessment.getAssessmentNotes());
        
        return new RiskAssessmentResponse(
            assessment.getAssessmentId(),
            "customer-from-profile", // Would need to look up customer from profile
            assessment.getPolicyType().toString(),
            assessment.getCalculatedRiskScore() != null ? assessment.getCalculatedRiskScore().value() : 0,
            assessment.getCalculatedRiskScore() != null ? assessment.getCalculatedRiskScore().getCategory().toString() : "UNKNOWN",
            assessment.getFinalPremium() != null ? assessment.getFinalPremium() : BigDecimal.ZERO,
            "USD", // Default currency for MVP
            details,
            assessment.getStatus().toString(),
            assessment.getCreatedAt(),
            assessment.getAssessorId()
        );
    }
    
    /**
     * Create a basic risk profile for new customers
     */
    private RiskProfile createBasicRiskProfile(String customerId) {
        // Create default values for MVP
        DrivingHistory defaultHistory = new DrivingHistory(0, 0, 5, LocalDate.of(2015, 1, 1));
        Address defaultAddress = new Address("Unknown", "Unknown", "00000", "XX", "US");
        
        RiskProfile profile = new RiskProfile(
            customerId, 
            RiskProfileType.AUTO, 
            defaultHistory, 
            defaultAddress, 
            30, // Default age
            "Unknown", 
            50000.0 // Default income
        );
        
        return riskProfileRepository.save(profile);
    }
    
    /**
     * Statistics record for assessment data
     */
    public record AssessmentStatistics(
        long totalAssessments,
        long completedAssessments,
        long rejectedAssessments,
        long pendingAssessments
    ) {}
    
    /**
     * Premium estimate record
     */
    public record PremiumEstimate(
        String profileId,
        RiskProfileType policyType,
        boolean isInsurable,
        BigDecimal basePremium,
        BigDecimal estimatedPremium,
        RiskScore riskScore
    ) {}
}
