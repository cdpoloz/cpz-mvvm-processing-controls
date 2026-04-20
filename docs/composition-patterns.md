# Composition Patterns

## Introduction

Composition in this framework means connecting public facades explicitly from the sketch.

The sketch owns behavior:

- listener wiring
- synchronization between controls
- validation decisions
- derived UI state

JSON is optional and structural. It can define controls, layout, style, and base text, but it does not define behavior.

There is no automatic binding layer and there is no declarative binding in JSON.

Composition means:

- recover the facades needed by the sketch
- connect those facades explicitly
- keep the synchronization logic visible in sketch code

---

## Composition Boundary

The boundary is intentional:

- JSON describes structure
- the sketch describes behavior

JSON owns:

- `controls`
- layout
- style
- base text

The sketch owns:

- listeners
- synchronization
- validation
- derived state

```text
JSON → ControlConfigLoader → Map<String, Control>
                          |
                      Sketch
                          |
                listeners + synchronization
```

`Map<String, Control>` is the composition surface for heterogeneous control sets. The sketch can draw every control through `Control`, then recover typed facades only where behavior needs them.

---

## Unidirectional Composition (Base Pattern)

`JsonMultiControlUnidirectionalBindingTest` is the base pattern.

It has one source of truth and one or more destinations.

```text
Slider -> NumericField
Slider -> Label
```

In this pattern:

- one control owns the source value
- the source is the single point of truth
- destination controls are updated from that source 
- synchronization is direct
- no anti-loop guard is needed

The listener is attached to the source:

```java
sldValue.setChangeListener(value -> syncFromSlider());
```

The synchronization routine updates every destination:

```java
private void syncFromSlider() {
    numValue.setValue(sldValue.getValue());
    refreshDerivedLabels();
}
```

This is the recommended default pattern. It is enough when the UI has a clear source and the other controls only need to reflect that source.

---

## Derived State

Derived UI state is not a source and should not drive synchronization.

Labels and other derived UI elements should be calculated from the source facade:

```java
lblCurrentValue.setText("Current value: " + sldValue.getFormattedValue());
```

The sketch should keep visible UI text in `Label` controls instead of drawing it with direct `text()` calls.

This keeps the composed UI consistent:

- JSON provides the initial label text and style
- the sketch updates derived label text
- rendering still happens through the loaded control collection

---

## Bidirectional Composition (Controlled Extension)

`JsonMultiControlBindingTest` is a controlled extension of the unidirectional pattern.

It keeps the same composition:

- same JSON
- same `Map<String, Control>`
- same typed facade recovery
- same input setup
- same derived label update

It adds only:

- one additional listener
- one additional method
- one anti-loop guard

The additional listener is attached to the second editable control:

```java
numValue.setChangeListener(value -> syncFromNumericField());
```

The numeric field path validates before committing:

```java
if (numValue.isValid()) {
    BigDecimal parsedValue = numValue.getValue();
    if (parsedValue != null) {
        sldValue.setValue(parsedValue);
        numValue.setValue(sldValue.getValue());
    }
}
refreshDerivedLabels();
```

The commit still goes through the slider. The slider owns range and step normalization, so the final synchronized value is read back from the slider.

---

## Anti-Loop Guard

Bidirectional composition can trigger listener loops.

The problem appears when:

- a user change in one control updates another control
- the programmatic update fires that other control's listener
- that listener tries to update the first control again

The sketch guards programmatic synchronization:

```java
if (internalUpdate) return;
```

Then it wraps the update:

```java
internalUpdate = true;
try {
    ...
} finally {
    internalUpdate = false;
}
```

The guard belongs to the sketch. It is not part of the framework and it is not declared in JSON.

Use it only when synchronization can flow in more than one direction.

---

## Source Of Truth

Unidirectional composition has a clear source of truth.

In `JsonMultiControlUnidirectionalBindingTest`, the source is:

```text
Slider
```

The numeric field and label are destinations.

Bidirectional composition still needs a final source for committed state.

In `JsonMultiControlBindingTest`, the final source is also the slider because it owns:

- range
- step
- normalization
- committed consistency

The numeric field can propose a value. The slider commits the normalized value.

That keeps both controls consistent after synchronization.

---

## Initial Synchronization

Initial synchronization is not automatic.

The sketch must make the initial composed state explicit in `setup()`:

```java
numValue.setValue(sldValue.getValue());
refreshDerivedLabels();
```

This makes startup behavior match runtime synchronization.

JSON provides the initial structure and base text, but it does not run synchronization logic.

---

## Pattern Summary

- Start simple, extend only when needed
- Unidirectional -> base pattern
- Bidirectional -> controlled extension
- Derived state -> explicit
- Validation -> localized
- Anti-loop -> only when synchronization can flow both ways
- JSON -> structure only
- Sketch -> behavior

---

## See Also

- [Binding](binding.md)
- [Architecture](architecture.md)
- [JSON Configuration](json-configuration.md)
