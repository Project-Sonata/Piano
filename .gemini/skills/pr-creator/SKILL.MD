---
name: pr-creator
description: Automates PR creation using `gh` CLI. Formats the title as "[Ticket Number]: Description" and generates the PR body using the `mr-assistant` skill.
---

# PR Creator Skill

You are an expert at automating the development lifecycle. Your goal is to streamline the creation of pull requests by ensuring they follow the project's naming conventions and include a high-quality description.

## Workflow

### 1. Extract Ticket Information
- **Identify Ticket Number**: Look for a ticket ID (e.g., `UDMP-9`, `SON-123`) in the current branch name. If not found, ask the user or look at recent commit messages.
- **Formulate Small Description**: Create a concise 5-7 word summary of the main change for the title.

### 2. Generate PR Body
- **Use `mr-assistant`**: Activate the `mr-assistant` skill to generate a structured, professional description based on the current `git diff`.
- **Review Content**: Ensure the generated body accurately reflects the changes.

### 3. Create Pull Request
- **Format Title**: Use the structure: `[Ticket Number]: Small description of the ticket`.
- **Execute Command**: Use `gh pr create` with the formatted title and the generated body.
  - Example: `gh pr create --title "[UDMP-9]: Update project readme" --body "<Generated MR Body>"`

## Guidelines
- **Title Formatting**: Always ensure the ticket number is in square brackets.
- **Branch Context**: Always check the current branch before starting.
- **Error Handling**: If `gh` is not authenticated or a PR already exists, inform the user clearly.

## Tools Required
- `git`: To check the current branch and status.
- `gh`: GitHub CLI to create the pull request.
- `mr-assistant` (Gemini Skill): To generate the PR body.
