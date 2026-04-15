# JSON Configuration

This document shows how to create controls from JSON configuration files.

In the current iteration, the framework supports a single `Button` created from JSON, including an optional SVG renderer configured through the style block.

JSON configuration is an additional layer on top of the existing public API. It does not replace direct control creation with `Button`, and it does not change the internal MVVM architecture of the framework.

---

## Overview


JSON configuration introduces an optional config-driven layer that solves a specific problem:

- it moves geometry and visual setup out of Java code
- it makes simple control setup easier to externalize
- it keeps behavior and input wiring in the sketch

The integration model is intentionally small:

- JSON describes configuration
- the loader parses JSON into a config DTO
- the factory creates the public `Button`
- the sketch still owns input and click behavior

Internally, the framework still uses the same MVVM-based button implementation. JSON is only a configuration layer in front of that flow.

---

## Architecture

The config-driven flow is:

```text
JSON → ButtonConfigLoader → ButtonConfig → ButtonFactory → Button facade → MVVM internals
```

Responsibilities:

- `ButtonConfig` stores the parsed button data
- `ButtonConfigLoader` reads the JSON file and validates the supported fields
- `ButtonFactory` creates a `Button` and applies state and style
- `Button` remains the public facade used by the sketch

This means the external setup becomes config-driven, but the runtime control pipeline remains the same.

---

## Minimal example (Button)

Minimal JSON:

```json
{
  "text": "JSON Button",
  "x": 300.0,
  "y": 125.0,
  "width": 220.0,
  "height": 60.0,
  "enabled": true,
  "visible": true
}
```

Minimal Java sketch flow:

```java
private static final String BUTTON_CONFIG_PATH = "data" + File.separator + "config" + File.separator + "button-test.json";

private InputManager inputManager;
private Button button;

public void setup() {
    ButtonConfigLoader loader = new ButtonConfigLoader(this);
    ButtonConfig config = loader.load(BUTTON_CONFIG_PATH);
    button = ButtonFactory.create(this, config);

    button.setClickListener(() -> {
        System.out.println("You clicked the JSON button!");
    });

    inputManager = new InputManager();
    inputManager.registerLayer(new ButtonInputLayer(0, button));
}
```

The important part is unchanged from the regular button flow:

- the sketch still assigns the click listener in Java
- the sketch still creates the `InputManager`
- the sketch still registers `ButtonInputLayer`

The configuration only defines structure and appearance. Behavior remains defined in Java.

---

## Style configuration

The optional `style` block maps JSON values to `ButtonStyleConfig`.

Example:

```json
"style": {
  "baseColor": "#3062DB",
  "textColor": "#FFFFFF",
  "strokeColor": "#FFFFFF",
  "strokeWeight": 2.0,
  "strokeWeightHover": 4.0,
  "cornerRadius": 18.0,
  "disabledAlpha": 90,
  "hoverBlendWithWhite": 0.12,
  "pressedBlendWithBlack": 0.25
}
```

Supported style fields in the current iteration:

- `baseColor`
- `textColor`
- `strokeColor`
- `strokeWeight`
- `strokeWeightHover`
- `cornerRadius`
- `disabledAlpha`
- `hoverBlendWithWhite`
- `pressedBlendWithBlack`

This block only controls appearance. It does not define listeners, input routing, or business behavior.

---

## SVG renderer

SVG support is configured through an optional `renderer` block inside `style`. The renderer configuration extends the style block without changing the control structure.

Example:

```json
"renderer": {
  "type": "svg",
  "path": "data/img/test.svg"
}
```

Notes:

- the block is optional
- only `svg` is supported in this iteration
- the factory creates `SvgButtonRenderer` internally when this block is present

This keeps the external JSON declarative while reusing the same rendering mechanism already used by the direct Java API.

---

## Full examples

### 1. Normal button from JSON

JSON:

```json
{
  "text": "JSON Button",
  "x": 300.0,
  "y": 125.0,
  "width": 220.0,
  "height": 60.0,
  "enabled": true,
  "visible": true,
  "style": {
    "baseColor": "#3062DB",
    "textColor": "#FFFFFF",
    "strokeColor": "#FFFFFF",
    "strokeWeight": 2.0,
    "strokeWeightHover": 4.0,
    "cornerRadius": 18.0,
    "disabledAlpha": 90,
    "hoverBlendWithWhite": 0.12,
    "pressedBlendWithBlack": 0.25
  }
}
```

