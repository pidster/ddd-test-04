# Domain-Driven Design Instructions for Lakeside Mutual

## Domain Understanding

### Business Context
Lakeside Mutual is an insurance company with complex business rules and regulatory requirements. Always consider:
- **Regulatory compliance** - Insurance is heavily regulated
- **Data privacy** - Customer data must be protected
- **Audit trails** - All changes must be traceable
- **Financial accuracy** - Monetary calculations must be precise

### Ubiquitous Language
Use these terms consistently throughout code and documentation:

#### Customer Context
- **Customer**: An individual or entity that purchases insurance
- **Prospect**: A potential customer who hasn't purchased yet
- **Contact Information**: Phone, email, address details
- **Communication Preferences**: How customer wants to be contacted

#### Policy Context
- **Policy**: An insurance contract between customer and company
- **Coverage**: Specific protections included in a policy
- **Premium**: The cost of insurance coverage
- **Deductible**: Amount customer pays before insurance coverage kicks in
- **Policy Term**: The duration of coverage
- **Renewal**: Extension of policy for another term
- **Quote**: Estimated cost for potential coverage

#### Claims Context
- **Claim**: A request for compensation under a policy
- **Incident**: The event that triggered the claim
- **Assessment**: Evaluation of claim validity and amount
- **Settlement**: Final resolution and payment of claim
- **Adjuster**: Person who evaluates claims

#### Billing Context
- **Invoice**: Bill sent to customer for premium
- **Payment**: Money received from customer
- **Billing Cycle**: Frequency of premium payments
- **Grace Period**: Time allowed for late payment

#### Risk Context
- **Risk Assessment**: Evaluation of potential loss likelihood
- **Underwriting**: Process of evaluating and pricing risk
- **Risk Profile**: Summary of customer's risk characteristics

## Bounded Context Implementation

### 1. Customer Context

#### Core Aggregates
```java
// Customer Aggregate Root
public class Customer extends AggregateRoot<CustomerId> {
    private CustomerName name;
    private ContactInformation contactInfo;
    private CommunicationPreferences preferences;
    private CustomerStatus status;
    
    // Business methods that protect invariants
    public void updateContactInformation(ContactInformation newContactInfo) {
        validateContactInformation(newContactInfo);
        this.contactInfo = newContactInfo;
        registerEvent(new CustomerContactUpdated(getId(), newContactInfo));
    }
    
    public void changeStatus(CustomerStatus newStatus, String reason) {
        if (!isValidStatusTransition(this.status, newStatus)) {
            throw new InvalidStatusTransitionException(this.status, newStatus);
        }
        this.status = newStatus;
        registerEvent(new CustomerStatusChanged(getId(), newStatus, reason));
    }
}
```

#### Value Objects
```java
public record CustomerName(String firstName, String lastName) {
    public CustomerName {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("First name cannot be empty");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be empty");
        }
    }
    
    public String fullName() {
        return firstName + " " + lastName;
    }
}

public record ContactInformation(
    PhoneNumber primaryPhone,
    PhoneNumber secondaryPhone,
    EmailAddress email,
    Address mailingAddress
) {
    public ContactInformation {
        if (email == null) {
            throw new IllegalArgumentException("Email is required");
        }
        if (mailingAddress == null) {
            throw new IllegalArgumentException("Mailing address is required");
        }
    }
}
```

### 2. Policy Context

#### Core Aggregates
```java
public class Policy extends AggregateRoot<PolicyId> {
    private CustomerId customerId;
    private PolicyNumber policyNumber;
    private PolicyTerm term;
    private Set<Coverage> coverages;
    private Premium premium;
    private PolicyStatus status;
    
    public void addCoverage(Coverage coverage) {
        validateCoverageAddition(coverage);
        
        if (this.coverages.contains(coverage)) {
            throw new DuplicateCoverageException(coverage.getType());
        }
        
        this.coverages.add(coverage);
        recalculatePremium();
        registerEvent(new CoverageAdded(getId(), coverage));
    }
    
    public void renew(PolicyTerm newTerm, Premium newPremium) {
        if (!canRenew()) {
            throw new PolicyCannotBeRenewedException(getId());
        }
        
        this.term = newTerm;
        this.premium = newPremium;
        this.status = PolicyStatus.ACTIVE;
        registerEvent(new PolicyRenewed(getId(), newTerm));
    }
    
    private void recalculatePremium() {
        // Domain service would handle complex premium calculation
    }
}
```

