# Toggle (SVG)

This tutorial shows the SVG variant of the public `Toggle` API using `SvgShapeRenderer`.

For the base control architecture, input pipeline, and recommended non-SVG setup, see [toggle.md](toggle.md).

---

## What changes when using SVG

The control architecture and input flow stay the same.

The only change is the renderer configured through the toggle style:

- the toggle is still created through the public `Toggle` facade
- input is still routed through `InputManager` and `ToggleInputLayer`
- visuals are rendered through `SvgShapeRenderer` instead of the default circle renderer path

So this document only focuses on the SVG-specific part of the setup. The public API remains identical to the base toggle example.

---

## Step-by-step setup

### 1. Create the toggle

```java
private InputManager inputManager;
private Toggle toggle;

public void setup() {
    float x = 300f;
    float y = 125f;
    float d = 100f;
    toggle = new Toggle(this, "tglSvgTest", 0, 3, x, y, d, d);
}
```

This is the same public setup used in the regular toggle example.

---

### 2. Assign the change action

```java
toggle.setChangeListener(value -> {
    // the code that executes after a toggle click goes here, for example:    
    System.out.println("Toggle state = " + value);
});
```

---

### 3. Configure the style and SVG renderer

```java
ToggleStyleConfig tsc = new ToggleStyleConfig();
tsc.setShapeRenderer(new SvgShapeRenderer(this, "data" + File.separator + "img" + File.separator + "test.svg"));
tsc.stateColors = new Integer[]{
    Colors.gray(70),
    Colors.rgb(232, 155, 44),
    Colors.rgb(32, 188, 176)
};
tsc.strokeColor = Colors.gray(255);
tsc.strokeWidth = 1.5f;
tsc.strokeWidthHover = 3.5f;
tsc.hoverBlendWithWhite = 0.14f;
tsc.pressedBlendWithBlack = 0.24f;
tsc.disabledAlpha = 70;

toggle.setStyle(new ParametricToggleStyle(tsc));
```

This is the key SVG-specific step.

The cross-platform asset path matches the example sketch:

`"data" + File.separator + "img" + File.separator + "test.svg"`

The SVG renderer can be configured in two equivalent ways:

- programmatically, as shown here with `ToggleStyleConfig` and `SvgShapeRenderer`
- through JSON using the `renderer` block described in [JSON Configuration](json-configuration.md)

Both approaches use the same internal styling and rendering infrastructure.

---

### 4. Register the reusable input layer

```java
inputManager = new InputManager();
inputManager.registerLayer(new ToggleInputLayer(0, toggle));
```

The sketch still owns the `InputManager`. The control does not take over global input responsibilities.

---

### 5. Draw the toggle

```java
public void draw() {
    background(28);
    toggle.draw();
}
```

The facade still delegates to the same internal rendering pipeline. Only the configured renderer changes.

---

## Full example

```java
import com.cpz.processing.controls.controls.toggle.Toggle;
import com.cpz.processing.controls.controls.toggle.config.ToggleStyleConfig;
import com.cpz.processing.controls.controls.toggle.input.ToggleInputLayer;
import com.cpz.processing.controls.controls.toggle.style.ParametricToggleStyle;
import com.cpz.processing.controls.controls.toggle.style.render.SvgShapeRenderer;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.utils.color.Colors;
import processing.core.PApplet;

import java.io.File;

public class ToggleSvgTest extends PApplet {

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
        toggle = new Toggle(this, "tglSvgTest", 0, 3, x, y, d, d);
        toggle.setChangeListener(value -> {
            // the code that executes after a toggle click goes here, for example:    
            System.out.println("Toggle state = " + value);
            currentState = value;
        });
        currentState = toggle.getState();
        // style
        ToggleStyleConfig tsc = new ToggleStyleConfig();
        tsc.setShapeRenderer(new SvgShapeRenderer(this, "data" + File.separator + "img" + File.separator + "test.svg"));
        tsc.stateColors = new Integer[]{
                Colors.gray(70),
                Colors.rgb(232, 155, 44),
                Colors.rgb(32, 188, 176)
        };
        tsc.strokeColor = Colors.gray(255);
        tsc.strokeWidth = 1.5f;
        tsc.strokeWidthHover = 3.5f;
        tsc.hoverBlendWithWhite = 0.14f;
        tsc.pressedBlendWithBlack = 0.24f;
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

## Notes

- `SvgShapeRenderer` handles SVG loading and drawing
- simple, lightweight SVG assets usually give the most predictable results
- Processing's SVG rendering behavior still applies because the renderer ultimately relies on Processing shape support
- this tutorial intentionally reuses the same input structure as the base `Toggle` example
- the same SVG setup is also available through JSON configuration, see [JSON Configuration](json-configuration.md)

---

## See also

- [Toggle](toggle.md)
- [JSON Configuration](json-configuration.md)
- [Input system](input-system.md)
- [Architecture](architecture.md)
