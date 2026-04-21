# Slider

`Slider` is the public facade recommended for the simple single-slider case.

Internally, the control still follows the framework's MVVM architecture. The facade does not replace that structure: it composes the existing `SliderModel`, `SliderViewModel`, `SliderView`, and `SliderInputAdapter` for you so the public API stays ergonomic without exposing MVVM internals from the common public surface.

---

## Responsibilities

The `Slider` control keeps the standard MVVM separation used across the framework:

- `Model` stores base state such as value, range, step, snap mode, and enabled state
- `ViewModel` handles interaction state such as hover, press, drag, wheel input, and value formatting
- `View` owns layout, hit testing, orientation-aware geometry, and `ViewState` construction
- `Style` resolves visual appearance
- `Renderer` performs drawing only

Rendering pipeline:

```text
ViewModel → ViewState → Style → RenderStyle → Renderer
```

This separation still exists even when the sketch uses the facade.

---

## Public facade

`Slider` is a convenience facade for the common case:

- it composes the default internal MVVM pieces
- it exposes a small public API for value, range, snap behavior, formatting, visibility, and style
- it exposes the stable control identity through `getCode()`
- it keeps `InputManager` ownership in the sketch
- it works with `SliderInputLayer` for the simple one-slider routing case

Use this API when you want the recommended public flow without manually wiring internal pieces. This is the recommended entry point for most use cases.

