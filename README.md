# CPZ MVVM Processing Controls
![Java](https://img.shields.io/badge/Java-25+-orange)
![Processing](https://img.shields.io/badge/Processing-4.5.x-blue)
![Status](https://img.shields.io/badge/status-active-brightgreen)
![License](https://img.shields.io/badge/license-MIT-lightgrey)

UI controls organized around a strict MVVM pipeline, explicit input routing, and theme-aware rendering.

------------------------------------------------------------------------

## Overview

This project is a UI framework intended for Processing sketches and for other host environments that can provide normalized input events.

The framework does not depend on Processing internally for input dispatch or interaction rules. It consumes framework-owned `PointerEvent` and `KeyboardEvent` instances that are expected to be produced by external adapters.

That separation keeps rendering concerns, interaction logic, and host-framework integration independent from each other.

------------------------------------------------------------------------

## Why this library?

Processing sketches often mix rendering, input handling, and state in a single class.

This project provides a structured alternative based on a strict MVVM pipeline, explicit input routing, and fully decoupled rendering.

Key characteristics:

- No reflection, no magic
- Explicit data flow
- Clear separation of concerns
- Designed for real-time rendering and external input integration

------------------------------------------------------------------------

## Input Philosophy

The framework does not own any input source. It only consumes normalized events provided by external adapters.

- Source-agnostic design keeps host-framework concerns outside the core controls package
- State-driven events make pointer and keyboard dispatch explicit and deterministic
- Separation of concerns keeps adapters responsible for translation and ViewModels responsible for behavior

------------------------------------------------------------------------

## Input Flow (Simplified)

```text
External Source (Processing, etc.)
        ↓
Adapter (external)
        ↓
InputManager
        ↓
InputLayer
        ↓
InputAdapter (per control)
        ↓
ViewModel
```

------------------------------------------------------------------------

## Event Model

`PointerEvent` includes:

- `type`
- `x`, `y`
- `pressed`
- `button`
- `shift`, `ctrl`, `alt`
- `wheelDelta`

`KeyboardEvent` includes:

- `type`
- `key`
- `keyCode`
- `shift`, `ctrl`, `alt`

Both event types are normalized at the adapter boundary and consumed by the framework without any dependency on Processing-specific event objects.

------------------------------------------------------------------------

## Quick Example

``` java
Binding.bind(sliderViewModel::getValue, value ->
labelViewModel.setText(value.toString()), sliderViewModel::addListener
);
```

`Slider` updates `Label` through an explicit unidirectional binding.

More advanced examples, including bidirectional binding demonstrations, are available in `BindingDevSketch`.

------------------------------------------------------------------------

## Getting Started

1. Add Processing to your project when Processing is your host environment.
2. Include this library in your classpath.
3. Create your ViewModels and Views.
4. Provide normalized input events through an external adapter.
5. Dispatch those events through `InputManager`.
6. Call `draw()` inside your host render loop.

Typical flow:

- ViewModels handle state and interaction
- Views handle layout and `ViewState` creation
- Styles resolve visual properties
- Renderers draw using the host rendering layer

You can find working examples in:

`src/com/cpz/processing/controls/examples`

------------------------------------------------------------------------

## Purpose

This project explores how to build reusable controls without collapsing rendering, interaction, and state into the same class.

Key goals:

- keep `Model`, `ViewModel`, `View`, `Style`, and `Renderer` responsibilities explicit
- centralize pointer and keyboard dispatch instead of handling input ad hoc in each host integration
- support theming and overlays without adding per-frame architectural noise
- validate lightweight ViewModel binding without coupling the binding layer to specific controls

------------------------------------------------------------------------

## Architecture

The main rendering and interaction flow is:

```text
Model → ViewModel → View → ViewState → Style → RenderStyle → Renderer
```

Layer responsibilities:

- `Model`: persistent control state with no rendering logic
- `ViewModel`: interaction state, commands, validation, and synchronization with the model
- `View`: layout, hit testing, text measurement, and `ViewState` construction
- `ViewState`: immutable frame data prepared by the view
- `Style`: visual resolution from `ViewState` plus `ThemeSnapshot`
- `Renderer`: pure drawing using already resolved values

Supporting infrastructure:

- `InputManager` dispatches pointer and keyboard events by layer priority
- `InputLayer` defines capture boundaries and event ownership
- `FocusManager` owns keyboard focus and restoration
- `OverlayManager` coordinates overlay ordering
- `ThemeManager` exposes cached `ThemeSnapshot` instances to styles

------------------------------------------------------------------------

## Rendering Model

The render path is designed for high-frequency rendering loops:

- the host application owns the managers it needs
- `ThemeManager` rebuilds its snapshot only when the theme changes
- views read the cached snapshot once per draw or measurement pass
- styles resolve colors, typography, and geometry-free render data
- renderers only draw and do not infer interaction state

This keeps theme work outside the hot render path and preserves MVVM boundaries.

------------------------------------------------------------------------

## Binding

The core binding utility is intentionally small and explicit.

- Unidirectional binding is part of the core API through `Binding.bind(...)`
- Multiple targets are supported by wiring more than one listener from the same source
- Bidirectional binding is not part of the core API and is demonstrated only as an advanced example in `BindingDevSketch`

This design avoids hidden data flows and keeps all synchronization logic visible at the application level.

See [Binding](docs/binding.md).

------------------------------------------------------------------------

## Example Usage

See `cpz-mvvm-processing-template` for an integration example.

That template demonstrates how to connect Processing callbacks to the framework through external adapters while keeping the control library itself source-agnostic.

------------------------------------------------------------------------

## Project Structure

- `src/com/cpz/processing/controls/common`: shared infrastructure such as binding
- `src/com/cpz/processing/controls/core`: cross-cutting MVVM, input, theme, overlay, focus, and layout primitives
- `src/com/cpz/processing/controls/controls`: concrete controls organized by feature
- `src/com/cpz/processing/controls/examples`: example sketches used as interactive playgrounds
- `docs`: human-facing documentation
- `docs/uml`: PlantUML diagrams

------------------------------------------------------------------------

## Documentation

- [Architecture](docs/architecture.md)
- [Binding](docs/binding.md)
- [Input System](docs/input-system.md)
- [Button](docs/button.md)
- [Checkbox](docs/checkbox.md)
- [Dropdown](docs/dropdown.md)
- [Label](docs/label.md)
- [NumericField](docs/numericfield.md)
- [RadioGroup](docs/radiogroup.md)
- [Slider](docs/slider.md)
- [TextField](docs/textfield.md)
- [Toggle](docs/toggle.md)

------------------------------------------------------------------------

## Design Philosophy

- Explicit over implicit
- Composition over coupling
- Rendering and interaction are strictly separated
- Input sources stay outside the framework core

------------------------------------------------------------------------

## Status

This project is actively used to validate MVVM patterns, explicit input routing, rendering separation, and `ViewModel` synchronization.

The focus is on architectural clarity and explicit behavior rather than framework-level abstraction or automation.

------------------------------------------------------------------------

## License

This project is licensed under the MIT License.
