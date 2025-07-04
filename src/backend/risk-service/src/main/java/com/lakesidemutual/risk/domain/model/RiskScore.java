package com.lakesidemutual.risk.domain.model;

import com.lakesidemutual.risk.domain.exception.InvalidRiskScoreException;

import java.math.BigDecimal;

/**
 * Value Object representing a risk score.
 * Risk scores range from 0 (lowest risk) to 1000 (highest risk).
 * 
 * @param value the numeric risk score value
 */
public record RiskScore(int value) {
    
    public RiskScore {
        if (value < 0 || value > 1000) {
            throw new InvalidRiskScoreException("Risk score must be between 0 and 1000, but was: " + value);
        }
    }
    
    /**
     * Determines the risk category based on the score value.
     * 
     * @return the risk category
     */
    public RiskCategory getCategory() {
        if (value <= 200) return RiskCategory.LOW;
        if (value <= 500) return RiskCategory.MEDIUM;
        if (value <= 800) return RiskCategory.HIGH;
        return RiskCategory.VERY_HIGH;
    }
    
    /**
     * Calculates the premium multiplier based on the risk category.
     * This multiplier is applied to base premiums to adjust for risk.
     * 
     * @return the multiplier for premium calculation
     */
    public BigDecimal getMultiplier() {
        return switch (getCategory()) {
            case LOW -> BigDecimal.valueOf(0.8);
            case MEDIUM -> BigDecimal.valueOf(1.0);
            case HIGH -> BigDecimal.valueOf(1.5);
            case VERY_HIGH -> BigDecimal.valueOf(2.0);
        };
    }
    
    /**
     * Checks if this risk score is considered high risk.
     * 
     * @return true if HIGH or VERY_HIGH category
     */
    public boolean isHighRisk() {
        return getCategory() == RiskCategory.HIGH || getCategory() == RiskCategory.VERY_HIGH;
    }
    
    /**
     * Creates a risk score with the minimum possible value.
     * 
     * @return minimum risk score (0)
     */
    public static RiskScore minimum() {
        return new RiskScore(0);
    }
    
    /**
     * Creates a risk score with the maximum possible value.
     * 
     * @return maximum risk score (1000)
     */
    public static RiskScore maximum() {
        return new RiskScore(1000);
    }
    
    /**
     * Creates a default risk score for new profiles.
     * 
     * @return default risk score (500 - medium risk)
     */
    public static RiskScore defaultScore() {
        return new RiskScore(500);
    }
    
    /**
     * Gets the numeric value of the risk score.
     * 
     * @return the score value
     */
    public int getValue() {
        return value;
    }
}
