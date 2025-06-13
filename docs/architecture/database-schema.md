# Database Schema Design

This document provides the database schema design for each bounded context in the Lakeside Mutual application. The schemas are designed following domain-driven design principles, with each bounded context having its own database.

## Customer Management Context

```mermaid
erDiagram
    CUSTOMER {
        uuid id PK
        string first_name
        string last_name
        date date_of_birth
        enum gender
        timestamp created_at
        timestamp updated_at
        boolean active
    }
    
    ADDRESS {
        uuid id PK
        uuid customer_id FK
        string street
        string house_number
        string city
        string postal_code
        string country
        boolean is_billing_address
        boolean is_shipping_address
        timestamp created_at
        timestamp updated_at
    }
    
    CONTACT_METHOD {
        uuid id PK
        uuid customer_id FK
        enum type "EMAIL, PHONE, MAIL"
        string value
        boolean is_preferred
        timestamp created_at
        timestamp updated_at
    }
    
    CUSTOMER_NOTE {
        uuid id PK
        uuid customer_id FK
        string content
        uuid created_by
        timestamp created_at
    }
    
    CUSTOMER ||--o{ ADDRESS : "has"
    CUSTOMER ||--o{ CONTACT_METHOD : "has"
    CUSTOMER ||--o{ CUSTOMER_NOTE : "has"
```

## Policy Management Context

```mermaid
erDiagram
    POLICY {
        uuid id PK
        string policy_number
        uuid customer_id
        date start_date
        date end_date
        enum status "DRAFT, ACTIVE, EXPIRED, CANCELLED"
        decimal premium_amount
        string premium_currency
        timestamp created_at
        timestamp updated_at
    }
    
    COVERAGE {
        uuid id PK
        uuid policy_id FK
        uuid product_id FK
        string name
        string description
        decimal coverage_amount
        string coverage_currency
        timestamp created_at
        timestamp updated_at
    }
    
    PRODUCT {
        uuid id PK
        string name
        string description
        string category
        boolean active
        timestamp created_at
        timestamp updated_at
    }
    
    POLICY_DOCUMENT {
        uuid id PK
        uuid policy_id FK
        string filename
        string mime_type
        string document_type
        blob content
        timestamp created_at
    }
    
    POLICY ||--o{ COVERAGE : "includes"
    COVERAGE ||--|| PRODUCT : "based on"
    POLICY ||--o{ POLICY_DOCUMENT : "has"
```

## Claims Processing Context

```mermaid
erDiagram
    CLAIM {
        uuid id PK
        string claim_number
        uuid policy_id
        date date_of_incident
        date date_of_filing
        enum status "SUBMITTED, UNDER_REVIEW, APPROVED, REJECTED, SETTLED"
        decimal claim_amount
        string claim_currency
        decimal settlement_amount
        string settlement_currency
        timestamp created_at
        timestamp updated_at
    }
    
    CLAIM_EVIDENCE {
        uuid id PK
        uuid claim_id FK
        string filename
        string mime_type
        string evidence_type
        blob content
        timestamp created_at
    }
    
    CLAIM_NOTE {
        uuid id PK
        uuid claim_id FK
        string content
        uuid created_by
        timestamp created_at
    }
    
    CLAIM_ASSESSMENT {
        uuid id PK
        uuid claim_id FK
        string result
        string justification
        uuid assessed_by
        timestamp assessed_at
    }
    
    CLAIM ||--o{ CLAIM_EVIDENCE : "has"
    CLAIM ||--o{ CLAIM_NOTE : "has"
    CLAIM ||--o{ CLAIM_ASSESSMENT : "has"
```

## Billing Context

```mermaid
erDiagram
    INVOICE {
        uuid id PK
        string invoice_number
        uuid customer_id
        uuid policy_id
        date due_date
        decimal amount
        string currency
        enum status "CREATED, SENT, PAID, OVERDUE, CANCELLED"
        timestamp created_at
        timestamp updated_at
    }
    
    PAYMENT {
        uuid id PK
        uuid invoice_id FK
        decimal amount
        string currency
        enum payment_method "CREDIT_CARD, BANK_TRANSFER, DIRECT_DEBIT"
        string transaction_reference
        timestamp payment_date
        timestamp created_at
    }
    
    PAYMENT_SCHEDULE {
        uuid id PK
        uuid policy_id FK
        enum frequency "MONTHLY, QUARTERLY, ANNUALLY"
        date first_payment_date
        date last_payment_date
        timestamp created_at
        timestamp updated_at
    }
    
    INVOICE ||--o{ PAYMENT : "receives"
    PAYMENT_SCHEDULE ||--o{ INVOICE : "generates"
```

## Risk Assessment Context

```mermaid
erDiagram
    RISK_PROFILE {
        uuid id PK
        uuid customer_id
        decimal risk_score
        timestamp calculation_date
        timestamp created_at
        timestamp updated_at
    }
    
    RISK_FACTOR {
        uuid id PK
        uuid risk_profile_id FK
        string factor_name
        string factor_value
        decimal weight
        timestamp created_at
        timestamp updated_at
    }
    
    PRICING_MODEL {
        uuid id PK
        uuid product_id
        string name
        string formula
        boolean active
        timestamp created_at
        timestamp updated_at
    }
    
    RISK_PROFILE ||--o{ RISK_FACTOR : "includes"
    PRICING_MODEL ||--o{ RISK_FACTOR : "uses"
```

## Event Store

To support the event-driven architecture, we also maintain an event store:

```mermaid
erDiagram
    EVENT {
        uuid id PK
        string event_type
        string aggregate_type
        uuid aggregate_id
        json payload
        uuid correlation_id
        uuid causation_id
        timestamp timestamp
        int version
    }
    
    SNAPSHOT {
        uuid id PK
        string aggregate_type
        uuid aggregate_id
        json payload
        int version
        timestamp timestamp
    }
    
    EVENT ||--o| SNAPSHOT : "builds"
```

## Notes on Database Selection

Each bounded context can use the most appropriate database technology:

1. **Customer Management** - PostgreSQL (relational data with complex queries)
2. **Policy Management** - PostgreSQL (relational data with transaction support)
3. **Claims Processing** - PostgreSQL + MongoDB (structured data + document storage for evidence)
4. **Billing** - PostgreSQL (financial transactions require ACID properties)
5. **Risk Assessment** - PostgreSQL + Redis (for complex calculations and caching)
6. **Event Store** - EventStoreDB (specialized for event sourcing)
