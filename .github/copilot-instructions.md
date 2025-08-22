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

## Persona System

### Overview
**ALWAYS** use the Persona MCP server to select and adopt the most appropriate AI persona for each development task. The Persona system provides specialized AI personalities with domain-specific expertise that significantly improve code quality, architectural decisions, and problem-solving effectiveness.

### When to Use Personas
- **Before starting any development task** - Get persona recommendations
- **When switching between different types of work** - Architecture → Implementation → Testing
- **When encountering problems or roadblocks** - Get specialized debugging/troubleshooting personas
- **For code reviews and optimization** - Use review-focused personas
- **When working with unfamiliar technologies** - Get domain-specific expert personas

### Core Persona Workflow

#### 1. Task Analysis & Persona Discovery
```
ALWAYS start with: mcp_persona_discover_persona_for_context
```
Use this to automatically discover the best personas for your current context:
- **File Context**: Pass current file paths/types (e.g., `["src/risk-service/RiskCalculator.java", "pom.xml"]`)
- **Project Type**: Specify project context (e.g., "microservices insurance system")
- **Current Task**: Brief description of what you're working on
- **Team Size**: Choose appropriate level (solo/small-team/large-team/enterprise)
- **Time Constraint**: Urgency level affects persona selection strategy

#### 2. Get Targeted Recommendations
```
Use: mcp_persona_recommend_persona for specific tasks
```
For targeted persona selection:
- **Title**: Clear task description (e.g., "Implement risk calculation algorithm")
- **Description**: Detailed context and requirements
- **Domain**: Technical domain (e.g., "financial-services", "backend-development")
- **Complexity**: Task complexity level (simple/moderate/complex/expert)
- **Keywords**: Relevant technologies (e.g., ["java", "spring-boot", "ddd", "microservices"])
- **Include Reasoning**: Always set to `true` for learning

#### 3. Persona Adoption
Once you receive persona recommendations, adopt the top-recommended persona immediately. The system provides ready-to-use adoption prompts.

### Persona Transition Strategy

#### Workflow-Based Transitions
Use `mcp_persona_suggest_persona_transition` when moving between phases:
- **Planning** → **Architecture Specialist**
- **Design** → **Domain Expert** or **Senior Software Architect**
- **Implementation** → **Backend Developer** or **Frontend Developer**
- **Testing** → **QA Engineer** or **Test Automation Specialist**
- **Debugging** → **Senior Debugger** or **Performance Engineer**
- **Documentation** → **Technical Writer**
- **Review** → **Senior Code Reviewer**

#### Problem-Specific Transitions
Switch personas when encountering:
- **Performance Issues** → Performance Engineer
- **Security Concerns** → Security Specialist
- **Database Problems** → Database Architect
- **Integration Issues** → Integration Specialist
- **Domain Modeling** → Domain-Driven Design Expert

### Domain-Specific Personas for Lakeside Mutual

#### Insurance Domain Experts
- **Insurance Domain Expert** - For business rule implementation
- **Financial Services Architect** - For billing and payments
- **Compliance Specialist** - For regulatory requirements
- **Risk Assessment Expert** - For underwriting logic

#### Technical Specialists
- **Spring Boot Expert** - For backend microservices
- **DDD Practitioner** - For domain modeling
- **Event-Driven Architecture Expert** - For Kafka integration
- **Database Designer** - For PostgreSQL/MongoDB schemas
- **React/TypeScript Expert** - For frontend development

#### Quality & Operations
- **Senior QA Engineer** - For testing strategies
- **DevOps Engineer** - For deployment and monitoring
- **Security Architect** - For security implementation
- **Performance Engineer** - For optimization

### Best Practices

#### Persona Selection Guidelines
1. **Match Complexity**: Expert personas for complex tasks, generalist for simple ones
2. **Context Matters**: Consider deadlines, team size, and project phase
3. **Domain First**: Always prefer domain-specific expertise when available
4. **Transition Proactively**: Switch before getting stuck, not after

#### Effective Persona Usage
1. **Provide Context**: Share relevant file paths, error messages, and project details
2. **Be Specific**: Clear task descriptions get better persona matches
3. **Trust Recommendations**: The system has 95% accuracy - follow suggestions
4. **Monitor Effectiveness**: Use analytics to track persona performance

#### Performance Monitoring
Use `mcp_persona_analyze_persona_effectiveness` to:
- Track progress with current persona
- Identify gaps in expertise
- Optimize persona selection over time
- Improve development velocity

### Integration with Development Workflow

#### Pre-Development Checklist
1. ✅ Run persona discovery for current context
2. ✅ Get persona recommendations for specific task
3. ✅ Adopt recommended persona
4. ✅ Proceed with development using persona expertise

#### During Development
- Monitor for workflow stage changes
- Switch personas when encountering different problem types
- Use persona expertise for architecture decisions
- Leverage domain knowledge for business logic

#### Post-Development
- Use review-focused personas for code quality
- Analyze persona effectiveness
- Document successful persona patterns for team learning

### Example Usage Patterns

#### Starting a New Feature
```
1. Discover context: Current files + "implement new risk calculation feature"
2. Get recommendations: Title="Risk Calculation Implementation" + domain context
3. Adopt: Top persona (likely Domain Expert or Insurance Specialist)
4. Develop: Use persona expertise for domain modeling and implementation
```

#### Debugging Issues
```
1. Transition check: workflowStage="debugging" + current problem
2. Switch to: Debugger or Performance Engineer based on issue type
3. Solve: Apply specialized debugging techniques
4. Return: To original persona or move to testing persona
```

This systematic approach ensures you always have the right expertise for the task at hand, leading to higher quality code, better architectural decisions, and faster problem resolution.

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
