package com.lakesidemutual.risk.interfaces.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO for risk assessment response.
 */
public record RiskAssessmentResponse(
    String assessmentId,
    String customerId,
    String policyType,
    int riskScore,
    String riskCategory,
    BigDecimal premiumAmount,
    String currency,
    Map<String, Object> assessmentDetails,
    String status,
    LocalDateTime assessedAt,
    String assessedBy
) {}
