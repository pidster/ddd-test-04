package com.lakesidemutual.risk.interfaces.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * DTO for updating a specific risk factor.
 */
public record UpdateRiskFactorRequest(
    @NotBlank(message = "Description is required")
    String description,
    
    @NotNull(message = "Impact is required")
    @Positive(message = "Impact must be positive")
    Double impact
) {}
