package com.lakesidemutual.risk.domain.service;

import com.lakesidemutual.risk.domain.model.*;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Domain service responsible for calculating risk scores based on various factors.
 * This service encapsulates the business logic for risk evaluation that doesn't
 * naturally belong to any specific aggregate.
 */
@Service
public class RiskCalculationService {
    
    private static final double BASE_SCORE = 500.0;
    private static final double MAX_SCORE = 850.0;
    private static final double MIN_SCORE = 300.0;
    
    /**
     * Calculate risk score for a given risk profile
     */
    public RiskScore calculateRiskScore(RiskProfile profile) {
        if (profile == null) {
            throw new IllegalArgumentException("Risk profile cannot be null");
        }
        
        double score = BASE_SCORE;
        
        // Apply age-based adjustments
        score = applyAgeAdjustments(score, profile.getAge());
        
        // Apply driving history adjustments
        score = applyDrivingHistoryAdjustments(score, profile.getDrivingHistory());
        
        // Apply occupation adjustments
        score = applyOccupationAdjustments(score, profile.getOccupation());
        
        // Apply income adjustments
        score = applyIncomeAdjustments(score, profile.getAnnualIncome());
        
        // Apply risk factors
        score = applyRiskFactors(score, profile.getRiskFactors());
        
        // Apply location-based adjustments
        score = applyLocationAdjustments(score, profile.getAddress());
        
        // Ensure score is within bounds
        score = Math.max(MIN_SCORE, Math.min(MAX_SCORE, score));
        
        return new RiskScore((int) Math.round(score));
    }
    
    /**
     * Calculate risk factors for a given profile
     */
    public Set<RiskFactor> calculateRiskFactors(RiskProfile profile) {
        Set<RiskFactor> factors = new java.util.HashSet<>();
        
        // Age-related factors
        if (profile.getAge() != null) {
            if (profile.getAge() < 25) {
                factors.add(new RiskFactor(RiskFactorType.DEMOGRAPHIC, "Young driver", 1.2));
            } else if (profile.getAge() > 65) {
                factors.add(new RiskFactor(RiskFactorType.DEMOGRAPHIC, "Senior driver", 1.1));
            }
        }
        
        // Driving history factors
        DrivingHistory history = profile.getDrivingHistory();
        if (history != null) {
            if (history.getAccidents() > 0) {
                double multiplier = 1.0 + (history.getAccidents() * 0.3);
                factors.add(new RiskFactor(RiskFactorType.DRIVING_HISTORY, 
                    "Previous accidents: " + history.getAccidents(), multiplier));
            }
            
            if (history.getViolations() > 0) {
                double multiplier = 1.0 + (history.getViolations() * 0.2);
                factors.add(new RiskFactor(RiskFactorType.DRIVING_HISTORY, 
                    "Traffic violations: " + history.getViolations(), multiplier));
            }
            
            if (history.getYearsOfExperience() != null && history.getYearsOfExperience() < 2) {
                factors.add(new RiskFactor(RiskFactorType.DRIVING_HISTORY, 
                    "Limited driving experience", 1.3));
            }
        }
        
        // Occupation factors
        if (profile.getOccupation() != null) {
            factors.addAll(getOccupationRiskFactors(profile.getOccupation()));
        }
        
        // Location factors
        if (profile.getAddress() != null) {
            factors.addAll(getLocationRiskFactors(profile.getAddress()));
        }
        
        return factors;
    }
    
    private double applyAgeAdjustments(double score, Integer age) {
        if (age == null) return score;
        
        if (age < 25) {
            return score - 50; // Young drivers are higher risk
        } else if (age >= 25 && age <= 65) {
            return score + 25; // Prime age drivers are lower risk
        } else {
            return score - 25; // Senior drivers have slightly higher risk
        }
    }
    
    private double applyDrivingHistoryAdjustments(double score, DrivingHistory history) {
        if (history == null) return score;
        
        // Penalize for accidents
        score -= (history.getAccidents() * 75);
        
        // Penalize for violations
        score -= (history.getViolations() * 50);
        
        // Bonus for experience
        if (history.getYearsOfExperience() != null) {
            if (history.getYearsOfExperience() >= 10) {
                score += 50;
            } else if (history.getYearsOfExperience() < 2) {
                score -= 75;
            }
        }
        
        return score;
    }
    
    private double applyOccupationAdjustments(double score, String occupation) {
        if (occupation == null) return score;
        
        String lowerOccupation = occupation.toLowerCase();
        
        // High-risk occupations
        if (lowerOccupation.contains("driver") || lowerOccupation.contains("pilot") || 
            lowerOccupation.contains("construction")) {
            return score - 40;
        }
        
        // Low-risk occupations
        if (lowerOccupation.contains("teacher") || lowerOccupation.contains("accountant") || 
            lowerOccupation.contains("engineer")) {
            return score + 30;
        }
        
        return score;
    }
    
    private double applyIncomeAdjustments(double score, Double annualIncome) {
        if (annualIncome == null) return score;
        
        if (annualIncome >= 100000) {
            return score + 25; // Higher income typically correlates with lower risk
        } else if (annualIncome < 30000) {
            return score - 25;
        }
        
        return score;
    }
    
    private double applyRiskFactors(double score, Set<RiskFactor> riskFactors) {
        if (riskFactors == null || riskFactors.isEmpty()) return score;
        
        for (RiskFactor factor : riskFactors) {
            // Apply risk factor impact (assuming negative impact reduces score)
            double impact = (factor.getImpact() - 1.0) * -100;
            score += impact;
        }
        
        return score;
    }
    
    private double applyLocationAdjustments(double score, Address address) {
        if (address == null) return score;
        
        // Simple state-based adjustments (in reality, this would be more sophisticated)
        String state = address.getState();
        if (state != null) {
            switch (state.toUpperCase()) {
                case "CA", "NY", "FL" -> score -= 30; // High-risk states
                case "ND", "SD", "WY" -> score += 30; // Low-risk states
                default -> score += 0;
            }
        }
        
        return score;
    }
    
    private Set<RiskFactor> getOccupationRiskFactors(String occupation) {
        Set<RiskFactor> factors = new java.util.HashSet<>();
        String lowerOccupation = occupation.toLowerCase();
        
        if (lowerOccupation.contains("driver")) {
            factors.add(new RiskFactor(RiskFactorType.OCCUPATION, "Professional driver", 1.4));
        } else if (lowerOccupation.contains("pilot")) {
            factors.add(new RiskFactor(RiskFactorType.OCCUPATION, "Pilot", 1.3));
        } else if (lowerOccupation.contains("construction")) {
            factors.add(new RiskFactor(RiskFactorType.OCCUPATION, "Construction worker", 1.2));
        }
        
        return factors;
    }
    
    private Set<RiskFactor> getLocationRiskFactors(Address address) {
        Set<RiskFactor> factors = new java.util.HashSet<>();
        
        if (address.getState() != null) {
            String state = address.getState().toUpperCase();
            switch (state) {
                case "CA", "NY", "FL" -> 
                    factors.add(new RiskFactor(RiskFactorType.LOCATION, "High-risk state: " + state, 1.2));
                case "ND", "SD", "WY" -> 
                    factors.add(new RiskFactor(RiskFactorType.LOCATION, "Low-risk state: " + state, 0.9));
            }
        }
        
        return factors;
    }
}
