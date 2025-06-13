# Testing Instructions for Lakeside Mutual

## Testing Strategy Overview

The testing strategy follows the testing pyramid with emphasis on domain logic testing and architectural compliance.

```
    ┌─────────────────┐
    │   E2E Tests     │  ← Few, critical user journeys
    └─────────────────┘
  ┌───────────────────────┐
  │  Integration Tests    │  ← API, Database, External systems
  └───────────────────────┘
┌─────────────────────────────┐
│      Unit Tests             │  ← Domain logic, Services, Components
└─────────────────────────────┘
```

## Backend Testing (Java/Spring Boot)

### Unit Testing Patterns

#### Domain Logic Testing
```java
class PolicyTest {
    
    @Test
    @DisplayName("Should calculate correct premium when adding coverage")
    void shouldCalculateCorrectPremiumWhenAddingCoverage() {
        // Given
        Policy policy = PolicyTestDataBuilder.aBasicPolicy()
            .withPremium(Money.of(USD, 100))
            .build();
        
        Coverage additionalCoverage = CoverageTestDataBuilder.aCoverage()
            .withType(COLLISION)
            .withLimit(Money.of(USD, 50000))
            .build();
        
        // When
        policy.addCoverage(additionalCoverage);
        
        // Then
        assertThat(policy.getPremium().getAmount())
            .isGreaterThan(BigDecimal.valueOf(100));
        
        assertThat(policy.getUncommittedEvents())
            .extracting(DomainEvent::getClass)
            .contains(CoverageAdded.class, PremiumRecalculated.class);
    }
    
    @Test
    @DisplayName("Should reject coverage addition for expired policy")
    void shouldRejectCoverageAdditionForExpiredPolicy() {
        // Given
        Policy expiredPolicy = PolicyTestDataBuilder.anExpiredPolicy().build();
        Coverage coverage = CoverageTestDataBuilder.aBasicCoverage().build();
        
        // When & Then
        PolicyException exception = assertThrows(PolicyException.class,
            () -> expiredPolicy.addCoverage(coverage));
        
        assertThat(exception.getMessage())
            .contains("Cannot add coverage to expired policy");
    }
}
```

#### Application Service Testing
```java
@ExtendWith(MockitoExtension.class)
class PolicyApplicationServiceTest {
    
    @Mock
    private PolicyRepository policyRepository;
    
    @Mock
    private CustomerRepository customerRepository;
    
    @Mock
    private PremiumCalculationService premiumService;
    
    @Mock
    private DomainEventPublisher eventPublisher;
    
    @InjectMocks
    private PolicyApplicationService policyService;
    
    @Test
    void shouldCreatePolicySuccessfully() {
        // Given
        CreatePolicyCommand command = CreatePolicyCommandBuilder.aValidCommand().build();
        Customer customer = CustomerTestDataBuilder.aValidCustomer().build();
        Premium calculatedPremium = new Premium(Money.of(USD, 150));
        
        when(customerRepository.findById(command.customerId()))
            .thenReturn(Optional.of(customer));
        when(premiumService.calculatePremium(any(), any(), any(), any()))
            .thenReturn(calculatedPremium);
        
        // When
        PolicyId policyId = policyService.createPolicy(command);
        
        // Then
        assertThat(policyId).isNotNull();
        verify(policyRepository).save(any(Policy.class));
        verify(eventPublisher).publishAll(any());
    }
    
    @Test
    void shouldThrowExceptionWhenCustomerNotFound() {
        // Given
        CreatePolicyCommand command = CreatePolicyCommandBuilder.aValidCommand().build();
        when(customerRepository.findById(command.customerId()))
            .thenReturn(Optional.empty());
        
        // When & Then
        assertThrows(CustomerNotFoundException.class,
            () -> policyService.createPolicy(command));
        
        verifyNoInteractions(policyRepository);
    }
}
```

### Integration Testing

