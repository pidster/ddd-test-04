package com.lakesidemutual.risk.interfaces.rest;

import com.lakesidemutual.risk.application.service.RiskAssessmentService;
import com.lakesidemutual.risk.domain.model.RiskAssessment;
import com.lakesidemutual.risk.interfaces.dto.RiskAssessmentRequest;
import com.lakesidemutual.risk.interfaces.dto.RiskAssessmentResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * REST Controller for Risk Assessment operations.
 * Handles risk assessment creation, retrieval, and recalculation.
 */
@RestController
@RequestMapping("/api/v1/risk/assessments")
@CrossOrigin(origins = "*")
public class RiskAssessmentController {

    private final RiskAssessmentService riskAssessmentService;

    public RiskAssessmentController(RiskAssessmentService riskAssessmentService) {
        this.riskAssessmentService = riskAssessmentService;
    }

    /**
     * Perform risk assessment for quote generation.
     * 
     * @param request The risk assessment request
     * @return Risk assessment response with pricing
     */
    @PostMapping
    public ResponseEntity<RiskAssessmentResponse> performAssessment(@Valid @RequestBody RiskAssessmentRequest request) {
        RiskAssessment assessment = riskAssessmentService.performAssessment(request);
        RiskAssessmentResponse response = convertToResponse(assessment);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get assessment history for a customer.
     * 
     * @param customerId The customer identifier
     * @return List of risk assessments for the customer
     */
    @GetMapping("/{customerId}")
    public ResponseEntity<List<RiskAssessmentResponse>> getAssessmentHistory(@PathVariable String customerId) {
        List<RiskAssessment> assessments = riskAssessmentService.getAssessmentHistory(customerId);
        List<RiskAssessmentResponse> responses = assessments.stream()
                .map(this::convertToResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    /**
     * Recalculate an existing assessment.
     * 
     * @param assessmentId The assessment identifier
     * @return Updated risk assessment response
     */
    @PutMapping("/{assessmentId}/recalculate")
    public ResponseEntity<RiskAssessmentResponse> recalculateAssessment(@PathVariable String assessmentId) {
        Optional<RiskAssessment> recalculatedAssessment = riskAssessmentService.recalculateAssessment(assessmentId);
        
        if (recalculatedAssessment.isPresent()) {
            RiskAssessmentResponse response = convertToResponse(recalculatedAssessment.get());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Convert domain model to response DTO.
     */
    private RiskAssessmentResponse convertToResponse(RiskAssessment assessment) {
        return riskAssessmentService.convertToResponse(assessment);
    }
}
