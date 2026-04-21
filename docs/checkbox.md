# Checkbox

`Checkbox` is the public facade recommended for the simple single-checkbox case.

Internally, the control still follows the framework's MVVM architecture. The facade does not replace that structure: it composes the existing `CheckboxModel`, `CheckboxViewModel`, `CheckboxView`, and `CheckboxInputAdapter` for you so the public API stays ergonomic without hiding the design of the framework.

---

## Responsibilities

The `Checkbox` control keeps the standard MVVM separation used across the framework:

- `Model` stores base state such as checked and enabled state
- `ViewModel` handles interaction state such as hover, press, and toggle activation
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

`Checkbox` is a convenience facade for the common case:

- it composes the default internal MVVM pieces
- it exposes a small public API for checked state, visibility, style, and change handling
- it exposes the stable control identity through `getCode()`
- it keeps `InputManager` ownership in the sketch
- it works with `CheckboxInputLayer` for the simple one-checkbox routing case

Use this API when you want the recommended public flow without manually wiring internal pieces. This is the recommended entry point for most use cases.

If you need to study or extend the architecture, see [Under the hood](#under-the-hood).

---

## Minimal example

A minimal public setup requires:

1. `Checkbox`
2. `InputManager`
3. `CheckboxInputLayer`
4. Forwarded `PointerEvent` callbacks from Processing

The framework does not provide input sources.

The application is responsible for capturing input and forwarding normalized events into the input pipeline.

High-level interaction and rendering flow:

```text
Processing → PointerEvent → InputManager → CheckboxInputLayer → Checkbox → View → Style → Renderer
```

---

## Step-by-step setup

This section shows the recommended public setup in a Processing sketch.

### 1. Create the checkbox

```java
private InputManager inputManager;
private Checkbox checkbox;

public void setup() {
    float x = 300f;
    float y = 125f;
    float w = 42f;
    float h = 42f;
    checkbox = new Checkbox(this, "chkPrimary", true, x, y, w, h);
}
```

The facade creates the internal control composition for you.

---

### 2. Assign the change action

```java
checkbox.setChangeListener(value -> {
    // the code that executes after a checkbox click goes here, for example:
    System.out.println("Checkbox checked = " + value);
});
```

This defines what happens when the checked state changes.

The listener receives the new boolean state through `ValueListener<Boolean>`.

The stable control identity is available through `checkbox.getCode()`.

---

### 3. Optional: customize the style

```java
CheckboxStyleConfig csc = new CheckboxStyleConfig();
csc.boxColor = Colors.rgb(48, 98, 219);
csc.boxHoverColor = Colors.rgb(74, 122, 234);
csc.boxPressedColor = Colors.rgb(34, 77, 184);
csc.checkColor = Colors.gray(255);
csc.borderColor = Colors.gray(255);
csc.borderWidth = 2.0f;
csc.borderWidthHover = 4.0f;
csc.cornerRadius = 10.0f;
csc.disabledAlpha = 90;
csc.checkInset = 0.20f;
checkbox.setStyle(new DefaultCheckboxStyle(csc));
```

This configuration controls the visual appearance:

- `boxColor` is the base fill color
- `boxHoverColor` is the fill color on hover
- `boxPressedColor` is the fill color while pressed
- `checkColor` is the check mark color
- `borderColor` is the border color
- `borderWidth` is the default border width
- `borderWidthHover` is the border width on hover
- `cornerRadius` controls rounded corners
- `disabledAlpha` controls disabled transparency
- `checkInset` controls the inner spacing of the check mark

Styles resolve visuals only. They do not contain interaction logic.

The checkbox can also be rendered using SVG. See [Checkbox (SVG)](checkbox-svg.md) for the specialized variant.

The same control can also be created from JSON. See [JSON Configuration](json-configuration.md) for the config-driven setup.

---

### 4. Register the reusable input layer

```java
inputManager = new InputManager();
inputManager.registerLayer(new CheckboxInputLayer(0, checkbox));
```

`InputManager` remains application-owned.

`CheckboxInputLayer` is the reusable bridge for the simple single-checkbox case:

- the sketch still owns the global input pipeline
- the layer forwards pointer events to the checkbox facade
- the control itself does not own the application's `InputManager`

---

### 5. Draw the checkbox

```java
public void draw() {
    background(28);
    checkbox.draw();
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
- `PRESS` starts a toggle interaction
- `RELEASE` ends the interaction and may toggle the checked state

This keeps the framework decoupled from Processing-specific APIs.

---

## State transitions

A checkbox typically moves through these interaction states:

```text
idle → hovered → pressed → released → idle
                         ↘ 
                            toggled
```

Typical flow:

- `MOVE` inside the view sets hover state
- `PRESS` inside the view sets pressed state
- `RELEASE` inside the view clears pressed state and toggles checked state
- `MOVE` outside the view clears hover state
- `RELEASE` outside the view ends the press without toggling

In practice, these transitions are handled by the internal input adapter and reflected in the internal `CheckboxViewModel`.

The public facade keeps that behavior unchanged.

---

## Under the hood

Internally, `Checkbox` still composes:

- `CheckboxModel`
- `CheckboxViewModel`
- `CheckboxView`
- `CheckboxInputAdapter`

So the architecture remains:

```text
Checkbox facade
  → CheckboxModel
  → CheckboxViewModel
  → CheckboxView
  → CheckboxInputAdapter
```

This matters for two reasons:

- the public API is simpler for onboarding and common usage
- the architectural layers remain available conceptually and in code for extension, debugging, and advanced scenarios

The facade is therefore a convenience entry point, not a new architecture.

---

## Full example

```java
import com.cpz.processing.controls.controls.checkbox.Checkbox;
import com.cpz.processing.controls.controls.checkbox.config.CheckboxStyleConfig;
import com.cpz.processing.controls.controls.checkbox.input.CheckboxInputLayer;
import com.cpz.processing.controls.controls.checkbox.style.DefaultCheckboxStyle;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.utils.color.Colors;
import processing.core.PApplet;

public class CheckboxTest extends PApplet {

    private InputManager inputManager;
    private Checkbox checkbox;
    private boolean currentValue;

    public void settings() {
        size(600, 300);
        smooth(8);
    }

    public void setup() {
        float x = 300f;
        float y = 125f;
        float w = 42f;
        float h = 42f;
        checkbox = new Checkbox(this, "chkTest", true, x, y, w, h);
        checkbox.setChangeListener(value -> currentValue = value);
        currentValue = checkbox.isChecked();
        // style
        CheckboxStyleConfig csc = new CheckboxStyleConfig();
        csc.boxColor = Colors.rgb(48, 98, 219);
        csc.boxHoverColor = Colors.rgb(74, 122, 234);
        csc.boxPressedColor = Colors.rgb(34, 77, 184);
        csc.checkColor = Colors.gray(255);
        csc.borderColor = Colors.gray(255);
        csc.borderWidth = 2.0f;
        csc.borderWidthHover = 4.0f;
        csc.cornerRadius = 10.0f;
        csc.disabledAlpha = 90;
        csc.checkInset = 0.20f;
        checkbox.setStyle(new DefaultCheckboxStyle(csc));
        // input manager
        inputManager = new InputManager();
        inputManager.registerLayer(new CheckboxInputLayer(0, checkbox));
        // text output
        textAlign(CENTER, CENTER);
    }

    public void draw() {
        background(28);
        checkbox.draw();
        text(checkbox.getCode() + " | Current checked state = " + currentValue, 300, 200);
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

- [Checkbox (SVG)](checkbox-svg.md)
- [JSON Configuration](json-configuration.md)
- [Input system](input-system.md)
- [Architecture](architecture.md)