Java:

```java
import com.cpz.processing.controls.controls.button.Button;
import com.cpz.processing.controls.controls.button.ButtonFactory;
import com.cpz.processing.controls.controls.button.config.ButtonConfig;
import com.cpz.processing.controls.controls.button.config.ButtonConfigLoader;
import com.cpz.processing.controls.controls.button.input.ButtonInputLayer;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.PointerEvent;
import processing.core.PApplet;

import java.io.File;

public class ButtonJsonTest extends PApplet {
    private static final String BUTTON_CONFIG_PATH = "data" + File.separator + "config" + File.separator + "button-test.json";

    private InputManager inputManager;
    private Button button;
    private int clickCount;

    public void settings() {
        size(600, 300);
        smooth(8);
    }

    public void setup() {
        ButtonConfigLoader loader = new ButtonConfigLoader(this);
        ButtonConfig config = loader.load(BUTTON_CONFIG_PATH);
        button = ButtonFactory.create(this, config);
        button.setClickListener(() -> {
            System.out.println("You clicked the JSON button!");
            clickCount++;
        });
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

### 2. SVG button from JSON

JSON:

```json
{
  "text": "SVG Button",
  "x": 300.0,
  "y": 125.0,
  "width": 150.0,
  "height": 130.0,
  "enabled": true,
  "visible": true,
  "style": {
    "baseColor": "#3062DB",
    "textColor": "#FFFFFF",
    "strokeColor": "#FFFFFF",
    "strokeWeight": 2.0,
    "strokeWeightHover": 4.0,
    "cornerRadius": 18.0,
    "disabledAlpha": 90,
    "hoverBlendWithWhite": 0.12,
    "pressedBlendWithBlack": 0.25,
    "renderer": {
      "type": "svg",
      "path": "data/img/test.svg"
    }
  }
}
```

Java:

```java
import com.cpz.processing.controls.controls.button.Button;
import com.cpz.processing.controls.controls.button.ButtonFactory;
import com.cpz.processing.controls.controls.button.config.ButtonConfig;
import com.cpz.processing.controls.controls.button.config.ButtonConfigLoader;
import com.cpz.processing.controls.controls.button.input.ButtonInputLayer;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.PointerEvent;
import processing.core.PApplet;

import java.io.File;

public class ButtonSvgJsonTest extends PApplet {
    private static final String BUTTON_CONFIG_PATH = "data" + File.separator + "config" + File.separator + "button-svg-test.json";

    private InputManager inputManager;
    private Button button;
    private int clickCount;

    public void settings() {
        size(600, 300);
        smooth(8);
    }

    public void setup() {
        ButtonConfigLoader loader = new ButtonConfigLoader(this);
        ButtonConfig config = loader.load(BUTTON_CONFIG_PATH);
        button = ButtonFactory.create(this, config);
        button.setClickListener(() -> {
            System.out.println("You clicked the SVG JSON button!");
            clickCount++;
        });
        // input manager
        inputManager = new InputManager();
        inputManager.registerLayer(new ButtonInputLayer(0, button));
        // text output
        textAlign(CENTER, CENTER);
    }

    public void draw() {
        background(28);
        button.draw();
        text("Current click count = " + clickCount, 300, 225);
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

## Validation rules

The current loader applies a small set of semantic validations:

- `width` must be greater than `0`
- `height` must be greater than `0`
- `renderer.type` must be `"svg"` when a renderer block is provided
- `renderer.path` must be non-empty when `renderer.type` is `"svg"`

Supported color formats:

- integer values
- `#RRGGBB`
- `#AARRGGBB`

If a required value is missing or invalid, the loader throws an `IllegalArgumentException` with a message that points to the problematic key and JSON file.

---

## Limitations

The current config-driven layer is intentionally minimal:

- only one control per JSON file
- only `Button` is supported
- no multiple controls
- no listeners declared in JSON
- no binding
- no complex layouts
- no generic cross-control factory system

This keeps the feature focused on validating the configuration flow without changing the main architecture of the framework.

---

## See also

- [Button](button.md)
- [Button (SVG)](button-svg.md)
- [Architecture](architecture.md)
- [Input system](input-system.md)