#### Domain Services
```java
@Service
public class PremiumCalculationService {
    
    public Premium calculatePremium(
        PolicyType policyType,
        Set<Coverage> coverages,
        RiskProfile riskProfile,
        PolicyTerm term
    ) {
        BigDecimal basePremium = calculateBasePremium(policyType, coverages);
        BigDecimal riskMultiplier = riskProfile.calculateMultiplier();
        BigDecimal termAdjustment = term.getAdjustmentFactor();
        
        BigDecimal totalPremium = basePremium
            .multiply(riskMultiplier)
            .multiply(termAdjustment);
            
        return new Premium(totalPremium);
    }
}
```

### 3. Claims Context

#### Core Aggregates
```java
public class Claim extends AggregateRoot<ClaimId> {
    private PolicyId policyId;
    private IncidentReport incident;
    private ClaimAmount requestedAmount;
    private ClaimStatus status;
    private List<Assessment> assessments;
    private Settlement settlement;
    
    public void submitForAssessment() {
        if (status != ClaimStatus.SUBMITTED) {
            throw new InvalidClaimStatusException("Claim must be submitted first");
        }
        
        validateIncidentReport();
        this.status = ClaimStatus.UNDER_ASSESSMENT;
        registerEvent(new ClaimSubmittedForAssessment(getId()));
    }
    
    public void addAssessment(Assessment assessment) {
        if (status != ClaimStatus.UNDER_ASSESSMENT) {
            throw new InvalidClaimStatusException("Claim not under assessment");
        }
        
        this.assessments.add(assessment);
        
        if (isAssessmentComplete()) {
            processAssessmentResults();
        }
    }
    
    public void approve(ClaimAmount approvedAmount, String reason) {
        validateApproval(approvedAmount);
        
        this.settlement = new Settlement(approvedAmount, LocalDateTime.now(), reason);
        this.status = ClaimStatus.APPROVED;
        registerEvent(new ClaimApproved(getId(), approvedAmount));
    }
}
```

### 4. Cross-Context Integration

#### Domain Events for Integration
```java
// Published by Customer Context
public record CustomerCreated(
    CustomerId customerId,
    CustomerName name,
    EmailAddress email,
    Instant occurredOn
) implements DomainEvent {}

// Consumed by Policy Context
@EventHandler
public class PolicyContextCustomerEventHandler {
    
    @Autowired
    private CustomerReadModelRepository customerReadModel;
    
    public void on(CustomerCreated event) {
        // Create read model for policy context
        CustomerReadModel readModel = new CustomerReadModel(
            event.customerId(),
            event.name(),
            event.email()
        );
        customerReadModel.save(readModel);
    }
}
```

#### Anti-Corruption Layers
```java
// When integrating with external systems
@Component
public class ExternalRiskServiceAdapter {
    
    private final ExternalRiskServiceClient externalClient;
    
    public RiskProfile getRiskProfile(CustomerId customerId) {
        try {
            ExternalRiskResponse response = externalClient.getRisk(customerId.getValue());
            return translateToRiskProfile(response);
        } catch (ExternalServiceException e) {
            throw new RiskProfileUnavailableException(customerId, e);
        }
    }
    
    private RiskProfile translateToRiskProfile(ExternalRiskResponse response) {
        // Translation logic to protect our domain model
        return RiskProfile.builder()
            .riskScore(new RiskScore(response.getScore()))
            .riskFactors(translateRiskFactors(response.getFactors()))
            .build();
    }
}
```

## Implementation Patterns

