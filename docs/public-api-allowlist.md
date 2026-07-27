# Public API Allowlist

## Status And Scope

This document defines the supported public surface for version `0.9.11`.
Public in bytecode and supported API are not synonyms: compatibility applies
to every currently linkable signature, while categories A and B identify the
surfaces consumers should deliberately build against.

The canonical, machine-checked inventory is
[`public-api-signatures.txt`](public-api-signatures.txt). Each non-comment line
has this format:

```text
CATEGORY|T|binary-type-name|normalized type signature
CATEGORY|M|declaring-binary-type-name|normalized declared member signature
```

Every accessible production type and every declared `public` or `protected`
constructor, field, and method has an individual entry. A type category applies
as the default policy for its members, while an explicit member line can use a
different category. This is used for SPI methods implemented by an A facade,
infrastructure accessors, and accidental constructors.

The reviewed `0.9.11` baseline contains:

| Category | Types | Members | Meaning |
|---|---:|---:|---|
| A | 145 | 1,737 | Supported consumer API |
| B | 78 | 623 | Supported advanced extension or integration SPI |
| C | 4 | 21 | Public infrastructure, not an ordinary application entry point |
| D | 53 | 562 | Compatible exposure retained for future review |
| **Total** | **280** | **2,943** | 123 packages containing classified types |

The inventory includes 2,926 public and 17 protected members. It preserves
constructors, generic types, declared exceptions, nested types, enum-generated
methods and constants, record accessors, default interface methods, and
synthetic bridge methods.

## Evidence Used

Classification uses repository behavior rather than package names:

- README, component documents and runnable examples identify ordinary
  consumer construction and integration flows.
- facade, JSON and public-constructor tests act as consumer-style evidence when
  they use only public signatures.
- direct tests of models, views, viewmodels, adapters or helpers prove current
  implementation behavior but do not by themselves promote those types to A
  or B.
- production call sites identify types used only to assemble facade or overlay
  internals.
- public signature dependencies determine when a supporting type must remain B
  or C even when consumers rarely name it directly.

## Categories

### A: Supported Consumer API

Category A is the normal sketch/application surface. Its finite behavior and
lifecycle remain defined by the control-specific documentation.

| Family | Included surface | Policy |
|---|---|---|
| Controls | `Control`; `Button`, `Checkbox`, `Toggle`, `Slider`, `Label`, `RadioGroup`, `TextField`, `NumericField`, `DropDown`, `Panel`, `Indicator`, `ProgressBar` | Public facade constructors and domain operations are supported |
| Geometry | `ControlBounds`, `ControlMeasure`, `MeasureMode`, `ResolvedBounds` | Absolute/relative contracts and finite-value validation are supported |
| JSON | `ControlConfigLoader`; control configs, style configs, loaders, nested config payloads, and factories | Direct and aggregate loading are supported; factory zero-argument constructors are a D exception |
| Styles | Documented default styles, default-style factories, mutable style configs, alignment/fill/orientation enums, and documented SVG renderer implementations | Ordinary appearance customization is supported |
| Input | `InputManager`, pointer/keyboard events and their enums, control-specific input layers, `KeyboardState`, `ProcessingKeyboardAdapter` | Register layers in one manager and preserve manager-scoped routing |
| Overlays | `OverlayManager`; notification API/config; tooltip value/config API, `TooltipArea`, `TooltipFactory`, `TooltipInputLayer`, `TooltipOverlayController` | Producer lifecycle and manager ownership are supported |
| Themes | `ThemeManager`, `DarkTheme`, `LightTheme`, `ThemeSnapshot`, `ThemeTokens` | Sketch-owned theme selection and immutable snapshots are supported |
| Callbacks | `ValueListener` and facade listener operations | Callback contracts exposed by facades are supported |

The exact A type and member list is the set of `A|...` entries in the
canonical signature file.

### B: Supported Advanced SPI

Category B is stable for advanced integrations. Consumers must honor the
following invariants:

| Family | Included surface | Obligations |
|---|---|---|
| Composable controls | `PointerRoutableControl`, `KeyboardRoutableControl`, `ParentSizeAwareControl`, `ParentContextAwareControl` | Keep child coordinates local; parent size and global offset are context only; release parent-scoped transient state from `onRemovedFromParent()` |
| Input extension | `InputLayer`, `DefaultInputLayer`, input targets/adapters, `Focusable`, `FocusManagerAware`, hit tests | Respect manager ownership, priority, focus attachment/detachment, capture, and event consumption |
| Rendering extension | Style interfaces, renderer interfaces/implementations, view-state records, render-style records, `SliderGeometry` | Treat state payloads as frame data; do not mutate control behavior from rendering |
| Overlay extension | `OverlayEntry`, tooltip targets/attachables/support, tooltip rendering payloads and legacy anchor provider | Pair registration with normal close/dispose lifecycle and do not bypass `OverlayManager` ownership |
| Theme extension | `Theme`, `ThemeProvider` | Return coherent immutable snapshots to style resolution |
| Foundation contracts | `Visible`, `Enableable` | Implement the state queried by supported focus/input SPI |

Methods such as `Panel.setParentOffset(...)`,
`Panel.clearParentOffset()`, the inherited
`ParentContextAwareControl.onRemovedFromParent()`, parent-size methods and
routable facade methods are B entries even though their declaring facade is A.

### C: Public Infrastructure

Category C remains linkable and compatibility-sensitive but is not the normal
entry point for application code:

- `FocusManager` and `FocusManager.FocusToken`: routing-scoped focus authority
  used by `InputManager`, focus-aware layers and overlays.
