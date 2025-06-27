# MVP Risk Service Implementation Plan

## Overview

This document provides a detailed implementation plan for the Minimum Viable Product (MVP) of the Risk Assessment Service - one of the core bounded contexts in the Lakeside Mutual insurance application. The Risk Service is strategically positioned as a **Complex Core** domain, requiring sophisticated risk evaluation and pricing algorithms.

## MVP Scope Definition

### What's Included in MVP
- **Core Risk Assessment**: Basic risk factor evaluation for auto insurance
- **Simple Pricing Engine**: Rule-based premium calculation
- **Risk Profile Management**: Create and update customer risk profiles
- **Integration Points**: Event consumption from Customer and Policy contexts
- **REST API**: Synchronous interfaces for quote generation
- **Basic Analytics**: Risk score distribution and pricing metrics

### What's Excluded from MVP
- Machine learning algorithms (Phase 2)
- External data provider integrations (Phase 2)
- Advanced fraud detection (Phase 2)
- Regulatory reporting (Phase 2)
- Historical risk analytics (Phase 2)
- Real-time risk monitoring (Phase 2)

## Domain Model for MVP

### Core Aggregates

#### Risk Profile Aggregate
```java
public class RiskProfile extends AggregateRoot<RiskProfileId> {
    private CustomerId customerId;
    private RiskScore overallScore;
    private Set<RiskFactor> riskFactors;
    private RiskCategory category;
    private LocalDateTime lastAssessment;
    private RiskProfileStatus status;
    
    // Business methods
    public void addRiskFactor(RiskFactor factor);
    public void updateRiskFactor(RiskFactorType type, Object value);
    public RiskScore calculateOverallScore();
    public void reassess();
}
```

#### Risk Assessment Aggregate
```java
public class RiskAssessment extends AggregateRoot<RiskAssessmentId> {
    private CustomerId customerId;
    private PolicyType policyType;
    private AssessmentRequest request;
    private AssessmentResult result;
    private LocalDateTime assessedAt;
    private UserId assessedBy;
    
    // Business methods
    public void performAssessment(RiskCalculationService service);
    public Money calculatePremium(PricingRules rules);
}
```

### Value Objects

#### Risk Score
```java
public record RiskScore(int value) {
    public RiskScore {
        if (value < 0 || value > 1000) {
            throw new IllegalArgumentException("Risk score must be between 0 and 1000");
        }
    }
    
    public RiskCategory getCategory() {
        if (value <= 200) return RiskCategory.LOW;
        if (value <= 500) return RiskCategory.MEDIUM;
        if (value <= 800) return RiskCategory.HIGH;
        return RiskCategory.VERY_HIGH;
    }
    
    public BigDecimal getMultiplier() {
        return switch (getCategory()) {
            case LOW -> BigDecimal.valueOf(0.8);
            case MEDIUM -> BigDecimal.valueOf(1.0);
            case HIGH -> BigDecimal.valueOf(1.5);
            case VERY_HIGH -> BigDecimal.valueOf(2.0);
        };
    }
}
```

#### Risk Factor
```java
public record RiskFactor(
    RiskFactorType type,
    Object value,
    int weight,
    LocalDateTime updatedAt
) {
    public int calculateScore() {
        return type.calculateScore(value) * weight;
    }
}

public enum RiskFactorType {
    AGE(customer -> calculateAgeScore((Integer) customer)),
    DRIVING_HISTORY(history -> calculateDrivingScore((DrivingHistory) history)),
    LOCATION(address -> calculateLocationScore((Address) address)),
    VEHICLE_TYPE(vehicle -> calculateVehicleScore((Vehicle) vehicle)),
    CREDIT_SCORE(score -> calculateCreditScore((Integer) score));
    
    private final Function<Object, Integer> calculator;
    
    public int calculateScore(Object value) {
        return calculator.apply(value);
    }
}
```

## API Design

### REST Endpoints

