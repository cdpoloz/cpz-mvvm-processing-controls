# RadioGroup

`RadioGroup` is the public facade recommended for the simple single-selection radio list case.

Internally, the control still follows the framework's MVVM architecture. The facade keeps the public API ergonomic and closed while reusing the existing internal control pipeline of the framework.

---

## Overview

`RadioGroup` is intended for single-selection option lists:

- it exposes a small public API for options, selected index, selected option, visibility, enabled state, layout, and style
- it exposes the stable control identity through `getCode()`
- it keeps input routing in the sketch through `InputManager` and `RadioGroupInputLayer`
- it supports both pointer selection and keyboard navigation in the simple single-group case

Selection semantics in the current iteration:

- only one option can be selected at a time
- `selectedIndex` may be `-1` when nothing is selected
- keyboard navigation moves the active option after the group has focus

---

## Public facade

`RadioGroup` is a convenience facade for the common case:

- it composes the default internal MVVM pieces required for a single radio group
- it exposes a small public API for options, selection, state, style, and layout
- it does not expose `getView()` or `getViewModel()`

This is the recommended entry point for normal sketch usage.

---

## Minimal example

A minimal public setup requires:

1. `RadioGroup`
2. `InputManager`
3. `RadioGroupInputLayer`
4. Forwarded `PointerEvent` and `KeyboardEvent` callbacks from Processing

High-level interaction flow:

```text
Processing → PointerEvent / KeyboardEvent → InputManager → RadioGroupInputLayer → RadioGroup facade
```

---

## Basic usage

```java
private RadioGroup radioGroup;

public void setup() {
    float x = 375f;
    float y = 70f;
    float w = 200f;
    radioGroup = new RadioGroup(
            this,
            "rgTest",
            List.of("Mercury", "Venus", "Earth", "Mars", "Jupiter"),
            2,
            x,
            y,
            w
    );
}
```

The constructor already applies the initial options, selection, position, and width used by the group.

---

## Selection

The facade exposes the current selection ergonomically:

```java
int selectedIndex = radioGroup.getSelectedIndex();
String selectedOption = radioGroup.getSelectedOption();

radioGroup.setSelectedIndex(3);
```

You can also observe selection changes:

```java
radioGroup.setChangeListener(index -> {
    // the code that executes after the radio group selection changes goes here, for example:
    System.out.println("Selected index = " + index);
});
```

The listener receives the new selected index through `ValueListener<Integer>`.

`getSelectedOption()` returns the selected option text, or `null` when nothing is selected.

---

## Options and layout

The facade exposes the option list and the main layout controls needed by the public example flow:

```java
radioGroup.setOptions(List.of("Small", "Medium", "Large"));
radioGroup.setPosition(350f, 140f);
radioGroup.setWidth(320f);
radioGroup.setItemHeight(34f);
radioGroup.setItemSpacing(10f);
```

`RadioGroup` derives its full height from the number of options, the configured item height, and the configured item spacing.

---

## Visibility and enabled state

The public facade exposes the standard control state methods:

```java
radioGroup.setVisible(true);
radioGroup.setEnabled(true);
```

- `visible` controls whether the group is drawn
- `enabled` controls whether the group can be interacted with

When the group is disabled, selection can still be set programmatically through the public API, but user interaction is suppressed.

---

## Styling

`RadioGroup` uses the existing radio group styling pipeline:

```java
RadioGroupStyleConfig rgsc = new RadioGroupStyleConfig();
rgsc.textOverride = Colors.gray(245);
rgsc.indicatorOverride = Colors.gray(255);
rgsc.hoveredBackgroundOverride = Colors.rgb(44, 56, 74);
rgsc.pressedBackgroundOverride = Colors.rgb(32, 42, 56);
rgsc.selectedDotOverride = Colors.rgb(56, 159, 232);
rgsc.itemHeight = 34.0f;
rgsc.itemSpacing = 10.0f;
rgsc.indicatorOuterDiameter = 18.0f;
rgsc.indicatorInnerDiameter = 8.0f;
rgsc.strokeWeight = 1.8f;
rgsc.textSize = 17.0f;
rgsc.cornerRadius = 8.0f;
rgsc.disabledAlpha = 85;

radioGroup.setStyle(new RadioGroupStyle(rgsc));
```

This configuration controls the visual appearance of the text, indicator, spacing, and per-item background states.

The facade uses the existing style mechanism. It does not introduce a new styling path.

---

## Input flow

The framework does not own the input source. It only consumes normalized input events provided by the application.

For the simple single-group case:

```java
inputManager = new InputManager();
inputManager.registerLayer(new RadioGroupInputLayer(0, radioGroup));

keyboardState = new KeyboardState();
processingKeyboardAdapter = new ProcessingKeyboardAdapter(keyboardState, inputManager);
```

Pointer callbacks are forwarded as usual:

```java
public void mouseMoved() {
    inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.MOVE, (float) mouseX, (float) mouseY, mouseButton));
}

public void mousePressed() {
    inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.PRESS, (float) mouseX, (float) mouseY, mouseButton));
}

public void mouseReleased() {
    inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.RELEASE, (float) mouseX, (float) mouseY, mouseButton));
}
```

Keyboard callbacks are normalized through `ProcessingKeyboardAdapter`:

```java
public void keyPressed() {
    processingKeyboardAdapter.keyPressed(key, keyCode);
}

public void keyReleased() {
    processingKeyboardAdapter.keyReleased(key, keyCode);
}

public void keyTyped() {
    processingKeyboardAdapter.keyTyped(key, keyCode);
}
```

