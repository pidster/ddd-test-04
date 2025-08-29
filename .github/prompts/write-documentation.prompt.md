---
title: "Write Docs"
description: "Instructions for writing documentation"
category: "documentation|development"
---

# Prompt for Writing Documentation from Code

## Instructions
You are tasked with generating documentation for the provided code. Follow these steps to ensure the documentation is clear, concise, and adheres to best practices:

1. **Understand the Code**  
    Analyze the provided code to understand its purpose, functionality, and structure. Identify key components such as classes, methods, functions, and their relationships.

2. **Document Key Elements**  
    For each key element in the code, provide:
    - **Purpose**: A brief description of what the element does.
    - **Parameters**: A list of input parameters with their types and descriptions.
    - **Return Values**: A description of the output or return value, if applicable.
    - **Usage**: Example usage or scenarios where the element is used.

3. **Follow Documentation Standards**  
    - Use proper formatting (e.g., Markdown, JSDoc, JavaDoc) based on the language and framework.
    - Maintain consistent terminology and style.
    - Include code snippets where necessary to illustrate usage.

4. **Highlight Important Details**  
    - Explain any complex logic or algorithms.
    - Document any dependencies or external integrations.
    - Note any limitations, assumptions, or edge cases.

5. **Update Related Documentation**  
    If the code modifies or extends existing functionality, ensure related documentation is updated to reflect the changes.

## Example Output
Here is an example of how the documentation should look:

### Function: `calculatePremium`
```java
/**
 * Calculates the insurance premium based on customer details and policy information.
 *
 * @param customerAge int - The age of the customer.
 * @param policyType String - The type of insurance policy (e.g., "auto", "home").
 * @param basePremium double - The base premium amount.
 * @return double - The calculated premium amount.
 * @throws IllegalArgumentException if input parameters are invalid.
 */
public double calculatePremium(int customerAge, String policyType, double basePremium) {
     // Implementation here
}
```

### Description
The `calculatePremium` function computes the insurance premium for a customer based on their age, the type of policy, and the base premium amount. It applies specific business rules to adjust the premium.

### Parameters
- `customerAge` (int): The age of the customer. Must be a positive integer.
- `policyType` (String): The type of insurance policy. Supported values are `"auto"`, `"home"`, and `"life"`.
- `basePremium` (double): The base premium amount in USD.

### Return Value
- A `double` representing the final calculated premium amount.

### Example Usage
```java
double premium = calculatePremium(30, "auto", 500.0);
System.out.println("Calculated Premium: " + premium);
```

### Notes
- Ensure that the `customerAge` is validated before calling this function.
- Additional discounts may apply based on customer loyalty, which is handled in a separate function.

---

Use this structure as a guide to document the provided code effectively.