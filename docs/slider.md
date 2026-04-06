# Slider

## Responsibilities

- `SliderModel` stores value, range, step, and snap behavior
- `SliderViewModel` manages pointer interaction and change notification
- `SliderView` converts geometry into normalized values and builds `SliderViewState`
- `SliderStyle` resolves track, thumb, and text visuals
- `SliderRenderer` draws the resolved frame

## Data Flow

1. Pointer input is translated by `SliderInputAdapter`.
2. `SliderViewModel` updates the model and interaction flags.
3. `SliderView` builds geometry and `SliderViewState`.
4. `SliderStyle` maps state and theme data to `SliderRenderStyle`.
5. `SliderRenderer` draws the slider.

## Notes

- the slider ViewModel is a common binding source because it exposes value listeners
- the view owns geometry, not value rules
- rendering stays allocation-light by consuming cached `ThemeSnapshot` data

## Related

- [Architecture](architecture.md)
- [Binding](binding.md)
