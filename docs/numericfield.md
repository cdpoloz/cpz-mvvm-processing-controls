# NumericField

`NumericField` is the public facade recommended for the simple single-line numeric input case.

Internally, the control still follows the framework's MVVM architecture. The facade keeps the public API ergonomic and closed while reusing the existing internal control pipeline.

---

## Overview

`NumericField` is a `TextField`-like control specialized for decimal numeric input:

- it exposes a small public API for text, parsed value, validity, visibility, enabled state, layout, style, and listeners
- it exposes the stable control identity through `getCode()`
- it keeps pointer and keyboard routing in the sketch through `InputManager` and `NumericFieldInputLayer`
- it acquires focus by click and only edits while focused

In the current iteration:

- allowed characters are digits, an optional leading `-`, and a single optional `.`
- intermediate states such as `""`, `"-"`, `"."`, and `"-."` are allowed during editing
- those intermediate states are not considered valid numeric values
- `getValue()` returns the parsed value derived from the current text buffer, or `null` while the current text is not a valid number
- `enter` is a no-op for the public single-line control
- SVG is not supported for `NumericField` in this iteration

---

## Responsibilities

The control keeps the standard MVVM separation used across the framework:

- `Model` stores durable numeric state and stable `code`
- `Model` may keep extra numeric constraints used internally by the closed implementation, but the public facade does not expose them in this iteration
- `ViewModel` owns numeric text editing, cursor movement, parsing, and validity
- `View` owns layout, hit testing, and `ViewState` construction
- `Style` resolves visual appearance from immutable state and theme data
- `Renderer` performs drawing only

Rendering pipeline:

```text
ViewModel → ViewState → Style → RenderStyle → Renderer
```

This separation still exists even when the sketch uses the facade.

---

## Public facade

`NumericField` is a convenience facade for the common case:

- it composes the default internal MVVM pieces and input adapters
- it exposes a small public API for text, parsed value, validity, state, layout, style, and drawing
- it exposes the stable control identity through `getCode()`
- it does not expose `getView()` or `getViewModel()`

This is the recommended entry point for normal sketch usage.

---

## Focus and keyboard

`NumericField` only receives keyboard input while it has focus.

Focus behavior in the public single-field flow:

- click inside the field to give it focus
- click outside the field to clear focus
- keyboard input is only routed while the field is focused

Supported keyboard behavior in the current iteration:

- typing inserts valid numeric characters at the cursor
- invalid characters are ignored
- `backspace` deletes the character before the cursor
- `delete` deletes the character after the cursor
- `left` and `right` move the cursor
- `home` moves the cursor to the start of the text
- `end` moves the cursor to the end of the text
- `enter` is currently a no-op

The sketch forwards keyboard input as delivered by the host application. Special host-key handling such as `ESC` remains an application decision and is not imposed by `NumericField`; the control does not reserve `ESC` or attach any built-in revert behavior.

---

## Basic usage

```java
private NumericField numericField;

public void setup() {
    float x = 380f;
    float y = 110f;
    float w = 420f;
    float h = 48f;
    numericField = new NumericField(
            this,
            "numTest",
            "12.5",
            x,
            y,
            w,
            h
    );
}
```

The constructor already applies the initial text, position, and size.

You can observe both text changes and valid numeric changes:

```java
numericField.setChangeListener(value -> {
    System.out.println("NumericField text = " + value);
});

numericField.setValueChangeListener(value -> {
    System.out.println("NumericField value = " + value);
});
```

---

## Validity and value

`NumericField` keeps text editing and parsed numeric value separate:

```java
String text = numericField.getText();
BigDecimal value = numericField.getValue();
boolean valid = numericField.isValid();
```

- `getText()` returns the current visible buffer
- `isValid()` reports whether the current visible buffer can be parsed right now as a numeric value
- `getValue()` returns the parsed `BigDecimal` derived from the current visible buffer only when that buffer is valid
- while editing, `getValue()` may temporarily differ from the last committed internal model value until focus is lost or another commit path normalizes the text

This keeps intermediate editing states usable without exposing MVVM internals.

---

## Styling

`NumericField` uses the existing numeric field styling pipeline:

```java
NumericFieldStyleConfig nfsc = new NumericFieldStyleConfig();
nfsc.backgroundColor = Colors.rgb(236, 242, 248);
nfsc.borderColor = Colors.rgb(72, 116, 156);
nfsc.textColor = Colors.rgb(28, 44, 62);
nfsc.cursorColor = Colors.rgb(38, 132, 212);
nfsc.selectionColor = Colors.rgb(182, 217, 248);
nfsc.selectionTextColor = Colors.rgb(28, 44, 62);
nfsc.textSize = 16.0f;

numericField.setStyle(new NumericFieldStyle(nfsc));
```

