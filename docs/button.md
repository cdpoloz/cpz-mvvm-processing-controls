# Button

## Concept

Action control for triggering a single operation on click.

## MVVM pieces

- Model: `ButtonModel`
- ViewModel: `ButtonViewModel`
- View: `ButtonView`
- Style: `ButtonStyle` / `DefaultButtonStyle`
- Renderer: `DefaultButtonRenderer` or `SvgButtonRenderer`

## Basic usage

```java
ButtonModel model = new ButtonModel("Save");
ButtonViewModel viewModel = new ButtonViewModel(model);
ButtonView view = new ButtonView(this, viewModel, 120f, 80f, 180f, 48f);

view.setPosition(160f, 120f);
view.setSize(200f, 52f);
view.setStyle(ButtonDefaultStyles.primary());
```

## Per-sketch theme usage

```java
ThemeManager themeManager = new ThemeManager(new LightTheme());
view.setStyle(ButtonDefaultStyles.primary(themeManager));
```

Or through config:

```java
ButtonStyleConfig config = new ButtonStyleConfig();
config.themeProvider = themeManager;
config.cornerRadius = 12f;
config.strokeWeight = 2f;
config.strokeWeightHover = 3f;

view.setStyle(new DefaultButtonStyle(config));
```

## Related

- `InputManager`
- `PointerInputAdapter`
- `ThemeProvider`
- `ButtonInputAdapter`
