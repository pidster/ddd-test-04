# GitHub Copilot Instructions for Lakeside Mutual

This directory contains comprehensive GitHub Copilot instructions to help AI assistants understand and work effectively with the Lakeside Mutual project.

## Files Overview

### 📋 [copilot-instructions.md](./copilot-instructions.md)
**Main project instructions** - Start here for overall project understanding
- Project overview and business domain
- Architecture principles and bounded contexts
- Technology stack (Java/Spring Boot, React/TypeScript)
- Code generation rules and naming conventions
- Development guidelines and quality standards

### ⚙️ [copilot-backend.md](./copilot-backend.md)
**Backend-specific instructions** - Java/Spring Boot microservices development
- Hexagonal architecture implementation
- DDD tactical patterns (Entities, Value Objects, Aggregates)
- Spring Boot configuration and patterns
- Repository and service layer implementation
- Event-driven architecture patterns
- Testing strategies for backend code

### 🎨 [copilot-frontend.md](./copilot-frontend.md)
**Frontend-specific instructions** - React/TypeScript applications
- TypeScript patterns and strict typing
- React functional components and hooks
- Redux with Redux Toolkit state management
- Material UI component patterns
- Service layer and API integration
- Error handling and performance optimization

### 🏗️ [copilot-ddd.md](./copilot-ddd.md)
**Domain-Driven Design patterns** - Core DDD implementation guidance
- Ubiquitous language definitions
- Bounded context implementation
- Aggregate design and invariant protection
- Domain events and cross-context integration
- Anti-corruption layers
- Repository and domain service patterns

### 🧪 [copilot-testing.md](./copilot-testing.md)
**Testing strategies and patterns** - Comprehensive testing approach
- Testing pyramid implementation
- Unit testing for domain logic
- Integration testing with Testcontainers
- Architecture testing with ArchUnit
- Frontend component and hook testing
- End-to-end testing with Playwright

## How to Use These Instructions

### For Developers
1. **Start with the main instructions** to understand the project context
2. **Reference specific files** based on what you're working on:
   - Backend development → `copilot-backend.md`
   - Frontend development → `copilot-frontend.md`
   - Domain modeling → `copilot-ddd.md`
   - Writing tests → `copilot-testing.md`

### For AI Assistants
These instructions provide:
- **Context about the business domain** (insurance company)
- **Architectural constraints** that must be respected
- **Code patterns and examples** for consistent implementation
- **Testing requirements** for quality assurance
- **Technology-specific guidance** for different parts of the stack

## Key Principles to Remember

### Domain-Driven Design
- **Respect bounded context boundaries** - Never access other contexts directly
- **Use ubiquitous language** consistently across code and documentation
- **Protect aggregate invariants** through proper encapsulation
- **Publish domain events** for cross-context communication

### Code Quality
- **Follow established patterns** shown in the examples
- **Maintain high test coverage** with meaningful tests
- **Use proper error handling** with domain-specific exceptions
- **Document architectural decisions** when making significant changes

### Architecture
- **Hexagonal architecture** for backend services
- **Clean separation** between domain, application, infrastructure, and interface layers
- **Event-driven communication** between bounded contexts
- **API-first design** with proper DTOs and validation

## Project Context

### Business Domain
Lakeside Mutual is an insurance company with these core capabilities:
- **Customer Management** - Registration, profiles, preferences
- **Policy Management** - Creation, quotes, renewals, coverage
- **Claims Processing** - Submission, assessment, settlement
- **Billing & Payments** - Premium calculations, invoicing
- **Risk Assessment** - Underwriting and risk evaluation

### Technology Stack
- **Backend**: Java 17, Spring Boot 3.x, PostgreSQL/MongoDB, Kafka
- **Frontend**: TypeScript 5.x, React 18.x, Redux Toolkit, Material UI
- **Infrastructure**: Docker, Kubernetes, Terraform
- **Testing**: JUnit 5, Jest, Playwright, Testcontainers

### Regulatory Requirements
As an insurance company, consider:
- **Data privacy** and protection requirements
- **Audit trails** for all business operations
- **Regulatory compliance** with insurance laws
- **Financial accuracy** in calculations and reporting

## Contributing to These Instructions

When updating these instructions:
1. **Keep examples current** with the actual codebase
2. **Maintain consistency** across all instruction files
3. **Update cross-references** when restructuring
4. **Test examples** to ensure they work as documented
5. **Consider the AI assistant perspective** when writing guidance

## Related Documentation

For more project context, also reference:
- [Domain Overview](../docs/domain-overview.md) - Business domain understanding
- [Bounded Contexts](../docs/bounded-contexts.md) - Context boundaries and relationships
- [Development Guidelines](../DEVELOPMENT_GUIDELINES.md) - Detailed development practices
- [Architecture ADRs](../docs/architecture/) - Architectural decision records

---

These instructions are designed to help both human developers and AI assistants build high-quality, maintainable code that respects the domain model and architectural principles of the Lakeside Mutual project.
