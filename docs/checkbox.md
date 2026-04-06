# Checkbox

`CheckboxView` builds a plain `CheckboxViewState`, reads the cached `ThemeSnapshot` once from its style, and `DefaultCheckboxStyle` resolves colors from that snapshot.

## Performance note

Theme resolution is outside the render loop. Checkbox draws reuse the cached snapshot and avoid per-frame theme copying.
