# RadioGroup

`RadioGroupView` builds a plain `RadioGroupViewState`, reads a cached `ThemeSnapshot` once from its style, and `RadioGroupStyle` uses snapshot tokens for item render styles.

## Performance note

Theme mapping is snapshot-based and no longer resolved dynamically inside style rendering.
