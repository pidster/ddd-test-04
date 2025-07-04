package com.lakesidemutual.risk.interfaces.dto;

import com.lakesidemutual.risk.domain.model.RiskProfileType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

/**
 * DTO for creating a new risk profile
 */
public record CreateRiskProfileRequest(
    @NotBlank(message = "Customer ID is required")
    String customerId,
    
    @NotNull(message = "Profile type is required")
    RiskProfileType profileType,
    
    @Valid
    DrivingHistoryDto drivingHistory,
    
    @Valid
    AddressDto address,
    
    @Min(value = 16, message = "Age must be at least 16")
    @Max(value = 120, message = "Age must be at most 120")
    Integer age,
    
    @Size(max = 100, message = "Occupation must not exceed 100 characters")
    String occupation,
    
    @DecimalMin(value = "0.0", inclusive = false, message = "Annual income must be positive")
    Double annualIncome
) {}
