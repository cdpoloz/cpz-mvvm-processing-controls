# CPZ MVVM Processing Controls

Java UI framework for Processing with strict MVVM layering, centralized input, overlays, and per-sketch theme isolation.

## Main features

- Strict pipeline: `Model -> ViewModel -> View -> ViewState -> Style -> RenderStyle -> Renderer`
- Centralized input via `InputManager` and `InputLayer`
- Keyboard focus via `FocusManager`
- Overlay stacking via `OverlayManager`
- Runtime theming via `ThemeProvider`, `ThemeManager`, `ThemeSnapshot`, and `ThemeTokens`
- Render loop designed to avoid theme-related allocations

## Performance model

The render path is optimized for high-frequency `draw()` loops:

- `ThemeManager` caches a `ThemeSnapshot`
- snapshots are rebuilt only when `setTheme(...)` is called
- `View` pulls the cached snapshot once per draw or measurement pass
- `Style` reads `snapshot.tokens`
- `Renderer` only consumes already-resolved frame data

This removes per-frame theme copying and prevents theme resolution work from happening inside style render code.

## Theme lifecycle

1. A sketch owns a `ThemeManager`
2. The sketch decides which theme data to apply and calls `setTheme(...)`
3. `ThemeManager` stores that theme data and rebuilds a cached `ThemeSnapshot`
4. `Style` maps `ViewState` + snapshot tokens into `RenderStyle`
6. `Renderer` draws with constant-time inputs

## Per-sketch example

```java
ThemeManager themeManager = new ThemeManager(new LightTheme());

ButtonView buttonView = new ButtonView(this, buttonViewModel, 180f, 150f, 220f, 60f);
buttonView.setStyle(ButtonDefaultStyles.primary(themeManager));
```

## Notes

- No global mutable theme state
- Theme toggling in example sketches is managed explicitly by sketch state, not by `instanceof` checks on `Theme`
- Styles without an explicit provider use a local `ThemeManager`, not a shared static fallback
- Input, overlay, and focus managers remain instance-based

## Binding (Experimental)

The library includes a minimal unidirectional binding mechanism for ViewModels.

See: [docs/binding.md](/C:/Users/carlos.polo/Software/CPZ/cpz-mvvm-processing-controls/docs/binding.md)

## Documentation

- [Architecture](/C:/Users/carlos.polo/Software/CPZ/cpz-mvvm-processing-controls/docs/architecture.md)
- [Binding](/C:/Users/carlos.polo/Software/CPZ/cpz-mvvm-processing-controls/docs/binding.md)
- [Input System](/C:/Users/carlos.polo/Software/CPZ/cpz-mvvm-processing-controls/docs/input-system.md)
- [Button](/C:/Users/carlos.polo/Software/CPZ/cpz-mvvm-processing-controls/docs/button.md)
- [Checkbox](/C:/Users/carlos.polo/Software/CPZ/cpz-mvvm-processing-controls/docs/checkbox.md)
- [DropDown](/C:/Users/carlos.polo/Software/CPZ/cpz-mvvm-processing-controls/docs/dropdown.md)
- [Label](/C:/Users/carlos.polo/Software/CPZ/cpz-mvvm-processing-controls/docs/label.md)
- [NumericField](/C:/Users/carlos.polo/Software/CPZ/cpz-mvvm-processing-controls/docs/numericfield.md)
- [RadioGroup](/C:/Users/carlos.polo/Software/CPZ/cpz-mvvm-processing-controls/docs/radiogroup.md)
- [Slider](/C:/Users/carlos.polo/Software/CPZ/cpz-mvvm-processing-controls/docs/slider.md)
- [TextField](/C:/Users/carlos.polo/Software/CPZ/cpz-mvvm-processing-controls/docs/textfield.md)
- [Toggle](/C:/Users/carlos.polo/Software/CPZ/cpz-mvvm-processing-controls/docs/toggle.md)
