# DropDown

## Responsibilities

- `DropDownView` keeps `DropDownViewState` focused on control state and layout data
- `DropDownOverlayController` registers an `InputLayer` for the expanded overlay and consumes framework-owned `PointerEvent` instances
- `DropDownViewModel` owns expanded state, option hover, and selection
- `DefaultDropDownStyle` resolves themed values from a cached snapshot

## Input Flow

1. An external adapter creates a `PointerEvent`.
2. `InputManager` dispatches the event through active layers in priority order.
3. The dropdown overlay input layer captures events while the overlay is expanded.
4. The overlay controller forwards pointer state to the dropdown view and view model.

## Performance Note

The dropdown style no longer performs theme-provider resolution during render. Theme changes rebuild the snapshot once; frames reuse it.
