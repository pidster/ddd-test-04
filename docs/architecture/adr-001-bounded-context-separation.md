# ADR-001: Bounded Context Separation

## Status
Accepted

## Context
Lakeside Mutual is a complex domain with multiple subdomains. According to Domain-Driven Design principles, we need to decide how to separate our system into bounded contexts to manage complexity.

## Decision
We will separate our system into the following bounded contexts, each implemented as an independent microservice:

1. **Customer Management Context** - Handling customer profiles and interactions
2. **Policy Management Context** - Managing the insurance policies and products
3. **Claims Processing Context** - Handling claims from submission to settlement
4. **Billing Context** - Managing invoices, payments, and financial transactions
5. **Risk Assessment Context** - Evaluating customer risk profiles and policy pricing

Each bounded context will:
- Have its own persistent storage
- Expose well-defined APIs (REST and/or events)
- Maintain its own domain model and ubiquitous language
- Be deployable independently

## Consequences
### Positive
- Clear separation of concerns and responsibilities
- Teams can work independently on different contexts
- Domain models can evolve independently
- Easier to scale or replace individual services
- Improved resilience (failure isolation)

### Negative
- Increased operational complexity
- Need for inter-service communication
- Potential for data duplication
- Distributed transactions become challenging
- Need for proper service discovery and API gateway

## Alternatives Considered
1. **Single Monolithic Application**
   - Easier to develop initially
   - Simpler deployment and testing
   - But harder to scale, maintain, and evolve
   - Limited team autonomy

2. **Functional Separation**
   - Separating by technical function rather than business domain
   - Could lead to anemic domain models
   - Would not align well with business capabilities

3. **More Granular Microservices**
   - Breaking down to even smaller services
   - Would increase complexity without proportional benefit
   - Too many services to manage effectively

## References
- Evans, Eric. Domain-Driven Design. 
- Lakeside Mutual domain documentation in the `/docs` directory
- [Bounded Context pattern](https://martinfowler.com/bliki/BoundedContext.html)
