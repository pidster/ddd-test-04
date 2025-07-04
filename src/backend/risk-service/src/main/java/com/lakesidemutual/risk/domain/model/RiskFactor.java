package com.lakesidemutual.risk.domain.model;

import java.time.LocalDateTime;

/**
 * Value Object representing a single risk factor that contributes to overall risk assessment.
 * 
 * @param type the type of risk factor
 * @param description human-readable description of this factor
 * @param impact the impact multiplier (1.0 = neutral, >1.0 = increases risk, <1.0 = decreases risk)
 */
public record RiskFactor(
    RiskFactorType type,
    String description,
    Double impact
) {
    
    public RiskFactor {
        if (type == null) {
            throw new IllegalArgumentException("Risk factor type cannot be null");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Risk factor description cannot be null or empty");
        }
        if (impact == null || impact <= 0) {
            throw new IllegalArgumentException("Risk factor impact must be positive, but was: " + impact);
        }
    }
    
    /**
     * Creates a new risk factor with specified impact.
     * 
     * @param type the risk factor type
     * @param description the factor description
     * @param impact the impact multiplier
     * @return new risk factor
     */
    public static RiskFactor create(RiskFactorType type, String description, Double impact) {
        return new RiskFactor(type, description, impact);
    }
    
    /**
     * Checks if this risk factor increases risk (impact > 1.0).
     * 
     * @return true if this factor increases risk
     */
    public boolean increasesRisk() {
        return impact > 1.0;
    }
    
    /**
     * Checks if this risk factor decreases risk (impact < 1.0).
     * 
     * @return true if this factor decreases risk
     */
    public boolean decreasesRisk() {
        return impact < 1.0;
    }
    
    /**
     * Gets the risk impact as a percentage change from neutral.
     * 
     * @return percentage impact (e.g., 1.2 returns 20.0 for 20% increase)
     */
    public double getImpactPercentage() {
        return (impact - 1.0) * 100.0;
    }
    
    // Getter methods for compatibility
    public RiskFactorType getType() { return type; }
    public String getDescription() { return description; }
    public Double getImpact() { return impact; }
}
