# Button

`Button` is the public facade recommended for the simple single-button case.

Internally, the control still follows the framework's MVVM architecture. The facade does not replace that structure: it composes the existing `ButtonModel`, `ButtonViewModel`, `ButtonView`, and `ButtonInputAdapter` for you so the public API stays ergonomic without hiding the design of the framework.

---

## Performance note

`ButtonView` builds a plain `ButtonViewState`, reads the cached `ThemeSnapshot` once from its style, and passes that snapshot into `DefaultButtonStyle`. The style reads tokens from the snapshot instead of resolving theme objects during render.

Themed button rendering no longer allocates from theme resolution in `draw()`. Theme snapshots are reused until the sketch changes theme.

---

## Responsibilities

The `Button` control keeps the standard MVVM separation used across the framework:

- `Model` stores base state such as text and enabled state
- `ViewModel` handles interaction state such as hover, press, and click
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

`Button` is a convenience facade for the common case:

- it composes the default internal MVVM pieces
- it exposes a small public API for text, state, style, and click handling
- it keeps `InputManager` ownership in the sketch
- it works with `ButtonInputLayer` for the simple one-button routing case

Use this API when you want the recommended public flow without manually wiring internal pieces. This is the recommended entry point for most use cases.

If you need to study or extend the architecture, see [Under the hood](#under-the-hood).

---

## Minimal example

A minimal public setup requires:

1. `Button`
2. `InputManager`
3. `ButtonInputLayer`
4. Forwarded `PointerEvent` callbacks from Processing

The framework does not provide input sources.

The application is responsible for capturing input and forwarding normalized events into the input pipeline.

High-level interaction and rendering flow:

```text
Processing → PointerEvent → InputManager → ButtonInputLayer → Button → View → Style → Renderer
```

---

## Step-by-step setup

This section shows the recommended public setup in a Processing sketch.

### 1. Create the button

```java
private InputManager inputManager;
private Button button;

public void setup() {
    float x = 300f;
    float y = 125f;
    float w = 200f;
    float h = 60f;
    button = new Button(this, "Simple Button", x, y, w, h);
}
```

The facade creates the internal control composition for you.

---

### 2. Assign the click action

```java
button.setClickListener(() -> {
    // the code that executes after a button click goes here, for example:
    System.out.println("You clicked the button!");
});
```

This defines what happens when the button is clicked.

---

### 3. Optional: customize the style

```java
ButtonStyleConfig bsc = new ButtonStyleConfig();
bsc.baseColor = Colors.rgb(48, 98, 219);
bsc.textColor = Colors.gray(255);
bsc.strokeColor = Colors.gray(255);
bsc.strokeWeight = 2.0f;
bsc.strokeWeightHover = 4.0f;
bsc.cornerRadius = 18.0f;
bsc.disabledAlpha = 90;
bsc.hoverBlendWithWhite = 0.12f;
bsc.pressedBlendWithBlack = 0.25f;
button.setStyle(new DefaultButtonStyle(bsc));
```

This configuration controls the visual appearance:

- `baseColor` is the main color
- `textColor` is the label color
- `strokeColor` is the border color
- `strokeWeight` is the default border width
- `strokeWeightHover` is the border width on hover
- `cornerRadius` controls rounded corners
- `disabledAlpha` controls disabled transparency
- `hoverBlendWithWhite` lightens the button on hover
- `pressedBlendWithBlack` darkens the button while pressed

Styles resolve visuals only. They do not contain interaction logic.

The button can also be rendered using SVG. See [Button (SVG)](button-svg.md) for the specialized variant.

---

### 4. Register the reusable input layer

```java
inputManager = new InputManager();
inputManager.registerLayer(new ButtonInputLayer(0, button));
```

`InputManager` remains application-owned.

`ButtonInputLayer` is the reusable bridge for the simple single-button case:

- the sketch still owns the global input pipeline
- the layer forwards pointer events to the button facade
- the control itself does not own the application's `InputManager`

---

### 5. Draw the button

```java
public void draw() {
    background(28);
    button.draw();
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
- `PRESS` starts a click interaction
- `RELEASE` ends a click interaction and may trigger the action

This keeps the framework decoupled from Processing-specific APIs.

---

## State transitions

A button typically moves through these interaction states:

```text
idle → hovered → pressed → released → idle
                         ↘ 
                            clicked
```

Typical flow:

- `MOVE` inside the view sets hover state
- `PRESS` inside the view sets pressed state
- `RELEASE` inside the view clears pressed state and triggers click
- `MOVE` outside the view clears hover state
- `RELEASE` outside the view ends the press without click

In practice, these transitions are handled by the internal input adapter and reflected in the internal `ButtonViewModel`.

The public facade keeps that behavior unchanged.

---

## Under the hood

Internally, `Button` still composes:

- `ButtonModel`
- `ButtonViewModel`
- `ButtonView`
- `ButtonInputAdapter`

So the architecture remains:

```text
Button facade
  → ButtonModel
  → ButtonViewModel
  → ButtonView
  → ButtonInputAdapter
```

This matters for two reasons:

- the public API is simpler for onboarding and common usage
- the architectural layers remain available conceptually and in code for extension, debugging, and advanced scenarios

The facade is therefore a convenience entry point, not a new architecture.

---

## Full example

```java
import com.cpz.processing.controls.controls.button.Button;
import com.cpz.processing.controls.controls.button.config.ButtonStyleConfig;
import com.cpz.processing.controls.controls.button.input.ButtonInputLayer;
import com.cpz.processing.controls.controls.button.style.DefaultButtonStyle;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.processing.controls.core.util.Colors;
import processing.core.PApplet;

public class ButtonTest extends PApplet {

    private InputManager inputManager;
    private Button button;
    private int clickCount;

    public void settings() {
        size(600, 300);
        smooth(8);
    }

    public void setup() {
        float x = 300f;
        float y = 125f;
        float w = 200f;
        float h = 60f;
        button = new Button(this, "Simple Button", x, y, w, h);
        button.setClickListener(() -> {
            // the code that executes after a button click goes here, for example:
            System.out.println("You clicked the button!");
            clickCount++;
        });
        // style
        ButtonStyleConfig bsc = new ButtonStyleConfig();
        bsc.baseColor = Colors.rgb(48, 98, 219);
        bsc.textColor = Colors.gray(255);
        bsc.strokeColor = Colors.gray(255);
        bsc.strokeWeight = 2.0f;
        bsc.strokeWeightHover = 4.0f;
        bsc.cornerRadius = 18.0f;
        bsc.disabledAlpha = 90;
        bsc.hoverBlendWithWhite = 0.12f;
        bsc.pressedBlendWithBlack = 0.25f;
        button.setStyle(new DefaultButtonStyle(bsc));
        // input manager
        inputManager = new InputManager();
        inputManager.registerLayer(new ButtonInputLayer(0, button));
        // text output
        textAlign(CENTER, CENTER);
    }

    public void draw() {
        background(28);
        button.draw();
        text("Current click count = " + clickCount, 300, 200);
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

- [Button (SVG)](button-svg.md)
- [Input system](input-system.md)
- [Architecture](architecture.md)
