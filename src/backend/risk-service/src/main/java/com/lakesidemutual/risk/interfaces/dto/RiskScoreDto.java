package com.lakesidemutual.risk.interfaces.dto;

import com.lakesidemutual.risk.domain.model.RiskCategory;
import com.lakesidemutual.risk.domain.model.RiskScore;

/**
 * DTO for risk score information
 */
public record RiskScoreDto(
    int value,
    RiskCategory category,
    boolean isHighRisk
) {
    
    public static RiskScoreDto fromDomain(RiskScore riskScore) {
        if (riskScore == null) {
            return null;
        }
        return new RiskScoreDto(
            riskScore.value(),
            riskScore.getCategory(),
            riskScore.isHighRisk()
        );
    }
    
    public RiskScore toDomain() {
        return new RiskScore(value);
    }
}
