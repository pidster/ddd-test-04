package com.lakesidemutual.risk.domain.event;

import com.lakesidemutual.risk.domain.model.RiskProfileType;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Domain event published when a risk assessment is started.
 */
public class RiskAssessmentStarted {
    
    private final String assessmentId;
    private final String profileId;
    private final RiskProfileType policyType;
    private final LocalDateTime occurredAt;
    
    public RiskAssessmentStarted(String assessmentId, String profileId, RiskProfileType policyType) {
        this.assessmentId = Objects.requireNonNull(assessmentId, "Assessment ID cannot be null");
        this.profileId = Objects.requireNonNull(profileId, "Profile ID cannot be null");
        this.policyType = Objects.requireNonNull(policyType, "Policy type cannot be null");
        this.occurredAt = LocalDateTime.now();
    }
    
    public String getAssessmentId() { return assessmentId; }
    public String getProfileId() { return profileId; }
    public RiskProfileType getPolicyType() { return policyType; }
    public LocalDateTime getOccurredAt() { return occurredAt; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RiskAssessmentStarted that = (RiskAssessmentStarted) o;
        return Objects.equals(assessmentId, that.assessmentId) &&
               Objects.equals(profileId, that.profileId) &&
               policyType == that.policyType &&
               Objects.equals(occurredAt, that.occurredAt);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(assessmentId, profileId, policyType, occurredAt);
    }
    
    @Override
    public String toString() {
        return "RiskAssessmentStarted{" +
                "assessmentId='" + assessmentId + '\'' +
                ", profileId='" + profileId + '\'' +
                ", policyType=" + policyType +
                ", occurredAt=" + occurredAt +
                '}';
    }
}
