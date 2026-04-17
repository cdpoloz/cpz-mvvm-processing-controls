# TextField

`TextField` is the public facade recommended for the simple single-line text input case.

Internally, the control still follows the framework's MVVM architecture. The facade keeps the public API ergonomic and closed while reusing the existing internal control pipeline.

---

## Overview

`TextField` is intended for single-line editable text:

- it exposes a small public API for text, visibility, enabled state, layout, style, and text change observation
- it exposes the stable control identity through `getCode()`
- it keeps pointer and keyboard routing in the sketch through `InputManager` and `TextFieldInputLayer`
- it acquires focus by click and only edits while focused

In the current iteration:

- text is edited in place at the current cursor position
- selection, copy, cut, and paste remain internal capabilities used by the keyboard target flow, but they are not part of the common facade API
- `enter` is a no-op for the public single-line control
- SVG is not supported for `TextField` in this iteration

---

## Responsibilities

The control keeps the standard MVVM separation used across the framework:

- `Model` stores durable text state and stable `code`
- `ViewModel` owns editing behavior such as cursor movement, backspace, delete, and focus-aware typing
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

`TextField` is a convenience facade for the common case:

- it composes the default internal MVVM pieces and input adapters
- it exposes a small public API for text, state, layout, style, and drawing
- it exposes the stable control identity through `getCode()`
- it does not expose `getView()` or `getViewModel()`

This is the recommended entry point for normal sketch usage.

---

## Focus and keyboard

`TextField` only receives keyboard input while it has focus.

Focus behavior in the public single-field flow:

- click inside the field to give it focus
- click outside the field to clear focus
- keyboard input is only routed while the field is focused

Supported keyboard behavior in the current iteration:

- typing inserts characters at the cursor
- `backspace` deletes the character before the cursor
- `delete` deletes the character after the cursor
- `left` and `right` move the cursor
- `home` moves the cursor to the start of the text
- `end` moves the cursor to the end of the text
- `enter` is currently a no-op

---

## Basic usage

```java
private TextField textField;

public void setup() {
    float x = 380f;
    float y = 110f;
    float w = 420f;
    float h = 48f;
    textField = new TextField(
            this,
            "txtTest",
            "Click to focus",
            x,
            y,
            w,
            h
    );
}
```

The constructor already applies the initial text, position, and size.

You can observe live text changes through the public facade:

```java
textField.setChangeListener(value -> {
    System.out.println("TextField text = " + value);
});
```

---

## Styling

`TextField` uses the existing text field styling pipeline:

```java
TextFieldStyleConfig tfsc = new TextFieldStyleConfig();
tfsc.backgroundColor = Colors.rgb(236, 242, 248);
tfsc.borderColor = Colors.rgb(72, 116, 156);
tfsc.textColor = Colors.rgb(28, 44, 62);
tfsc.cursorColor = Colors.rgb(38, 132, 212);
tfsc.selectionColor = Colors.rgb(182, 217, 248);
tfsc.selectionTextColor = Colors.rgb(28, 44, 62);
tfsc.textSize = 16.0f;

textField.setStyle(new DefaultTextFieldStyle(tfsc));
```

The facade uses the existing style mechanism. It does not introduce a new styling path.

---

## Input flow

The framework does not own the input source. It only consumes normalized input events provided by the application.

For the simple single-text-field case:

```java
inputManager = new InputManager();
inputManager.registerLayer(new TextFieldInputLayer(0, textField));

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

`TextFieldInputLayer` only routes keyboard events while the field is focused.

The sketch forwards keyboard input as delivered by the host application. Special host-key handling such as `ESC` remains an application decision and is not imposed by `TextField`.

---

## Full example

This example matches `TextFieldTest.java`:

```java
private InputManager inputManager;
private KeyboardState keyboardState;
private ProcessingKeyboardAdapter processingKeyboardAdapter;
private TextField textField;
private String currentText;

public void setup() {
    float x = 380f;
    float y = 110f;
    float w = 420f;
    float h = 48f;
    textField = new TextField(this, "txtTest", "Click to focus", x, y, w, h);
    textField.setChangeListener(value -> {
        System.out.println("TextField text = " + value);
        currentText = value;
    });
    currentText = textField.getText();

    TextFieldStyleConfig tfsc = new TextFieldStyleConfig();
    tfsc.backgroundColor = Colors.rgb(236, 242, 248);
    tfsc.borderColor = Colors.rgb(72, 116, 156);
    tfsc.textColor = Colors.rgb(28, 44, 62);
    tfsc.cursorColor = Colors.rgb(38, 132, 212);
    tfsc.selectionColor = Colors.rgb(182, 217, 248);
    tfsc.selectionTextColor = Colors.rgb(28, 44, 62);
    tfsc.textSize = 16.0f;
    textField.setStyle(new DefaultTextFieldStyle(tfsc));

    inputManager = new InputManager();
    inputManager.registerLayer(new TextFieldInputLayer(0, textField));
    keyboardState = new KeyboardState();
    processingKeyboardAdapter = new ProcessingKeyboardAdapter(keyboardState, inputManager);
    textAlign(CENTER, CENTER);
}

public void draw() {
    background(28);
    textField.draw();
    fill(180);
    text(textField.getCode() + " | focused = " + textField.isFocused(), 380, 220);
    text("text = " + currentText, 380, 250);
    text("click the field to focus | type text | backspace/delete edit | left/right/home/end move | enter no-op", 380, 280);
}

public void mousePressed() {
    inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.PRESS, (float) mouseX, (float) mouseY, mouseButton));
}

public void mouseDragged() {
    inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.DRAG, (float) mouseX, (float) mouseY, mouseButton));
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
```

---

## JSON usage

`TextField` also supports the same minimal config-driven path used by the other closed controls:

```text
JSON → TextFieldConfigLoader → TextFieldConfig → TextFieldFactory → TextField facade → MVVM internals
```

Minimal JSON:

```json
{
  "code": "txtJsonTest",
  "text": "Config-driven text field",
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
private static final String TEXT_FIELD_CONFIG_PATH = "data" + File.separator + "config" + File.separator + "textfield-test.json";

private TextField textField;

public void setup() {
    TextFieldConfigLoader loader = new TextFieldConfigLoader(this);
    TextFieldConfig config = loader.load(TEXT_FIELD_CONFIG_PATH);
    textField = TextFieldFactory.create(this, config);
}
```

The loader validates the small supported surface directly:

- `code` is required
- `text` defaults to `""` when omitted
- `width` and `height` must be greater than `0`

The sketch still owns input wiring and any behavior attached to text changes.

---

## Limitations

`TextField` intentionally stays small in the current iteration:

- single-line editing only
- no public selection API on the facade
- no public copy/paste API on the facade
- no SVG renderer path
- `enter` is currently a no-op

This keeps the public control aligned with the small closed-control pattern already used by the framework.
