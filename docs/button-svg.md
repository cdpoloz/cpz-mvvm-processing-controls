# Button (SVG)

This tutorial shows the SVG variant of the public `Button` API using `SvgButtonRenderer`.

For the base control architecture, input pipeline, and recommended non-SVG setup, see [button.md](button.md).

---

## What changes when using SVG

The control architecture and input flow stay the same.

The only change is the renderer configured through the button style:

- the button is still created through the public `Button` facade
- input is still routed through `InputManager` and `ButtonInputLayer`
- visuals are rendered through `SvgButtonRenderer` instead of the default primitive-only renderer path

So this document only focuses on the SVG-specific part of the setup. The public API remains identical to the base button example.

---

## Step-by-step setup

### 1. Create the button

```java
private InputManager inputManager;
private Button button;

public void setup() {
    float x = 300f;
    float y = 125f;
    float w = 150f;
    float h = 130f;
    button = new Button(this, "btnSvgPrimary", "SVG Button", x, y, w, h);
}
```

This is the same public setup used in the regular button example.

---

### 2. Assign the click action

```java
button.setClickListener(() -> {
    // the code that executes after a button click goes here, for example:
    System.out.println("You clicked the SVG button!");
});
```

---

### 3. Configure the style and SVG renderer

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
bsc.setRenderer(new SvgButtonRenderer(this, "data" + File.separator + "img" + File.separator + "test.svg"));

button.setStyle(new DefaultButtonStyle(bsc));
```

This is the key SVG-specific step.

The cross-platform asset path matches the example sketch:

`"data" + File.separator + "img" + File.separator + "test.svg"`

The SVG renderer can be configured in two equivalent ways:

- programmatically, as shown here with `ButtonStyleConfig` and `SvgButtonRenderer`
- through JSON using the `renderer` block described in [JSON Configuration](json-configuration.md)

Both approaches use the same internal styling and rendering infrastructure.

---

### 4. Register the reusable input layer

```java
inputManager = new InputManager();
inputManager.registerLayer(new ButtonInputLayer(0, button));
```

The sketch still owns the `InputManager`. The control does not take over global input responsibilities.

---

### 5. Draw the button

```java
public void draw() {
    background(28);
    button.draw();
}
```

The facade still delegates to the same internal rendering pipeline. Only the configured renderer changes.

---

## Full example

```java
import com.cpz.processing.controls.controls.button.Button;
import com.cpz.processing.controls.controls.button.config.ButtonStyleConfig;
import com.cpz.processing.controls.controls.button.input.ButtonInputLayer;
import com.cpz.processing.controls.controls.button.style.DefaultButtonStyle;
import com.cpz.processing.controls.controls.button.style.render.SvgButtonRenderer;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.processing.controls.core.util.Colors;
import processing.core.PApplet;

import java.io.File;

public class ButtonSvgTest extends PApplet {

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
        float w = 150f;
        float h = 130f;
        button = new Button(this, "btnSvgTest", "SVG Button", x, y, w, h);
        button.setClickListener(() -> {
            // the code that executes after a button click goes here, for example:
            System.out.println("You clicked the SVG button!");
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
        bsc.setRenderer(new SvgButtonRenderer(this, "data" + File.separator + "img" + File.separator + "test.svg"));
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
        text(button.getCode() + " | Current click count = " + clickCount, 300, 225);
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

- `SvgButtonRenderer` handles SVG loading and drawing
- simple, lightweight SVG assets usually give the most predictable results
- Processing's SVG rendering behavior still applies because the renderer ultimately relies on Processing shape support
- this tutorial intentionally reuses the same input structure as the base `Button` example

---

## See also

- [Button](button.md)
- [Input system](input-system.md)
- [Architecture](architecture.md)
