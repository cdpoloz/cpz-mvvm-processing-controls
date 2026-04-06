# Toggle

## Concept

Cyclic state control that can behave as a binary toggle or as a multi-state selector.

## MVVM pieces

- Model: `ToggleModel`
- ViewModel: `ToggleViewModel`
- View: `ToggleView`
- Style: `ToggleStyle` / `ParametricToggleStyle`
- Renderer: `ToggleShapeRenderer` and implementations

## Basic usage

```java
ToggleModel model = new ToggleModel();
ToggleViewModel viewModel = new ToggleViewModel(model);
ToggleView view = new ToggleView(this, viewModel, 200f, 120f, 64f);

viewModel.setTotalStates(2);
view.setPosition(220f, 160f);
view.setSize(72f);
view.setStyle(ToggleDefaultStyles.circular(themeManager));
```

## Related

- `InputManager`
- `ToggleInputAdapter`
- `ThemeProvider`
- `CircleShapeRenderer`
