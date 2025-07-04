package com.lakesidemutual.risk.domain.event;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Domain event published when a risk profile is updated.
 */
public class RiskProfileUpdated {
    
    private final String profileId;
    private final String customerId;
    private final LocalDateTime occurredAt;
    
    public RiskProfileUpdated(String profileId, String customerId) {
        this.profileId = Objects.requireNonNull(profileId, "Profile ID cannot be null");
        this.customerId = Objects.requireNonNull(customerId, "Customer ID cannot be null");
        this.occurredAt = LocalDateTime.now();
    }
    
    public String getProfileId() { return profileId; }
    public String getCustomerId() { return customerId; }
    public LocalDateTime getOccurredAt() { return occurredAt; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RiskProfileUpdated that = (RiskProfileUpdated) o;
        return Objects.equals(profileId, that.profileId) &&
               Objects.equals(customerId, that.customerId) &&
               Objects.equals(occurredAt, that.occurredAt);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(profileId, customerId, occurredAt);
    }
    
    @Override
    public String toString() {
        return "RiskProfileUpdated{" +
                "profileId='" + profileId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", occurredAt=" + occurredAt +
                '}';
    }
}
