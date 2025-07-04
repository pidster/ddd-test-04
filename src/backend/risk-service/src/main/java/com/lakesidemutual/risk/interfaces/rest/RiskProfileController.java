package com.lakesidemutual.risk.interfaces.rest;

import com.lakesidemutual.risk.application.service.RiskProfileService;
import com.lakesidemutual.risk.domain.model.RiskProfile;
import com.lakesidemutual.risk.domain.model.RiskProfileType;
import com.lakesidemutual.risk.interfaces.dto.CreateRiskProfileRequest;
import com.lakesidemutual.risk.interfaces.dto.RiskProfileResponse;
import com.lakesidemutual.risk.interfaces.dto.UpdateRiskFactorRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST Controller for Risk Profile management operations.
 * Handles customer risk profile creation, retrieval, and updates.
 */
@RestController
@RequestMapping("/api/v1/risk/profiles")
@CrossOrigin(origins = "*")
@Validated
public class RiskProfileController {

    private final RiskProfileService riskProfileService;

    public RiskProfileController(RiskProfileService riskProfileService) {
        this.riskProfileService = riskProfileService;
    }

    /**
     * Get customer risk profile by customer ID.
     * 
     * @param customerId The customer identifier
     * @return Risk profile response or 404 if not found
     */
    @GetMapping("/{customerId}")
    public ResponseEntity<RiskProfileResponse> getRiskProfile(@PathVariable @NotBlank String customerId) {
        Optional<RiskProfile> riskProfile = riskProfileService.findByCustomerId(customerId);
        
        if (riskProfile.isPresent()) {
            RiskProfileResponse response = convertToResponse(riskProfile.get());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get all risk profiles for a customer
     */
    @GetMapping("/customer/{customerId}/all")
    public ResponseEntity<List<RiskProfileResponse>> getAllRiskProfilesForCustomer(
            @PathVariable @NotBlank String customerId) {
        
        List<RiskProfile> profiles = riskProfileService.getRiskProfilesForCustomer(customerId);
        if (profiles.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        List<RiskProfileResponse> responses = profiles.stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(responses);
    }

    /**
     * Get risk profiles by type
     */
    @GetMapping("/type/{profileType}")
    public ResponseEntity<List<RiskProfileResponse>> getRiskProfilesByType(
            @PathVariable @NotBlank String profileType) {
        
        try {
            RiskProfileType type = RiskProfileType.valueOf(profileType.toUpperCase());
            List<RiskProfile> profiles = riskProfileService.getRiskProfilesByType(type);
            
            List<RiskProfileResponse> responses = profiles.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(responses);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get high-risk profiles
     */
    @GetMapping("/high-risk")
    public ResponseEntity<List<RiskProfileResponse>> getHighRiskProfiles() {
        List<RiskProfile> profiles = riskProfileService.getHighRiskProfiles();
        
        List<RiskProfileResponse> responses = profiles.stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(responses);
    }

    /**
     * Create or update a risk profile.
     * 
     * @param request The risk profile creation request
     * @return Created risk profile response
     */
    @PostMapping
    public ResponseEntity<RiskProfileResponse> createRiskProfile(@Valid @RequestBody CreateRiskProfileRequest request) {
        try {
            RiskProfile riskProfile = riskProfileService.createRiskProfile(request);
            RiskProfileResponse response = convertToResponse(riskProfile);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Update a specific risk factor for a customer.
     * 
     * @param customerId The customer identifier
     * @param factorType The risk factor type to update
     * @param request The update request
     * @return Updated risk profile response
     */
    @PutMapping("/{customerId}/factors/{factorType}")
    public ResponseEntity<RiskProfileResponse> updateRiskFactor(
            @PathVariable @NotBlank String customerId,
            @PathVariable @NotBlank String factorType,
            @Valid @RequestBody UpdateRiskFactorRequest request) {
        
        try {
            Optional<RiskProfile> updatedProfile = riskProfileService.updateRiskFactor(customerId, factorType, request);
            
            if (updatedProfile.isPresent()) {
                RiskProfileResponse response = convertToResponse(updatedProfile.get());
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Recalculate risk score for a profile
     */
    @PostMapping("/{customerId}/recalculate")
    public ResponseEntity<RiskProfileResponse> recalculateRiskScore(@PathVariable @NotBlank String customerId) {
        try {
            RiskProfile profile = riskProfileService.recalculateRiskScore(customerId);
            RiskProfileResponse response = convertToResponse(profile);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Check if customer has a risk profile
     */
    @GetMapping("/customer/{customerId}/exists")
    public ResponseEntity<Boolean> hasRiskProfile(
            @PathVariable @NotBlank String customerId,
            @RequestParam(required = false, defaultValue = "AUTO") String profileType) {
        
        try {
            RiskProfileType type = RiskProfileType.valueOf(profileType.toUpperCase());
            boolean exists = riskProfileService.hasRiskProfile(customerId, type);
            return ResponseEntity.ok(exists);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Convert domain model to response DTO.
     */
    private RiskProfileResponse convertToResponse(RiskProfile riskProfile) {
        return riskProfileService.convertToResponse(riskProfile);
    }
}
