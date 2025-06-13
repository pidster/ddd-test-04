# Lakeside Mutual Bounded Contexts

Bounded Contexts represent distinct areas of the domain with clear boundaries. Each bounded context has its own ubiquitous language, models, and implementations.

## Bounded Context Map

```mermaid
flowchart TD
    subgraph "Customer Management Context"
        CustomerProfile[Customer Profile]
        Communication[Communication Preferences]
    end
    
    subgraph "Policy Management Context"
        PolicyAdmin[Policy Administration]
        ProductCatalog[Product Catalog]
        Underwriting[Underwriting]
    end
    
    subgraph "Claims Processing Context"
        ClaimHandling[Claim Handling]
        FraudDetection[Fraud Detection]
        ClaimSettlement[Claim Settlement]
    end
    
    subgraph "Billing Context"
        Invoicing[Invoicing]
        PaymentProcessing[Payment Processing]
        AccountsReceivable[Accounts Receivable]
    end
    
    subgraph "Risk Assessment Context"
        RiskEvaluation[Risk Evaluation]
        Pricing[Pricing]
    end

    CustomerProfile -->|Provides customer data to| PolicyAdmin
    CustomerProfile -->|Provides customer data to| ClaimHandling
    CustomerProfile -->|Provides customer data to| Invoicing
    
    PolicyAdmin -->|Provides policy details to| ClaimHandling
    PolicyAdmin -->|Provides policy details to| Invoicing
    
    ProductCatalog -->|Provides product information to| Underwriting
    ProductCatalog -->|Provides product information to| Pricing
    
    Underwriting -->|Risk assessment request| RiskEvaluation
    RiskEvaluation -->|Pricing request| Pricing
    
    ClaimHandling -->|Fraud check| FraudDetection
    ClaimHandling -->|Settlement request| ClaimSettlement
    ClaimSettlement -->|Payment instructions| PaymentProcessing
    
    Invoicing -->|Payment instructions| PaymentProcessing
    PaymentProcessing -->|Payment reconciliation| AccountsReceivable
```

## Bounded Context Descriptions

### Customer Management Context
Responsible for managing all customer-related information, including personal details, contact information, communication preferences, and customer interactions.

**Key Concepts:**
- Customer Profile
- Communication Preferences
- Customer Interactions
- Customer Documents

### Policy Management Context
Manages the entire lifecycle of insurance policies, from quotation to termination.

**Key Concepts:**
- Policy
- Coverage
- Underwriting
- Product Catalog
- Policy Documents

### Claims Processing Context
Handles all aspects of processing insurance claims, from initial reporting to settlement.

**Key Concepts:**
- Claim
- Claim Assessment
- Fraud Detection
- Settlement
- Claim Documents

### Billing Context
Manages all financial aspects related to insurance policies.

**Key Concepts:**
- Invoice
- Payment
- Premium Calculation
- Account Balance
- Payment Schedule

### Risk Assessment Context
Evaluates risks associated with policies and determines appropriate pricing.

**Key Concepts:**
- Risk Factor
- Risk Score
- Pricing Model
- Actuarial Data

## Context Mapping Patterns

The interactions between bounded contexts follow these DDD context mapping patterns:

1. **Customer Management → Policy Management**: Partnership relation
2. **Customer Management → Claims Processing**: Customer/Supplier relationship
3. **Policy Management → Claims Processing**: Conformist relationship
4. **Policy Management → Billing**: Customer/Supplier relationship
5. **Claims Processing → Billing**: Customer/Supplier relationship
