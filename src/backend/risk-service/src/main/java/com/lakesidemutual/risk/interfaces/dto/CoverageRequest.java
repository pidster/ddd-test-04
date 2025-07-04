package com.lakesidemutual.risk.interfaces.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * DTO for coverage request within risk assessment.
 */
public record CoverageRequest(
    @NotBlank
    String coverageType,
    
    @NotNull
    BigDecimal coverageAmount,
    
    BigDecimal deductible
) {}
