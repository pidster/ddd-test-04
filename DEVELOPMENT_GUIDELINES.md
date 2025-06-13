# Lakeside Mutual Development Guidelines

This document provides guidelines for developers working on the Lakeside Mutual project. Following these guidelines ensures consistent, high-quality code across the codebase.

## Table of Contents
1. [Domain-Driven Design Implementation](#domain-driven-design-implementation)
2. [Coding Standards](#coding-standards)
3. [Testing Strategy](#testing-strategy)
4. [Documentation Requirements](#documentation-requirements)
5. [Performance Considerations](#performance-considerations)
6. [Security Guidelines](#security-guidelines)

## Domain-Driven Design Implementation

### Ubiquitous Language
- Always use the terminology defined in the domain documentation
- Refer to the [domain-overview.md](./docs/domain-overview.md) for the big picture
- Use the same terms in code, comments, documentation, and team discussions

### Domain Model Implementation
- Implement aggregates as defined in [aggregates.md](./docs/aggregates.md)
- Protect aggregate invariants through encapsulation
- Implement value objects as immutable classes
- Use domain events as described in [domain-events.md](./docs/domain-events.md)

### Bounded Context Boundaries
- Respect the bounded contexts defined in [bounded-contexts.md](./docs/bounded-contexts.md)
- Do not directly access repositories from other bounded contexts
- Use context maps to define relationships between contexts
- Implement Anti-Corruption Layers (ACL) when necessary

## Coding Standards

### General
- Write clean, readable code
- Follow the principle of least surprise
- Keep methods short (< 30 lines) and focused
- Avoid magic numbers and strings
- Use meaningful variable and method names

### Java Guidelines
- Follow the [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
- Use Java 17 features appropriately
- Prefer composition over inheritance
- Use interfaces for abstractions
- Leverage Spring's dependency injection

### TypeScript/React Guidelines
- Follow the [Airbnb TypeScript Style Guide](https://github.com/airbnb/javascript/tree/master/react)
- Use TypeScript interfaces to define props and state
- Use functional components with hooks
- Handle component lifecycle appropriately
- Extract business logic into custom hooks

## Testing Strategy

### Test Pyramid
Implement tests following the test pyramid:
- **Unit Tests**: Test domain logic in isolation
- **Integration Tests**: Test service interactions
- **End-to-End Tests**: Test critical user journeys

### Test Coverage Requirements
- Domain and application layers: 90% coverage
- Infrastructure layer: 70% coverage
- User interface: Component tests for all components

### Test Quality
- Test behavior, not implementation
- Make tests readable and maintainable
- Use appropriate test doubles (mocks, stubs)
- Create meaningful assertions
- Avoid test interdependencies

## Documentation Requirements

### Code Documentation
- Document public APIs with JavaDoc/JSDoc
- Explain complex algorithms and business rules
- Document assumptions and preconditions
- Keep comments up to date with code changes

### Technical Documentation
- Document architecture decisions in ADRs
- Create sequence diagrams for complex interactions
- Document integration points between services
- Keep documentation synchronized with code changes

## Performance Considerations

### Database Optimization
- Design efficient schemas
- Use appropriate indexes
- Monitor and optimize query performance
- Consider caching strategies

### API Performance
- Implement pagination for collection endpoints
- Support filtering at the API level
- Consider API versioning from the start
- Implement rate limiting

### Frontend Performance
- Optimize bundle size
- Implement lazy loading
- Use React.memo for expensive components
- Profile and optimize rendering performance

## Security Guidelines

### Authentication and Authorization
- Use OAuth2/OIDC for authentication
- Implement proper role-based access control
- Validate permissions at the service level
- Never trust client-side authorization

### API Security
- Validate all input data
- Sanitize outputs to prevent XSS
- Implement CSRF protection
- Use HTTPS for all endpoints

### Data Protection
- Follow GDPR principles for PII
- Encrypt sensitive data
- Implement proper audit logging
- Define data retention policies
