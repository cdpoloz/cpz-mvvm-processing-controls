# Label

## Concept

Passive text control for status, captions, and supporting information.

## MVVM pieces

- Model: `LabelModel`
- ViewModel: `LabelViewModel`
- View: `LabelView`
- Style: `LabelStyle` / `DefaultLabelStyle`
- Renderer: `DefaultTextRenderer`

## Basic usage

```java
LabelModel model = new LabelModel();
LabelViewModel viewModel = new LabelViewModel(model);
LabelView view = new LabelView(this, viewModel, 120f, 80f);

viewModel.setText("Status");
view.setPosition(160f, 120f);
view.setStyle(LabelDefaultStyles.defaultText());
```

## Per-sketch theme usage

```java
ThemeManager themeManager = new ThemeManager(new LightTheme());
view.setStyle(LabelDefaultStyles.defaultText(themeManager));
```

## Related

- `ThemeProvider`
- `LabelStyleConfig`
- `LabelTypography`