#### Risk Profile Management
```yaml
# Get customer risk profile
GET /api/v1/risk/profiles/{customerId}
Response: RiskProfileResponse

# Create/update risk profile
POST /api/v1/risk/profiles
Request: CreateRiskProfileRequest
Response: RiskProfileResponse

# Update specific risk factor
PUT /api/v1/risk/profiles/{customerId}/factors/{factorType}
Request: UpdateRiskFactorRequest
Response: RiskProfileResponse
```

#### Risk Assessment
```yaml
# Perform risk assessment for quote
POST /api/v1/risk/assessments
Request: RiskAssessmentRequest
Response: RiskAssessmentResponse

# Get assessment history
GET /api/v1/risk/assessments/{customerId}
Response: List<RiskAssessmentResponse>

# Recalculate existing assessment
PUT /api/v1/risk/assessments/{assessmentId}/recalculate
Response: RiskAssessmentResponse
```

#### Analytics (Read-only)
```yaml
# Get risk distribution metrics
GET /api/v1/risk/analytics/distribution
Response: RiskDistributionResponse

# Get pricing metrics
GET /api/v1/risk/analytics/pricing
Response: PricingMetricsResponse
```

### DTOs

#### Request DTOs
```java
public record CreateRiskProfileRequest(
    String customerId,
    Integer age,
    String drivingLicenseNumber,
    DrivingHistoryRequest drivingHistory,
    AddressRequest address,
    VehicleRequest vehicle
) {}

public record RiskAssessmentRequest(
    String customerId,
    String policyType,
    List<CoverageRequest> requestedCoverages,
    Map<String, Object> additionalFactors
) {}
```

#### Response DTOs
```java
public record RiskProfileResponse(
    String customerId,
    int overallScore,
    String riskCategory,
    List<RiskFactorResponse> factors,
    LocalDateTime lastAssessment,
    String status
) {}

public record RiskAssessmentResponse(
    String assessmentId,
    String customerId,
    int riskScore,
    String riskCategory,
    BigDecimal recommendedPremium,
    List<RiskFactorResponse> factors,
    LocalDateTime assessedAt
) {}
```

## Implementation Plan

### Sprint 1: Core Infrastructure (Week 1)

#### Tasks
- [ ] **Project Setup**
  - Maven project structure with Spring Boot 3.x
  - Database configuration (PostgreSQL)
  - Docker configuration
  - Basic CI/CD pipeline

- [ ] **Domain Layer Foundation**
  - Risk Profile aggregate implementation
  - Risk Score and Risk Factor value objects
  - Domain exceptions hierarchy
  - Repository interfaces

- [ ] **Infrastructure Layer**
  - JPA entities and mappings
  - Repository implementations
  - Database migration scripts (Flyway)

**Acceptance Criteria:**
- Project builds successfully with all dependencies
- Database schema created and migrated
- Basic aggregate operations work (save/load)
- Unit tests pass with >80% coverage

### Sprint 2: Risk Calculation Engine (Week 2)

#### Tasks
- [ ] **Risk Calculation Service**
  - Age-based risk calculation
  - Driving history evaluation
  - Location-based risk assessment
  - Vehicle type risk factors

- [ ] **Pricing Engine**
  - Base premium calculation
  - Risk multiplier application
  - Coverage-specific adjustments
  - Pricing rules configuration

- [ ] **Domain Services**
  - RiskCalculationService implementation
  - PricingService implementation
  - Risk factor validation

**Acceptance Criteria:**
- Risk scores calculated correctly for all factor types
- Premium calculations produce accurate results
- Business rules properly enforced
- Comprehensive unit tests for all calculations

### Sprint 3: Event Integration (Week 3)

#### Tasks
- [ ] **Event Consumers**
  - CustomerRegistered event handler
  - CustomerProfileUpdated event handler
  - PolicyCreated event handler
  - Address change event handler

- [ ] **Event Publishers**
  - RiskProfileCreated event
  - RiskScoreCalculated event
  - RiskAssessmentCompleted event

- [ ] **Kafka Configuration**
  - Consumer configuration
  - Producer configuration
  - Error handling and dead letter queues
  - Event serialization/deserialization

