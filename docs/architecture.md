# Architecture

## MVVM

- `Model`: pure state, no Processing dependency
- `ViewModel`: interaction logic, no coordinates or drawing
- `View`: layout, hit testing, and `ViewState` creation
- `Style`: visual resolution from control state and `ThemeTokens`
- `RenderStyle`: final snapshot for the current frame
- `Renderer`: drawing only

## Pipeline

```text
ViewState -> Style -> RenderStyle -> Renderer
```

## Principles

- No business logic in `Renderer`
- No Processing dependency in `Model` or `ViewModel`
- Strict responsibility boundaries
- No global runtime context for control state or themes

## Shared systems

- `InputManager` for pointer and keyboard dispatch
- `FocusManager` for keyboard focus
- `OverlayManager` for overlay priority
- `ThemeProvider` for style-time theme resolution
- `ThemeManager` as a per-sketch `ThemeProvider`
- `LayoutConfig` and `LayoutResolver` for proportional positioning

## Theme flow

Theme resolution is now injected instead of global:

1. A sketch owns a `ThemeManager` or another `ThemeProvider`.
2. The provider is passed through `StyleConfig` or style constructors/factories.
3. The `Style` reads `themeProvider.getTheme().tokens()` during render.
4. The `Renderer` only receives resolved frame values in `RenderStyle`.

Backward compatibility is preserved through an internal default fallback used only when no provider is supplied.

## Related

- [Input System](/C:/Users/carlos.polo/Software/CPZ/cpz-mvvm-processing-controls/docs/input-system.md)
- [README](/C:/Users/carlos.polo/Software/CPZ/cpz-mvvm-processing-controls/README.md)
