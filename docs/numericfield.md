# NumericField

## Concept

Text-based numeric input backed by `BigDecimal`, with separate numeric value and editable text buffer.

## MVVM pieces

- Model: `NumericFieldModel`
- ViewModel: `NumericFieldViewModel`
- View: `NumericFieldView`
- Style: `NumericFieldStyle`
- Renderer: `DefaultNumericFieldRenderer`

## Theme-aware customization

```java
NumericFieldStyleConfig config = new NumericFieldStyleConfig();
config.themeProvider = themeManager;
config.textSize = 18f;

view.setStyle(new NumericFieldStyle(config));
```

## Notes

- Parsing and commit rules stay in the `ViewModel`.
- Wheel, arrows, and modifiers are handled by the existing input pipeline.
- Theme resolution is style-local and per sketch when a provider is supplied.

## Related

- `FocusManager`
- `KeyboardInputAdapter`
- `NumericFieldInputAdapter`
- `ThemeProvider`
