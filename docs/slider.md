# Slider

## Responsibilities

- `SliderModel` stores value, range, step, and snap behavior
- `SliderViewModel` manages pointer interaction and change notification
- `SliderView` converts geometry into normalized values and builds `SliderViewState`
- `SliderInputAdapter` accepts framework-owned `PointerEvent` instances and maps them to slider interactions
- `SliderStyle` resolves track, thumb, and text visuals
- `SliderRenderer` draws the resolved frame

## Data Flow

1. An external adapter creates a `PointerEvent`.
2. `InputManager` dispatches that event through the active `InputLayer`.
3. `SliderInputAdapter` handles `MOVE`, `PRESS`, `DRAG`, `RELEASE`, and `WHEEL`.
4. `SliderViewModel` updates the model and interaction flags.
5. `SliderView` builds geometry and `SliderViewState`.
6. `SliderStyle` maps state and theme data to `SliderRenderStyle`.
7. `SliderRenderer` draws the slider.

## Notes

- wheel handling uses `PointerEvent` modifier state for step scaling
- the slider ViewModel is a common binding source because it exposes value listeners
- the view owns geometry, not value rules
- rendering stays allocation-light by consuming cached `ThemeSnapshot` data

## Related

- [Architecture](architecture.md)
- [Binding](binding.md)
