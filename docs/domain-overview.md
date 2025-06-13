# Lakeside Mutual Domain Overview

Lakeside Mutual is an insurance company that handles policies, customer management, and claims processing. This document provides an overview of the domain model derived from the Event Storming session.

## High-Level Domain Model

```mermaid
graph TD
    Customer[Customer Management] --> Policy[Policy Management]
    Customer --> Claims[Claims Processing]
    Policy --> Claims
    Policy --> Billing[Billing & Payments]
    Claims --> Billing
```

## Core Business Capabilities

1. **Customer Management**
   - Customer registration and onboarding
   - Profile updates and management
   - Communication preferences

2. **Policy Management**
   - Policy creation and quotes
   - Policy updates and renewals
   - Coverage management

3. **Claims Processing**
   - Claim submission
   - Claim assessment
   - Claim settlement

4. **Billing & Payments**
   - Premium calculations
   - Payment processing
   - Invoice generation

## Strategic Domain Classification

Using the Core Domain Chart approach:

```mermaid
quadrantChart
    title Core Domain Chart
    x-axis Low Strategic → High Strategic
    y-axis Low Complexity → High Complexity
    quadrant-1 Supporting
    quadrant-2 Generic
    quadrant-3 Core
    quadrant-4 Complex Core
    "Customer Management": [0.7, 0.5]
    "Policy Management": [0.9, 0.8]
    "Claims Processing": [0.8, 0.9]
    "Billing & Payments": [0.6, 0.6]
```

The core domains of Lakeside Mutual are Policy Management and Claims Processing, which represent the highest strategic value and complexity.
