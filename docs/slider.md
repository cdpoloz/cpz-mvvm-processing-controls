# Slider

`SliderView` builds a plain `SliderViewState`, reads a cached `ThemeSnapshot` once from its style, and `SliderStyle` resolves track, progress, thumb, and text colors from snapshot tokens.

## Performance note

Slider rendering no longer allocates from theme resolution. The hot render path uses cached snapshot data.
