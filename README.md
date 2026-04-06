# CPZ MVVM Processing Controls

Java UI framework for Processing with strict MVVM layering, centralized input, overlays, and per-sketch theme isolation.

## Main features

- Strict pipeline: `Model -> ViewModel -> View -> Style -> RenderStyle -> Renderer`
- Centralized input via `InputManager` and `InputLayer`
- Keyboard focus via `FocusManager`
- Overlay stacking via `OverlayManager`
- Runtime theming via `ThemeProvider`, `ThemeManager`, and `ThemeTokens`
- Reusable controls with `DevSketch` examples

## Mental model

The intended usage pattern is stable across controls:

1. Create the `Model`.
2. Create the `ViewModel`.
3. Create the `View`.
4. Optionally configure a `Style` or `StyleConfig`.
5. Register the control in the sketch input flow.
6. Call `draw()` every frame.

If the concern is visual, it usually belongs in `View`, `Style`, or `Renderer`.
If the concern is interaction logic, it usually belongs in `ViewModel`.

## Project structure

```text
src/com/cpz/processing/controls
|- core
|  |- focus
|  |- input
|  |- layout
|  |- model
|  |- overlay
|  |- style
|  |- theme
|  |- util
|  |- view
|  `- viewmodel
|- controls
|  |- button
|  |- checkbox
|  |- dropdown
|  |- label
|  |- numericfield
|  |- radiogroup
|  |- slider
|  |- textfield
|  `- toggle
`- dev
```

## Quick start

Run a `DevSketch` from [Launcher.java](/C:/Users/carlos.polo/Software/CPZ/cpz-mvvm-processing-controls/src/com/cpz/processing/controls/dev/Launcher.java) or start a sketch class directly.

```java
// In Launcher.java
// PApplet.main(ButtonDevSketch.class);
// PApplet.main(ToggleDevSketch.class);
// PApplet.main(TextFieldDevSketch.class);
PApplet.main(ThemeDevSketch.class);
```

## MVVM summary

- `Model`: pure state, no Processing dependency
- `ViewModel`: interaction rules, no coordinates or drawing
- `View`: layout, hit testing, and `ViewState` creation
- `Style`: resolves appearance from `ThemeTokens` and control state
- `RenderStyle`: final frame snapshot
- `Renderer`: drawing only

## Render pipeline

```text
ViewState -> Style -> RenderStyle -> Renderer
```

The `Renderer` does not contain business logic and `ViewModel` does not know pixels.

## Input system

Input is centralized:

- `InputManager` is the single dispatch point.
- `InputLayer` defines priority and event capture.
- Control adapters translate Processing input into `ViewModel` intentions.
- `FocusManager` coordinates keyboard ownership.
- `OverlayManager` gives overlays priority when active.

See [docs/input-system.md](/C:/Users/carlos.polo/Software/CPZ/cpz-mvvm-processing-controls/docs/input-system.md).

## Theming

The theme system is instance-based:

- `ThemeProvider` exposes the active `Theme`.
- `ThemeManager` implements `ThemeProvider` and is designed to be owned per sketch.
- Styles read `themeProvider.getTheme().tokens()` during render.
- `StyleConfig` objects can optionally carry a `ThemeProvider`.

Default behavior still works without extra wiring. If no provider is supplied, styles fall back to an internal default theme for backward compatibility.

### Per-sketch example

```java
ThemeManager themeManager = new ThemeManager(new LightTheme());

ButtonView buttonView = new ButtonView(this, buttonViewModel, 180f, 150f, 220f, 60f);
buttonView.setStyle(ButtonDefaultStyles.primary(themeManager));

TextFieldStyleConfig config = new TextFieldStyleConfig();
config.themeProvider = themeManager;
textFieldView.setStyle(new DefaultTextFieldStyle(config));
```

This keeps theme changes isolated to the owning sketch.

## Controls

- [Button](/C:/Users/carlos.polo/Software/CPZ/cpz-mvvm-processing-controls/docs/button.md)
- [Toggle](/C:/Users/carlos.polo/Software/CPZ/cpz-mvvm-processing-controls/docs/toggle.md)
- [Label](/C:/Users/carlos.polo/Software/CPZ/cpz-mvvm-processing-controls/docs/label.md)
- [Checkbox](/C:/Users/carlos.polo/Software/CPZ/cpz-mvvm-processing-controls/docs/checkbox.md)
- [Slider](/C:/Users/carlos.polo/Software/CPZ/cpz-mvvm-processing-controls/docs/slider.md)
- [TextField](/C:/Users/carlos.polo/Software/CPZ/cpz-mvvm-processing-controls/docs/textfield.md)
- [NumericField](/C:/Users/carlos.polo/Software/CPZ/cpz-mvvm-processing-controls/docs/numericfield.md)
- [DropDown](/C:/Users/carlos.polo/Software/CPZ/cpz-mvvm-processing-controls/docs/dropdown.md)
- [RadioGroup](/C:/Users/carlos.polo/Software/CPZ/cpz-mvvm-processing-controls/docs/radiogroup.md)

## Documentation

- [Architecture](/C:/Users/carlos.polo/Software/CPZ/cpz-mvvm-processing-controls/docs/architecture.md)
- [Input System](/C:/Users/carlos.polo/Software/CPZ/cpz-mvvm-processing-controls/docs/input-system.md)
- [Button](/C:/Users/carlos.polo/Software/CPZ/cpz-mvvm-processing-controls/docs/button.md)
- [Toggle](/C:/Users/carlos.polo/Software/CPZ/cpz-mvvm-processing-controls/docs/toggle.md)
- [Label](/C:/Users/carlos.polo/Software/CPZ/cpz-mvvm-processing-controls/docs/label.md)
- [Checkbox](/C:/Users/carlos.polo/Software/CPZ/cpz-mvvm-processing-controls/docs/checkbox.md)
- [Slider](/C:/Users/carlos.polo/Software/CPZ/cpz-mvvm-processing-controls/docs/slider.md)
- [TextField](/C:/Users/carlos.polo/Software/CPZ/cpz-mvvm-processing-controls/docs/textfield.md)
- [NumericField](/C:/Users/carlos.polo/Software/CPZ/cpz-mvvm-processing-controls/docs/numericfield.md)
- [DropDown](/C:/Users/carlos.polo/Software/CPZ/cpz-mvvm-processing-controls/docs/dropdown.md)
- [RadioGroup](/C:/Users/carlos.polo/Software/CPZ/cpz-mvvm-processing-controls/docs/radiogroup.md)