The facade uses the existing style mechanism. It does not introduce a new styling path.

---

## Input flow

The framework does not own the input source. It only consumes normalized input events provided by the application.

For the simple single-numeric-field case:

```java
inputManager = new InputManager();
inputManager.registerLayer(new NumericFieldInputLayer(0, numericField));

keyboardState = new KeyboardState();
processingKeyboardAdapter = new ProcessingKeyboardAdapter(keyboardState, inputManager);
```

Pointer callbacks are forwarded as usual:

```java
public void mousePressed() {
    inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.PRESS, (float) mouseX, (float) mouseY, mouseButton));
}

public void mouseDragged() {
    inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.DRAG, (float) mouseX, (float) mouseY, mouseButton));
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

`NumericFieldInputLayer` only routes keyboard events while the field is focused, enabled, and visible. Pointer events are only consumed when the field keeps focus ownership after handling them, and wheel input is ignored in this simple public version.

---

## Full example

This example matches `NumericFieldTest.java`:

```java
private InputManager inputManager;
private KeyboardState keyboardState;
private ProcessingKeyboardAdapter processingKeyboardAdapter;
private NumericField numericField;
private String currentText;
private BigDecimal currentValue;
private boolean currentValid;

public void setup() {
    float x = 380f;
    float y = 110f;
    float w = 420f;
    float h = 48f;
    numericField = new NumericField(this, "numTest", "12.5", x, y, w, h);
    numericField.setChangeListener(value -> {
        System.out.println("NumericField text = " + value);
        currentText = numericField.getText();
        currentValue = numericField.getValue();
        currentValid = numericField.isValid();
    });
    numericField.setValueChangeListener(value -> System.out.println("NumericField value = " + value));

    NumericFieldStyleConfig nfsc = new NumericFieldStyleConfig();
    nfsc.backgroundColor = Colors.rgb(236, 242, 248);
    nfsc.borderColor = Colors.rgb(72, 116, 156);
    nfsc.textColor = Colors.rgb(28, 44, 62);
    nfsc.cursorColor = Colors.rgb(38, 132, 212);
    nfsc.selectionColor = Colors.rgb(182, 217, 248);
    nfsc.selectionTextColor = Colors.rgb(28, 44, 62);
    nfsc.textSize = 16.0f;
    numericField.setStyle(new NumericFieldStyle(nfsc));

    inputManager = new InputManager();
    inputManager.registerLayer(new NumericFieldInputLayer(0, numericField));
    keyboardState = new KeyboardState();
    processingKeyboardAdapter = new ProcessingKeyboardAdapter(keyboardState, inputManager);
    textAlign(CENTER, CENTER);
}
```

---

## JSON usage

`NumericField` also supports the same minimal config-driven path used by the other closed controls:

```text
JSON → NumericFieldConfigLoader → NumericFieldConfig → NumericFieldFactory → NumericField facade → MVVM internals
```

Minimal JSON:

```json
{
  "code": "numJsonTest",
  "text": "12.5",
  "x": 380.0,
  "y": 110.0,
  "width": 420.0,
  "height": 48.0,
  "enabled": true,
  "visible": true
}
```

Minimal Java sketch flow:

```java
private static final String NUMERIC_FIELD_CONFIG_PATH = "data" + File.separator + "config" + File.separator + "numericfield-test.json";

private NumericField numericField;

public void setup() {
    NumericFieldConfigLoader loader = new NumericFieldConfigLoader(this);
    NumericFieldConfig config = loader.load(NUMERIC_FIELD_CONFIG_PATH);
    numericField = NumericFieldFactory.create(this, config);
}
```

The loader validates the small supported surface directly:

- `code` is required
- `text` defaults to `""` when omitted
- `text` must follow the same simple text rules as the control buffer: digits, an optional leading `-`, and at most one `.`
- `width` and `height` must be greater than `0`

---

## Limitations

`NumericField` intentionally stays small in the current iteration:

- single-line editing only
- no public selection API on the facade
- no public copy/paste API on the facade
- no min/max, clamp, or step controls in the public facade
- no wheel-based value adjustment in the public input layer
- no SVG renderer path
- `enter` is currently a no-op

This keeps the public control aligned with the small closed-control pattern already used by the framework.
