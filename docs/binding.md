# Binding

The library now includes a minimal unidirectional binding utility for ViewModels.

## Scope

- Unidirectional only
- Explicit wiring
- No reflection
- No automatic property discovery
- No bidirectional synchronization yet

This keeps the MVVM layers clear: one ViewModel exposes a value, another ViewModel receives updates through an explicit setter.

## Example

```java
Binding.bind(
    sliderVM::getValue,
    value -> labelVM.setText(value.toString()),
    sliderVM::addListener
);
```

## Philosophy

Binding in this library is intentionally small and explicit. The goal is to validate a simple MVVM-friendly flow without introducing generic base abstractions too early.

## Current demo

`BindingDevSketch` demonstrates:

- `SliderViewModel` as the source
- `LabelViewModel` as the target
- immediate initial synchronization
- reactive updates when the slider value changes

## Note

Bidirectional binding is intentionally out of scope for this phase and can be added later on top of this minimal foundation.
