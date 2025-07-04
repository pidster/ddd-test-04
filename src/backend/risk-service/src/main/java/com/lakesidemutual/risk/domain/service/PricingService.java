package com.lakesidemutual.risk.domain.service;

import com.lakesidemutual.risk.domain.model.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Set;

/**
 * Domain service responsible for calculating insurance pricing
 * based on risk assessment results.
 */
@Service
public class PricingService {
    
    // Base premium rates by policy type (monthly premiums)
    private static final Map<RiskProfileType, BigDecimal> BASE_PREMIUMS = Map.of(
        RiskProfileType.AUTO, new BigDecimal("150.00"),
        RiskProfileType.HOME, new BigDecimal("100.00"),
        RiskProfileType.LIFE, new BigDecimal("75.00"),
        RiskProfileType.HEALTH, new BigDecimal("400.00"),
        RiskProfileType.BUSINESS, new BigDecimal("500.00")
    );
    
    /**
     * Calculate risk multiplier based on risk score and factors
     */
    public BigDecimal calculateRiskMultiplier(RiskScore riskScore, Set<RiskFactor> riskFactors) {
        if (riskScore == null) {
            throw new IllegalArgumentException("Risk score cannot be null");
        }
        
        // Base multiplier from risk score
        BigDecimal multiplier = calculateScoreBasedMultiplier(riskScore);
        
        // Apply risk factors
        if (riskFactors != null && !riskFactors.isEmpty()) {
            multiplier = applyRiskFactors(multiplier, riskFactors);
        }
        
        // Ensure multiplier is within reasonable bounds
        multiplier = multiplier.max(new BigDecimal("0.5")); // Minimum 50% of base
        multiplier = multiplier.min(new BigDecimal("3.0")); // Maximum 300% of base
        
        return multiplier.setScale(3, RoundingMode.HALF_UP);
    }
    
    /**
     * Get base premium for a policy type
     */
    public BigDecimal getBasePremium(RiskProfileType policyType) {
        if (policyType == null) {
            throw new IllegalArgumentException("Policy type cannot be null");
        }
        
        return BASE_PREMIUMS.getOrDefault(policyType, new BigDecimal("200.00"));
    }
    
    /**
     * Calculate final premium based on base premium and risk multiplier
     */
    public BigDecimal calculateFinalPremium(BigDecimal basePremium, BigDecimal riskMultiplier) {
        if (basePremium == null || riskMultiplier == null) {
            throw new IllegalArgumentException("Base premium and risk multiplier cannot be null");
        }
        
        if (basePremium.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Base premium must be positive");
        }
        
        if (riskMultiplier.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Risk multiplier must be positive");
        }
        
        return basePremium.multiply(riskMultiplier).setScale(2, RoundingMode.HALF_UP);
    }
    
    /**
     * Calculate discount percentage for good drivers
     */
    public BigDecimal calculateDiscountPercentage(RiskScore riskScore, Set<RiskFactor> riskFactors) {
        if (riskScore == null) return BigDecimal.ZERO;
        
        BigDecimal discount = BigDecimal.ZERO;
        
        // Score-based discount
        if (riskScore.getValue() >= 750) {
            discount = discount.add(new BigDecimal("0.15")); // 15% discount
        } else if (riskScore.getValue() >= 700) {
            discount = discount.add(new BigDecimal("0.10")); // 10% discount
        } else if (riskScore.getValue() >= 650) {
            discount = discount.add(new BigDecimal("0.05")); // 5% discount
        }
        
        // Additional discounts for good driving record
        if (riskFactors != null) {
            long goodDriverFactors = riskFactors.stream()
                .filter(factor -> factor.getImpact() < 1.0)
                .count();
            
            if (goodDriverFactors > 0) {
                discount = discount.add(new BigDecimal("0.05")); // Additional 5% for good driving
            }
        }
        
        // Cap discount at 25%
        return discount.min(new BigDecimal("0.25"));
    }
    
    /**
     * Determine if a risk profile is insurable based on risk assessment
     */
    public boolean isInsurable(RiskScore riskScore, Set<RiskFactor> riskFactors) {
        if (riskScore == null) return false;
        
        // Reject if risk score is too low
        if (riskScore.getValue() < 350) {
            return false;
        }
        
        // Check for specific high-risk factors that make profile uninsurable
        if (riskFactors != null) {
            boolean hasExtremeRisk = riskFactors.stream()
                .anyMatch(factor -> factor.getImpact() >= 2.0); // 200% or higher impact
            
            if (hasExtremeRisk) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Calculate annual premium from monthly premium
     */
    public BigDecimal calculateAnnualPremium(BigDecimal monthlyPremium) {
        if (monthlyPremium == null) {
            throw new IllegalArgumentException("Monthly premium cannot be null");
        }
        
        // Apply small discount for annual payment (2%)
        BigDecimal annualDiscount = new BigDecimal("0.98");
        return monthlyPremium.multiply(new BigDecimal("12"))
                            .multiply(annualDiscount)
                            .setScale(2, RoundingMode.HALF_UP);
    }
    
    private BigDecimal calculateScoreBasedMultiplier(RiskScore riskScore) {
        int score = riskScore.getValue();
        
        // Convert score to multiplier using a sliding scale
        if (score >= 750) {
            return new BigDecimal("0.80"); // 20% discount
        } else if (score >= 700) {
            return new BigDecimal("0.90"); // 10% discount
        } else if (score >= 650) {
            return new BigDecimal("0.95"); // 5% discount
        } else if (score >= 600) {
            return new BigDecimal("1.00"); // Base rate
        } else if (score >= 550) {
            return new BigDecimal("1.10"); // 10% surcharge
        } else if (score >= 500) {
            return new BigDecimal("1.25"); // 25% surcharge
        } else if (score >= 450) {
            return new BigDecimal("1.50"); // 50% surcharge
        } else if (score >= 400) {
            return new BigDecimal("1.75"); // 75% surcharge
        } else {
            return new BigDecimal("2.00"); // 100% surcharge
        }
    }
    
    private BigDecimal applyRiskFactors(BigDecimal baseMultiplier, Set<RiskFactor> riskFactors) {
        BigDecimal adjustedMultiplier = baseMultiplier;
        
        for (RiskFactor factor : riskFactors) {
            // Apply factor impact to multiplier
            BigDecimal factorAdjustment = new BigDecimal(factor.getImpact().toString());
            adjustedMultiplier = adjustedMultiplier.multiply(factorAdjustment);
        }
        
        return adjustedMultiplier;
    }
}
