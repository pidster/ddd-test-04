package com.lakesidemutual.risk.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for RiskScore value object
 */
class RiskScoreTest {

    @Test
    @DisplayName("Should create valid risk score")
    void shouldCreateValidRiskScore() {
        // Given
        int validScore = 500;
        
        // When
        RiskScore riskScore = new RiskScore(validScore);
        
        // Then
        assertEquals(validScore, riskScore.value());
        assertEquals(RiskCategory.MEDIUM, riskScore.getCategory());
        assertFalse(riskScore.isHighRisk());
    }

    @Test
    @DisplayName("Should throw exception for invalid negative score")
    void shouldThrowExceptionForNegativeScore() {
        // Given
        int invalidScore = -1;
        
        // When & Then
        assertThrows(Exception.class, () -> new RiskScore(invalidScore));
    }

    @Test
    @DisplayName("Should throw exception for score above maximum")
    void shouldThrowExceptionForScoreAboveMaximum() {
        // Given
        int invalidScore = 1001;
        
        // When & Then
        assertThrows(Exception.class, () -> new RiskScore(invalidScore));
    }

    @Test
    @DisplayName("Should correctly categorize low risk")
    void shouldCorrectlyCategorizeTypes() {
        // Test LOW risk
        RiskScore lowRisk = new RiskScore(100);
        assertEquals(RiskCategory.LOW, lowRisk.getCategory());
        assertFalse(lowRisk.isHighRisk());

        // Test MEDIUM risk
        RiskScore mediumRisk = new RiskScore(350);
        assertEquals(RiskCategory.MEDIUM, mediumRisk.getCategory());
        assertFalse(mediumRisk.isHighRisk());

        // Test HIGH risk
        RiskScore highRisk = new RiskScore(700);
        assertEquals(RiskCategory.HIGH, highRisk.getCategory());
        assertTrue(highRisk.isHighRisk());

        // Test VERY_HIGH risk
        RiskScore veryHighRisk = new RiskScore(900);
        assertEquals(RiskCategory.VERY_HIGH, veryHighRisk.getCategory());
        assertTrue(veryHighRisk.isHighRisk());
    }
}
