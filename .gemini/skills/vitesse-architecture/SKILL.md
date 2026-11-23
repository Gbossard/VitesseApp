---
name: vitesse-architecture
description: Project architecture, multi-module boundaries, MVVM/Compose patterns, Navigation 3, Material 3 and CI verification rules (ktlint, JUnit4/MockK) for VitesseApp. Always apply when creating, refactoring or debugging features, ViewModels, UI screens, data layer, or unit tests.
---

# VitesseApp Architecture & Conventions

Keep code simple, readable and testable. No abstraction without a real need.
When combining this skill with official Android skills, **the constraints in this skill win**.

## 1. Modules & Boundaries
- **Modules:** `:app`, `:core`, `:core:testing`, `:feature:home`, `:feature:details`, `:feature:edit-page`.
  Do not create new modules unless explicitly requested.
- **Strict isolation:** `:feature:*` modules MUST NEVER depend on each other.
  All data, DI modules, and shared UI/utilities live in `:core`.
  `:app` only contains the `Application`, `MainActivity`, and the top-level Navigation 3 setup (`NavKey`s and `VitesseNavDisplay`) connecting the features together.
- **Feature layout:**
    - `ui/screens/`: `<Name>Route.kt`, `<Name>Screen.kt`, `<Name>ViewModel.kt`, `<Name>UiState.kt`
    - `ui/composable/`: reusable feature-specific components
    - `ui/util/`: feature-specific UI extensions and helpers
- **Reuse `:core`:** Always check and reuse existing data sources, repositories, analytics trackers,
  theme, and shared UI states (loading, error, photo components) in `:core` before creating new ones.

## 2. Stack & Coding Standards
- Kotlin, Jetpack Compose, Material 3 (light and dark), Coil, Hilt, Room, Retrofit,
  Coroutines + Flow.
- **Route / Screen split:**
    - `XxxRoute` handles the ViewModel (`hiltViewModel`) and state collection (`collectAsStateWithLifecycle`).
    - `XxxScreen` is internal.
    - `XxxScreen` is purely stateless (state + lambdas) for Previews and UI tests.
- **ViewModels:** `@HiltViewModel`; use `@AssistedInject` with `@Assisted` for runtime route arguments (never `SavedStateHandle`). Expose `uiState` as a `StateFlow`.
- **UI:** Material 3 (colors via `MaterialTheme`). No hardcoded colors.

## 3. Navigation 3 (`:app` only)
- Navigation code lives only in `:app` (`@Serializable` `NavKey`s, `VitesseNavDisplay`)
- `NavDisplay` must set `entryDecorators` (saveable state + `rememberViewModelStoreNavEntryDecorator()`), so each entry gets its own ViewModel.

## 4. Testing & Quality Gates
- Stack: JUnit4, MockK, Turbine.
- **Mandatory before completion:** run `./gradlew ktlintCheck` and `./gradlew testDebugUnitTest`. Both must pass.
