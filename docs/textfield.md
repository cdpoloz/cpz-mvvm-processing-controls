# TextField

## Responsibilities

- `TextFieldModel` stores plain text
- `TextFieldViewModel` manages cursor movement, selection, clipboard operations, and focus-aware editing
- `TextFieldView` performs hit testing, text measurement, and `TextFieldViewState` construction
- `TextFieldInputAdapter` consumes framework-owned `PointerEvent` instances for press, drag, and release
- `DefaultTextFieldStyle` resolves themed render values
- `DefaultTextFieldRenderer` draws the resolved text field frame

## Behavior

- editing rules live in the ViewModel
- the input adapter uses pointer coordinates from `PointerEvent` to drive cursor and selection updates through the view
- the renderer is purely graphical and does not infer state

## Input Flow

1. An external source emits pointer input.
2. An external adapter creates a `PointerEvent` and dispatches it through `InputManager`.
3. The active `InputLayer` forwards the event to `TextFieldInputAdapter`.
4. The adapter interprets geometry using `TextFieldView` and updates focus-aware editing through the `ViewModel`.

## Notes

- the text field is the string-oriented counterpart to `NumericField`
- both controls follow the same MVVM, focus, and source-agnostic input contracts

## Related

- [Architecture](architecture.md)
