# TextField

## Responsibilities

- `TextFieldModel` stores plain text
- `TextFieldViewModel` manages cursor movement, selection, clipboard operations, and focus-aware editing
- `TextFieldView` performs hit testing, text measurement, and `TextFieldViewState` construction
- `DefaultTextFieldStyle` resolves themed render values
- `DefaultTextFieldRenderer` draws the resolved text field frame

## Behavior

- editing rules live in the ViewModel
- the view translates pointer position into cursor and selection indices
- the renderer is purely graphical and does not infer state

## Notes

- the text field is the string-oriented counterpart to `NumericField`
- both controls follow the same MVVM and render pipeline contracts

## Related

- [Architecture](architecture.md)
