# Input System

Input remains unchanged architecturally:

- `InputManager` dispatches events
- `InputLayer` defines priority
- `FocusManager` handles keyboard ownership
- `OverlayManager` handles overlay precedence

## Relation to theme performance

Theme optimization is independent from input:

- theme snapshots are built on theme change
- views read snapshots when building `ViewState`
- input still feeds `ViewModel` through the existing adapters

This keeps MVVM boundaries intact while moving theme work out of the render loop.
