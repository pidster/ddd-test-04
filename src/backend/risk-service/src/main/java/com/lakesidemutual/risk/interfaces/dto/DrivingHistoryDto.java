package com.lakesidemutual.risk.interfaces.dto;

import com.lakesidemutual.risk.domain.model.DrivingHistory;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

/**
 * DTO for driving history information
 */
public record DrivingHistoryDto(
    @Min(value = 0, message = "Number of accidents cannot be negative")
    @Max(value = 50, message = "Number of accidents seems unrealistic")
    Integer accidents,
    
    @Min(value = 0, message = "Number of violations cannot be negative")
    @Max(value = 50, message = "Number of violations seems unrealistic")
    Integer violations,
    
    @Min(value = 0, message = "Years of experience cannot be negative")
    @Max(value = 80, message = "Years of experience seems unrealistic")
    Integer yearsOfExperience,
    
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "License date must be in YYYY-MM-DD format")
    String licenseDate
) {
    
    public static DrivingHistoryDto fromDomain(DrivingHistory drivingHistory) {
        if (drivingHistory == null) {
            return null;
        }
        return new DrivingHistoryDto(
            drivingHistory.accidents(),
            drivingHistory.violations(),
            drivingHistory.yearsOfExperience(),
            drivingHistory.licenseDate() != null ? drivingHistory.licenseDate().toString() : null
        );
    }
    
    public DrivingHistory toDomain() {
        LocalDate parsedDate = licenseDate != null ? LocalDate.parse(licenseDate) : null;
        return new DrivingHistory(accidents, violations, yearsOfExperience, parsedDate);
    }
}
