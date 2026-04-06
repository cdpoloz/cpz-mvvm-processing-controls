# Architecture

## Pipeline

```text
Model -> ViewModel -> View -> ViewState -> Style -> RenderStyle -> Renderer
```

## Boundaries

- `Model`: pure state
- `ViewModel`: interaction logic
- `View`: geometry, measurement, and `ViewState` construction
- `ViewState`: immutable frame input for style resolution
- `Style`: pure mapping from state plus `ThemeSnapshot` to render values
- `Renderer`: drawing only

## Theme architecture

- `ThemeProvider` exposes the cached `ThemeSnapshot`
- `ThemeManager` implements `ThemeProvider`
- `ThemeSnapshot` is rebuilt only when the theme changes
- `View` reads the snapshot once and passes it to style resolution
- styles must not resolve theme objects dynamically during render

## Performance principle

No theme allocation in the render loop:

- no per-frame `Theme.copy()`
- no per-frame `ThemeTokens` copying
- no per-frame theme lookup inside styles

Theme work happens at theme-change time, not frame time.

## Theme toggle flow

`ThemeDevSketch` owns a `ThemeManager`, passes that instance into every themed control style during `setup()`, and tracks the active theme explicitly in sketch state. On `t`, the sketch flips that flag and calls `themeManager.setTheme(...)` with the matching theme data. Because each view reads the cached snapshot from its bound style on every draw, the next frame reflects the new theme without per-frame allocation.

## Related

- [README](/C:/Users/carlos.polo/Software/CPZ/cpz-mvvm-processing-controls/README.md)
- [Input System](/C:/Users/carlos.polo/Software/CPZ/cpz-mvvm-processing-controls/docs/input-system.md)
