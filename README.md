# Lakeside Mutual Domain Model

This repository contains a Domain-Driven Design model for Lakeside Mutual, an insurance company. The model was created during an Event Storming workshop and has been translated into Markdown documents with Mermaid diagrams.

## Repository Structure

- [Domain Overview](docs/domain-overview.md): Overview of the entire domain model
- [Bounded Contexts](docs/bounded-contexts.md): Defined bounded contexts within the system
- [Aggregates](docs/aggregates.md): Core aggregates and entities
- [Domain Events](docs/domain-events.md): Events that flow through the system
- [Commands](docs/commands.md): Commands that trigger state changes
- [Policies](docs/policies.md): Business policies and rules
- [User Journeys](docs/user-journeys.md): Key user flows through the system

## Event Storming Legend

- 🟠 Domain Events: Something that happened in the system
- 🔵 Commands: A user's intent to do something
- 💛 Actors: Users or systems that interact with our system
- 🟣 Aggregates: Clusters of domain objects that form consistency boundaries
- 🟢 Read Models: Information models designed for reading/querying
- ❤️ Policies: Business rules that react to events
- 💙 External Systems: Systems that interface with ours

## Conversion Status

✅ Completed: The original Event Storming diagram has been successfully converted into structured Markdown documents with Mermaid diagrams. Each document focuses on a specific aspect of the domain model, providing detailed descriptions and visual representations of the domain concepts.