**Acceptance Criteria:**
- Events properly consumed from other contexts
- Risk profiles automatically created/updated
- Domain events published correctly
- Integration tests verify event flow

### Sprint 4: REST API Implementation (Week 4)

#### Tasks
- [ ] **Controllers**
  - RiskProfileController
  - RiskAssessmentController
  - RiskAnalyticsController

- [ ] **Application Services**
  - RiskProfileApplicationService
  - RiskAssessmentApplicationService
  - Query services for analytics

- [ ] **Validation & Error Handling**
  - Request validation annotations
  - Global exception handler
  - Proper HTTP status codes
  - Error response DTOs

**Acceptance Criteria:**
- All API endpoints functional and tested
- Proper validation and error handling
- OpenAPI documentation generated
- Postman collection for testing

### Sprint 5: Testing & Quality (Week 5)

#### Tasks
- [ ] **Testing Strategy**
  - Unit tests for all domain logic
  - Integration tests with Testcontainers
  - Contract tests for API endpoints
  - Architecture tests with ArchUnit

- [ ] **Quality Assurance**
  - Code coverage >80% for business logic
  - SonarQube quality gate passing
  - Performance testing setup
  - Security vulnerability scanning

- [ ] **Documentation**
  - API documentation refinement
  - Domain model documentation
  - Deployment guide
  - Troubleshooting guide

**Acceptance Criteria:**
- All quality gates pass
- Performance meets requirements (<200ms)
- Documentation complete and accurate
- Ready for integration with other services

## Technical Implementation Details

### Project Structure
```
src/
├── main/
│   ├── java/
│   │   └── com/lakesidemutual/risk/
│   │       ├── domain/
│   │       │   ├── model/          # Aggregates, entities, value objects
│   │       │   ├── service/        # Domain services
│   │       │   ├── repository/     # Repository interfaces
│   │       │   └── event/          # Domain events
│   │       ├── application/
│   │       │   ├── service/        # Application services
│   │       │   ├── command/        # Command DTOs
│   │       │   ├── query/          # Query DTOs
│   │       │   └── event/          # Event handlers
│   │       ├── infrastructure/
│   │       │   ├── persistence/    # JPA repositories
│   │       │   ├── messaging/      # Kafka configuration
│   │       │   └── external/       # External service adapters
│   │       └── interfaces/
│   │           ├── rest/           # REST controllers
│   │           └── dto/            # Request/Response DTOs
│   └── resources/
│       ├── application.yml
│       └── db/migration/           # Flyway migrations
└── test/
    ├── java/
    │   └── com/lakesidemutual/risk/
    │       ├── domain/             # Domain unit tests
    │       ├── application/        # Service tests
    │       ├── infrastructure/     # Integration tests
    │       └── interfaces/         # API tests
    └── resources/
        └── application-test.yml
```

### Database Schema
```sql
-- Risk profiles table
CREATE TABLE risk_profiles (
    id UUID PRIMARY KEY,
    customer_id VARCHAR(50) NOT NULL,
    overall_score INTEGER NOT NULL,
    risk_category VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    last_assessment TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    version INTEGER NOT NULL DEFAULT 0
);

-- Risk factors table
CREATE TABLE risk_factors (
    id UUID PRIMARY KEY,
    risk_profile_id UUID NOT NULL,
    factor_type VARCHAR(50) NOT NULL,
    factor_value TEXT NOT NULL,
    weight INTEGER NOT NULL,
    score INTEGER NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    FOREIGN KEY (risk_profile_id) REFERENCES risk_profiles(id)
);

-- Risk assessments table
CREATE TABLE risk_assessments (
    id UUID PRIMARY KEY,
    customer_id VARCHAR(50) NOT NULL,
    policy_type VARCHAR(50) NOT NULL,
    risk_score INTEGER NOT NULL,
    recommended_premium DECIMAL(10,2) NOT NULL,
    assessment_data JSONB,
    assessed_at TIMESTAMP NOT NULL,
    assessed_by VARCHAR(50)
);

-- Indexes
CREATE INDEX idx_risk_profiles_customer_id ON risk_profiles(customer_id);
CREATE INDEX idx_risk_factors_profile_id ON risk_factors(risk_profile_id);
CREATE INDEX idx_risk_assessments_customer_id ON risk_assessments(customer_id);
```

