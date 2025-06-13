# GitHub Copilot Instructions for Lakeside Mutual

## Project Overview

This is **Lakeside Mutual**, a Domain-Driven Design (DDD) implementation of an insurance company's core business domains. The project follows microservices architecture with a focus on maintaining clear bounded context boundaries and implementing proper DDD patterns.

### Business Domain
Lakeside Mutual is an insurance company that handles:
- Customer Management (registration, profiles, communication preferences)
- Policy Management (creation, quotes, updates, renewals, coverage)
- Claims Processing (submission, assessment, settlement)
- Billing & Payments (premium calculations, payment processing, invoicing)
- Risk Assessment (underwriting, risk evaluation)

## Architecture Principles

### Domain-Driven Design
- **Always respect bounded context boundaries** - Never access repositories from other bounded contexts directly
- **Use ubiquitous language** - Refer to domain documentation for correct terminology
- **Implement aggregates properly** - Protect invariants through encapsulation
- **Domain events are crucial** - Use for cross-context communication
- **Value objects are immutable** - Always implement as immutable classes

### Bounded Contexts
The system is organized into these bounded contexts:
1. **Customer Context** - Customer management and profiles
2. **Policy Context** - Policy lifecycle and management
3. **Claims Context** - Claims processing workflow
4. **Billing Context** - Financial transactions and billing
5. **Risk Context** - Risk assessment and underwriting

### Technology Stack

#### Backend (Java/Spring Boot)
- **Java 17** with Spring Boot 3.x
- **PostgreSQL** for structured data, **MongoDB** for document storage
- **Apache Kafka** for event-driven communication
- **REST APIs** for synchronous communication
- **Maven** for dependency management

#### Frontend (React/TypeScript)
- **TypeScript 5.x** with React 18.x
- **Redux with Redux Toolkit** for state management
- **Material UI** with styled-components for styling
- **Jest** for unit testing

## Code Generation Rules

### Backend Java Code
- Use **Spring Boot 3.x** patterns and annotations
- Implement **hexagonal architecture** (domain, application, infrastructure, interfaces layers)
- Follow **DDD tactical patterns**:
  - Entities with proper identity
  - Value objects as immutable classes
  - Aggregates with protected invariants
  - Domain services for domain logic
  - Application services for orchestration
  - Repository interfaces in domain layer
- Use **Java 17 features** (records, sealed classes, pattern matching where appropriate)
- Follow **Spring conventions** for dependency injection and configuration
- Implement **proper exception handling** with custom domain exceptions
- Use **JUnit 5** for testing with proper test structure

### Frontend TypeScript Code
- Use **TypeScript strict mode** - all code must be strongly typed
- Follow **React functional components** with hooks
- Use **Redux Toolkit** for state management
- Implement **Material UI** components consistently
- Follow **component composition** patterns
- Use **custom hooks** for reusable logic
- Implement **proper error boundaries**

### Testing Requirements
- **Unit tests** are required for all business logic
- **Integration tests** for repositories and external integrations
- **Architecture tests** using ArchUnit (backend) or custom rules (frontend)
- **End-to-end tests** for critical user journeys
- **Test coverage** minimum requirements must be met

## File and Package Naming Conventions

### Backend Java
```
com.lakesidemutual.{context}/
├── domain/           # Entities, value objects, domain services
├── application/      # Application services, commands, events
├── infrastructure/   # Repositories, external adapters
└── interfaces/       # Controllers, DTOs, REST endpoints
```

### Frontend React
```
src/
├── components/       # Reusable UI components
├── pages/           # Page-level components
├── services/        # API integration services
├── store/           # Redux state management
├── hooks/           # Custom React hooks
├── types/           # TypeScript type definitions
└── utils/           # Utility functions
```

## Development Guidelines

### Code Quality Standards
- Follow **Checkstyle** and **PMD** rules for Java
- Use **ESLint** and **Prettier** for TypeScript
- Maintain **high code coverage** (minimum thresholds defined)
- **No duplicate code** - extract to shared utilities
- **Proper documentation** - JavaDoc for Java, JSDoc for TypeScript

### Domain Model Implementation
- **Aggregate boundaries** must be respected
- **Domain events** should be published for state changes
- **Anti-corruption layers** when integrating with external systems
- **Context mapping** for inter-context relationships
- **Ubiquitous language** consistency across code and documentation

### Error Handling
- **Custom domain exceptions** for business rule violations
- **Proper HTTP status codes** in REST APIs
- **Graceful error handling** in frontend components
- **Logging** at appropriate levels with structured format

## Documentation Requirements

When creating or modifying code:
- Update **API documentation** (OpenAPI/Swagger)
- Update **domain documentation** if business logic changes
- Create **ADRs** for significant architectural decisions
- Update **Mermaid diagrams** if domain model changes
- Maintain **README files** for each service/module

## Security Considerations
- **Input validation** at all boundaries
- **Authentication/authorization** checks
- **Secure communication** between services
- **Data protection** for sensitive customer information
- **Audit logging** for compliance requirements

## Performance Guidelines
- **Lazy loading** for expensive operations
- **Caching** strategies for frequently accessed data
- **Database optimization** with proper indexing
- **Async processing** for long-running operations
- **Resource cleanup** and proper connection management

Remember: This is an insurance domain with strict regulatory requirements. Always consider data privacy, audit trails, and compliance when implementing features.