#### Repository Testing with Testcontainers
```java
@SpringBootTest
@Testcontainers
@Transactional
class PolicyRepositoryIntegrationTest {
    
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("lakeside_test")
            .withUsername("test")
            .withPassword("test");
    
    @Autowired
    private PolicyRepository policyRepository;
    
    @Autowired
    private TestEntityManager entityManager;
    
    @Test
    void shouldPersistAndRetrievePolicy() {
        // Given
        Policy policy = PolicyTestDataBuilder.aValidPolicy().build();
        
        // When
        policyRepository.save(policy);
        entityManager.flush();
        entityManager.clear();
        
        Optional<Policy> retrieved = policyRepository.findById(policy.getId());
        
        // Then
        assertThat(retrieved).isPresent();
        assertThat(retrieved.get().getPolicyNumber())
            .isEqualTo(policy.getPolicyNumber());
    }
    
    @Test
    void shouldFindPoliciesByCustomerId() {
        // Given
        CustomerId customerId = new CustomerId("customer-123");
        Policy policy1 = PolicyTestDataBuilder.aPolicyForCustomer(customerId).build();
        Policy policy2 = PolicyTestDataBuilder.aPolicyForCustomer(customerId).build();
        
        policyRepository.save(policy1);
        policyRepository.save(policy2);
        entityManager.flush();
        
        // When
        List<Policy> policies = policyRepository.findByCustomerId(customerId);
        
        // Then
        assertThat(policies).hasSize(2);
    }
}
```

#### REST API Testing
```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class PolicyControllerIntegrationTest {
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Autowired
    private PolicyRepository policyRepository;
    
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15");
    
    @Test
    void shouldCreatePolicySuccessfully() {
        // Given
        CreatePolicyRequest request = CreatePolicyRequestBuilder.aValidRequest().build();
        
        // When
        ResponseEntity<PolicyResponse> response = restTemplate.postForEntity(
            "/api/v1/policies",
            request,
            PolicyResponse.class
        );
        
        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().policyNumber()).isNotNull();
        
        // Verify persistence
        Optional<Policy> saved = policyRepository.findById(
            new PolicyId(response.getBody().id())
        );
        assertThat(saved).isPresent();
    }
    
    @Test
    void shouldReturnBadRequestForInvalidData() {
        // Given
        CreatePolicyRequest invalidRequest = CreatePolicyRequestBuilder.anInvalidRequest().build();
        
        // When
        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(
            "/api/v1/policies",
            invalidRequest,
            ErrorResponse.class
        );
        
        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().errors()).isNotEmpty();
    }
}
```

### Architecture Testing with ArchUnit
```java
@AnalyzeClasses(packagesOf = PolicyApplication.class)
class ArchitectureTest {
    
    @ArchTest
    static final ArchRule domainShouldNotDependOnInfrastructure =
        noClasses().that().resideInAPackage("..domain..")
            .should().dependOnClassesThat().resideInAPackage("..infrastructure..");
    
    @ArchTest
    static final ArchRule domainShouldNotDependOnInterfaces =
        noClasses().that().resideInAPackage("..domain..")
            .should().dependOnClassesThat().resideInAPackage("..interfaces..");
    
    @ArchTest
    static final ArchRule applicationShouldNotDependOnInfrastructure =
        noClasses().that().resideInAPackage("..application..")
            .should().dependOnClassesThat().resideInAPackage("..infrastructure..");
    
    @ArchTest
    static final ArchRule repositoriesShouldBeInterfaces =
        classes().that().haveNameMatching(".*Repository")
            .and().resideInAPackage("..domain..")
            .should().beInterfaces();
    
    @ArchTest
    static final ArchRule domainServicesShouldBeInDomainPackage =
        classes().that().areAnnotatedWith(Service.class)
            .and().haveNameMatching(".*DomainService")
            .should().resideInAPackage("..domain.service..");
    
    @ArchTest
    static final ArchRule entitiesShouldNotDependOnRepositories =
        noClasses().that().resideInAPackage("..domain.model..")
            .should().dependOnClassesThat().resideInAPackage("..repository..");
}
```

