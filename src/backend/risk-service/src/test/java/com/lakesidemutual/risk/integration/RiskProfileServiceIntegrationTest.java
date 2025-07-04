package com.lakesidemutual.risk.integration;

import com.lakesidemutual.risk.application.service.RiskProfileService;
import com.lakesidemutual.risk.domain.model.RiskProfile;
import com.lakesidemutual.risk.domain.model.RiskProfileType;
import com.lakesidemutual.risk.interfaces.dto.CreateRiskProfileRequest;
import com.lakesidemutual.risk.interfaces.dto.DrivingHistoryDto;
import com.lakesidemutual.risk.interfaces.dto.AddressDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test for RiskProfileService
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class RiskProfileServiceIntegrationTest {

    @Autowired
    private RiskProfileService riskProfileService;

    @Test
    public void testCreateRiskProfile() {
        // Given
        CreateRiskProfileRequest request = new CreateRiskProfileRequest(
            "TEST-001",
            RiskProfileType.AUTO,
            new DrivingHistoryDto(0, 0, 5, "2020-01-01"),
            new AddressDto("123 Test St", "Test City", "TS", "12345", "USA"),
            25,
            "Software Developer",
            60000.0
        );

        // When
        RiskProfile profile = riskProfileService.createRiskProfile(request);

        // Then
        assertNotNull(profile);
        assertEquals("TEST-001", profile.getCustomerId());
        assertEquals(RiskProfileType.AUTO, profile.getProfileType());
        assertNotNull(profile.getCurrentRiskScore());
        assertTrue(profile.getRiskFactors().size() > 0);
    }
}
