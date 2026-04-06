# Checkbox

## Concept

Boolean control for marking or clearing an option.

## MVVM pieces

- Model: `CheckboxModel`
- ViewModel: `CheckboxViewModel`
- View: `CheckboxView`
- Style: `CheckboxStyle` / `DefaultCheckboxStyle`
- Renderer: `DefaultCheckboxRenderer` or `SvgCheckboxRenderer`

## Basic usage

```java
CheckboxModel model = new CheckboxModel(false);
CheckboxViewModel viewModel = new CheckboxViewModel(model);
CheckboxView view = new CheckboxView(this, viewModel, 140f, 100f, 28f);

view.setPosition(180f, 140f);
view.setSize(32f);
view.setStyle(CheckboxDefaultStyles.standard());
```

## Per-sketch theme usage

```java
ThemeManager themeManager = new ThemeManager(new DarkTheme());
view.setStyle(CheckboxDefaultStyles.standard(themeManager));
```

## Related

- `InputManager`
- `CheckboxInputAdapter`
- `ThemeProvider`
