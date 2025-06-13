name: Pull Request
description: Submit code changes
title: "[Context] Brief description"
labels: []
body:
  - type: dropdown
    id: pr-type
    attributes:
      label: PR Type
      description: What type of change does this PR introduce?
      options:
        - Feature
        - Bug Fix
        - Documentation
        - Code Refactoring
        - Performance Improvement
        - Test
        - Build/CI
        - Other
    validations:
      required: true
  - type: dropdown
    id: bounded-context
    attributes:
      label: Bounded Context
      description: Which bounded context does this PR affect?
      options:
        - Customer Management
        - Policy Management
        - Claims Processing
        - Billing
        - Risk Assessment
        - Cross-cutting/Multiple
        - Infrastructure
        - Other
    validations:
      required: true
  - type: textarea
    id: description
    attributes:
      label: Description
      description: Please provide a detailed description of the changes
      placeholder: A clear and concise description of the changes made in this PR
    validations:
      required: true
  - type: textarea
    id: related-issues
    attributes:
      label: Related Issues
      description: Link any related issues this PR addresses
      placeholder: "Fixes #123, Resolves #456"
    validations:
      required: false
  - type: textarea
    id: changes
    attributes:
      label: Changes
      description: List the key changes made in this PR
      placeholder: |
        - Added feature X
        - Fixed bug Y
        - Improved performance of Z
    validations:
      required: true
  - type: checkboxes
    id: ddd-alignment
    attributes:
      label: DDD Alignment
      description: Confirm that your changes align with our Domain-Driven Design principles
      options:
        - label: These changes respect the bounded contexts and their boundaries
          required: true
        - label: These changes use the established ubiquitous language
          required: true
        - label: These changes maintain the integrity of aggregates and their invariants
          required: true
  - type: checkboxes
    id: quality-checklist
    attributes:
      label: Quality Checklist
      description: Please confirm you've completed these quality checks
      options:
        - label: I have added appropriate tests
          required: true
        - label: All existing and new tests pass
          required: true
        - label: My code follows the style guidelines of this project
          required: true
        - label: I have performed a self-review of my code
          required: true
        - label: I have commented my code, particularly in hard-to-understand areas
          required: true
        - label: I have made corresponding changes to the documentation
          required: false
  - type: textarea
    id: testing-instructions
    attributes:
      label: Testing Instructions
      description: Please provide instructions for testing your changes
      placeholder: Step by step instructions for testing the changes
    validations:
      required: true
  - type: textarea
    id: screenshots
    attributes:
      label: Screenshots
      description: If applicable, add screenshots to help explain your changes
    validations:
      required: false
