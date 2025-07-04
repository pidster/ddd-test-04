package com.lakesidemutual.risk.domain.model;

/**
 * Enumeration of possible statuses for a risk assessment.
 */
public enum AssessmentStatus {
    /**
     * Assessment is currently being processed
     */
    IN_PROGRESS,
    
    /**
     * Assessment has been completed successfully with approved risk level
     */
    COMPLETED,
    
    /**
     * Assessment has been rejected due to high risk or other factors
     */
    REJECTED,
    
    /**
     * Assessment is on hold pending additional information
     */
    ON_HOLD
}
