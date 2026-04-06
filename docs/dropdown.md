# DropDown

## Concept

Single-selection control with an integrated overlay list.

## MVVM pieces

- Model: `DropDownModel`
- ViewModel: `DropDownViewModel`
- View: `DropDownView`
- Style: `DefaultDropDownStyle`
- Renderer: `DefaultDropDownRenderer`

## Basic usage

```java
DropDownModel model = new DropDownModel(java.util.List.of("Alpha", "Beta", "Gamma"), 0);
DropDownViewModel viewModel = new DropDownViewModel(model);
DropDownView view = new DropDownView(this, viewModel, 220f, 140f, 280f, 42f);

DropDownStyleConfig config = new DropDownStyleConfig();
config.themeProvider = themeManager;
view.setStyle(new DefaultDropDownStyle(config));
```

## Notes

- The overlay still belongs to the owning `DropDown`.
- `OverlayManager` and `InputManager` remain instance-scoped.
- Theme resolution happens in the style through the injected `ThemeProvider`.

## Related

- `OverlayManager`
- `DropDownOverlayController`
- `FocusManager`
- `ThemeProvider`
