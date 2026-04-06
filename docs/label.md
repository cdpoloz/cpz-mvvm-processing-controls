# Label

## Responsibilities

- `LabelModel` stores the current text
- `LabelViewModel` exposes label state to the view layer
- `LabelView` measures text and builds `LabelViewState`
- `DefaultLabelStyle` resolves typography and colors from the theme
- `DefaultTextRenderer` performs drawing only

## Notes

- the label control does not own business logic
- text measurement is cached in the view
- theme data is consumed through `ThemeSnapshot`, not resolved ad hoc during drawing

## Related

- [Architecture](architecture.md)
