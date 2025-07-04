package com.lakesidemutual.risk.interfaces.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * DTO for risk assessment request.
 */
public record RiskAssessmentRequest(
    @NotBlank
    String customerId,
    
    @NotBlank
    String policyType,
    
    @NotNull
    List<CoverageRequest> requestedCoverages,
    
    Map<String, Object> additionalFactors
) {}
