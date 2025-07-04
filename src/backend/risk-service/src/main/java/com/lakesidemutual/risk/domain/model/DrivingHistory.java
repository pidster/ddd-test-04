package com.lakesidemutual.risk.domain.model;

import java.time.LocalDate;

/**
 * Value Object representing a customer's driving history.
 * Contains simple driving record information.
 * 
 * @param accidents Number of accidents in driving history
 * @param violations Number of traffic violations in driving history
 * @param yearsOfExperience Number of years of driving experience
 * @param licenseDate Date when driver's license was first issued
 */
public record DrivingHistory(
    Integer accidents,
    Integer violations,
    Integer yearsOfExperience,
    LocalDate licenseDate
) {
    
    public DrivingHistory {
        if (accidents != null && accidents < 0) {
            throw new IllegalArgumentException("Number of accidents cannot be negative");
        }
        if (violations != null && violations < 0) {
            throw new IllegalArgumentException("Number of violations cannot be negative");
        }
        if (yearsOfExperience != null && yearsOfExperience < 0) {
            throw new IllegalArgumentException("Years of experience cannot be negative");
        }
    }
    
    /**
     * Checks if the driver has a clean record (no accidents or violations).
     * 
     * @return true if no accidents or violations
     */
    public boolean isCleanRecord() {
        return (accidents == null || accidents == 0) && (violations == null || violations == 0);
    }
    
    /**
     * Checks if the driver is experienced (5+ years).
     * 
     * @return true if experienced driver
     */
    public boolean isExperiencedDriver() {
        return yearsOfExperience != null && yearsOfExperience >= 5;
    }
    
    /**
     * Creates a clean driving history record.
     * 
     * @param yearsOfExperience years of driving experience
     * @param licenseDate license issue date
     * @return clean driving history
     */
    public static DrivingHistory clean(Integer yearsOfExperience, LocalDate licenseDate) {
        return new DrivingHistory(0, 0, yearsOfExperience, licenseDate);
    }
    
    // Getter methods for compatibility
    public Integer getAccidents() { return accidents; }
    public Integer getViolations() { return violations; }
    public Integer getYearsOfExperience() { return yearsOfExperience; }
    public LocalDate getLicenseDate() { return licenseDate; }
}
