package com.lakesidemutual.risk.domain.exception;

/**
 * Exception thrown when an invalid risk score value is provided.
 */
public class InvalidRiskScoreException extends IllegalArgumentException {
    
    public InvalidRiskScoreException(String message) {
        super(message);
    }
    
    public InvalidRiskScoreException(String message, Throwable cause) {
        super(message, cause);
    }
}
