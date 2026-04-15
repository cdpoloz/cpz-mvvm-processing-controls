# Toggle

`Toggle` is the public facade recommended for the simple single-toggle case.

Internally, the control still follows the framework's MVVM architecture. The facade does not replace that structure: it composes the existing `ToggleModel`, `ToggleViewModel`, `ToggleView`, and `ToggleInputAdapter` for you so the public API stays ergonomic without hiding the design of the framework.

---

## Performance note

`ToggleView` keeps `ToggleViewState` theme-free, reads a cached `ThemeSnapshot` once from its style, and `ParametricToggleStyle` reads snapshot tokens for per-state fill and stroke resolution.

Toggle rendering no longer performs theme resolution inside the draw path. Snapshot reuse keeps the hot path constant-time.

---

## Responsibilities

The `Toggle` control keeps the standard MVVM separation used across the framework:

- `Model` stores base state such as current state, previous state, total states, and enabled state
- `ViewModel` handles interaction state such as hover, press, and state cycling on activation
- `View` owns layout, hit testing, and `ViewState` construction
- `Style` resolves visual appearance
- `Renderer` performs drawing only

Rendering pipeline:

```text
ViewModel → ViewState → Style → RenderStyle → Renderer
```

This separation still exists even when the sketch uses the facade.

---

## Public facade

`Toggle` is a convenience facade for the common case:

- it composes the default internal MVVM pieces
- it exposes a small public API for state, visibility, style, and change handling
- it exposes the stable control identity through `getCode()`
- it keeps `InputManager` ownership in the sketch
- it works with `ToggleInputLayer` for the simple one-toggle routing case

Use this API when you want the recommended public flow without manually wiring internal pieces. This is the recommended entry point for most use cases.