### Test Data Builders
```java
public class PolicyTestDataBuilder {
    private PolicyId id = new PolicyId(UUID.randomUUID().toString());
    private CustomerId customerId = new CustomerId("customer-123");
    private PolicyNumber policyNumber = new PolicyNumber("POL-" + System.currentTimeMillis());
    private PolicyType type = PolicyType.AUTO;
    private PolicyStatus status = PolicyStatus.ACTIVE;
    private Premium premium = new Premium(Money.of(USD, 100));
    private PolicyTerm term = PolicyTerm.annual();
    private Set<Coverage> coverages = Set.of(CoverageTestDataBuilder.aBasicCoverage().build());
    
    public static PolicyTestDataBuilder aValidPolicy() {
        return new PolicyTestDataBuilder();
    }
    
    public static PolicyTestDataBuilder anExpiredPolicy() {
        return new PolicyTestDataBuilder()
            .withStatus(PolicyStatus.EXPIRED)
            .withTerm(PolicyTerm.of(LocalDate.now().minusYears(2), LocalDate.now().minusYears(1)));
    }
    
    public PolicyTestDataBuilder withId(PolicyId id) {
        this.id = id;
        return this;
    }
    
    public PolicyTestDataBuilder withCustomerId(CustomerId customerId) {
        this.customerId = customerId;
        return this;
    }
    
    public PolicyTestDataBuilder withStatus(PolicyStatus status) {
        this.status = status;
        return this;
    }
    
    public Policy build() {
        return Policy.builder()
            .id(id)
            .customerId(customerId)
            .policyNumber(policyNumber)
            .type(type)
            .status(status)
            .premium(premium)
            .term(term)
            .coverages(coverages)
            .build();
    }
}
```

## Frontend Testing (React/TypeScript)

### Component Testing with React Testing Library
```typescript
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { Provider } from 'react-redux';
import { ThemeProvider } from '@mui/material/styles';
import { CustomerDetail } from './CustomerDetail';
import { createMockStore } from '../../../test/utils/mockStore';
import { mockCustomer } from '../../../test/fixtures/customerFixtures';

describe('CustomerDetail', () => {
  const mockStore = createMockStore({
    customer: {
      customers: {
        'customer-1': mockCustomer,
      },
      loading: {},
      errors: {},
    },
  });

  const renderWithProviders = (component: React.ReactElement) => {
    return render(
      <Provider store={mockStore}>
        <ThemeProvider theme={theme}>
          {component}
        </ThemeProvider>
      </Provider>
    );
  };

  it('should display customer information correctly', () => {
    renderWithProviders(
      <CustomerDetail customerId={createCustomerId('customer-1')} />
    );

    expect(screen.getByText('John Doe')).toBeInTheDocument();
    expect(screen.getByText('john@example.com')).toBeInTheDocument();
    expect(screen.getByText('123 Main St')).toBeInTheDocument();
  });

  it('should handle edit mode toggle', async () => {
    renderWithProviders(
      <CustomerDetail customerId={createCustomerId('customer-1')} />
    );

    const editButton = screen.getByRole('button', { name: /edit/i });
    fireEvent.click(editButton);

    await waitFor(() => {
      expect(screen.getByDisplayValue('John Doe')).toBeInTheDocument();
    });

    expect(screen.getByRole('button', { name: /save/i })).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /cancel/i })).toBeInTheDocument();
  });

  it('should show loading state', () => {
    const loadingStore = createMockStore({
      customer: {
        customers: {},
        loading: { 'customer-1': true },
        errors: {},
      },
    });

    render(
      <Provider store={loadingStore}>
        <ThemeProvider theme={theme}>
          <CustomerDetail customerId={createCustomerId('customer-1')} />
        </ThemeProvider>
      </Provider>
    );

    expect(screen.getByRole('progressbar')).toBeInTheDocument();
  });

  it('should show error state', () => {
    const errorStore = createMockStore({
      customer: {
        customers: {},
        loading: {},
        errors: { 'customer-1': 'Failed to load customer' },
      },
    });

    render(
      <Provider store={errorStore}>
        <ThemeProvider theme={theme}>
          <CustomerDetail customerId={createCustomerId('customer-1')} />
        </ThemeProvider>
      </Provider>
    );

    expect(screen.getByText(/failed to load customer/i)).toBeInTheDocument();
  });
});
```

