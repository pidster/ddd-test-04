package com.lakesidemutual.risk.domain.model;

import com.lakesidemutual.risk.domain.event.RiskAssessmentCompleted;
import com.lakesidemutual.risk.domain.event.RiskAssessmentStarted;
import org.springframework.data.domain.AbstractAggregateRoot;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * RiskAssessment aggregate root representing the assessment process
 * and results for a specific insurance quote or policy.
 * 
 * This aggregate encapsulates the complete risk evaluation process,
 * including calculated scores, pricing factors, and assessment status.
 */
@Entity
@Table(name = "risk_assessments")
public class RiskAssessment extends AbstractAggregateRoot<RiskAssessment> {
    
    @Id
    @Column(name = "assessment_id")
    private String assessmentId;
    
    @Column(name = "profile_id", nullable = false)
    private String profileId;
    
    @Column(name = "policy_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private RiskProfileType policyType;
    
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private AssessmentStatus status;
    
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "value", column = @Column(name = "calculated_risk_score")),
        @AttributeOverride(name = "category", column = @Column(name = "calculated_risk_category"))
    })
    private RiskScore calculatedRiskScore;
    
    @Column(name = "base_premium", precision = 10, scale = 2)
    private BigDecimal basePremium;
    
    @Column(name = "risk_multiplier", precision = 5, scale = 3)
    private BigDecimal riskMultiplier;
    
    @Column(name = "final_premium", precision = 10, scale = 2)
    private BigDecimal finalPremium;
    
    @ElementCollection
    @CollectionTable(name = "assessment_risk_factors", 
                    joinColumns = @JoinColumn(name = "assessment_id"))
    private Set<RiskFactor> assessedRiskFactors = new HashSet<>();
    
    @Column(name = "assessment_notes", length = 1000)
    private String assessmentNotes;
    
    @Column(name = "assessor_id")
    private String assessorId;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "completed_at")
    private LocalDateTime completedAt;
    
    @Version
    private Long version;

    // Default constructor for JPA
    protected RiskAssessment() {}

    /**
     * Start a new risk assessment
     */
    public RiskAssessment(String profileId, RiskProfileType policyType, 
                         BigDecimal basePremium, String assessorId) {
        this.assessmentId = UUID.randomUUID().toString();
        this.profileId = Objects.requireNonNull(profileId, "Profile ID cannot be null");
        this.policyType = Objects.requireNonNull(policyType, "Policy type cannot be null");
        this.basePremium = Objects.requireNonNull(basePremium, "Base premium cannot be null");
        this.assessorId = assessorId;
        this.status = AssessmentStatus.IN_PROGRESS;
        this.assessedRiskFactors = new HashSet<>();
        this.createdAt = LocalDateTime.now();
        
        // Validate base premium
        if (basePremium.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Base premium must be positive");
        }
        
        registerEvent(new RiskAssessmentStarted(this.assessmentId, this.profileId, this.policyType));
    }

    /**
     * Complete the risk assessment with calculated results
     */
    public void completeAssessment(RiskScore calculatedScore, Set<RiskFactor> riskFactors,
                                  BigDecimal riskMultiplier, String notes) {
        if (this.status != AssessmentStatus.IN_PROGRESS) {
            throw new IllegalStateException("Assessment can only be completed when in progress");
        }
        
        Objects.requireNonNull(calculatedScore, "Calculated risk score cannot be null");
        Objects.requireNonNull(riskMultiplier, "Risk multiplier cannot be null");
        
        if (riskMultiplier.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Risk multiplier must be positive");
        }
        
        this.calculatedRiskScore = calculatedScore;
        this.riskMultiplier = riskMultiplier;
        this.assessedRiskFactors.clear();
        if (riskFactors != null) {
            this.assessedRiskFactors.addAll(riskFactors);
        }
        this.assessmentNotes = notes;
        this.completedAt = LocalDateTime.now();
        
        // Calculate final premium
        this.finalPremium = this.basePremium.multiply(this.riskMultiplier);
        
        this.status = AssessmentStatus.COMPLETED;
        
        registerEvent(new RiskAssessmentCompleted(
            this.assessmentId, 
            this.profileId, 
            this.calculatedRiskScore,
            this.finalPremium
        ));
    }

    /**
     * Reject the assessment (e.g., due to high risk)
     */
    public void rejectAssessment(String rejectionReason) {
        if (this.status != AssessmentStatus.IN_PROGRESS) {
            throw new IllegalStateException("Assessment can only be rejected when in progress");
        }
        
        this.status = AssessmentStatus.REJECTED;
        this.assessmentNotes = rejectionReason;
        this.completedAt = LocalDateTime.now();
        
        registerEvent(new RiskAssessmentCompleted(
            this.assessmentId, 
            this.profileId, 
            this.calculatedRiskScore,
            BigDecimal.ZERO // No premium for rejected assessments
        ));
    }

    /**
     * Check if assessment is completed (either approved or rejected)
     */
    public boolean isCompleted() {
        return status == AssessmentStatus.COMPLETED || status == AssessmentStatus.REJECTED;
    }

    /**
     * Check if assessment was approved
     */
    public boolean isApproved() {
        return status == AssessmentStatus.COMPLETED;
    }

    /**
     * Check if assessment was rejected
     */
    public boolean isRejected() {
        return status == AssessmentStatus.REJECTED;
    }

    /**
     * Get premium discount/surcharge percentage based on risk
     */
    public BigDecimal getPremiumAdjustmentPercentage() {
        if (riskMultiplier == null) {
            return BigDecimal.ZERO;
        }
        return riskMultiplier.subtract(BigDecimal.ONE).multiply(new BigDecimal("100"));
    }

    // Getters
    public String getAssessmentId() { return assessmentId; }
    public String getProfileId() { return profileId; }
    public RiskProfileType getPolicyType() { return policyType; }
    public AssessmentStatus getStatus() { return status; }
    public RiskScore getCalculatedRiskScore() { return calculatedRiskScore; }
    public BigDecimal getBasePremium() { return basePremium; }
    public BigDecimal getRiskMultiplier() { return riskMultiplier; }
    public BigDecimal getFinalPremium() { return finalPremium; }
    public Set<RiskFactor> getAssessedRiskFactors() { return Collections.unmodifiableSet(assessedRiskFactors); }
    public String getAssessmentNotes() { return assessmentNotes; }
    public String getAssessorId() { return assessorId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public Long getVersion() { return version; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RiskAssessment that = (RiskAssessment) o;
        return Objects.equals(assessmentId, that.assessmentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(assessmentId);
    }

    @Override
    public String toString() {
        return "RiskAssessment{" +
                "assessmentId='" + assessmentId + '\'' +
                ", profileId='" + profileId + '\'' +
                ", policyType=" + policyType +
                ", status=" + status +
                ", calculatedRiskScore=" + calculatedRiskScore +
                ", finalPremium=" + finalPremium +
                '}';
    }
}
