# Backend Development Instructions for Lakeside Mutual

## Java/Spring Boot Microservices

### Architecture Pattern: Hexagonal Architecture

Each microservice follows the hexagonal architecture pattern with these layers:

```
src/main/java/com/lakesidemutual/{context}/
├── domain/                 # Domain Layer (Business Logic)
│   ├── model/             # Entities, Value Objects, Aggregates
│   ├── service/           # Domain Services
│   ├── repository/        # Repository Interfaces
│   ├── exception/         # Domain Exceptions
│   └── event/             # Domain Events
├── application/           # Application Layer (Use Cases)
│   ├── service/           # Application Services
│   ├── command/           # Command DTOs
│   ├── query/             # Query DTOs
│   └── handler/           # Command/Query Handlers
├── infrastructure/        # Infrastructure Layer (Technical Details)
│   ├── persistence/       # Repository Implementations
│   ├── messaging/         # Event Publishing/Consuming
│   ├── external/          # External Service Adapters
│   └── config/            # Configuration Classes
└── interfaces/            # Interface Layer (API)
    ├── rest/              # REST Controllers
    ├── dto/               # Data Transfer Objects
    └── mapper/            # DTO Mappers
```

### DDD Implementation Guidelines

#### Entities
```java
@Entity
@Table(name = "customers")
public class Customer {
    @Id
    private CustomerId id;
    
    // Always use Value Objects for complex attributes
    private CustomerName name;
    private EmailAddress email;
    private Address address;
    
    // Protect invariants
    public void updateEmail(EmailAddress newEmail) {
        if (newEmail == null) {
            throw new CustomerException("Email cannot be null");
        }
        // Business logic here
        this.email = newEmail;
        // Publish domain event
        registerEvent(new CustomerEmailUpdated(this.id, newEmail));
    }
}
```

#### Value Objects
```java
public record EmailAddress(String value) {
    public EmailAddress {
        if (value == null || !isValid(value)) {
            throw new IllegalArgumentException("Invalid email address");
        }
    }
    
    private static boolean isValid(String email) {
        // Validation logic
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
}
```

#### Aggregates
```java
@Entity
public class Policy extends AggregateRoot<PolicyId> {
    // Aggregate boundary protection
    private final Set<Coverage> coverages = new HashSet<>();
    
    public void addCoverage(Coverage coverage) {
        // Business rule validation
        if (isActive() && coverage.getEffectiveDate().isBefore(LocalDate.now())) {
            throw new PolicyException("Cannot add past-dated coverage to active policy");
        }
        
        coverages.add(coverage);
        registerEvent(new CoverageAdded(getId(), coverage.getId()));
    }
    
    // Expose read-only view
    public Set<Coverage> getCoverages() {
        return Collections.unmodifiableSet(coverages);
    }
}
```

#### Domain Services
```java
@Service
public class PremiumCalculationService {
    
    public Premium calculatePremium(Policy policy, RiskProfile riskProfile) {
        // Complex domain logic that doesn't belong to a single entity
        BigDecimal basePremium = calculateBasePremium(policy);
        BigDecimal riskMultiplier = riskProfile.getRiskMultiplier();
        
        return new Premium(basePremium.multiply(riskMultiplier));
    }
}
```

#### Repositories (Interface in Domain)
```java
public interface CustomerRepository {
    Optional<Customer> findById(CustomerId id);
    Optional<Customer> findByEmail(EmailAddress email);
    void save(Customer customer);
    void delete(CustomerId id);
}
```

#### Repository Implementation (Infrastructure)
```java
@Repository
public class JpaCustomerRepository implements CustomerRepository {
    
    @Autowired
    private CustomerJpaRepository jpaRepository;
    
    @Override
    public Optional<Customer> findById(CustomerId id) {
        return jpaRepository.findById(id.getValue())
            .map(this::toDomainModel);
    }
    
    private Customer toDomainModel(CustomerEntity entity) {
        // Mapping logic
    }
}
```

### Spring Boot Patterns

