package com.lakesidemutual.risk.application.service;

import com.lakesidemutual.risk.domain.model.*;
import com.lakesidemutual.risk.domain.repository.RiskProfileRepository;
import com.lakesidemutual.risk.domain.service.RiskCalculationService;
import com.lakesidemutual.risk.interfaces.dto.CreateRiskProfileRequest;
import com.lakesidemutual.risk.interfaces.dto.RiskProfileResponse;
import com.lakesidemutual.risk.interfaces.dto.RiskFactorDto;
import com.lakesidemutual.risk.interfaces.dto.RiskScoreDto;
import com.lakesidemutual.risk.interfaces.dto.UpdateRiskFactorRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Application service for managing risk profiles.
 * Orchestrates domain operations and maintains transaction boundaries.
 */
@Service
@Transactional
public class RiskProfileService {
    
    private final RiskProfileRepository riskProfileRepository;
    private final RiskCalculationService riskCalculationService;
    
    @Autowired
    public RiskProfileService(RiskProfileRepository riskProfileRepository,
                             RiskCalculationService riskCalculationService) {
        this.riskProfileRepository = riskProfileRepository;
        this.riskCalculationService = riskCalculationService;
    }
    
    /**
     * Create a new risk profile for a customer
     */
    public RiskProfile createRiskProfile(String customerId, RiskProfileType profileType,
                                        DrivingHistory drivingHistory, Address address,
                                        Integer age, String occupation, Double annualIncome) {
        
        // Check if profile already exists for this customer and type
        if (riskProfileRepository.existsByCustomerIdAndProfileType(customerId, profileType)) {
            throw new IllegalArgumentException("Risk profile already exists for customer " + 
                customerId + " and type " + profileType);
        }
        
        // Create new profile
        RiskProfile profile = new RiskProfile(
            customerId, profileType, drivingHistory, address, age, occupation, annualIncome
        );
        
        // Calculate initial risk factors and score
        Set<RiskFactor> riskFactors = riskCalculationService.calculateRiskFactors(profile);
        profile.updateRiskFactors(riskFactors);
        
        RiskScore calculatedScore = riskCalculationService.calculateRiskScore(profile);
        profile.updateRiskScore(calculatedScore);
        
        return riskProfileRepository.save(profile);
    }
    
    /**
     * Update an existing risk profile
     */
    public RiskProfile updateRiskProfile(String profileId, DrivingHistory drivingHistory,
                                        Address address, Integer age, String occupation,
                                        Double annualIncome) {
        
        RiskProfile profile = riskProfileRepository.findById(profileId)
            .orElseThrow(() -> new IllegalArgumentException("Risk profile not found: " + profileId));
        
        // Update profile information
        profile.updatePersonalInfo(address, age, occupation, annualIncome);
        profile.updateDrivingHistory(drivingHistory);
        
        // Recalculate risk factors and score
        Set<RiskFactor> riskFactors = riskCalculationService.calculateRiskFactors(profile);
        profile.updateRiskFactors(riskFactors);
        
        RiskScore calculatedScore = riskCalculationService.calculateRiskScore(profile);
        profile.updateRiskScore(calculatedScore);
        
        return riskProfileRepository.save(profile);
    }
    
    /**
     * Recalculate risk score for a profile
     */
    public RiskProfile recalculateRiskScore(String profileId) {
        RiskProfile profile = riskProfileRepository.findById(profileId)
            .orElseThrow(() -> new IllegalArgumentException("Risk profile not found: " + profileId));
        
        Set<RiskFactor> riskFactors = riskCalculationService.calculateRiskFactors(profile);
        profile.updateRiskFactors(riskFactors);
        
        RiskScore calculatedScore = riskCalculationService.calculateRiskScore(profile);
        profile.updateRiskScore(calculatedScore);
        
        return riskProfileRepository.save(profile);
    }
    
    /**
     * Get risk profile by ID
     */
    @Transactional(readOnly = true)
    public Optional<RiskProfile> getRiskProfile(String profileId) {
        return riskProfileRepository.findById(profileId);
    }
    
    /**
     * Get risk profiles for a customer
     */
    @Transactional(readOnly = true)
    public List<RiskProfile> getRiskProfilesForCustomer(String customerId) {
        return riskProfileRepository.findByCustomerId(customerId);
    }
    
