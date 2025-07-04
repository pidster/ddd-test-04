package com.lakesidemutual.risk.domain.model;

/**
 * Enumeration of different types of risk profiles
 * that can be assessed in the insurance system.
 */
public enum RiskProfileType {
    /**
     * Risk profile for auto insurance policies
     */
    AUTO,
    
    /**
     * Risk profile for homeowners insurance policies
     */
    HOME,
    
    /**
     * Risk profile for life insurance policies
     */
    LIFE,
    
    /**
     * Risk profile for health insurance policies
     */
    HEALTH,
    
    /**
     * Risk profile for business/commercial insurance policies
     */
    BUSINESS
}
