package com.lakesidemutual.risk.application.service;

import com.lakesidemutual.risk.domain.model.*;
import com.lakesidemutual.risk.domain.repository.RiskProfileRepository;
import com.lakesidemutual.risk.domain.service.RiskCalculationService;
import com.lakesidemutual.risk.interfaces.dto.CreateRiskProfileRequest;
import com.lakesidemutual.risk.interfaces.dto.DrivingHistoryDto;
import com.lakesidemutual.risk.interfaces.dto.AddressDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Optional;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for RiskProfileService
 */
class RiskProfileServiceTest {

    private RiskProfileRepository riskProfileRepository;
    private RiskCalculationService riskCalculationService;
    private RiskProfileService riskProfileService;

    @BeforeEach
    void setUp() {
        // Create test implementations instead of mocks
        riskProfileRepository = new TestRiskProfileRepository();
        riskCalculationService = new RiskCalculationService();
        riskProfileService = new RiskProfileService(riskProfileRepository, riskCalculationService);
    }

    @Test
    @DisplayName("Should create new risk profile successfully")
    void shouldCreateNewRiskProfileSuccessfully() {
        // Given
        String customerId = "CUST-001";
        CreateRiskProfileRequest request = new CreateRiskProfileRequest(
            customerId,
            RiskProfileType.AUTO,
            new DrivingHistoryDto(0, 0, 10, "2010-01-01"),
            new AddressDto("123 Main St", "Anytown", "CA", "12345", "USA"),
            30,
            "Software Engineer",
            75000.0
        );

        // When
        RiskProfile result = riskProfileService.createRiskProfile(request);

        // Then
        assertNotNull(result);
        assertEquals(customerId, result.getCustomerId());
        assertEquals(RiskProfileType.AUTO, result.getProfileType());
        assertNotNull(result.getCurrentRiskScore());
        assertNotNull(result.getRiskFactors());
        assertTrue(result.getRiskFactors().size() > 0);
    }

    @Test
    @DisplayName("Should find risk profile by customer ID")
    void shouldFindRiskProfileByCustomerId() {
        // Given
        String customerId = "CUST-002";
        CreateRiskProfileRequest request = new CreateRiskProfileRequest(
            customerId,
            RiskProfileType.AUTO,
            new DrivingHistoryDto(0, 0, 10, "2010-01-01"),
            new AddressDto("456 Oak Ave", "Boston", "MA", "02102", "USA"),
            35,
            "Teacher",
            50000.0
        );
        
        // Create the profile first
        riskProfileService.createRiskProfile(request);

        // When
        Optional<RiskProfile> result = riskProfileService.findByCustomerId(customerId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(customerId, result.get().getCustomerId());
    }

    @Test
    @DisplayName("Should return empty when profile not found")
    void shouldReturnEmptyWhenProfileNotFound() {
        // Given
        String nonExistentCustomerId = "CUST-999";

        // When
        Optional<RiskProfile> result = riskProfileService.findByCustomerId(nonExistentCustomerId);

        // Then
        assertFalse(result.isPresent());
    }

    /**
     * Test implementation of RiskProfileRepository for testing purposes
     */
    private static class TestRiskProfileRepository implements RiskProfileRepository {
        private final Map<String, RiskProfile> profiles = new HashMap<>();

        @Override
        public RiskProfile save(RiskProfile riskProfile) {
            String key = riskProfile.getCustomerId() + "_" + riskProfile.getProfileType();
            profiles.put(key, riskProfile);
            return riskProfile;
        }

        @Override
        public Optional<RiskProfile> findById(String profileId) {
            return profiles.values().stream()
                    .filter(profile -> profile.getProfileId().equals(profileId))
                    .findFirst();
        }

        @Override
        public List<RiskProfile> findByCustomerId(String customerId) {
            return profiles.values().stream()
                    .filter(profile -> profile.getCustomerId().equals(customerId))
                    .collect(Collectors.toList());
        }

        @Override
        public Optional<RiskProfile> findByCustomerIdAndProfileType(String customerId, RiskProfileType profileType) {
            String key = customerId + "_" + profileType;
            return Optional.ofNullable(profiles.get(key));
        }

        @Override
        public List<RiskProfile> findByProfileType(RiskProfileType profileType) {
            return profiles.values().stream()
                    .filter(profile -> profile.getProfileType() == profileType)
                    .collect(Collectors.toList());
        }

        @Override
        public List<RiskProfile> findHighRiskProfiles() {
            return profiles.values().stream()
                    .filter(profile -> profile.getCurrentRiskScore().getCategory() == RiskCategory.HIGH ||
                                     profile.getCurrentRiskScore().getCategory() == RiskCategory.VERY_HIGH)
                    .collect(Collectors.toList());
        }

        @Override
        public void delete(RiskProfile riskProfile) {
            String key = riskProfile.getCustomerId() + "_" + riskProfile.getProfileType();
            profiles.remove(key);
        }

        @Override
        public boolean existsByCustomerIdAndProfileType(String customerId, RiskProfileType profileType) {
            String key = customerId + "_" + profileType;
            return profiles.containsKey(key);
        }

        @Override
        public List<RiskProfile> findAll() {
            return new ArrayList<>(profiles.values());
        }
    }
}
