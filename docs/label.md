# Label

`LabelView` keeps `LabelViewState` theme-free, reads a cached `ThemeSnapshot` once from its style, and `DefaultLabelStyle` consumes snapshot tokens directly.

## Performance note

Theme lookups are removed from the label render path. The render loop only reads already-cached theme data.