### Configuration
```yaml
# application.yml
spring:
  application:
    name: risk-service
  datasource:
    url: jdbc:postgresql://localhost:5432/lakeside_risk
    username: ${DB_USERNAME:risk_user}
    password: ${DB_PASSWORD:risk_password}
  jpa:
    hibernate:
      ddl-auto: validate
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
  kafka:
    consumer:
      bootstrap-servers: localhost:9092
      group-id: risk-service
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.springframework.kafka.support.serializer.JsonDeserializer
    producer:
      bootstrap-servers: localhost:9092
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.springframework.kafka.support.serializer.JsonSerializer

lakeside:
  risk:
    pricing:
      base-premium: 500.00
      max-premium: 2000.00
    assessment:
      cache-ttl: 3600
```

## Risk Assessment Algorithm (MVP)

### Simple Rule-Based Engine
```java
@Service
public class RiskCalculationService {
    
    public RiskScore calculateRiskScore(RiskProfile profile) {
        int totalScore = 0;
        int totalWeight = 0;
        
        for (RiskFactor factor : profile.getRiskFactors()) {
            totalScore += factor.calculateScore();
            totalWeight += factor.getWeight();
        }
        
        return new RiskScore(totalWeight > 0 ? totalScore / totalWeight : 0);
    }
    
    private int calculateAgeScore(Integer age) {
        if (age < 25) return 80;      // Higher risk
        if (age < 35) return 40;      // Medium risk
        if (age < 55) return 20;      // Lower risk
        if (age < 75) return 30;      // Slightly higher
        return 60;                    // Senior driver risk
    }
    
    private int calculateDrivingScore(DrivingHistory history) {
        int score = 0;
        score += history.getAccidents().size() * 25;
        score += history.getViolations().size() * 15;
        score += Math.max(0, 5 - history.getYearsLicensed()) * 10;
        return Math.min(score, 100);
    }
    
    private int calculateLocationScore(Address address) {
        // Simplified ZIP code based scoring
        String zipCode = address.getZipCode();
        // High-risk areas: urban centers
        if (isHighRiskZip(zipCode)) return 60;
        // Medium-risk areas: suburbs
        if (isMediumRiskZip(zipCode)) return 30;
        // Low-risk areas: rural
        return 15;
    }
    
    private int calculateVehicleScore(Vehicle vehicle) {
        int score = 0;
        // Vehicle age
        int age = LocalDate.now().getYear() - vehicle.getYear();
        score += Math.min(age * 2, 20);
        
        // Vehicle type
        score += switch (vehicle.getType()) {
            case SPORTS_CAR -> 40;
            case LUXURY -> 25;
            case SUV -> 15;
            case SEDAN -> 10;
            case COMPACT -> 5;
        };
        
        return score;
    }
}
```

### Pricing Engine
```java
@Service
public class PricingService {
    
    @Value("${lakeside.risk.pricing.base-premium}")
    private BigDecimal basePremium;
    
    public Money calculatePremium(RiskAssessment assessment, List<Coverage> coverages) {
        BigDecimal premium = basePremium;
        
        // Apply risk multiplier
        premium = premium.multiply(assessment.getRiskScore().getMultiplier());
        
        // Apply coverage adjustments
        for (Coverage coverage : coverages) {
            premium = premium.add(calculateCoveragePremium(coverage, assessment));
        }
        
        return new Money(premium);
    }
    
    private BigDecimal calculateCoveragePremium(Coverage coverage, RiskAssessment assessment) {
        BigDecimal coveragePremium = coverage.getBasePremium();
        
        // Apply coverage-specific risk adjustments
        BigDecimal riskAdjustment = coveragePremium
            .multiply(assessment.getRiskScore().getMultiplier())
            .subtract(coveragePremium);
            
        return coveragePremium.add(riskAdjustment);
    }
}
```

