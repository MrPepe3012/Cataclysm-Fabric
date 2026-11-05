# Agent Guidelines

## Code structure

Always try to compact code into the fewest classes possible, as long as this does not add complexity.

Before creating a class, check whether it could be a subsystem of something that already exists: a nested type, a method, or a member of an existing class. Create a new class only when it genuinely keeps the code simpler.

Give classes the least ambiguous names possible. A name should say exactly what the class does, without needing to read it.

## Callbacks

Write every callback in the format of Fabric's `CommandRegistrationCallback`: one `@FunctionalInterface` per callback, named for its single purpose, with a public `Event` field called `EVENT` and one method.