If you need to study or extend the architecture, see [Under the hood](#under-the-hood).

---

## Minimal example

A minimal public setup requires:

1. `Toggle`
2. `InputManager`
3. `ToggleInputLayer`
4. Forwarded `PointerEvent` callbacks from Processing

The framework does not provide input sources.

The application is responsible for capturing input and forwarding normalized events into the input pipeline.

High-level interaction and rendering flow:

```text
Processing → PointerEvent → InputManager → ToggleInputLayer → Toggle → View → Style → Renderer
```

---

## Step-by-step setup

This section shows the recommended public setup in a Processing sketch.

### 1. Create the toggle

```java
private InputManager inputManager;
private Toggle toggle;

public void setup() {
    float x = 300f;
    float y = 125f;
    float d = 100f;
    toggle = new Toggle(this, "tglTest", 0, 3, x, y, d, d);
}
```

The facade creates the internal control composition for you.

This example uses a three-state toggle, so clicks cycle through states `0`, `1`, and `2`.

---

### 2. Assign the change action

```java
toggle.setChangeListener(value -> {
    // the code that executes after a toggle click goes here, for example:
    System.out.println("Toggle state = " + value);
});
```

This defines what happens when the state changes.

The listener receives the new integer state through `ValueListener<Integer>`.

The stable control identity is available through `toggle.getCode()`.

---

### 3. Optional: customize the style

```java
ToggleStyleConfig tsc = new ToggleStyleConfig();
tsc.setShapeRenderer(new CircleShapeRenderer());
tsc.stateColors = new Integer[]{
    Colors.gray(70),
    Colors.rgb(232, 155, 44),
    Colors.rgb(32, 188, 176)
};
tsc.strokeColor = Colors.gray(255);
tsc.strokeWidth = 2.0f;
tsc.strokeWidthHover = 4.0f;
tsc.hoverBlendWithWhite = 0.18f;
tsc.pressedBlendWithBlack = 0.20f;
tsc.disabledAlpha = 70;
toggle.setStyle(new ParametricToggleStyle(tsc));
```

This configuration controls the visual appearance:

- `stateColors` defines the fill color for each state
- `strokeColor` is the outline color
- `strokeWidth` is the default outline width
- `strokeWidthHover` is the outline width on hover
- `hoverBlendWithWhite` lightens the shape on hover
- `pressedBlendWithBlack` darkens the shape while pressed
- `disabledAlpha` controls disabled transparency
- `shapeRenderer` defines the geometric renderer used by the style

Styles resolve visuals only. They do not contain interaction logic.

The toggle can also be rendered using SVG. See [Toggle (SVG)](toggle-svg.md) for the specialized variant.

The same control can also be created from JSON. See [JSON Configuration](json-configuration.md) for the config-driven setup.

---

### 4. Register the reusable input layer

```java
inputManager = new InputManager();
inputManager.registerLayer(new ToggleInputLayer(0, toggle));
```

`InputManager` remains application-owned.

`ToggleInputLayer` is the reusable bridge for the simple single-toggle case:

- the sketch still owns the global input pipeline
- the layer forwards pointer events to the toggle facade
- the control itself does not own the application's `InputManager`

---

### 5. Draw the toggle

```java
public void draw() {
    background(28);
    toggle.draw();
}
```

During `draw()`:

1. the facade delegates to its internal `View`
2. the view reads state from the internal `ViewModel`
3. the style resolves `RenderStyle`
4. the renderer draws the final result

---

## Input flow

The framework does not own the input source. It only consumes normalized input events provided by the application.

Processing callbacks such as `mouseMoved` and `mousePressed` are converted into `PointerEvent` and sent to the `InputManager`.

```java
public void mouseMoved() {
    inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.MOVE, (float) mouseX, (float) mouseY, mouseButton));
}

public void mouseDragged() {
    inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.DRAG, (float) mouseX, (float) mouseY, mouseButton));
}

public void mousePressed() {
    inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.PRESS, (float) mouseX, (float) mouseY, mouseButton));
}

public void mouseReleased() {
    inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.RELEASE, (float) mouseX, (float) mouseY, mouseButton));
}
```

Event roles:

- `MOVE` updates hover state
- `DRAG` keeps pointer interaction continuous
- `PRESS` starts a state change interaction
- `RELEASE` ends the interaction and may advance the state

This keeps the framework decoupled from Processing-specific APIs.

---

## State transitions

A toggle typically moves through these interaction states:

```text
idle → hovered → pressed → released → idle
                         ↘
                            next state
```

Typical flow:

- `MOVE` inside the view sets hover state
- `PRESS` inside the view sets pressed state
- `RELEASE` inside the view clears pressed state and advances the state index
- `MOVE` outside the view clears hover state
- `RELEASE` outside the view ends the press without changing state

For a three-state toggle, the observed cycle is:

```text
0 → 1 → 2 → 0
```

In practice, these transitions are handled by the internal input adapter and reflected in the internal `ToggleViewModel`.

The public facade keeps that behavior unchanged.

The direct Java API is intentionally permissive in the current iteration:

- `setTotalStates(int)` coerces the control to at least `1` state
- `setState(int)` clamps the requested value into the current valid range

This differs from the JSON loader, which validates `totalStates` and `state` strictly before the control is created. See [JSON Configuration](json-configuration.md).

---

## Under the hood

Internally, `Toggle` still composes:

- `ToggleModel`
- `ToggleViewModel`
- `ToggleView`
- `ToggleInputAdapter`

So the architecture remains:

```text
Toggle facade
  → ToggleModel
  → ToggleViewModel
  → ToggleView
  → ToggleInputAdapter
```

This matters for two reasons:

- the public API is simpler for onboarding and common usage
- the architectural layers remain available conceptually and in code for extension, debugging, and advanced scenarios

The facade is therefore a convenience entry point, not a new architecture.

---

## Full example

```java
import com.cpz.processing.controls.controls.toggle.Toggle;
import com.cpz.processing.controls.controls.toggle.config.ToggleStyleConfig;
import com.cpz.processing.controls.controls.toggle.input.ToggleInputLayer;
import com.cpz.processing.controls.controls.toggle.style.ParametricToggleStyle;
import com.cpz.processing.controls.controls.toggle.style.render.CircleShapeRenderer;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.processing.controls.core.util.Colors;
import processing.core.PApplet;

public class ToggleTest extends PApplet {

    private InputManager inputManager;
    private Toggle toggle;
    private int currentState;

    public void settings() {
        size(600, 320);
        smooth(8);
    }

    public void setup() {
        float x = 300f;
        float y = 125f;
        float d = 100f;
        toggle = new Toggle(this, "tglTest", 0, 3, x, y, d, d);
        toggle.setChangeListener(value -> {
            // the code that executes after a toggle click goes here, for example:
            System.out.println("Toggle state = " + value);
            currentState = value;
        });
        currentState = toggle.getState();
        // style
        ToggleStyleConfig tsc = new ToggleStyleConfig();
        tsc.setShapeRenderer(new CircleShapeRenderer());
        tsc.stateColors = new Integer[]{
                Colors.gray(70),
                Colors.rgb(232, 155, 44),
                Colors.rgb(32, 188, 176)
        };
        tsc.strokeColor = Colors.gray(255);
        tsc.strokeWidth = 2.0f;
        tsc.strokeWidthHover = 4.0f;
        tsc.hoverBlendWithWhite = 0.18f;
        tsc.pressedBlendWithBlack = 0.20f;
        tsc.disabledAlpha = 70;
        toggle.setStyle(new ParametricToggleStyle(tsc));
        // input manager
        inputManager = new InputManager();
        inputManager.registerLayer(new ToggleInputLayer(0, toggle));
        // text output
        textAlign(CENTER, CENTER);
    }

    public void draw() {
        background(28);
        toggle.draw();
        text(toggle.getCode() + " | state = " + currentState + " / " + (toggle.getTotalStates() - 1), 300, 210);
        text("click to cycle 0 → 1 → 2 → 0", 300, 250);
    }

    public void mouseMoved() {
        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.MOVE, (float) mouseX, (float) mouseY, mouseButton));
    }

    public void mouseDragged() {
        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.DRAG, (float) mouseX, (float) mouseY, mouseButton));
    }

    public void mousePressed() {
        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.PRESS, (float) mouseX, (float) mouseY, mouseButton));
    }

    public void mouseReleased() {
        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.RELEASE, (float) mouseX, (float) mouseY, mouseButton));
    }
}
```

---

## See also

- [Toggle (SVG)](toggle-svg.md)
- [JSON Configuration](json-configuration.md)
- [Input system](input-system.md)
- [Architecture](architecture.md)
