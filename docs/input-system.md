# Input System

## Purpose

The input layer decouples sketch events from control behavior.

Instead of each Processing sketch manually mutating controls, input is routed through reusable adapters and managers.

## Input Model

The input system follows a hybrid approach:

- Event-driven at the boundary (Processing callbacks)
- State-driven within the framework (KeyboardState)

This allows deterministic behavior, consistent modifier handling, and support for complex interactions such as key combinations and continuous input. This model ensures that input handling remains predictable, testable, and independent of the underlying rendering framework.

## Main Components

- `ProcessingKeyboardAdapter`: converts raw Processing key callbacks into framework-owned keyboard events
- `KeyboardState`: tracks pressed keys and derives modifier state (`SHIFT`, `CTRL`, `ALT`) without relying on Processing event objects
- `InputManager`: dispatches pointer and keyboard events in priority order
- `InputLayer`: defines capture order and event ownership
- `FocusManager`: tracks keyboard focus and focus restoration
- `PointerInputTarget`: contract for pointer-aware ViewModels
- `KeyboardInputTarget`: contract for focusable text-oriented ViewModels
- Control-specific input adapters: translate geometry-aware events from the view into ViewModel calls

## Flow

Keyboard input follows the flow below. Pointer input follows a similar path but bypasses the keyboard adapter.
```text {id="inputflow"}
Processing -> ProcessingKeyboardAdapter -> KeyboardState -> InputManager -> input adapters -> ViewModel
```

1. Processing acts only as a raw event source.
2. Pointer callbacks create `PointerEvent` directly, while keyboard callbacks go through `ProcessingKeyboardAdapter`.
3. `ProcessingKeyboardAdapter` updates `KeyboardState` before dispatching a `KeyboardEvent`.
4. `InputManager` forwards the event to active layers from highest to lowest priority.
5. A layer may consume the event and stop propagation.
6. Adapters interpret coordinates or keys and call the ViewModel.
7. The ViewModel updates state without needing direct access to Processing event objects or deprecated Processing keyboard APIs.

## Keyboard State

- `KeyboardState` maintains the set of currently pressed keys
- Modifier flags are derived from internal state, not from deprecated Processing keyboard event state
- Keyboard handling is state-driven rather than relying on transient event objects
- The framework owns keyboard state and only consumes raw `key` and `keyCode` values from Processing

## MVVM Boundary

- Views provide layout and hit testing
- Input adapters translate events using that view geometry
- ViewModels own interaction rules
- Models remain passive

This ensures that interaction logic remains independent from both the rendering pipeline and the host framework.

## Related

- [Architecture](architecture.md)
