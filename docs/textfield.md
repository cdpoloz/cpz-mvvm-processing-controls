# TextField

## Concept

Editable text field with cursor, selection, and focus integration.

## MVVM pieces

- Model: `TextFieldModel`
- ViewModel: `TextFieldViewModel`
- View: `TextFieldView`
- Style: `TextFieldStyle` / `DefaultTextFieldStyle`
- Renderer: `DefaultTextFieldRenderer`

## Theme-aware customization

```java
TextFieldStyleConfig config = new TextFieldStyleConfig();
config.themeProvider = themeManager;
config.textSize = 18f;

view.setStyle(new DefaultTextFieldStyle(config));
```

## Related

- `FocusManager`
- `KeyboardInputAdapter`
- `TextFieldInputAdapter`
- `ThemeProvider`
