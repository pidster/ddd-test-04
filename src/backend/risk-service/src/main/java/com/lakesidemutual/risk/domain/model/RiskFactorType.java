package com.lakesidemutual.risk.domain.model;

/**
 * Enumeration of different categories of risk factors
 * used in insurance risk assessment.
 */
public enum RiskFactorType {
    
    /**
     * Demographics-related risk factors (age, gender, etc.)
     */
    DEMOGRAPHIC,
    
    /**
     * Driving history related factors (accidents, violations, experience)
     */
    DRIVING_HISTORY,
    
    /**
     * Location-based risk factors (state, zip code, crime rates)
     */
    LOCATION,
    
    /**
     * Occupation-related risk factors
     */
    OCCUPATION,
    
    /**
     * Vehicle-related risk factors (make, model, year, safety features)
     */
    VEHICLE,
    
    /**
     * Credit and financial risk factors
     */
    FINANCIAL,
    
    /**
     * Health-related risk factors (for life/health insurance)
     */
    HEALTH,
    
    /**
     * Property-related risk factors (for homeowners insurance)
     */
    PROPERTY,
    
    /**
     * Business-related risk factors (for commercial insurance)
     */
    BUSINESS,
    
    /**
     * Other miscellaneous risk factors
     */
    OTHER
}
