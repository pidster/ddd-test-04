package com.lakesidemutual.risk.domain.model;

import com.lakesidemutual.risk.domain.event.RiskProfileCreated;
import com.lakesidemutual.risk.domain.event.RiskProfileUpdated;
import org.springframework.data.domain.AbstractAggregateRoot;

import jakarta.persistence.*;

/**
 * RiskProfile aggregate root representing a customer's risk profile
 * for insurance underwriting purposes.
 * 
 * This aggregate encapsulates all risk-related information about a customer
 * and maintains invariants related to risk assessment data consistency.
 */
@Entity
@Table(name = "risk_profiles")
public class RiskProfile extends AbstractAggregateRoot<RiskProfile> {
    
    @Id
    @Column(name = "profile_id")
    private String profileId;
    
    @Column(name = "customer_id", nullable = false)
    private String customerId;
    
    @Column(name = "profile_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private RiskProfileType profileType;
    
    @Embedded
    private RiskScore currentRiskScore;
    
    @ElementCollection
    @CollectionTable(name = "risk_profile_factors", 
                    joinColumns = @JoinColumn(name = "profile_id"))
    private Set<RiskFactor> riskFactors = new HashSet<>();
    
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "accidents", column = @Column(name = "driving_accidents")),
        @AttributeOverride(name = "violations", column = @Column(name = "driving_violations")),
        @AttributeOverride(name = "yearsOfExperience", column = @Column(name = "driving_years_experience")),
        @AttributeOverride(name = "licenseDate", column = @Column(name = "driving_license_date"))
    })
    private DrivingHistory drivingHistory;
    
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "street", column = @Column(name = "address_street")),
        @AttributeOverride(name = "city", column = @Column(name = "address_city")),
        @AttributeOverride(name = "state", column = @Column(name = "address_state")),
        @AttributeOverride(name = "zipCode", column = @Column(name = "address_zip_code")),
        @AttributeOverride(name = "country", column = @Column(name = "address_country"))
    })
    private Address address;
    
    @Column(name = "age")
    private Integer age;
    
    @Column(name = "occupation")
    private String occupation;
    
    @Column(name = "annual_income")
    private Double annualIncome;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    @Version
    private Long version;

    // Default constructor for JPA
    protected RiskProfile() {}

    /**
     * Create a new RiskProfile for a customer
     */
    public RiskProfile(String customerId, RiskProfileType profileType, 
                      DrivingHistory drivingHistory, Address address,
                      Integer age, String occupation, Double annualIncome) {
        this.profileId = UUID.randomUUID().toString();
        this.customerId = Objects.requireNonNull(customerId, "Customer ID cannot be null");
        this.profileType = Objects.requireNonNull(profileType, "Profile type cannot be null");
        this.drivingHistory = drivingHistory;
        this.address = address;
        this.age = validateAge(age);
        this.occupation = occupation;
        this.annualIncome = validateAnnualIncome(annualIncome);
        this.riskFactors = new HashSet<>();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        
        // Initialize with default risk score
        this.currentRiskScore = RiskScore.defaultScore();
        
        // Publish domain event
        registerEvent(new RiskProfileCreated(this.profileId, this.customerId, this.profileType));
    }

    /**
     * Update risk factors for this profile
     */
    public void updateRiskFactors(Set<RiskFactor> newRiskFactors) {
        Objects.requireNonNull(newRiskFactors, "Risk factors cannot be null");
        
        this.riskFactors.clear();
        this.riskFactors.addAll(newRiskFactors);
        this.updatedAt = LocalDateTime.now();
        
        registerEvent(new RiskProfileUpdated(this.profileId, this.customerId));
    }

    /**
     * Update the current risk score
     */
    public void updateRiskScore(RiskScore newRiskScore) {
        Objects.requireNonNull(newRiskScore, "Risk score cannot be null");
        
        this.currentRiskScore = newRiskScore;
        this.updatedAt = LocalDateTime.now();
        
        registerEvent(new RiskProfileUpdated(this.profileId, this.customerId));
    }

    /**
     * Update driving history
     */
    public void updateDrivingHistory(DrivingHistory newDrivingHistory) {
        this.drivingHistory = newDrivingHistory;
        this.updatedAt = LocalDateTime.now();
        
        registerEvent(new RiskProfileUpdated(this.profileId, this.customerId));
    }

    /**
     * Update personal information
     */
    public void updatePersonalInfo(Address newAddress, Integer newAge, 
                                  String newOccupation, Double newAnnualIncome) {
        this.address = newAddress;
        this.age = validateAge(newAge);
        this.occupation = newOccupation;
        this.annualIncome = validateAnnualIncome(newAnnualIncome);
        this.updatedAt = LocalDateTime.now();
        
        registerEvent(new RiskProfileUpdated(this.profileId, this.customerId));
    }

    /**
     * Check if the risk profile is high risk based on current score
     */
    public boolean isHighRisk() {
        return currentRiskScore != null && currentRiskScore.isHighRisk();
    }

    /**
     * Get all risk factors of a specific type
     */
    public Set<RiskFactor> getRiskFactorsByType(RiskFactorType type) {
        return riskFactors.stream()
                .filter(factor -> factor.getType().equals(type))
                .collect(java.util.stream.Collectors.toSet());
    }

    private Integer validateAge(Integer age) {
        if (age != null && (age < 16 || age > 120)) {
            throw new IllegalArgumentException("Age must be between 16 and 120");
        }
        return age;
    }

    private Double validateAnnualIncome(Double income) {
        if (income != null && income < 0) {
            throw new IllegalArgumentException("Annual income cannot be negative");
        }
        return income;
    }

    // Getters
    public String getProfileId() { return profileId; }
    public String getCustomerId() { return customerId; }
    public RiskProfileType getProfileType() { return profileType; }
    public RiskScore getCurrentRiskScore() { return currentRiskScore; }
    public Set<RiskFactor> getRiskFactors() { return Collections.unmodifiableSet(riskFactors); }
    public DrivingHistory getDrivingHistory() { return drivingHistory; }
    public Address getAddress() { return address; }
    public Integer getAge() { return age; }
    public String getOccupation() { return occupation; }
    public Double getAnnualIncome() { return annualIncome; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public Long getVersion() { return version; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RiskProfile that = (RiskProfile) o;
        return Objects.equals(profileId, that.profileId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(profileId);
    }

    @Override
    public String toString() {
        return "RiskProfile{" +
                "profileId='" + profileId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", profileType=" + profileType +
                ", currentRiskScore=" + currentRiskScore +
                ", age=" + age +
                ", occupation='" + occupation + '\'' +
                '}';
    }
}