## Testing Strategy

### Unit Tests
```java
@ExtendWith(MockitoExtension.class)
class RiskCalculationServiceTest {
    
    @InjectMocks
    private RiskCalculationService riskCalculationService;
    
    @Test
    void shouldCalculateCorrectRiskScoreForYoungDriver() {
        // Given
        RiskProfile profile = RiskProfileTestBuilder.youngDriverProfile().build();
        
        // When
        RiskScore score = riskCalculationService.calculateRiskScore(profile);
        
        // Then
        assertThat(score.getValue()).isGreaterThan(500);
        assertThat(score.getCategory()).isEqualTo(RiskCategory.HIGH);
    }
    
    @Test
    void shouldCalculateLowerRiskForExperiencedDriver() {
        // Given
        RiskProfile profile = RiskProfileTestBuilder.experiencedDriverProfile().build();
        
        // When
        RiskScore score = riskCalculationService.calculateRiskScore(profile);
        
        // Then
        assertThat(score.getValue()).isLessThan(300);
        assertThat(score.getCategory()).isIn(RiskCategory.LOW, RiskCategory.MEDIUM);
    }
}
```

### Integration Tests
```java
@SpringBootTest
@Testcontainers
@DirtiesContext
class RiskProfileRepositoryIntegrationTest {
    
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("risk_test")
            .withUsername("test")
            .withPassword("test");
    
    @Autowired
    private RiskProfileRepository repository;
    
    @Test
    void shouldPersistAndRetrieveRiskProfile() {
        // Given
        RiskProfile profile = RiskProfileTestBuilder.validProfile().build();
        
        // When
        repository.save(profile);
        Optional<RiskProfile> retrieved = repository.findByCustomerId(profile.getCustomerId());
        
        // Then
        assertThat(retrieved).isPresent();
        assertThat(retrieved.get().getOverallScore()).isEqualTo(profile.getOverallScore());
    }
}
```

## Monitoring & Observability

### Key Metrics
- **Business Metrics**:
  - Risk assessments performed per hour
  - Average risk score distribution
  - Premium calculation accuracy
  - Assessment response time

- **Technical Metrics**:
  - API response times (p50, p95, p99)
  - Database query performance
  - Event processing latency
  - Error rates and types

### Health Checks
```java
@Component
public class RiskServiceHealthIndicator implements HealthIndicator {
    
    @Override
    public Health health() {
        // Check database connectivity
        // Check Kafka connectivity
        // Verify calculation services
        return Health.up()
            .withDetail("database", "UP")
            .withDetail("kafka", "UP")
            .withDetail("calculation-engine", "UP")
            .build();
    }
}
```

## Success Criteria

### Functional Requirements
- [ ] Risk profiles created for all customers
- [ ] Risk scores calculated within 2 seconds
- [ ] Premium calculations accurate to business rules
- [ ] Event integration working with other services
- [ ] All API endpoints functional and documented

### Non-Functional Requirements
- [ ] 99.9% uptime during business hours
- [ ] <200ms response time for 95th percentile
- [ ] <2 seconds for complex risk calculations
- [ ] Support 1000 concurrent assessment requests
- [ ] Zero data loss in event processing

### Quality Requirements
- [ ] >80% code coverage on business logic
- [ ] SonarQube quality gate passing
- [ ] Zero critical security vulnerabilities
- [ ] All architecture tests passing
- [ ] API documentation 100% accurate

## Future Enhancements (Post-MVP)

### Phase 2 Capabilities
- Machine learning risk models
- External data integration (credit bureaus, DMV)
- Real-time fraud detection
- Advanced analytics and reporting
- Regulatory compliance reporting
- A/B testing for pricing strategies

This MVP provides the foundation for sophisticated risk assessment while keeping the initial implementation manageable and focused on core business value.
