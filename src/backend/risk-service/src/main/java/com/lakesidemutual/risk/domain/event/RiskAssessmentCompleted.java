package com.lakesidemutual.risk.domain.event;

import com.lakesidemutual.risk.domain.model.RiskScore;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Domain event published when a risk assessment is completed.
 */
public class RiskAssessmentCompleted {
    
    private final String assessmentId;
    private final String profileId;
    private final RiskScore riskScore;
    private final BigDecimal finalPremium;
    private final LocalDateTime occurredAt;
    
    public RiskAssessmentCompleted(String assessmentId, String profileId, 
                                  RiskScore riskScore, BigDecimal finalPremium) {
        this.assessmentId = Objects.requireNonNull(assessmentId, "Assessment ID cannot be null");
        this.profileId = Objects.requireNonNull(profileId, "Profile ID cannot be null");
        this.riskScore = riskScore; // Can be null for rejected assessments
        this.finalPremium = finalPremium; // Can be zero for rejected assessments
        this.occurredAt = LocalDateTime.now();
    }
    
    public String getAssessmentId() { return assessmentId; }
    public String getProfileId() { return profileId; }
    public RiskScore getRiskScore() { return riskScore; }
    public BigDecimal getFinalPremium() { return finalPremium; }
    public LocalDateTime getOccurredAt() { return occurredAt; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RiskAssessmentCompleted that = (RiskAssessmentCompleted) o;
        return Objects.equals(assessmentId, that.assessmentId) &&
               Objects.equals(profileId, that.profileId) &&
               Objects.equals(riskScore, that.riskScore) &&
               Objects.equals(finalPremium, that.finalPremium) &&
               Objects.equals(occurredAt, that.occurredAt);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(assessmentId, profileId, riskScore, finalPremium, occurredAt);
    }
    
    @Override
    public String toString() {
        return "RiskAssessmentCompleted{" +
                "assessmentId='" + assessmentId + '\'' +
                ", profileId='" + profileId + '\'' +
                ", riskScore=" + riskScore +
                ", finalPremium=" + finalPremium +
                ", occurredAt=" + occurredAt +
                '}';
    }
}
