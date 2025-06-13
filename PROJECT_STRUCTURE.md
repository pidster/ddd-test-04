# Lakeside Mutual Project Structure

This repository contains the implementation of the Lakeside Mutual insurance application, organized according to Domain-Driven Design principles.

## Directory Structure

```
lakeside-mutual/
├── docs/                     # Domain documentation
│   ├── bounded-contexts.md   # Bounded contexts definition
│   ├── aggregates.md         # Core aggregates and entities
│   ├── domain-events.md      # Domain events
│   ├── commands.md           # Commands
│   ├── policies.md           # Business policies
│   └── user-journeys.md      # User flows
│
├── src/                      # Source code
│   ├── backend/              # Backend microservices
│   │   ├── customer-service/ # Customer bounded context
│   │   │   └── src/
│   │   │       └── main/
│   │   │           └── java/
│   │   │               └── com/
│   │   │                   └── lakesidemutual/
│   │   │                       └── customer/
│   │   │                           ├── domain/        # Domain layer (entities, value objects)
│   │   │                           ├── application/   # Application layer (services, commands, events)
│   │   │                           ├── infrastructure/# Infrastructure layer (repositories, adapters)
│   │   │                           └── interfaces/    # Interface layer (controllers, DTOs)
│   │   │
│   │   ├── policy-service/   # Policy bounded context
│   │   ├── claims-service/   # Claims bounded context
│   │   ├── billing-service/  # Billing bounded context
│   │   └── risk-service/     # Risk assessment bounded context
│   │
│   └── frontend/             # Frontend applications
│       ├── customer-portal/  # Customer-facing UI
│       │   └── src/
│       │       ├── components/   # Reusable UI components
│       │       ├── pages/        # Page components
│       │       ├── store/        # State management
│       │       └── services/     # API services
│       │
│       └── admin-portal/     # Admin-facing UI
│
└── infra/                    # Infrastructure as Code
    ├── kubernetes/           # Kubernetes manifests
    ├── terraform/            # Terraform configurations
    ├── docker/               # Dockerfiles and compose files
    └── ci-cd/                # CI/CD pipeline configurations
```

## Technology Stack

### Backend

- **Language**: Java with Spring Boot
- **Architecture**: Microservices aligned with bounded contexts
- **Persistence**: 
  - PostgreSQL for relational data
  - EventStoreDB for event sourcing
- **Communication**: REST APIs and asynchronous messaging

### Frontend

- **Framework**: React.js with TypeScript
- **State Management**: Redux
- **UI Components**: Material UI

### Infrastructure

- **Containerization**: Docker
- **Orchestration**: Kubernetes
- **IaC**: Terraform
- **CI/CD**: GitHub Actions

## Development Setup

TBD

## Deployment

TBD
