# NumericField

## Responsibilities

- `NumericFieldModel` stores numeric constraints and formatting scale
- `NumericFieldViewModel` manages editing, selection, commit, and numeric validation
- `NumericFieldView` handles layout, cursor positioning, and `NumericFieldViewState`
- `NumericFieldStyle` resolves colors and text visuals from theme data
- `DefaultNumericFieldRenderer` renders the resolved frame without deciding interaction rules

## Behavior

- intermediate states such as `""`, `"-"`, and `"0."` remain local to the editor buffer
- only valid numeric transitions update the committed model value
- external updates are synchronized back into the text buffer only when the user is not editing

## Notes

- this makes the control suitable as a safe binding target
- bidirectional examples should be composed outside the core API

## Related

- [Architecture](architecture.md)
- [Binding](binding.md)
