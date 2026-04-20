# Theme

Theming in this framework means providing visual tokens to control styles through a `ThemeManager`.

The current public model is explicit:

- the sketch owns a `ThemeManager`
- controls are created through public facades
- styles receive the sketch-owned theme provider
- the sketch changes the theme when needed
- styles read the current `ThemeSnapshot` during rendering without requiring explicit updates in the sketch

There is no global theme singleton.

---

## Official Example

The official facade-based theme example is:

- `ThemeFacadeSketch`

It uses public facades:

- `Button`
- `Toggle`
- `Checkbox`
- `Slider`
- `TextField`
- `Label`

It does not use internal MVVM types such as `ButtonView`, `ButtonViewModel`, or `ButtonInputAdapter`. The layout below matches the current implementation of ThemeFacadeSketch.

The sketch size is:

```java
size(920, 520);
smooth(8);
```

The visible layout is created directly in the sketch.

Labels:

```java
add(label("lblTitle", "Theme Facade Sketch", 80.0f, 34.0f, 760.0f, 34.0f, titleStyle));
add(label("lblHelp", "Press 't' to toggle LightTheme and DarkTheme when TextField is not focused.", 80.0f, 72.0f, 760.0f, 28.0f, bodyStyle));

lblTheme = label("lblTheme", "", 80.0f, 112.0f, 300.0f, 24.0f, valueStyle);

add(label("lblButton", "Button", 150.0f, 148.0f, 180.0f, 22.0f, captionStyle));
add(label("lblToggle", "Toggle", 382.5f, 148.0f, 90.0f, 22.0f, captionStyle));
add(label("lblCheckbox", "Checkbox", 545.0f, 148.0f, 120.0f, 22.0f, captionStyle));
add(label("lblSlider", "Slider progress and thumb", 85.0f, 258.0f, 280.0f, 22.0f, captionStyle));
add(label("lblTextField", "TextField focus, caret and selection", 80.0f, 360.0f, 360.0f, 22.0f, captionStyle));

lblSliderValue = label("lblSliderValue", "", 400.0f, 288.0f, 260.0f, 28.0f, valueStyle);

add(label("lblValidation", "Validate background, text, borders, hover, pressed, focus, selection and slider colors after toggling.", 80.0f, 462.0f, 760.0f, 42.0f, bodyStyle));
```

Interactive controls:

```java
btnToggleTheme = new Button(this, "btnToggleTheme", "Toggle Theme", 170.0f, 180.0f, 180.0f, 35.0f);
tglMode = new Toggle(this, "tglMode", 0, 2, 400.0f, 180.0f, 35.0f);
chkEnabled = new Checkbox(this, "chkEnabled", true, 570.0f, 180.0f, 35.0f);

sldAmount = new Slider(
        this,
        "sldAmount",
        BigDecimal.ZERO,
        new BigDecimal("100"),
        new BigDecimal("5"),
        new BigDecimal("45"),
        230.0f,
        286.0f,
        300.0f,
        68.0f
);

txtSample = new TextField(this, "txtSample", "Focus here, type, select text", 230.0f, 394.0f, 300f, 40.0f);
```

---

## Theme Ownership

The sketch owns the manager:

```java
private final ThemeManager themeManager = new ThemeManager(new LightTheme());
```

The same manager is passed to public default styles:

```java
btnToggleTheme.setStyle(ButtonDefaultStyles.primary(themeManager));
tglMode.setStyle(ToggleDefaultStyles.circular(themeManager));
chkEnabled.setStyle(CheckboxDefaultStyles.standard(themeManager));
sldAmount.setStyle(SliderDefaultStyles.standard(themeManager));
txtSample.setStyle(TextFieldDefaultStyles.standard(themeManager));
```

Labels use the same pattern through their public style configuration:

```java
LabelStyleConfig config = new LabelStyleConfig();
config.textSize = 15.0f;
config.themeProvider = themeManager;
label.setStyle(new DefaultLabelStyle(config));
```

The important point is that every style receives the same sketch-owned `ThemeManager`.

---

## Theme Toggle

The example toggles theme in the sketch:

```java
private void toggleTheme() {
    lightTheme = !lightTheme;
    themeManager.setTheme(lightTheme ? new LightTheme() : new DarkTheme());
    refreshDerivedLabels();
}
```

`ThemeManager.setTheme(...)` replaces the current theme and rebuilds the cached `ThemeSnapshot`.

The sketch-owned `ThemeManager` is the single source of truth for the active theme. Controls do not store independent theme state and do not need extra synchronization. Styles read the current snapshot when rendering the controls.

---

## Input And Shortcuts

The example keeps input explicit.

Pointer events are normalized by the sketch and forwarded to a sketch-owned input layer:

```java
inputManager.registerLayer(new ThemeFacadeInputLayer(0));
```

The layer calls the input methods exposed by each concrete facade:

```java
btnToggleTheme.handlePointerEvent(event);
tglMode.handlePointerEvent(event);
chkEnabled.handlePointerEvent(event);
sldAmount.handlePointerEvent(event);
txtSample.handlePointerEvent(event);
```

Those methods belong to the specific public facade APIs. They are not part of the shared `Control` interface, which remains the minimal common surface for drawing, visibility, enabled state, code identity, and positioning.

Keyboard input follows the same focus rule used by the current examples:

- if `TextField` is focused, it receives keyboard events
- if no text field is focused, typed `t` toggles the theme

```java
if (txtSample.isFocused()) {
    txtSample.handleKeyboardEvent(event);
    return true;
}

if (event.getType() == KeyboardEvent.Type.TYPE && (event.getKey() == 't' || event.getKey() == 'T')) {
    toggleTheme();
    return true;
}
```

Derived labels are refreshed by the sketch:

```java
private void refreshDerivedLabels() {
    lblTheme.setText("Current theme: " + (lightTheme ? "LightTheme" : "DarkTheme"));
    lblSliderValue.setText("Slider value: " + sldAmount.getFormattedValue());
}
```

The shortcut belongs to the sketch. It is not a framework-global command.

---

## What To Validate

Run `ThemeFacadeSketch` and press `t` when the text field is not focused.

Validate:

- background color changes with the current theme
- label text follows theme tokens
- button border, fill, hover, and pressed states update
- toggle and checkbox colors update
- slider track, progress, and thumb colors update
- text field border, focus, caret, and selection colors update
- all controls update together because their styles share the same `ThemeManager`

Also focus the text field and press `t`. The field should consume the key instead of toggling the global shortcut.

---

## Architecture Fit

The theme flow stays aligned with the public facade architecture:

```text
Sketch → ThemeManager → ThemeSnapshot
                      |
                 public styles
                      |
                 renderers
```

The sketch owns the theme decision.

Styles consume `ThemeSnapshot`.

Controls expose closed public facades and hide MVVM internals while styles and renderers keep the visual pipeline aligned with the current snapshot.

This keeps theming explicit without adding global state or exposing internal rendering objects.

---

## See Also

- [Architecture](architecture.md)
- [Control](control.md)
- [Binding](binding.md)
- [JSON Configuration](json-configuration.md)