### Hook Testing
```typescript
import { renderHook, act } from '@testing-library/react';
import { useCustomerForm } from './useCustomerForm';
import { mockCustomer } from '../../../test/fixtures/customerFixtures';

describe('useCustomerForm', () => {
  it('should initialize with empty form data', () => {
    const { result } = renderHook(() => useCustomerForm());

    expect(result.current.formData.name).toBe('');
    expect(result.current.formData.email).toBe('');
    expect(result.current.errors).toEqual({});
  });

  it('should initialize with customer data', () => {
    const { result } = renderHook(() => useCustomerForm(mockCustomer));

    expect(result.current.formData.name).toBe('John Doe');
    expect(result.current.formData.email).toBe('john@example.com');
  });

  it('should validate form data', async () => {
    const { result } = renderHook(() => useCustomerForm());
    const mockSubmit = jest.fn();

    act(() => {
      result.current.setFormData({
        name: '',
        email: 'invalid-email',
        address: '123 Main St',
      });
    });

    await act(async () => {
      await result.current.handleSubmit(mockSubmit);
    });

    expect(result.current.errors.name).toBe('Name is required');
    expect(result.current.errors.email).toBe('Valid email is required');
    expect(mockSubmit).not.toHaveBeenCalled();
  });

  it('should submit valid form data', async () => {
    const { result } = renderHook(() => useCustomerForm());
    const mockSubmit = jest.fn().mockResolvedValue(undefined);

    act(() => {
      result.current.setFormData({
        name: 'John Doe',
        email: 'john@example.com',
        address: '123 Main St',
      });
    });

    await act(async () => {
      await result.current.handleSubmit(mockSubmit);
    });

    expect(result.current.errors).toEqual({});
    expect(mockSubmit).toHaveBeenCalledWith({
      name: 'John Doe',
      email: 'john@example.com',
      address: '123 Main St',
    });
  });
});
```

### Redux Testing
```typescript
import { configureStore } from '@reduxjs/toolkit';
import { customerSlice, fetchCustomer } from './customerSlice';
import { customerService } from '../../../services/customerService';

jest.mock('../../../services/customerService');
const mockCustomerService = customerService as jest.Mocked<typeof customerService>;

describe('customerSlice', () => {
  let store: ReturnType<typeof configureStore>;

  beforeEach(() => {
    store = configureStore({
      reducer: {
        customer: customerSlice.reducer,
      },
    });
  });

  it('should handle successful customer fetch', async () => {
    const mockCustomer = mockCustomerFixture();
    mockCustomerService.getById.mockResolvedValue(mockCustomer);

    await store.dispatch(fetchCustomer(createCustomerId('customer-1')));

    const state = store.getState().customer;
    expect(state.customers['customer-1']).toEqual(mockCustomer);
    expect(state.loading['customer-1']).toBe(false);
    expect(state.errors['customer-1']).toBeUndefined();
  });

  it('should handle failed customer fetch', async () => {
    const errorMessage = 'Customer not found';
    mockCustomerService.getById.mockRejectedValue(new Error(errorMessage));

    await store.dispatch(fetchCustomer(createCustomerId('customer-1')));

    const state = store.getState().customer;
    expect(state.customers['customer-1']).toBeUndefined();
    expect(state.loading['customer-1']).toBe(false);
    expect(state.errors['customer-1']).toBe(errorMessage);
  });
});
```

## End-to-End Testing