    /**
     * Get risk profile for a customer and specific profile type
     */
    @Transactional(readOnly = true)
    public Optional<RiskProfile> getRiskProfileForCustomerAndType(String customerId, 
                                                                 RiskProfileType profileType) {
        return riskProfileRepository.findByCustomerIdAndProfileType(customerId, profileType);
    }
    
    /**
     * Get all high-risk profiles for review
     */
    @Transactional(readOnly = true)
    public List<RiskProfile> getHighRiskProfiles() {
        return riskProfileRepository.findHighRiskProfiles();
    }
    
    /**
     * Get all risk profiles by type
     */
    @Transactional(readOnly = true)
    public List<RiskProfile> getRiskProfilesByType(RiskProfileType profileType) {
        return riskProfileRepository.findByProfileType(profileType);
    }
    
    /**
     * Delete a risk profile
     */
    public void deleteRiskProfile(String profileId) {
        RiskProfile profile = riskProfileRepository.findById(profileId)
            .orElseThrow(() -> new IllegalArgumentException("Risk profile not found: " + profileId));
        
        riskProfileRepository.delete(profile);
    }
    
    /**
     * Check if customer has a risk profile of specific type
     */
    @Transactional(readOnly = true)
    public boolean hasRiskProfile(String customerId, RiskProfileType profileType) {
        return riskProfileRepository.existsByCustomerIdAndProfileType(customerId, profileType);
    }
    
    /**
     * Find risk profile by customer ID (required by controller)
     */
    @Transactional(readOnly = true)
    public Optional<RiskProfile> findByCustomerId(String customerId) {
        List<RiskProfile> profiles = riskProfileRepository.findByCustomerId(customerId);
        return profiles.isEmpty() ? Optional.empty() : Optional.of(profiles.get(0));
    }
    
    /**
     * Create risk profile from DTO request
     */
    public RiskProfile createRiskProfile(CreateRiskProfileRequest request) {
        // Convert DTO to domain objects
        DrivingHistory drivingHistory = convertDrivingHistory(request.drivingHistory());
        Address address = convertAddress(request.address());
        
        return createRiskProfile(
            request.customerId(),
            RiskProfileType.AUTO, // Default for MVP
            drivingHistory,
            address,
            request.age(),
            request.occupation(),
            request.annualIncome()
        );
    }
    
    /**
     * Update a specific risk factor
     */
    public Optional<RiskProfile> updateRiskFactor(String customerId, String factorType, UpdateRiskFactorRequest request) {
        Optional<RiskProfile> profileOpt = findByCustomerId(customerId);
        if (profileOpt.isEmpty()) {
            return Optional.empty();
        }
        
        RiskProfile profile = profileOpt.get();
        RiskFactorType riskFactorType = RiskFactorType.valueOf(factorType.toUpperCase());
        
        // Create new risk factor
        RiskFactor newFactor = new RiskFactor(
            riskFactorType,
            request.description(),
            request.impact()
        );
        
        // Add the new factor to existing factors
        Set<RiskFactor> currentFactors = new java.util.HashSet<>(profile.getRiskFactors());
        currentFactors.add(newFactor);
        profile.updateRiskFactors(currentFactors);
        
        // Recalculate risk score
        RiskScore calculatedScore = riskCalculationService.calculateRiskScore(profile);
        profile.updateRiskScore(calculatedScore);
        
        return Optional.of(riskProfileRepository.save(profile));
    }
    
    /**
     * Convert domain model to response DTO
     */
    public RiskProfileResponse convertToResponse(RiskProfile profile) {
        return RiskProfileResponse.fromDomain(profile);
    }
    
    /**
     * Convert DTO to domain driving history
     */
    private DrivingHistory convertDrivingHistory(com.lakesidemutual.risk.interfaces.dto.DrivingHistoryDto dto) {
        if (dto == null) {
            return null;
        }
        return dto.toDomain();
    }
    
    /**
     * Convert DTO to domain address
     */
    private Address convertAddress(com.lakesidemutual.risk.interfaces.dto.AddressDto dto) {
        if (dto == null) {
            return null;
        }
        return dto.toDomain();
    }
}
