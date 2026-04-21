# CPZ MVVM Processing Controls
![Java](https://img.shields.io/badge/Java-25+-orange)
![Processing](https://img.shields.io/badge/Processing-4.5.x-blue)
![Status](https://img.shields.io/badge/status-active-brightgreen)
![License](https://img.shields.io/badge/license-MIT-lightgrey)
[![GitHub](https://img.shields.io/badge/GitHub-cdpoloz-181717?logo=github)](https://github.com/cdpoloz)

A UI control framework for Processing built around a strict MVVM architecture,
explicit input routing, and high-performance rendering.

---

## Key design decisions

- Strict MVVM pipeline (Model → ViewModel → View → Style → Renderer)
- Fully decoupled input system (no Processing dependency in interaction logic)
- Explicit composition instead of implicit binding systems
- Sketch-owned theming (no global state, no singletons)
- Facade-based public API with hidden MVVM internals

---

## Installation

This library is currently distributed as a JAR.

`cpz-mvvm-processing-controls` also requires `cpz-utils`. In the current
repository layout, the expected dependency JAR is:

```text
lib/cpz-utils/cpz-utils.jar
```

`cpz-utils` provides shared utility APIs used by the controls project. Color
helpers such as `Colors.rgb(...)` and `Colors.gray(...)` now belong to
`com.cpz.utils.color.Colors`; they are no longer part of
`com.cpz.processing.controls`.

### Option 1 — Using the release

1. Download the latest release JAR from GitHub
2. Add it to your project classpath
3. Add Processing 4.5.x (`core.jar`) as well
4. Add `cpz-utils.jar` to the same classpath

### Option 2 — Using source

1. Clone the repository
2. Add it as a module/dependency in your project
3. Add `lib/cpz-utils/cpz-utils.jar` to the module dependencies/classpath

Once added, you can run the quick example below.

### IntelliJ setup

The module file expects a project library named `cpz-utils`. If IntelliJ cannot
resolve it after cloning, add a library pointing to
`lib/cpz-utils/cpz-utils.jar` and attach it to the
`cpz-mvvm-processing-controls` module. Processing must also be configured as a
module dependency.

### Manual classpath

When compiling or running manually, include both Processing and `cpz-utils`.
For example, from this repository:

```text
javac -cp "lib/Processing 4.5.2/core-4.5.2.jar:lib/cpz-utils/cpz-utils.jar:src" ...
java  -cp "lib/Processing 4.5.2/core-4.5.2.jar:lib/cpz-utils/cpz-utils.jar:src" ...
```

Use the classpath separator required by your platform (`:` on Linux/macOS, `;`
on Windows).

---

## Quick example

Here is a minimal Processing sketch using a single control:

```java
import com.cpz.processing.controls.controls.button.Button;
import com.cpz.processing.controls.controls.button.style.ButtonDefaultStyles;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.processing.controls.core.theme.LightTheme;
import com.cpz.processing.controls.core.theme.ThemeManager;
import processing.core.PApplet;

public class MinimalSketch extends PApplet {

    private ThemeManager themeManager;
    private InputManager inputManager;

    private Button button;

    public void settings() {
        size(400, 200);
    }

    public void setup() {
        themeManager = new ThemeManager(new LightTheme());
        inputManager = new InputManager();

        button = new Button(this, "btnHello", "Click me", 140, 80, 120, 40);
        button.setStyle(ButtonDefaultStyles.primary(themeManager));

        button.setClickListener(() -> {
            System.out.println("Hello from MVVM control!");
        });

        // Simple input layer (direct routing for this example)
        inputManager.registerLayer(new SimpleInputLayer());
    }

    public void draw() {
        background(240);
        button.draw();
    }

    public void mousePressed() {
        inputManager.dispatchPointer(
                new PointerEvent(PointerEvent.Type.PRESS, mouseX, mouseY, mouseButton)
        );
    }

    public void mouseReleased() {
        inputManager.dispatchPointer(
                new PointerEvent(PointerEvent.Type.RELEASE, mouseX, mouseY, mouseButton)
        );
    }

    private class SimpleInputLayer extends com.cpz.processing.controls.core.input.DefaultInputLayer {

        public SimpleInputLayer() {
            super(0);
        }

        @Override
        public boolean handlePointerEvent(PointerEvent event) {
            button.handlePointerEvent(event);
            return true;
        }
    }
}
```

Run it, click the button, and you should see output in the console.

This example shows the core interaction flow:
- create a control facade
- attach a listener
- dispatch input through InputManager

---

## Why this library?

Processing sketches often mix rendering, input handling, and state in a single class.

This project provides a structured alternative based on a strict MVVM pipeline, explicit input routing, and fully decoupled rendering.

What this gives you:

- predictable and debuggable behavior (no hidden state or implicit wiring)
- clear separation between rendering, interaction, and state
- full control from the sketch (no framework-owned lifecycle surprises)
- reusable controls without sacrificing transparency

---

## Mental Model

At a high level, the framework works like this:

```text
Sketch (owns everything)
   ├── ThemeManager
   ├── InputManager
   └── Controls (public facades)

Input:
External Source → Adapter → InputManager → InputLayer → Control facade → ViewModel

Rendering:
Model → ViewModel → View → ViewState → Style → Renderer
```

---

## Overview

This project is a UI framework intended for Processing sketches and for other host environments that can provide normalized input events.

The public control layer is exposed through closed ergonomic facades such as `Button`, `Checkbox`, `Toggle`, `Slider`, `Label`, `RadioGroup`, `TextField`, `NumericField`, and `DropDown`.

Those facades also share a lightweight public contract, `Control`, for the small transversal surface that is common across the controls without exposing MVVM internals.

The JSON layer can also create one or more controls from a single document and returns them as `Map<String, Control>`. JSON remains structural only: binding and behavior wiring stay in sketch code.

The framework does not depend on Processing internally for input dispatch or interaction rules. It consumes framework-owned `PointerEvent` and `KeyboardEvent` instances that are expected to be produced by external adapters.

That separation keeps rendering concerns, interaction logic, and host-framework integration independent from each other.

---

## Input Philosophy

The framework does not own any input source. It only consumes normalized events provided by external adapters.

- Source-agnostic design keeps host-framework concerns outside the core controls package
- State-driven events make pointer and keyboard dispatch explicit and deterministic
- Separation of concerns keeps adapters responsible for translation and ViewModels responsible for behavior

---

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

---

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

---

## Composition Example (JSON + Binding)
```java
ControlConfigLoader loader = new ControlConfigLoader(this);
Map<String, Control> controls = loader.load("data/config/json-multicontrol-binding-test.json");

Slider slider = (Slider) controls.get("sldValue");
NumericField numericField = (NumericField) controls.get("numValue");
Label currentValue = (Label) controls.get("lblCurrentValue");

slider.setChangeListener(value -> {
    numericField.setValue(value);
    currentValue.setText("Current value: " + slider.getFormattedValue());
});
```

This keeps structure in JSON and behavior in the sketch: `ControlConfigLoader` creates closed facades, `Map<String, Control>` provides the common composition surface, and the sketch wires the controls together.

The canonical progression starts with `JsonMultiControlUnidirectionalBindingTest`, where `Slider` is the only source and updates `NumericField` plus `Label` from one listener. `JsonMultiControlBindingTest` extends the same composition into controlled bidirectional synchronization by adding a numeric-field listener, one extra sync routine, and a local anti-loop guard.

---

## Getting Started

1. Add Processing to your project when Processing is your host environment.
2. Include this library in your classpath.
3. Include `lib/cpz-utils/cpz-utils.jar` in the same module/classpath.
4. Create controls directly through the public facades or load them from JSON with `ControlConfigLoader`.
5. Provide normalized input events through an external adapter.
6. Dispatch those events through `InputManager`.
7. Resolve listeners and binding in the sketch.
8. Call `draw()` inside your host render loop.

Typical flow:

- facades expose the public API used by the sketch
- `Control` provides the minimal common contract when a mixed collection is enough
- JSON can create `Map<String, Control>` for composition-oriented sketches
- binding stays in the sketch instead of being declared in JSON

At the public API level, each concrete facade keeps its own domain-specific methods, while `Control` provides only the minimal common contract for drawing, code identity, visibility, enabled state, and positioning.

You can find working examples in:

`src/com/cpz/processing/controls/examples`

---

## When to use this

This framework is a good fit when:

- you are building non-trivial Processing applications with multiple UI controls
- you want strict separation between rendering, interaction, and state
- you need predictable input handling (keyboard + pointer)
- you want theming without global state
- you prefer explicit composition over hidden binding systems

It may not be the best fit for:

- very small sketches with minimal UI
- quick prototypes where structure is not a concern

---

## Purpose

This project explores how to build reusable controls without collapsing rendering, interaction, and state into the same class.

Key goals:

- keep `Model`, `ViewModel`, `View`, `Style`, and `Renderer` responsibilities explicit
- centralize pointer and keyboard dispatch instead of handling input ad hoc in each host integration
- support theming and overlays without adding per-frame architectural noise
- validate explicit synchronization between public facades without adding framework-level binding helpers

---

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
- `ThemeManager` is owned by the host sketch and exposes cached `ThemeSnapshot` instances to styles

Public API notes:

- closed concrete facades remain the main public entry points for each control
- the public `Control` interface captures only the minimal transversal facade surface
- `Control` is distinct from `ControlView`, which belongs to the internal MVVM view layer
- the JSON layer creates closed facades and returns them through `Map<String, Control>`
- JSON does not define binding; binding remains sketch-side

---

## Architecture Overview

```text
                ┌───────────────┐
                │    Sketch     │
                │ (owns state)  │
                └──────┬────────┘
                       │
        ┌──────────────┼──────────────┐
        │              │              │
 ┌─────────────┐ ┌─────────────┐ ┌─────────────┐
 │ ThemeManager│ │ InputManager│ │  Controls   │
 │             │ │             │ │ (facades)   │
 └──────┬──────┘ └──────┬──────┘ └──────┬──────┘
        │               │               │
        │               │               ▼
        │               │        ┌─────────────┐
        │               │        │ ViewModel   │
        │               │        └──────┬──────┘
        │               │               │
        │               │        ┌─────────────┐
        │               │        │ View        │
        │               │        └──────┬──────┘
        │               │               │
        │               │        ┌─────────────┐
        │               │        │ Style       │◄──── ThemeSnapshot
        │               │        └──────┬──────┘
        │               │               │
        │               │        ┌─────────────┐
        │               │        │ Renderer    │
        │               │        └─────────────┘
```

---

## Rendering Model

The render path is designed for high-frequency rendering loops:

- the host application owns the managers it needs
- `ThemeManager` rebuilds its snapshot only when the theme changes
- views read the cached snapshot once per draw or measurement pass
- styles resolve colors, typography, and geometry-free render data
- renderers only draw and do not infer interaction state

This keeps theme work outside the hot render path and preserves MVVM boundaries.

---

## Binding

Binding is explicit and sketch-side.

- controls are composed through public facades
- JSON can load a mixed set of controls as `Map<String, Control>`
- the sketch wires listeners and synchronization explicitly
- JSON does not define binding

The base example is unidirectional; the bidirectional example is a small extension of it. This keeps all synchronization logic visible at the application level and preserves a single public narrative around facades and composition.

See [Binding](docs/binding.md).

---

## Example Usage

For a complete, ready-to-run Processing integration, see the companion template:

👉 https://github.com/cdpoloz/cpz-mvvm-processing-template

That template shows:
- how to wire Processing input callbacks
- how to use `InputManager` and `InputLayer`
- how to compose controls using public facades
- how to structure a real sketch using this framework

---

## Project Structure

- `src/com/cpz/processing/controls/common`: shared controls infrastructure; generic helpers such as color utilities live in `cpz-utils`
- `src/com/cpz/processing/controls/core`: cross-cutting MVVM, input, theme, overlay, focus, and layout primitives
- `src/com/cpz/processing/controls/controls`: public control facades and the minimal shared `Control` contract
- `src/com/cpz/processing/controls/examples`: example sketches used as interactive playgrounds
- `lib/cpz-utils/cpz-utils.jar`: required external utility dependency, including `com.cpz.utils.color.Colors`
- `docs`: human-facing documentation
- `docs/uml`: PlantUML diagrams

---

## Documentation

- [Control](docs/control.md)
- [Architecture](docs/architecture.md)
- [Composition Patterns](docs/composition-patterns.md)
- [Binding](docs/binding.md)
- [Input System](docs/input-system.md)
- [JSON Configuration](docs/json-configuration.md)
- [Theme](docs/theme.md)
- [Button](docs/button.md)
- [Checkbox](docs/checkbox.md)
- [Dropdown](docs/dropdown.md)
- [Label](docs/label.md)
- [NumericField](docs/numericfield.md)
- [RadioGroup](docs/radiogroup.md)
- [Slider](docs/slider.md)
- [TextField](docs/textfield.md)
- [Toggle](docs/toggle.md)

The JSON documentation includes the canonical multi-control binding progression: first unidirectional sketch synchronization, then controlled bidirectional synchronization, both loaded from the same structural JSON document. The theme documentation uses `ThemeFacadeSketch` as the public example for sketch-owned theming with closed facades.

---

## Design Philosophy

- Explicit over implicit
- Composition over coupling
- Rendering and interaction are strictly separated
- Input sources stay outside the framework core

---

## Status

This project is actively used to validate MVVM patterns, explicit input routing, rendering separation, and `ViewModel` synchronization.

The focus is on architectural clarity and explicit behavior rather than framework-level abstraction or automation.

---

## License

`cpz-mvvm-processing-controls` is released under the MIT License. See [LICENSE](LICENSE).

---

## Author

**Carlos Polo Zamora**  
GitHub: https://github.com/cdpoloz  
Alias: CPZ / cepezeta / cdpoloz