### User Journey Testing with Playwright
```typescript
import { test, expect } from '@playwright/test';

test.describe('Policy Creation Journey', () => {
  test('should create a new policy from start to finish', async ({ page }) => {
    // Login
    await page.goto('/login');
    await page.fill('[data-testid=email]', 'agent@lakesidemutual.com');
    await page.fill('[data-testid=password]', 'password123');
    await page.click('[data-testid=login-button]');

    // Navigate to policy creation
    await page.click('[data-testid=policies-menu]');
    await page.click('[data-testid=create-policy-button]');

    // Fill customer information
    await page.fill('[data-testid=customer-search]', 'john@example.com');
    await page.click('[data-testid=customer-select-button]');

    // Select policy type
    await page.selectOption('[data-testid=policy-type]', 'AUTO');

    // Add coverages
    await page.check('[data-testid=coverage-liability]');
    await page.check('[data-testid=coverage-collision]');

    // Set coverage limits
    await page.selectOption('[data-testid=liability-limit]', '100000');
    await page.selectOption('[data-testid=collision-limit]', '50000');

    // Review and submit
    await page.click('[data-testid=calculate-premium-button]');
    
    await expect(page.locator('[data-testid=premium-amount]')).toBeVisible();
    
    await page.click('[data-testid=create-policy-button]');
    
    // Verify success
    await expect(page.locator('[data-testid=success-message]')).toBeVisible();
    await expect(page.locator('[data-testid=policy-number]')).toBeVisible();
  });

  test('should handle validation errors gracefully', async ({ page }) => {
    await page.goto('/policies/create');

    // Try to submit without required fields
    await page.click('[data-testid=create-policy-button]');

    // Verify error messages
    await expect(page.locator('[data-testid=customer-error]')).toContainText('Customer is required');
    await expect(page.locator('[data-testid=policy-type-error]')).toContainText('Policy type is required');
  });
});
```

## Testing Utilities

### Mock Store Creator
```typescript
import { configureStore, PreloadedState } from '@reduxjs/toolkit';
import { RootState } from '../store';
import { customerSlice } from '../store/slices/customerSlice';

export const createMockStore = (preloadedState?: PreloadedState<RootState>) => {
  return configureStore({
    reducer: {
      customer: customerSlice.reducer,
      policy: policySlice.reducer,
      claims: claimsSlice.reducer,
    },
    preloadedState,
  });
};
```

### Test Fixtures
```typescript
export const mockCustomerFixture = (overrides?: Partial<Customer>): Customer => ({
  id: 'customer-1',
  name: 'John Doe',
  email: 'john@example.com',
  address: {
    street: '123 Main St',
    city: 'Anytown',
    zipCode: '12345',
    country: 'US',
  },
  createdAt: new Date('2023-01-01'),
  updatedAt: new Date('2023-01-01'),
  ...overrides,
});
```

## CI/CD Integration

### Jest Configuration
```json
{
  "preset": "ts-jest",
  "testEnvironment": "jsdom",
  "setupFilesAfterEnv": ["<rootDir>/src/test/setupTests.ts"],
  "testMatch": ["**/__tests__/**/*.(ts|tsx)", "**/*.(test|spec).(ts|tsx)"],
  "coverageThreshold": {
    "global": {
      "branches": 80,
      "functions": 80,
      "lines": 80,
      "statements": 80
    }
  },
  "collectCoverageFrom": [
    "src/**/*.{ts,tsx}",
    "!src/**/*.d.ts",
    "!src/test/**",
    "!src/**/*.stories.tsx"
  ]
}
```

### Quality Gates
- **Unit Test Coverage**: Minimum 80% for domain logic
- **Integration Test Coverage**: All API endpoints
- **Architecture Tests**: Must pass for all bounded contexts
- **Performance Tests**: Response times under 200ms for 95th percentile
- **Security Tests**: OWASP compliance scanning

Remember: Tests are documentation of expected behavior. Make them readable and maintainable.
