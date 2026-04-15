# Checkbox (SVG)

This tutorial shows the SVG variant of the public `Checkbox` API using `SvgCheckboxRenderer`.

For the base control architecture, input pipeline, and recommended non-SVG setup, see [checkbox.md](checkbox.md).

---

## What changes when using SVG

The control architecture and input flow stay the same.

The only change is the renderer configured through the checkbox style:

- the checkbox is still created through the public `Checkbox` facade
- input is still routed through `InputManager` and `CheckboxInputLayer`
- visuals are rendered through `SvgCheckboxRenderer` instead of the default primitive-only renderer path

So this document only focuses on the SVG-specific part of the setup. The public API remains identical to the base checkbox example.

---

## Step-by-step setup

### 1. Create the checkbox

```java
private InputManager inputManager;
private Checkbox checkbox;

public void setup() {
    float x = 300f;
    float y = 125f;
    float w = 88f;
    float h = 72f;
    checkbox = new Checkbox(this, "chkSvgPrimary", true, x, y, w, h);
}
```

This is the same public setup used in the regular checkbox example.

---

### 2. Assign the change action

```java
checkbox.setChangeListener(value -> {
    // the code that executes after a checkbox click goes here, for example:
    System.out.println("Checkbox checked = " + value);
});
```

---

### 3. Configure the style and SVG renderer

```java
CheckboxStyleConfig csc = new CheckboxStyleConfig();
csc.boxColor = Colors.rgb(48, 98, 219);
csc.boxHoverColor = Colors.rgb(74, 122, 234);
csc.boxPressedColor = Colors.rgb(34, 77, 184);
csc.checkColor = Colors.gray(255);
csc.borderColor = Colors.gray(255);
csc.borderWidth = 2.0f;
csc.borderWidthHover = 4.0f;
csc.cornerRadius = 0.0f;
csc.disabledAlpha = 90;
csc.checkInset = 0.22f;
csc.setRenderer(new SvgCheckboxRenderer(this, "data" + File.separator + "img" + File.separator + "test.svg"));

checkbox.setStyle(new DefaultCheckboxStyle(csc));
```

This is the key SVG-specific step.

The cross-platform asset path matches the example sketch:

`"data" + File.separator + "img" + File.separator + "test.svg"`

The SVG renderer can be configured in two equivalent ways:

- programmatically, as shown here with `CheckboxStyleConfig` and `SvgCheckboxRenderer`
- through JSON using the `renderer` block described in [JSON Configuration](json-configuration.md)

Both approaches use the same internal styling and rendering infrastructure.

---

### 4. Register the reusable input layer

```java
inputManager = new InputManager();
inputManager.registerLayer(new CheckboxInputLayer(0, checkbox));
```

The sketch still owns the `InputManager`. The control does not take over global input responsibilities.

---

### 5. Draw the checkbox

```java
public void draw() {
    background(28);
    checkbox.draw();
}
```

The facade still delegates to the same internal rendering pipeline. Only the configured renderer changes.

---

## Full example

```java
import com.cpz.processing.controls.controls.checkbox.Checkbox;
import com.cpz.processing.controls.controls.checkbox.CheckboxFactory;
import com.cpz.processing.controls.controls.checkbox.config.CheckboxConfig;
import com.cpz.processing.controls.controls.checkbox.config.CheckboxConfigLoader;
import com.cpz.processing.controls.controls.checkbox.input.CheckboxInputLayer;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.PointerEvent;
import processing.core.PApplet;

import java.io.File;

public class CheckboxSvgJsonTest extends PApplet {
    private static final String CHECKBOX_CONFIG_PATH = "data" + File.separator + "config" + File.separator + "checkbox-svg-test.json";

    private InputManager inputManager;
    private Checkbox checkbox;
    private boolean currentValue;

    public void settings() {
        size(600, 300);
        smooth(8);
    }

    public void setup() {
        CheckboxConfigLoader loader = new CheckboxConfigLoader(this);
        CheckboxConfig config = loader.load(CHECKBOX_CONFIG_PATH);
        checkbox = CheckboxFactory.create(this, config);
        checkbox.setChangeListener(value -> currentValue = value);
        currentValue = checkbox.isChecked();
        // input manager
        inputManager = new InputManager();
        inputManager.registerLayer(new CheckboxInputLayer(0, checkbox));
        // text output
        textAlign(CENTER, CENTER);
    }

    public void draw() {
        background(28);
        checkbox.draw();
        text(checkbox.getCode() + " | Current checked state = " + currentValue, 300, 225);
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

- `SvgCheckboxRenderer` handles SVG loading and drawing
- simple, lightweight SVG assets usually give the most predictable results
- Processing's SVG rendering behavior still applies because the renderer ultimately relies on Processing shape support
- this tutorial intentionally reuses the same input structure as the base `Checkbox` example
- the same SVG setup is also available through JSON configuration, see [JSON Configuration](json-configuration.md)

---

## See also

- [Checkbox](checkbox.md)
- [JSON Configuration](json-configuration.md)
- [Input system](input-system.md)
- [Architecture](architecture.md)