- `FontLoader` and `FontLoader.FontResolver`: shared lazy font infrastructure
  exposed transitively by style config fields.
- `InputManager.getFocusManager()`: advanced access to the manager-owned
  authority; callers must not create competing ownership scopes.

These types are not described as internal because consumers can link them.
They remain visible to satisfy current integration signatures.

### D: Future Visibility Review

Category D remains fully present in `0.9.11`. It is not freely changeable, and
this classification does not deprecate, remove, move, or reduce any signature.

| Candidate signature/family | Evidence and reason | Compatibility impact | Suggested migration and reconsideration condition |
|---|---|---|---|
| `DropDownCoordinator` and `InputManager.getDropDownCoordinator()` | Only `DropDownOverlayController` consumes the accessor; the coordinator exposes only a public constructor while operational methods are package-private | Hiding the type or accessor breaks linkage/reflection | In a future compatibility-breaking line, keep coordination wholly behind `InputManager`; reconsider only with a migration note and binary compatibility policy |
| `DropDownOverlayController` | Constructed by `DropDown`; coordinates internal overlay/view state and is not used by documented consumer flows | Moving or hiding breaks direct construction | Retain facade operations as replacement; reconsider in a future major line |
| `ControlCode.auto(String)` and `ControlCode.requireNonBlank(String)` | Used across facades, models and configs; no consumer documentation or public signature requires the helper type | Hiding breaks callers that adopted the utility | Consumers can use facade-generated codes or their own validation; preserve non-blank Unicode semantics until an incompatible release explicitly migrates it |
| Control-specific `model`, `view`, `viewmodel`, and `*InputAdapter` types | Constructed by facades and internal pipelines; examples and functional docs direct consumers to facades and input layers | Large source/binary impact for direct MVVM users | Keep facades, B render/input SPI and payload records as supported replacements; review only after a major-version migration plan |
| `core.layout`, `ControlView`, abstract viewmodels, and internal style helpers | Used by the current MVVM implementation, not by supported facade workflows | Subclassing or direct construction could exist | Replace direct use with geometry, facade and B SPI contracts before any future package/visibility change |
| `JsonConfigSupport`, `TooltipJsonSupport`, `Util`, `LabelAlignMapper` | Shared parsing/example/render helpers with implementation-specific assumptions | Static callers would lose linkage | Move logic behind supported loaders/renderers only in a future incompatible line |
| Tooltip model/view/viewmodel implementation | Owned by the documented A controller and B tooltip rendering path | Direct construction may exist despite no documented workflow | Continue through `TooltipOverlayController`, `TooltipTarget` and configs; review only in a future major line |
| Public zero-argument constructors of nine `*Factory` types | Compiler-generated because no private constructor was declared; factories are used through static `create(...)` | Removing constructors is binary/source incompatible | Keep static `create(...)`; constructor removal requires an incompatible release |

The signature file records the exact D members, including the nine affected
factory constructors, so the grouped policy above cannot hide a future drift.

## Mandatory Review Decisions

### DropDown Coordination

`DropDownCoordinator` and
`InputManager.getDropDownCoordinator()` are D. They are routing-scoped
infrastructure accidentally exposed as a type/accessor pair, not an extension
contract. The current invariants remain binding: every manager owns one
non-static coordinator, only registered dropdowns participate, and managers
remain isolated. No global coordination is permitted.

### Parent Context

`ParentContextAwareControl` is B. It is a genuine optional composition SPI:
`Panel` recognizes it without depending on a concrete child class. Implementors
must retain local bounds and use the supplied offset only for global features.
`Panel.setParentOffset(...)` and `Panel.clearParentOffset()` are B obligations
on an A facade. `onRemovedFromParent()` is inherited as a B default method.

### Control Codes

`ControlCode` and both public static methods are D. The type is shared
infrastructure rather than supported application API. Its current behavior is
nevertheless compatibility-sensitive: `requireNonBlank(...)` rejects null,
empty and Unicode-whitespace-only strings, while `auto(...)` preserves legacy
code generation.

## Drift Check

`PublicApiSurfaceTest` inspects compiled production classes during `mvn test`.
It fails with `+` and `-` diagnostics when a type or member appears, disappears
or changes without review. It also verifies:

- deterministic output after reversing discovery order;
- explicit A/B/C/D classification for every accessible type;
- no A/B signature transitively exposes a D project type;
- the mandatory classifications above;
- exclusion of `examples/**` and `main/**`, including a structured check of the
  Maven JAR exclusion configuration.

Run the focused check with:

```bash
mvn --batch-mode --no-transfer-progress -Dtest=PublicApiSurfaceTest test
```

To approve a future surface change:

1. Run the focused test and review every reported `+` or `-` signature.
2. Determine A/B/C/D from consumer documentation, examples, external-style
   tests, internal call sites and transitive signature dependencies.
3. Update this policy when the family or lifecycle changes.
4. Add or remove the normalized signature line in
   `public-api-signatures.txt`, preserving lexical signature order.
5. Treat any removal or changed signature as a compatibility decision; do not
   approve it merely to make the test green.
6. Run the focused test, full suite, Javadocs and whitespace checks.

## Limits

This is a reviewed current-surface guard, not a historical ABI checker.
Inherited project members are recorded once at their declaring type, while
each type signature records its superclass and interfaces. Members inherited
only from external dependencies are not duplicated. The snapshot does not
freeze behavior, parameter names, annotations, Javadocs, constant values,
serialization form, or reflective ordering. Package-private/private elements
are outside scope. There is no JPMS export boundary in this project.
