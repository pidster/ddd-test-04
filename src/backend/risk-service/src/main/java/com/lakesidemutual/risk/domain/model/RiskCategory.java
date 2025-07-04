package com.lakesidemutual.risk.domain.model;

/**
 * Enumeration of risk categories used for classification and business logic.
 * 
 * These categories determine pricing multipliers and underwriting decisions.
 */
public enum RiskCategory {
    /**
     * Low risk customers - typically experienced drivers with clean records.
     * Eligible for discounts and preferred rates.
     */
    LOW("Low Risk"),
    
    /**
     * Medium risk customers - standard risk profile.
     * Standard rates and terms apply.
     */
    MEDIUM("Medium Risk"),
    
    /**
     * High risk customers - require additional scrutiny.
     * Higher premiums and potentially limited coverage options.
     */
    HIGH("High Risk"),
    
    /**
     * Very high risk customers - may require special underwriting.
     * Highest premiums or potential coverage denial.
     */
    VERY_HIGH("Very High Risk");
    
    private final String displayName;
    
    RiskCategory(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Checks if this category represents a high-risk classification.
     * 
     * @return true for HIGH and VERY_HIGH categories
     */
    public boolean isHighRisk() {
        return this == HIGH || this == VERY_HIGH;
    }
    
    /**
     * Checks if this category is eligible for standard underwriting.
     * 
     * @return true for LOW and MEDIUM categories
     */
    public boolean isStandardUnderwriting() {
        return this == LOW || this == MEDIUM;
    }
}
