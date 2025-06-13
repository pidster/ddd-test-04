# Lakeside Mutual User Journeys

User journeys map the path users take when interacting with the system, highlighting the touchpoints and experiences across different bounded contexts.

## Customer Onboarding Journey

```mermaid
flowchart LR
    Start((Start)) --> Research[Research Insurance Options]
    Research --> RequestQuote[Request Quote]
    RequestQuote --> ReviewQuote[Review Quote]
    ReviewQuote --> Decision{Proceed?}
    Decision -->|Yes| Register[Register Account]
    Decision -->|No| End1((End))
    Register --> ProvideDetails[Provide Details]
    ProvideDetails --> VerifyIdentity[Verify Identity]
    VerifyIdentity --> SelectPayment[Select Payment Method]
    SelectPayment --> ConfirmPolicy[Confirm Policy]
    ConfirmPolicy --> ReceiveDocuments[Receive Documents]
    ReceiveDocuments --> End2((End))
    
    %% Bounded context annotations
    subgraph "Customer Management Context"
        Register
        ProvideDetails
        VerifyIdentity
    end
    
    subgraph "Policy Management Context"
        RequestQuote
        ReviewQuote
        ConfirmPolicy
    end
    
    subgraph "Billing Context"
        SelectPayment
        ReceiveDocuments
    end
```

## Claims Processing Journey

```mermaid
flowchart TD
    Start((Start)) --> Incident[Incident Occurs]
    Incident --> NotifyInsurer[Notify Insurer]
    NotifyInsurer --> SubmitClaim[Submit Claim]
    SubmitClaim --> ProvideEvidence[Provide Evidence]
    ProvideEvidence --> ClaimReview[Claim Under Review]
    ClaimReview --> Decision{Decision}
    Decision -->|Approved| SettlementOffer[Receive Settlement Offer]
    Decision -->|Rejected| ReceiveExplanation[Receive Explanation]
    ReceiveExplanation --> Appeal{Appeal?}
    Appeal -->|Yes| SubmitAppeal[Submit Appeal]
    Appeal -->|No| End1((End))
    SubmitAppeal --> ClaimReview
    SettlementOffer --> AcceptOffer{Accept Offer?}
    AcceptOffer -->|Yes| ReceivePayment[Receive Payment]
    AcceptOffer -->|No| Negotiate[Negotiate]
    Negotiate --> ClaimReview
    ReceivePayment --> End2((End))
    
    %% Bounded context annotations
    subgraph "Customer Management Context"
        Incident
        NotifyInsurer
    end
    
    subgraph "Claims Processing Context"
        SubmitClaim
        ProvideEvidence
        ClaimReview
        Decision
        SubmitAppeal
        ReceiveExplanation
        SettlementOffer
        AcceptOffer
        Negotiate
    end
    
    subgraph "Billing Context"
        ReceivePayment
    end
```

## Policy Renewal Journey

```mermaid
flowchart TD
    Start((Start)) --> RenewalReminder[Receive Renewal Reminder]
    RenewalReminder --> ReviewPolicy[Review Current Policy]
    ReviewPolicy --> Decision{Make Changes?}
    Decision -->|Yes| RequestChanges[Request Changes]
    Decision -->|No| ConfirmRenewal[Confirm Renewal]
    RequestChanges --> ReviewChanges[Review Modified Policy]
    ReviewChanges --> AcceptChanges{Accept Changes?}
    AcceptChanges -->|Yes| ConfirmRenewal
    AcceptChanges -->|No| Decision
    ConfirmRenewal --> SelectPayment[Select Payment Method]
    SelectPayment --> ProcessPayment[Process Payment]
    ProcessPayment --> ReceiveDocuments[Receive Updated Documents]
    ReceiveDocuments --> End((End))
    
    %% Bounded context annotations
    subgraph "Customer Management Context"
        RenewalReminder
        ReviewPolicy
    end
    
    subgraph "Policy Management Context"
        Decision
        RequestChanges
        ReviewChanges
        AcceptChanges
        ConfirmRenewal
    end
    
    subgraph "Billing Context"
        SelectPayment
        ProcessPayment
        ReceiveDocuments
    end
```

## Journey Touchpoints Analysis

Each user journey crosses multiple bounded contexts, demonstrating how the contexts need to collaborate to deliver a cohesive user experience.

### Customer Onboarding Journey Touchpoints

| Touchpoint | Bounded Context | Events Generated | Commands Processed |
|------------|-----------------|------------------|-------------------|
| Research Insurance Options | External | None | None |
| Request Quote | Policy Management | QuoteRequested | RequestQuote |
| Review Quote | Policy Management | QuoteGenerated | None |
| Register Account | Customer Management | CustomerRegistered | RegisterCustomer |
| Provide Details | Customer Management | CustomerProfileUpdated | UpdateCustomerProfile |
| Verify Identity | Customer Management | CustomerVerified | VerifyCustomer |
| Select Payment Method | Billing | PaymentMethodAdded | AddPaymentMethod |
| Confirm Policy | Policy Management | PolicyCreated | CreatePolicy |
| Receive Documents | Billing | InvoiceCreated, DocumentsGenerated | None |

### Claims Processing Journey Touchpoints

| Touchpoint | Bounded Context | Events Generated | Commands Processed |
|------------|-----------------|------------------|-------------------|
| Incident Occurs | External | None | None |
| Notify Insurer | Customer Management | CustomerContactRecorded | RecordCustomerContact |
| Submit Claim | Claims Processing | ClaimFiled | FileClaim |
| Provide Evidence | Claims Processing | ClaimEvidenceAdded | AddClaimEvidence |
| Claim Under Review | Claims Processing | ClaimAssessed | AssessClaim |
| Receive Settlement Offer | Claims Processing | ClaimApproved | ApproveClaim |
| Receive Explanation | Claims Processing | ClaimRejected | RejectClaim |
| Submit Appeal | Claims Processing | ClaimAppealFiled | FileClaimAppeal |
| Receive Payment | Billing | PaymentProcessed | ProcessPayment |

### Policy Renewal Journey Touchpoints

| Touchpoint | Bounded Context | Events Generated | Commands Processed |
|------------|-----------------|------------------|-------------------|
| Receive Renewal Reminder | Customer Management | RenewalReminderSent | SendRenewalReminder |
| Review Current Policy | Customer Management | None | None |
| Request Changes | Policy Management | PolicyChangeRequested | RequestPolicyChange |
| Review Modified Policy | Policy Management | PolicyModified | None |
| Confirm Renewal | Policy Management | PolicyRenewed | RenewPolicy |
| Select Payment Method | Billing | PaymentMethodSelected | SelectPaymentMethod |
| Process Payment | Billing | PaymentReceived | ProcessPayment |
| Receive Updated Documents | Billing | DocumentsGenerated | GenerateDocuments |
