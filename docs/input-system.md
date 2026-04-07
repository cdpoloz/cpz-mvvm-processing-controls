# Input System

## Purpose

The input layer decouples host-framework callbacks from control behavior.

The controls project does not depend on Processing for input dispatch. External sources are expected to translate host events into framework-owned `PointerEvent` and `KeyboardEvent` instances, then submit those events to `InputManager`.

## Input Model

The input system is state-driven.

- External adapters own source-specific callback handling
- `PointerEvent` and `KeyboardEvent` carry the state needed by the framework
- `InputManager` dispatches framework-owned events without knowing where they came from
- control input adapters translate event data plus view geometry into `ViewModel` operations

This keeps interaction deterministic, testable, and source-agnostic.

## Main Components

- External source: any host environment that produces raw input, such as Processing
- External adapter: converts source-specific callbacks or state into framework-owned events
- `PointerEvent`: immutable pointer snapshot used for dispatch
- `KeyboardEvent`: immutable keyboard snapshot used for dispatch
- `KeyboardState`: tracks pressed keys and derives modifier state for keyboard dispatch
- `InputManager`: dispatches pointer and keyboard events in priority order
- `InputLayer`: defines capture order and event ownership
- `FocusManager`: tracks keyboard focus and focus restoration
- `PointerInputTarget`: contract for pointer-aware ViewModels
- `KeyboardInputTarget`: contract for focusable text-oriented ViewModels
- Control-specific input adapters: handle `PointerEvent` or `KeyboardEvent` and translate geometry-aware input into `ViewModel` calls

## PointerEvent

`PointerEvent` represents a framework-owned pointer snapshot. It currently includes:

- `type`: `MOVE`, `PRESS`, `RELEASE`, `DRAG`, `WHEEL`
- `x`, `y`: pointer position in control-space coordinates
- `pressed`: whether the pointer is currently pressed
- `button`: source-defined button identifier
- `shift`, `ctrl`, `alt`: modifier key state at dispatch time
- `wheelDelta`: signed wheel delta for wheel events

The framework consumes this structure directly. Pointer-aware adapters should accept `PointerEvent` rather than Processing-specific event types.

## KeyboardEvent

`KeyboardEvent` represents a framework-owned keyboard snapshot. It includes:

- `type`: `PRESS`, `TYPE`, `RELEASE`
- `key`: character value when available
- `keyCode`: source-defined key code
- `shift`, `ctrl`, `alt`: modifier key state at dispatch time

Keyboard dispatch is also source-agnostic. A host adapter is responsible for updating any external keyboard state and constructing `KeyboardEvent`.

## Flow

The full source-agnostic flow is:

```text
External Source -> Adapter -> InputManager -> InputLayer -> InputAdapter -> ViewModel
```

In practice:

1. A host environment emits raw pointer or keyboard input.
2. An external adapter converts that source-specific input into `PointerEvent` or `KeyboardEvent`.
3. `InputManager` forwards the event to active layers from highest to lowest priority.
4. Each `InputLayer` decides whether it should handle the event and whether propagation stops.
5. A control or overlay input adapter interprets coordinates, buttons, wheel delta, or key data.
6. The adapter calls the `ViewModel`.
7. The `ViewModel` updates interaction state and model data without any dependency on Processing APIs.

## Dispatch Rules

- `InputManager` does not depend on Processing types
- layers are ordered by priority
- inactive layers are skipped
- a layer that consumes an event stops propagation
- pointer and keyboard dispatch share the same layer ordering model
- focus remains managed by `FocusManager`, not by the external source

## Keyboard State

- `KeyboardState` maintains the set of currently pressed keys
- modifier flags are derived from state rather than deprecated host APIs
- keyboard handling remains state-driven even when the host delivers transient callbacks
- `ProcessingKeyboardAdapter` is one example of an external adapter, not a requirement of the framework

## MVVM Boundary

- views provide layout and hit testing
- input adapters consume framework-owned events using that view geometry
- ViewModels own interaction rules
- models remain passive

This keeps interaction logic independent from both the rendering pipeline and the host environment.

## Related

- [Architecture](architecture.md)