### Repository Patterns
```java
// Domain repository interface (in domain layer)
public interface PolicyRepository {
    Optional<Policy> findById(PolicyId id);
    Optional<Policy> findByPolicyNumber(PolicyNumber policyNumber);
    List<Policy> findByCustomerId(CustomerId customerId);
    void save(Policy policy);
    void delete(PolicyId id);
}

// Infrastructure implementation
@Repository
@Transactional
public class JpaPolicyRepository implements PolicyRepository {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    public Optional<Policy> findById(PolicyId id) {
        PolicyEntity entity = entityManager.find(PolicyEntity.class, id.getValue());
        return Optional.ofNullable(entity).map(this::toDomain);
    }
    
    @Override
    public void save(Policy policy) {
        PolicyEntity entity = toEntity(policy);
        if (entity.getId() == null) {
            entityManager.persist(entity);
        } else {
            entityManager.merge(entity);
        }
    }
}
```

### Application Services
```java
@Service
@Transactional
public class PolicyApplicationService {
    
    private final PolicyRepository policyRepository;
    private final CustomerRepository customerRepository;
    private final PremiumCalculationService premiumService;
    private final DomainEventPublisher eventPublisher;
    
    public PolicyId createPolicy(CreatePolicyCommand command) {
        // Validate customer exists
        Customer customer = customerRepository.findById(command.customerId())
            .orElseThrow(() -> new CustomerNotFoundException(command.customerId()));
        
        // Calculate premium using domain service
        Premium premium = premiumService.calculatePremium(
            command.policyType(),
            command.coverages(),
            customer.getRiskProfile(),
            command.term()
        );
        
        // Create policy aggregate
        Policy policy = Policy.create(
            command.customerId(),
            command.policyType(),
            command.coverages(),
            premium,
            command.term()
        );
        
        // Persist
        policyRepository.save(policy);
        
        // Publish events
        eventPublisher.publishAll(policy.getUncommittedEvents());
        
        return policy.getId();
    }
}
```

### Event Sourcing (for Audit Requirements)
```java
// For contexts requiring full audit trails
@Entity
public class PolicyEventStore {
    @Id
    private String eventId;
    private String aggregateId;
    private String eventType;
    private String eventData;
    private Instant timestamp;
    private String causedBy;
    
    // Event sourcing implementation
}

@Service
public class PolicyEventSourcingService {
    
    public void appendEvent(DomainEvent event) {
        PolicyEventStore eventStore = new PolicyEventStore(
            UUID.randomUUID().toString(),
            event.getAggregateId(),
            event.getClass().getSimpleName(),
            serialize(event),
            event.getOccurredOn(),
            getCurrentUser()
        );
        
        eventStoreRepository.save(eventStore);
    }
    
    public Policy reconstructFromEvents(PolicyId policyId) {
        List<DomainEvent> events = loadEvents(policyId);
        return Policy.fromEvents(events);
    }
}
```

## Best Practices

### Domain Model Protection
1. **Never expose domain objects directly in REST APIs** - Use DTOs
2. **Validate invariants in constructors and methods** - Fail fast
3. **Use value objects for complex attributes** - Encapsulate business rules
4. **Publish domain events for side effects** - Maintain loose coupling
5. **Keep aggregates small** - Focus on consistency boundaries

### Error Handling
```java
// Domain-specific exceptions
public class PolicyException extends DomainException {
    public PolicyException(String message) {
        super(message);
    }
}

public class InvalidPolicyStateException extends PolicyException {
    public InvalidPolicyStateException(PolicyId policyId, PolicyStatus currentStatus, String operation) {
        super(String.format("Cannot perform %s on policy %s in status %s", 
            operation, policyId, currentStatus));
    }
}
```

### Testing Strategies
```java
// Domain logic testing
@Test
void shouldNotAllowCoverageAdditionAfterExpiration() {
    // Given
    Policy expiredPolicy = PolicyTestDataBuilder.anExpiredPolicy().build();
    Coverage newCoverage = CoverageTestDataBuilder.aBasicCoverage().build();
    
    // When & Then
    assertThrows(PolicyExpiredException.class, 
        () -> expiredPolicy.addCoverage(newCoverage));
}

// Integration testing with domain events
@Test
void shouldPublishPolicyCreatedEvent() {
    // Given
    CreatePolicyCommand command = new CreatePolicyCommand(/*...*/);
    
    // When
    PolicyId policyId = policyService.createPolicy(command);
    
    // Then
    verify(eventPublisher).publish(any(PolicyCreated.class));
}
```

Remember: The domain model is the heart of the application. Protect it fiercely and let it evolve naturally as business understanding deepens.