#### Configuration
```java
@Configuration
@EnableJpaRepositories(basePackages = "com.lakesidemutual.{context}.infrastructure.persistence")
@EntityScan(basePackages = "com.lakesidemutual.{context}.domain.model")
public class PersistenceConfig {
    
    @Bean
    @Primary
    public DataSource dataSource() {
        // DataSource configuration
    }
}
```

#### REST Controllers
```java
@RestController
@RequestMapping("/api/v1/customers")
@Validated
public class CustomerController {
    
    private final CustomerApplicationService customerService;
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerResponse createCustomer(@Valid @RequestBody CreateCustomerRequest request) {
        CreateCustomerCommand command = CustomerMapper.toCommand(request);
        Customer customer = customerService.createCustomer(command);
        return CustomerMapper.toResponse(customer);
    }
    
    @GetMapping("/{customerId}")
    public CustomerResponse getCustomer(@PathVariable String customerId) {
        CustomerId id = new CustomerId(customerId);
        Customer customer = customerService.getCustomer(id);
        return CustomerMapper.toResponse(customer);
    }
}
```

#### Application Services
```java
@Service
@Transactional
public class CustomerApplicationService {
    
    private final CustomerRepository customerRepository;
    private final DomainEventPublisher eventPublisher;
    
    public Customer createCustomer(CreateCustomerCommand command) {
        // Validation
        if (customerRepository.findByEmail(command.email()).isPresent()) {
            throw new CustomerAlreadyExistsException(command.email());
        }
        
        // Create aggregate
        Customer customer = Customer.create(
            command.name(),
            command.email(),
            command.address()
        );
        
        // Persist
        customerRepository.save(customer);
        
        // Publish events
        eventPublisher.publish(customer.getUncommittedEvents());
        
        return customer;
    }
}
```

### Event-Driven Architecture

#### Domain Events
```java
public record CustomerCreated(
    CustomerId customerId,
    EmailAddress email,
    Instant occurredOn
) implements DomainEvent {
    
    public CustomerCreated(CustomerId customerId, EmailAddress email) {
        this(customerId, email, Instant.now());
    }
}
```

#### Event Handlers
```java
@Component
public class CustomerEventHandler {
    
    @EventHandler
    public void on(CustomerCreated event) {
        // Handle the event (e.g., send welcome email)
        log.info("Customer created: {}", event.customerId());
    }
}
```

### Testing Patterns

#### Unit Tests
```java
@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {
    
    @Mock
    private CustomerRepository customerRepository;
    
    @InjectMocks
    private CustomerApplicationService customerService;
    
    @Test
    void shouldCreateCustomer() {
        // Given
        CreateCustomerCommand command = new CreateCustomerCommand(
            new CustomerName("John Doe"),
            new EmailAddress("john@example.com"),
            new Address("123 Main St", "Anytown", "12345")
        );
        
        // When
        Customer customer = customerService.createCustomer(command);
        
        // Then
        assertThat(customer.getName()).isEqualTo(command.name());
        verify(customerRepository).save(any(Customer.class));
    }
}
```

#### Integration Tests
```java
@SpringBootTest
@Testcontainers
class CustomerRepositoryIntegrationTest {
    
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("lakeside_test")
            .withUsername("test")
            .withPassword("test");
    
    @Test
    void shouldPersistCustomer() {
        // Test repository integration
    }
}
```

#### Architecture Tests
```java
@AnalyzeClasses(packagesOf = CustomerService.class)
class ArchitectureTest {
    
    @ArchTest
    static final ArchRule domainShouldNotDependOnInfrastructure =
        noClasses().that().resideInAPackage("..domain..")
            .should().dependOnClassesThat().resideInAPackage("..infrastructure..");
}
```

### Performance Considerations

- Use `@Transactional(readOnly = true)` for read operations
- Implement pagination for large result sets
- Use projections for read-only queries
- Consider caching for frequently accessed data
- Use async processing for heavy operations

### Security

- Validate all inputs at controller level
- Use method-level security annotations
- Implement audit logging
- Sanitize data before persistence
- Follow OWASP guidelines

Remember: Focus on the business domain first, then add technical implementation details.
