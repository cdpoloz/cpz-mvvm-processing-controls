# RadioGroup

## Concept

Mutually exclusive selection control for choosing one option from a list.

## MVVM pieces

- Model: `RadioGroupModel`
- ViewModel: `RadioGroupViewModel`
- View: `RadioGroupView`
- Style: `RadioGroupStyle`
- Renderer: `DefaultRadioGroupRenderer`

## Theme-aware customization

```java
RadioGroupStyleConfig config = new RadioGroupStyleConfig();
config.themeProvider = themeManager;
config.itemHeight = 30f;
config.itemSpacing = 8f;

view.setStyle(new RadioGroupStyle(config));
```

## Related

- `InputManager`
- `FocusManager`
- `RadioGroupInputAdapter`
- `ThemeProvider`
