# Toggle

`ToggleView` keeps `ToggleViewState` theme-free, reads a cached `ThemeSnapshot` once from its style, and `ParametricToggleStyle` reads snapshot tokens for per-state fill and stroke resolution.

## Performance note

Toggle rendering no longer performs theme resolution inside the draw path. Snapshot reuse keeps the hot path constant-time.
