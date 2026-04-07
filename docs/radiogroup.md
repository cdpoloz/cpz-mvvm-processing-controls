# RadioGroup

## Responsibilities

- `RadioGroupViewModel` owns selection and per-option interaction state
- `RadioGroupView` resolves option hit testing and builds the render state consumed by the style layer
- `RadioGroupInputAdapter` accepts framework-owned `PointerEvent` instances and maps them to option-level interactions

## Input Flow

1. An external adapter creates a `PointerEvent`.
2. `InputManager` dispatches that event to the active `InputLayer`.
3. `RadioGroupInputAdapter` translates pointer coordinates into option indices.
4. `RadioGroupViewModel` updates hover, press, release, and selection state.

## Performance Note

Theme mapping is snapshot-based and no longer resolved dynamically inside style rendering.
