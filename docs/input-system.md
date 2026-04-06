# Input System

## Purpose

The input layer decouples sketch events from control behavior.

Instead of each Processing sketch manually mutating controls, input is routed through reusable adapters and managers.

## Main Components

- `InputManager`: dispatches pointer and keyboard events in priority order
- `InputLayer`: defines capture order and event ownership
- `FocusManager`: tracks keyboard focus and focus restoration
- `PointerInputTarget`: contract for pointer-aware ViewModels
- `KeyboardInputTarget`: contract for focusable text-oriented ViewModels
- control-specific input adapters: translate geometry-aware events from the view into ViewModel calls

## Flow

1. The sketch creates `PointerEvent` or `KeyboardEvent`.
2. `InputManager` forwards the event to active layers from highest to lowest priority.
3. A layer may consume the event and stop propagation.
4. Adapters interpret coordinates or keys and call the ViewModel.
5. The ViewModel updates state without needing direct access to Processing event objects.

## MVVM Boundary

- views provide layout and hit testing
- input adapters translate events using that view geometry
- ViewModels own interaction rules
- models remain passive

This keeps interaction logic outside the sketch and outside the render path.

## Related

- [Architecture](architecture.md)
