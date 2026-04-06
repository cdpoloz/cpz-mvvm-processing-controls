# Button

`ButtonView` builds a plain `ButtonViewState`, reads the cached `ThemeSnapshot` once from its style, and passes that snapshot into `DefaultButtonStyle`. The style reads tokens from the snapshot instead of resolving theme objects during render.

## Performance note

Themed button rendering no longer allocates from theme resolution in `draw()`. Theme snapshots are reused until the sketch changes theme.
