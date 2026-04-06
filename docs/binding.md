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

``` java
Binding.bind(
    sliderVM::getValue,
    value -> labelVM.setText(value.toString()),
    sliderVM::addListener
);
```

## Philosophy

Binding in this library is intentionally small and explicit. The goal is to validate a simple MVVM-friendly flow without introducing generic base abstractions too early.

Bidirectional binding is not part of the core API and is implemented as an example using listeners.

## Current demo

`BindingDevSketch` demonstrates:

- `SliderViewModel` as the source
- `LabelViewModel` as one target
- `NumericFieldViewModel` as another target
- immediate initial synchronization
- reactive updates when the slider value changes

## Multiple Targets

Binding can be applied to multiple targets from a single source.

``` java
Binding.bind(
    sliderVM::getValue,
    value -> labelVM.setText(value.toString()),
    sliderVM::addListener
);

Binding.bind(
    sliderVM::getValue,
    numericVM::setValue,
    sliderVM::addListener
);
```

> NumericFieldViewModel safely ignores external updates while the user is editing.

## Bidirectional Binding (Advanced)

Bidirectional binding is not part of the core API but can be implemented in a sketch or composition root using existing listeners.

``` java
boolean internalUpdate = false;

sliderVM.addListener(value -> {
    if (internalUpdate) return;

    internalUpdate = true;
    try {
        numericVM.setValue(value);
    } finally {
        internalUpdate = false;
    }
});

numericVM.setOnValueChanged(value -> {
    if (internalUpdate) return;

    internalUpdate = true;
    try {
        sliderVM.setValue(value);
    } finally {
        internalUpdate = false;
    }
});
```

> This approach avoids infinite loops by using a local update guard.

## Note

Bidirectional binding is intentionally out of scope for this phase and can be added later on top of this minimal foundation.