If you need to study or extend the architecture, see [Under the hood](#under-the-hood).

---

## Minimal example

A minimal public setup requires:

1. `Slider`
2. `InputManager`
3. `SliderInputLayer`
4. Forwarded `PointerEvent` callbacks from Processing

The framework does not provide input sources.

The application is responsible for capturing input and forwarding normalized events into the input pipeline.

High-level interaction and rendering flow:

```text
Processing → PointerEvent → InputManager → SliderInputLayer → Slider → View → Style → Renderer
```

---

## Step-by-step setup

This section shows the recommended public setup in a Processing sketch.

### 1. Create the slider

```java
private InputManager inputManager;
private Slider slider;

public void setup() {
    float x = 300f;
    float y = 130f;
    float w = 320f;
    float h = 72f;
    slider = new Slider(
            this,
            "sldTest",
            new BigDecimal("0"),
            new BigDecimal("1"),
            new BigDecimal("0.05"),
            new BigDecimal("0.35"),
            x,
            y,
            w,
            h
    );
}
```

The facade creates the internal control composition for you.

This example uses a horizontal slider with a value range from `0` to `1` in `0.05` steps.

---

### 2. Assign the change action

```java
slider.setChangeListener(value -> {
    // the code that executes after the slider value changes goes here, for example:
    System.out.println("Slider value = " + value);
});
```

This defines what happens when the slider value changes.

The listener receives the new `BigDecimal` value through `ValueListener<BigDecimal>`.

The stable control identity is available through `slider.getCode()`.

---

### 3. Optional: configure formatting and style

```java
slider.setFormatter(value -> value.setScale(2, RoundingMode.HALF_UP).toPlainString());
slider.setShowValueText(true);

SliderStyleConfig ssc = new SliderStyleConfig();
ssc.trackColor = Colors.rgb(62, 72, 86);
ssc.trackHoverColor = Colors.rgb(86, 98, 116);
ssc.trackPressedColor = Colors.rgb(44, 52, 64);
ssc.trackStrokeColor = Colors.gray(220);
ssc.trackStrokeWeight = 1.5f;
ssc.trackStrokeWeightHover = 2.5f;
ssc.trackThickness = 10.0f;
ssc.activeTrackColor = Colors.rgb(56, 159, 232);
ssc.activeTrackHoverColor = Colors.rgb(98, 184, 244);
ssc.activeTrackPressedColor = Colors.rgb(38, 124, 184);
ssc.thumbColor = Colors.gray(255);
ssc.thumbHoverColor = Colors.rgb(198, 234, 255);
ssc.thumbPressedColor = Colors.rgb(170, 220, 255);
ssc.thumbStrokeColor = Colors.rgb(32, 78, 120);
ssc.thumbStrokeWeight = 2.0f;
ssc.thumbStrokeWeightHover = 3.0f;
ssc.thumbSize = 28.0f;
ssc.textColor = Colors.gray(245);
ssc.disabledAlpha = 90;
ssc.showValueText = true;

slider.setStyle(new SliderStyle(ssc));
```

This configuration controls the visual appearance:

- `trackColor`, `trackHoverColor`, and `trackPressedColor` define the base track colors
- `activeTrackColor`, `activeTrackHoverColor`, and `activeTrackPressedColor` define the filled portion
- `thumbColor`, `thumbHoverColor`, and `thumbPressedColor` define the thumb colors
- `trackStrokeColor` and `thumbStrokeColor` define outline colors
- `trackThickness` controls track geometry
- `thumbSize` controls thumb size
- `showValueText` controls whether the resolved text is drawn by the slider renderer

The facade also exposes range and behavior methods such as:

- `setValue(BigDecimal)`
- `setMin(BigDecimal)`
- `setMax(BigDecimal)`
- `setStep(BigDecimal)`
- `setSnapMode(SnapMode)`
- `setOrientation(SliderOrientation)`

Styles resolve visuals only. They do not contain interaction logic.

The slider thumb can also be rendered using SVG. See [Slider (SVG)](slider-svg.md) for the specialized variant.

The same control can also be created from JSON. See [JSON Configuration](json-configuration.md) for the config-driven setup.

---

### 4. Register the reusable input layer

```java
inputManager = new InputManager();
inputManager.registerLayer(new SliderInputLayer(0, slider));
```

`InputManager` remains application-owned.

`SliderInputLayer` is the reusable bridge for the simple single-slider case:

- the sketch still owns the global input pipeline
- the layer forwards pointer events to the slider facade
- the control itself does not own the application's `InputManager`

---

### 5. Draw the slider

```java
public void draw() {
    background(28);
    slider.draw();
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

Processing callbacks such as `mouseMoved`, `mousePressed`, and `mouseWheel` are converted into `PointerEvent` and sent to the `InputManager`.

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

public void mouseWheel(MouseEvent event) {
    inputManager.dispatchPointer(new PointerEvent(
            PointerEvent.Type.WHEEL,
            (float) mouseX,
            (float) mouseY,
            mouseButton,
            (float) event.getCount(),
            event.isShiftDown(),
            event.isControlDown()
    ));
}
```

Event roles:

- `MOVE` updates hover state
- `DRAG` keeps the drag interaction continuous
- `PRESS` starts a drag interaction and updates the current value
- `RELEASE` ends the drag interaction
- `WHEEL` adjusts the value while the pointer is hovering the slider

Wheel semantics in the current iteration:

- base wheel increment uses the configured `step`
- `SHIFT` multiplies the wheel step by `10`
- `CTRL` multiplies the wheel step by `0.1`

This keeps the framework decoupled from Processing-specific APIs.

---

## Value semantics

The slider facade exposes the same value semantics implemented internally by the MVVM layers:

- `min` must be lower than `max`
- `step` must be greater than `0`
- `setValue(BigDecimal)` clamps to the configured range
- `SnapMode.ALWAYS` snaps during pointer interaction
- `SnapMode.ON_RELEASE` allows free drag movement and snaps when the drag ends

The public API intentionally reuses `BigDecimal` because slider ranges and steps are numeric-domain concerns, not just rendering values.

The direct Java API is intentionally permissive in the current iteration:

- `setValue(BigDecimal)` clamps out-of-range values
- `setSnapMode(SnapMode)` re-normalizes the current value according to the selected mode

This differs from the JSON loader, which validates slider range, step, value, orientation, and `snapMode` strictly before the control is created. See [JSON Configuration](json-configuration.md).

---

## Under the hood

Internally, `Slider` still composes:

- `SliderModel`
- `SliderViewModel`
- `SliderView`
- `SliderInputAdapter`

So the architecture remains:

```text
Slider facade
  → SliderModel
  → SliderViewModel
  → SliderView
  → SliderInputAdapter
```

This matters for two reasons:

- the public API is simpler for onboarding and common usage
- the architectural layers remain available conceptually and in code for extension, debugging, and advanced scenarios

The facade is therefore a convenience entry point, not a new architecture.

---

## Full example

```java
import com.cpz.processing.controls.controls.slider.Slider;
import com.cpz.processing.controls.controls.slider.config.SliderStyleConfig;
import com.cpz.processing.controls.controls.slider.input.SliderInputLayer;
import com.cpz.processing.controls.controls.slider.style.SliderStyle;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.utils.color.Colors;
import processing.core.PApplet;
import processing.event.MouseEvent;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SliderTest extends PApplet {
    private InputManager inputManager;
    private Slider slider;
    private String currentValue;

    public void settings() {
        size(600, 340);
        smooth(8);
    }

    public void setup() {
        float x = 300f;
        float y = 130f;
        float w = 320f;
        float h = 72f;
        slider = new Slider(
                this,
                "sldTest",
                new BigDecimal("0"),
                new BigDecimal("1"),
                new BigDecimal("0.05"),
                new BigDecimal("0.35"),
                x,
                y,
                w,
                h
        );
        slider.setFormatter(value -> value.setScale(2, RoundingMode.HALF_UP).toPlainString());
        slider.setChangeListener(value -> currentValue = slider.getFormattedValue());
        currentValue = slider.getFormattedValue();
        // style
        SliderStyleConfig ssc = new SliderStyleConfig();
        ssc.trackColor = Colors.rgb(62, 72, 86);
        ssc.trackHoverColor = Colors.rgb(86, 98, 116);
        ssc.trackPressedColor = Colors.rgb(44, 52, 64);
        ssc.trackStrokeColor = Colors.gray(220);
        ssc.trackStrokeWeight = 1.5f;
        ssc.trackStrokeWeightHover = 2.5f;
        ssc.trackThickness = 10.0f;
        ssc.activeTrackColor = Colors.rgb(56, 159, 232);
        ssc.activeTrackHoverColor = Colors.rgb(98, 184, 244);
        ssc.activeTrackPressedColor = Colors.rgb(38, 124, 184);
        ssc.thumbColor = Colors.gray(255);
        ssc.thumbHoverColor = Colors.rgb(198, 234, 255);
        ssc.thumbPressedColor = Colors.rgb(170, 220, 255);
        ssc.thumbStrokeColor = Colors.rgb(32, 78, 120);
        ssc.thumbStrokeWeight = 2.0f;
        ssc.thumbStrokeWeightHover = 3.0f;
        ssc.thumbSize = 28.0f;
        ssc.textColor = Colors.gray(245);
        ssc.disabledAlpha = 90;
        ssc.showValueText = true;
        slider.setStyle(new SliderStyle(ssc));
        // input manager
        inputManager = new InputManager();
        inputManager.registerLayer(new SliderInputLayer(0, slider));
        // text output
        textAlign(CENTER, CENTER);
    }

    public void draw() {
        background(28);
        slider.draw();
        text(slider.getCode() + " | value = " + currentValue, 300, 240);
        text("drag the thumb or use the mouse wheel while hovering", 300, 275);
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

    public void mouseWheel(MouseEvent event) {
        inputManager.dispatchPointer(new PointerEvent(
                PointerEvent.Type.WHEEL,
                (float) mouseX,
                (float) mouseY,
                mouseButton,
                (float) event.getCount(),
                event.isShiftDown(),
                event.isControlDown()
        ));
    }
}
```

---

## See also

- [Slider (SVG)](slider-svg.md)
- [JSON Configuration](json-configuration.md)
- [Input system](input-system.md)
- [Architecture](architecture.md)
