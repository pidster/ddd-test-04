# Contributing to Lakeside Mutual

Thank you for your interest in contributing to the Lakeside Mutual project! This document provides guidelines and instructions to help you contribute effectively.

## Development Process

### 1. Domain-Driven Design Principles

All contributions should adhere to the Domain-Driven Design principles established in the docs directory. Before making changes, please familiarize yourself with:
- The bounded contexts and their responsibilities
- The ubiquitous language for each context
- The aggregate boundaries and invariants

### 2. Branch Strategy

- `main` - Production-ready code
- `develop` - Integration branch for features
- `feature/[context]-[description]` - For new features
- `bugfix/[context]-[description]` - For bug fixes
- `refactor/[context]-[description]` - For code improvements

Example: `feature/customer-address-change`

### 3. Pull Request Process

1. Create a branch from `develop` using the naming convention above
2. Implement your changes with appropriate tests
3. Update documentation if necessary
4. Submit a pull request to the `develop` branch
5. Address any feedback from code reviews
6. Ensure CI/CD pipeline passes

## Coding Standards

### General Principles

- Code should be simple, explicit, and readable
- Prefer clarity over cleverness
- Follow SOLID principles
- Write code for humans, not computers

### Backend (Java)

- Follow [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
- Use consistent naming patterns for classes in each DDD layer
- Document public APIs with Javadoc
- Keep methods small and focused

### Frontend (React/TypeScript)

- Follow [Airbnb React/JSX Style Guide](https://github.com/airbnb/javascript/tree/master/react)
- Use functional components with hooks
- Organize components using the Atomic Design methodology
- Maintain separation between presentation and business logic

## Testing Requirements

- Backend: Minimum 80% unit test coverage for domain and application layers
- Frontend: Component tests for all UI components, integration tests for key user flows
- Write tests at the appropriate level:
  - Unit tests for domain logic
  - Integration tests for service interactions
  - End-to-end tests for critical user journeys

## Commit Message Format

Follow the [Conventional Commits](https://www.conventionalcommits.org/) specification:

```
<type>(<scope>): <short summary>
```

Example:
```
feat(customer): add address change functionality
fix(policy): resolve premium calculation issue
```

Types: feat, fix, docs, style, refactor, test, chore

## Code Review Guidelines

### For Authors

- Keep PRs small and focused
- Provide clear context about changes
- Self-review before submission
- Respond to feedback positively

### For Reviewers

- Focus on business logic correctness
- Check adherence to DDD principles
- Verify test coverage
- Look for security and performance issues
- Provide constructive feedback

## Getting Help

If you have questions about contributing:
- Reach out in the #lakeside-mutual Slack channel
- Consult the tech lead for your bounded context
- Check existing documentation in the /docs directory