The public single-group flow supports:

- mouse hover and click selection
- focus acquisition by clicking an option
- after the group has focus, `↑` moves the active option up and `↓` moves it down
- `enter` or `space` commits the active option
- `←` and `→` also navigate because they use the same keyboard target flow, but `↑` and `↓` are the clearest keys for the vertical list

---

## JSON configuration

`RadioGroup` also supports the same minimal config-driven path used by the other closed controls:

```text
JSON -> controls[] -> RadioGroupConfigLoader -> RadioGroupConfig -> RadioGroupFactory -> RadioGroup facade -> MVVM internals
```

Minimal JSON:

```json
{
  "code": "rgJsonTest",
  "options": ["Mercury", "Venus", "Earth", "Mars", "Jupiter"],
  "selectedIndex": 2,
  "x": 375.0,
  "y": 70.0,
  "width": 200.0,
  "enabled": true,
  "visible": true
}
```

Minimal Java sketch flow:

```java
private static final String RADIO_GROUP_CONFIG_PATH = "data" + File.separator + "config" + File.separator + "radiogroup-test.json";

private RadioGroup radioGroup;

public void setup() {
    RadioGroupConfigLoader loader = new RadioGroupConfigLoader(this);
    RadioGroupConfig config = loader.load(RADIO_GROUP_CONFIG_PATH);
    radioGroup = RadioGroupFactory.create(this, config);
}
```

The current JSON validation is intentionally small and explicit:

- `code` is required
- `options` is required and must contain at least one string
- `selectedIndex` must be `-1` or a valid option index
- `width` must be greater than `0`

For simple single-control examples, `RadioGroupConfigLoader` accepts the wrapped `controls` document when it contains exactly one `radiogroup` entry. Binding and listeners still remain outside JSON.

---

## SVG support

`RadioGroup` does not provide SVG renderer support in the current iteration.

The control currently uses the existing radio-group renderer path only. There is no `radiogroup-svg.md` document because no separate SVG configuration path exists for this control at this stage.

---

## Full example

```java
import com.cpz.processing.controls.controls.radiogroup.RadioGroup;
import com.cpz.processing.controls.controls.radiogroup.config.RadioGroupStyleConfig;
import com.cpz.processing.controls.controls.radiogroup.input.RadioGroupInputLayer;
import com.cpz.processing.controls.controls.radiogroup.style.RadioGroupStyle;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.processing.controls.core.util.Colors;
import com.cpz.processing.controls.input.KeyboardState;
import com.cpz.processing.controls.input.ProcessingKeyboardAdapter;
import processing.core.PApplet;

import java.util.List;

public class RadioGroupTest extends PApplet {
    private InputManager inputManager;
    private KeyboardState keyboardState;
    private ProcessingKeyboardAdapter processingKeyboardAdapter;
    private RadioGroup radioGroup;
    private String currentSelection;

    public void settings() {
        size(700, 360);
        smooth(8);
    }

    public void setup() {
        radioGroup = new RadioGroup(
                this,
                "rgTest",
                List.of("Mercury", "Venus", "Earth", "Mars", "Jupiter"),
                2,
                350f,
                140f,
                320f
        );
        radioGroup.setChangeListener(index -> currentSelection = radioGroup.getSelectedOption());
        currentSelection = radioGroup.getSelectedOption();
        // style
        RadioGroupStyleConfig rgsc = new RadioGroupStyleConfig();
        rgsc.textOverride = Colors.gray(245);
        rgsc.indicatorOverride = Colors.gray(255);
        rgsc.hoveredBackgroundOverride = Colors.rgb(44, 56, 74);
        rgsc.pressedBackgroundOverride = Colors.rgb(32, 42, 56);
        rgsc.selectedDotOverride = Colors.rgb(56, 159, 232);
        rgsc.itemHeight = 34.0f;
        rgsc.itemSpacing = 10.0f;
        rgsc.indicatorOuterDiameter = 18.0f;
        rgsc.indicatorInnerDiameter = 8.0f;
        rgsc.strokeWeight = 1.8f;
        rgsc.textSize = 17.0f;
        rgsc.cornerRadius = 8.0f;
        rgsc.disabledAlpha = 85;
        radioGroup.setStyle(new RadioGroupStyle(rgsc));
        // input manager
        inputManager = new InputManager();
        inputManager.registerLayer(new RadioGroupInputLayer(0, radioGroup));
        keyboardState = new KeyboardState();
        processingKeyboardAdapter = new ProcessingKeyboardAdapter(keyboardState, inputManager);
        // text output
        textAlign(CENTER, CENTER);
    }

    public void draw() {
        background(28);
        radioGroup.draw();
        fill(180);
        text(radioGroup.getCode() + " | selected = " + currentSelection, 350, 300);
        text("click an option to focus | ↑ up | ↓ down | enter/space select", 350, 330);
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

    public void keyPressed() {
        processingKeyboardAdapter.keyPressed(key, keyCode);
    }

    public void keyReleased() {
        processingKeyboardAdapter.keyReleased(key, keyCode);
    }

    public void keyTyped() {
        processingKeyboardAdapter.keyTyped(key, keyCode);
    }
}
```

---

## Notes / limitations

- `RadioGroup` does not expose `getView()` or `getViewModel()`
- the public examples use only the facade and the single-group `RadioGroupInputLayer`
- the public API is intentionally small and aligned with the other closed control facades
- SVG is not part of the radio-group public surface in this iteration

---

## See also

- [JSON Configuration](json-configuration.md)
- [Input system](input-system.md)
- [Architecture](architecture.md)

