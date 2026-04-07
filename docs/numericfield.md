# NumericField

## Responsibilities

- `NumericFieldModel` stores numeric constraints and formatting scale
- `NumericFieldViewModel` manages editing, selection, commit, numeric validation, and wheel-driven stepping
- `NumericFieldView` handles layout, cursor positioning, and `NumericFieldViewState`
- `NumericFieldInputAdapter` consumes framework-owned `PointerEvent` instances for move, press, drag, release, and wheel
- `NumericFieldStyle` resolves colors and text visuals from theme data
- `DefaultNumericFieldRenderer` renders the resolved frame without deciding interaction rules

## Behavior

- intermediate states such as `""`, `"-"`, and `"0."` remain local to the editor buffer
- only valid numeric transitions update the committed model value
- external updates are synchronized back into the text buffer only when the user is not editing
- wheel interactions use modifier state from the dispatched `PointerEvent`

## Input Flow

1. An external adapter creates a `PointerEvent`.
2. `InputManager` dispatches the event through the active `InputLayer`.
3. `NumericFieldInputAdapter` interprets pointer coordinates and wheel data using the view and focus manager.
4. `NumericFieldViewModel` applies editing, selection, commit, or increment and decrement rules.

## Notes

- this makes the control suitable as a safe binding target
- bidirectional examples should be composed outside the core API

## Related

- [Architecture](architecture.md)
- [Binding](binding.md)
