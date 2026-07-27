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
- `InputManager`: dispatches pointer and keyboard events in priority order and
  owns the exclusive focus scope and drop-down coordination scope for its
  registered layers
- `InputLayer`: defines capture order and event ownership
- `FocusManager`: tracks exclusive keyboard focus and focus restoration within
  one `InputManager`
- `FocusManagerAware`: optional capability used to attach and detach a layer or
  container from that scope without extending the minimal `Control` contract
- `PointerInputTarget`: contract for pointer-aware ViewModels
- `KeyboardInputTarget`: contract for focusable text-oriented ViewModels
- Control-specific input adapters: handle `PointerEvent` or `KeyboardEvent` and translate geometry-aware input into `ViewModel` calls
- `TooltipInputLayer`: passive pointer layer that refreshes tooltip overlays without consuming events
- `NotificationManager`: render-only overlay owner; it does not register pointer or keyboard input layers

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
- input layers may group multiple controls that share the same priority and routing behavior
- controls of the same type should usually be registered in one shared control-specific layer instead of one layer per control
- inactive layers are skipped
- a layer that consumes an event stops propagation
- passive layers such as `TooltipInputLayer` can observe pointer motion and return `false`
- notification overlays do not participate in input dispatch in `0.9.0`
- pointer and keyboard dispatch share the same layer ordering model
- focus remains managed by the `FocusManager` owned by `InputManager`, not by
  the external source
- registering a focus-aware layer joins that manager's scope; unregistering it
  releases any focus owned through the layer
- separate `InputManager` instances have independent focus scopes and no
  global focus registry is used
- drop-down sibling transfer is limited to controls currently attached to the
  same `InputManager`; unregistering a layer or removing a panel child releases
  that association

## Keyboard State

- `KeyboardState` maintains the set of currently pressed keys
- modifier flags are derived from state rather than deprecated host APIs
- keyboard handling remains state-driven even when the host delivers transient callbacks
- `ProcessingKeyboardAdapter` is one example of an external adapter, not a requirement of the framework

## KeyboardInputTarget

`KeyboardInputTarget` is the focusable editing contract used by text-oriented ViewModels. It exposes character insertion and editing operations rather than host-specific key constants.

Core editing operations include:

- `onKeyTyped(char key)`
- `insertText(String text)`
- `backspace()`
- `deleteForward()`
- `moveCursorLeft()`
- `moveCursorRight()`
- `moveCursorHome()`
- `moveCursorEnd()`
- `moveCursorLeftWithSelection()`
- `moveCursorRightWithSelection()`
- `moveCursorHomeWithSelection()`
- `moveCursorEndWithSelection()`
- `selectAll()`
- `deleteSelection()`
- `copySelection()`
- `cutSelection()`
- `pasteFromClipboard()`

## MVVM Boundary

- views provide layout and hit testing
- input adapters consume framework-owned events using that view geometry
- ViewModels own interaction rules
- models remain passive

This keeps interaction logic independent from both the rendering pipeline and the host environment.

## Related

- [Architecture](architecture.md)
- [Tooltip](tooltip.md)
- [Notification](notification.md)
