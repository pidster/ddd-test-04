package com.lakesidemutual.risk.interfaces.dto;

import com.lakesidemutual.risk.domain.model.RiskFactor;
import com.lakesidemutual.risk.domain.model.RiskFactorType;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * DTO for risk factor information
 */
public record RiskFactorDto(
    RiskFactorType type,
    String description,
    Double impact
) {
    
    public static RiskFactorDto fromDomain(RiskFactor riskFactor) {
        return new RiskFactorDto(
            riskFactor.type(),
            riskFactor.description(),
            riskFactor.impact()
        );
    }
    
    public static Set<RiskFactorDto> fromDomain(Set<RiskFactor> riskFactors) {
        if (riskFactors == null) {
            return Set.of();
        }
        return riskFactors.stream()
            .map(RiskFactorDto::fromDomain)
            .collect(Collectors.toSet());
    }
    
    public RiskFactor toDomain() {
        return new RiskFactor(type, description, impact);
    }
    
    public static Set<RiskFactor> toDomain(Set<RiskFactorDto> dtos) {
        if (dtos == null) {
            return Set.of();
        }
        return dtos.stream()
            .map(RiskFactorDto::toDomain)
            .collect(Collectors.toSet());
    }
}
