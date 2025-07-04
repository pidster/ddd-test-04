package com.lakesidemutual.risk.domain.event;

import com.lakesidemutual.risk.domain.model.RiskProfileType;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Domain event published when a new risk profile is created.
 */
public class RiskProfileCreated {
    
    private final String profileId;
    private final String customerId;
    private final RiskProfileType profileType;
    private final LocalDateTime occurredAt;
    
    public RiskProfileCreated(String profileId, String customerId, RiskProfileType profileType) {
        this.profileId = Objects.requireNonNull(profileId, "Profile ID cannot be null");
        this.customerId = Objects.requireNonNull(customerId, "Customer ID cannot be null");
        this.profileType = Objects.requireNonNull(profileType, "Profile type cannot be null");
        this.occurredAt = LocalDateTime.now();
    }
    
    public String getProfileId() { return profileId; }
    public String getCustomerId() { return customerId; }
    public RiskProfileType getProfileType() { return profileType; }
    public LocalDateTime getOccurredAt() { return occurredAt; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RiskProfileCreated that = (RiskProfileCreated) o;
        return Objects.equals(profileId, that.profileId) &&
               Objects.equals(customerId, that.customerId) &&
               profileType == that.profileType &&
               Objects.equals(occurredAt, that.occurredAt);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(profileId, customerId, profileType, occurredAt);
    }
    
    @Override
    public String toString() {
        return "RiskProfileCreated{" +
                "profileId='" + profileId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", profileType=" + profileType +
                ", occurredAt=" + occurredAt +
                '}';
    }
}
