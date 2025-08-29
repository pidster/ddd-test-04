# Lint Fixes To-Do List

Below is a checklist of all major lint issues found in the customer-portal frontend. Each item includes details needed to complete the fix.

The customer-portal frontend is found in the project directory: 'src/frontend/customer-portal'.

## Lint Fixes

- [x] **Install missing dependencies**
    - Run: `npm install react react-dom @mui/material @mui/icons-material @reduxjs/toolkit react-redux react-router-dom react-error-boundary`
    - Verify all required packages are present in `package.json`. (Completed)

- [x] **Fix import/module resolution errors**
    - All required directories and files for imports now exist in the correct locations. No files needed to be moved; all referenced modules are present. (Completed)

- [ ] **Rename TypeScript interfaces/types to use required prefixes**
    - Prefix interfaces with `I` (e.g., `User` → `IUser`, `Claim` → `IClaim`).
    - Prefix type aliases with `T` (e.g., `RootState` → `TRootState`, `AppDispatch` → `TAppDispatch`).
    - Update all references throughout the codebase.

- [ ] **Add explicit return types to all exported functions**
    - In hooks, components, and store files, specify return types for all exported functions.
    - Example: `const myFunc = (): MyType => { ... }`

- [ ] **Refactor functions/components exceeding maximum allowed lines (50)**
    - Split large components/functions into smaller, reusable components or helper functions.
    - Target files: All pages and some components (see lint output for line counts).

- [ ] **Remove unused variables/imports**
    - Delete unused imports such as `AccountCircle`, `PersonIcon`, etc.
    - Remove any variables declared but not used.

- [ ] **Fix accessibility and formatting issues**
    - Remove `autoFocus` from input fields.
    - Replace unescaped single quotes `'` in JSX with HTML entities (e.g., `&apos;`, `&#39;`).

- [ ] **Replace all `any` types with specific types**
    - Find all usages of `any` and replace with more specific types.
    - Example: `any` → `string`, `number`, or a custom interface/type.

- [ ] **Verify and fix all local module imports**
    - Ensure all referenced local files exist and are correctly exported.
    - Update import statements to match actual file names and paths.

---

**Tip:** Start with dependency installation and import fixes, then address naming conventions and return types, followed by refactoring and formatting issues.

Check off each item as you complete the fixes.
