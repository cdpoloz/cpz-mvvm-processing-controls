# TextField

`TextFieldView` keeps `TextFieldViewState` theme-free, reads a cached `ThemeSnapshot` once per draw or measurement pass, and passes that snapshot into `DefaultTextFieldStyle`. Text measurement helpers reuse the same snapshot.

## Performance note

Theme overhead has been removed from the text field render loop. Snapshot access is stable and allocation-free from the theming side.
