package com.lakesidemutual.risk.interfaces.dto;

import com.lakesidemutual.risk.domain.model.RiskProfile;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * DTO for risk profile response
 */
public record RiskProfileResponse(
    String profileId,
    String customerId,
    String profileType,
    RiskScoreDto currentRiskScore,
    Set<RiskFactorDto> riskFactors,
    DrivingHistoryDto drivingHistory,
    AddressDto address,
    Integer age,
    String occupation,
    Double annualIncome,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    
    public static RiskProfileResponse fromDomain(RiskProfile profile) {
        return new RiskProfileResponse(
            profile.getProfileId(),
            profile.getCustomerId(),
            profile.getProfileType().name(),
            RiskScoreDto.fromDomain(profile.getCurrentRiskScore()),
            RiskFactorDto.fromDomain(profile.getRiskFactors()),
            DrivingHistoryDto.fromDomain(profile.getDrivingHistory()),
            AddressDto.fromDomain(profile.getAddress()),
            profile.getAge(),
            profile.getOccupation(),
            profile.getAnnualIncome(),
            profile.getCreatedAt(),
            profile.getUpdatedAt()
        );
    }
}
