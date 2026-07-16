# CPZ MVVM Processing Controls
![Java](https://img.shields.io/badge/Java-17+-orange)
![Processing](https://img.shields.io/badge/Processing-4.5.x-blue)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.cdpoloz/cpz-mvvm-processing-controls?label=Maven%20Central&color=1f6feb)](https://central.sonatype.com/artifact/io.github.cdpoloz/cpz-mvvm-processing-controls)
![Status](https://img.shields.io/badge/status-active-brightgreen)
![License](https://img.shields.io/badge/license-Apache--2.0-lightgrey)
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
- Panel-based grouping for controls that share a local coordinate space

---

## Installation

This project is built as a Maven JAR. Dependencies are resolved by Maven; the
repository does not require a local dependency directory or manual Processing
JAR setup.

### Requirements

- JDK 17 or newer, matching the Maven compiler release used by the project
- Maven

Processing itself does not need to be installed manually for the Maven build.
Maven resolves Processing Core and `cpz-utils` as transitive dependencies of
this library.

### Dependency levels

CPZ dependencies:

- `io.github.cdpoloz:cpz-utils:0.2.3`
- controlled by the author and versioned as a normal Maven dependency
- provides shared utility APIs used by the controls project
- color helpers such as `Colors.rgb(...)` and `Colors.gray(...)` belong to
  `com.cpz.utils.color.Colors`; they are no longer part of
  `com.cpz.processing.controls`

External dependencies:

- `org.processing:core:4.5.5`
- resolved by Maven from configured repositories
- provides Processing rendering APIs and keeps its own license, separate from
  this project's Apache 2.0 license

### Maven dependency

If consuming a published or locally installed build, add the controls artifact
to your Maven project:

```xml
<dependency>
    <groupId>io.github.cdpoloz</groupId>
    <artifactId>cpz-mvvm-processing-controls</artifactId>
    <version>0.9.2</version>
</dependency>
```

Version `0.9.2` is the current stable release available from Maven Central.
Processing Core (`org.processing:core:4.5.5`) and `cpz-utils`
(`io.github.cdpoloz:cpz-utils:0.2.3`) are resolved transitively by Maven; do
not add them as manually copied JARs.

### Local development

Build this project with:

```text
git clone <cpz-mvvm-processing-controls-repository-url>
cd cpz-mvvm-processing-controls
mvn clean test
mvn clean package
```

Use `mvn clean install` in this repository when another local project needs to
consume this library from your local Maven repository.

### IntelliJ setup

Open the repository as a Maven project and reload Maven dependencies. If
IntelliJ cannot resolve the dependencies, reload the Maven project and verify
that Maven Central is reachable. Processing Core and `cpz-utils` are resolved
transitively by Maven.

### Maven layout

The project is prepared for Maven with the main library code in
`src/main/java`. Example sketches and the development launcher live under
`src/main/java/com/cpz/processing/controls/examples` and
`src/main/java/com/cpz/processing/controls/main`; the Maven JAR plugin excludes
those packages from the published library JAR.

When Maven is available, the expected build commands are:

```text
mvn clean test
mvn clean package
mvn clean install
```

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

## Panel Example

`Panel` groups child controls without changing the `Control` contract. Child
coordinates are local to the panel. If the panel is at `(100, 80)` and a child
button is at `(10, 20)`, the child is drawn and hit-tested at `(110, 100)`.
Panels are transparent by default, but can draw optional background and stroke
chrome through runtime `PanelStyle` setters or the JSON `style` block.

```java
Panel panel = new Panel(this, "pnlSettings", 100, 80, 320, 220);

panel.add(new Label(this, "lblTitle", "Settings", 16, 16, 220, 28));
panel.add(new Button(this, "btnSave", "Save", 24, 70, 120, 40));

inputManager.registerLayer(new PanelInputLayer(0, panel));

public void draw() {
    background(32);
    panel.draw();
}
```

Moving the panel with `setPosition(...)` moves the group visually and
interactively without rewriting child coordinates. `DropDown` can be added as
a runtime child with `panel.add(dropDown)`: the collapsed field uses panel-local
coordinates, while the expanded list remains a global overlay. JSON can
configure both controls, but the parent-child relationship is still established
in Java.

See [Panel](docs/panel.md),
`src/main/java/com/cpz/processing/controls/examples/panel/PanelVisualTest.java`,
and `src/main/java/com/cpz/processing/controls/examples/panel/PanelDropDownJsonTest.java`.

---

## Relative Bounds And Text Size

Relative measures are explicit. The loader does not infer that values between
`0` and `1` are relative.

- relative `x` uses `parentWidth * factor`
- relative `y` uses `parentHeight * factor`
- relative `width` uses `parentHeight * factor`
- relative `height` uses `parentHeight * factor`
- relative `textSize` uses `parentHeight * factor`

Legacy absolute JSON remains valid:

```json
{
  "x": 100,
  "y": 50,
  "width": 200,
  "height": 40
}
```

The explicit JSON format is:

```json
{
  "type": "button",
  "code": "btnSave",
  "text": "Save",
  "bounds": {
    "mode": "relative",
    "x": 0.1,
    "y": 0.2,
    "width": 0.35,
    "height": 0.08
  },
  "textSize": {
    "mode": "relative",
    "value": 0.035
  }
}
```

Precedence rules:

- `bounds` wins over legacy `x` / `y` / `width` / `height`
- top-level `textSize` wins over legacy `style.textSize`

Current limitations:

- `Panel.children` is not a JSON feature; compose panel children in Java
- layout, padding, clipping, scroll, titles, and shadows are not part of `Panel`

See `src/main/java/com/cpz/processing/controls/examples/button/ButtonRelativeTest.java`
for a minimal `ControlBounds.relative(...)` sketch and
`src/main/java/com/cpz/processing/controls/examples/button/ButtonJsonRelativeTest.java`
with `data/config/button-relative.json` for the equivalent JSON example.

---

## Release 0.7.0

Version `0.7.0` adds `Indicator`, a simple non-interactive LED-style control.

`Indicator` supports:

- programmatic on/off state
- runtime `onColor` and `offColor`
- runtime `strokeColor` and `strokeWeight`
- absolute and relative `ControlBounds`
- default circle, SVG, and alpha-mask PNG renderers through the same style renderer convention used by Button and Toggle
- tooltips, including runtime tooltip text updates
- JSON loading through `type: "indicator"`
- use as a child of `Panel`

`Indicator` does not consume pointer or keyboard input in this version. See
[Indicator](docs/indicator.md) and the examples under
`src/main/java/com/cpz/processing/controls/examples/indicator`.

```java
Indicator indicator = new Indicator(this, "indStatus", 40, 40, 24, 24)
        .setTooltip("Status");
indicator.setOn(true);
indicator.setOnColor(0xFF2ECC71);
```

---

## Release 0.6.0

Version `0.6.0` includes:

- `Panel` as a control container with local coordinates for child controls
- optional `PointerRoutableControl` and `KeyboardRoutableControl` routing hooks
- `ControlBounds` and `ControlMeasure` with `ABSOLUTE` and `RELATIVE` modes
- relative bounds for the main controls API
- relative `textSize` for controls with public text rendering
- JSON support for relative `bounds` and `textSize` with legacy compatibility
- `ButtonRelativeTest` as a minimal runnable relative-bounds reference
- `ButtonJsonRelativeTest` and `data/config/button-relative.json` as minimal
  runnable JSON-relative references

Historical limitations in `0.6.0`:

- `Panel.children` from JSON was not part of that release
- `DropDown` inside a `Panel` was not supported interactively in that release

Those bullets describe the `0.6.0` release state. In the current codebase,
runtime `Panel + DropDown` composition is supported; `Panel.children` in JSON
is still not implemented.

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

Tooltips:
TooltipAttachable / TooltipTarget → TooltipOverlayController → OverlayManager

Notifications:
NotificationManager → OverlayManager
```

---

## Overview

This project is a UI framework intended for Processing sketches and for other host environments that can provide normalized input events.

The public control layer is exposed through closed ergonomic facades such as `Button`, `Checkbox`, `Toggle`, `Slider`, `Label`, `RadioGroup`, `TextField`, `NumericField`, `DropDown`, `Panel`, `Indicator`, and `ProgressBar`.

`Notification` is a toast-style runtime overlay feature, not a `Control`. It is
created programmatically through `NotificationManager`, does not consume
pointer or keyboard input in `0.9.2`, and is not configured inside
`controls[]`.

Notification manager/style defaults can be loaded from standalone JSON through
`NotificationConfigLoader`, for example `data/config/notification.json`. This
JSON is separate from the controls JSON layer: it configures placement,
durations, max visible count, and visual style only. It does not define
messages, trigger notifications, or register `Notification` as a control type.

`ProgressBar` is the non-interactive progress display control introduced before
the notification work and remains available in `0.9.2`.

Those facades also share a lightweight public contract, `Control`, for the small transversal surface that is common across the controls without exposing MVVM internals.

`Panel` is the container facade for grouping controls. It uses local child
coordinates and receives input through `PanelInputLayer`. JSON creation for
panels is not part of the current JSON layer.

Tooltips are reusable overlays attached through `TooltipTarget`; controls and
manual regions that own mutable tooltip data also implement `TooltipAttachable`.
They can be used with controls or arbitrary Processing regions such as images
and manually drawn rectangles without changing the `Control` contract.

Tooltip text can be updated at runtime with `setTooltipText(...)`; call
`TooltipOverlayController.refresh()` after programmatic updates when the pointer
is already over the target. Disabled controls can still show tooltips while
ignoring their normal input actions; disabling the `Tooltip` itself with
`tooltip.setEnabled(false)` hides the overlay.

Control JSON can include tooltip blocks, reusable root `tooltipStyles`, and
`styleRef` references. Standalone tooltips can also be loaded with
`TooltipFactory.loadFromJson(...)` and assigned to manual `TooltipArea`
targets.

Notifications are runtime status messages owned by the sketch. Use them for
non-blocking info, success, warning, and error feedback. Use a future
dialog/modal feature, not `Notification`, for blocking user decisions.

The JSON layer can also create one or more controls from a single document and returns them as `Map<String, Control>`. JSON can configure control layout and supported style blocks, including `Panel.style`; binding, behavior wiring, and `Panel` parent-child relationships stay in sketch code.

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

1. Add this library as a Maven dependency, or build and install it locally with Maven.
2. Let Maven resolve Processing Core and `cpz-utils` transitively from Maven Central.
3. Create controls directly through the public facades or load them from JSON with `ControlConfigLoader`.
4. Provide normalized input events through an external adapter.
5. Dispatch those events through `InputManager`.
6. Resolve listeners and binding in the sketch.
7. Call `draw()` inside your host render loop.

Typical flow:

- facades expose the public API used by the sketch
- `Control` provides the minimal common contract when a mixed collection is enough
- JSON can create `Map<String, Control>` for composition-oriented sketches
- binding stays in the sketch instead of being declared in JSON

At the public API level, each concrete facade keeps its own domain-specific methods, while `Control` provides only the minimal common contract for drawing, code identity, visibility, enabled state, and positioning.

You can find working examples in:

`examples/src/main/java/com/cpz/processing/controls/examples`

The tooltip JSON example is
`src/main/java/com/cpz/processing/controls/examples/tooltip/TooltipVisualJsonTest.java`.
It uses `data/config/tooltip-visual-test.json` for control tooltip blocks and
`data/config/server-tooltip.json` for a standalone tooltip assigned to a manual
`TooltipArea`.

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

- `src/main/java/com/cpz/processing/controls/common`: shared controls infrastructure; generic helpers such as color utilities live in `cpz-utils`
- `src/main/java/com/cpz/processing/controls/core`: cross-cutting MVVM, input, theme, overlay, focus, and layout primitives
- `src/main/java/com/cpz/processing/controls/controls`: public control facades and the minimal shared `Control` contract
- `src/main/resources`: main resources for future library resources
- `src/test/java` and `src/test/resources`: standard Maven test source and resource roots
- `src/main/java/com/cpz/processing/controls/examples`: example sketches used as interactive playgrounds, excluded from the main JAR
- `src/main/java/com/cpz/processing/controls/main`: development launcher, excluded from the main JAR
- `data`: example and sketch assets kept at the repository root so existing `data/...` Processing paths continue to work
- `docs`: human-facing documentation
- `docs/uml`: PlantUML diagrams

Text-rendering controls (`Button`, `Label`, `Slider`, `RadioGroup`,
`TextField`, `NumericField`, and `DropDown`) support an optional per-control
`PFont`, including JSON `style.font` paths. Fonts from JSON are loaded when the
control is created. A `null` font preserves the font active in
Processing/PGraphics; the theme does not yet define a global font.

Tooltip JSON follows the same render-loop rule: fonts from `tooltipStyles` and
standalone tooltip JSON are loaded during configuration/control creation, not
from `draw()`.

---

## Documentation

- [Control](docs/control.md)
- [Architecture](docs/architecture.md)
- [Composition Patterns](docs/composition-patterns.md)
- [Binding](docs/binding.md)
- [Input System](docs/input-system.md)
- [JSON Configuration](docs/json-configuration.md)
- [Panel](docs/panel.md)
- [Tooltip](docs/tooltip.md)
- [Notification](docs/notification.md)
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

`cpz-mvvm-processing-controls` is released under the Apache License, Version 2.0. See [LICENSE](LICENSE).

The examples include JetBrains Mono, distributed under the SIL Open Font
License 1.1. See [OFL](data/font/OFL.txt).

---

## Author

**Carlos Polo Zamora**  
GitHub: https://github.com/cdpoloz  
Alias: CPZ / cepezeta / cdpoloz
