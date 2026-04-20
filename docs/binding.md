# Binding

Binding in this framework means explicit synchronization between public controls in the sketch.

There is no declarative binding layer in JSON and there is no hidden runtime magic. The recommended composition flow is:

- load or create closed facades
- keep structure in JSON when useful
- recover the typed facades needed for behavior
- wire those controls together explicitly in the sketch

The official progression is:

- `JsonMultiControlUnidirectionalBindingTest`
- `JsonMultiControlBindingTest`
- [json-multicontrol-binding-test.json](../data/config/json-multicontrol-binding-test.json)

Both sketches use the same JSON file so the comparison stays focused on sketch-side composition.

For a shorter pattern guide, see [Composition Patterns](composition-patterns.md).

---

## Composition In The Sketch

The JSON file defines:

- the control set
- layout
- style
- base text

It does not define:

- listeners
- binding
- business behavior

The sketch loads the multi-control document and receives a heterogeneous public composition surface:

```java
ControlConfigLoader loader = new ControlConfigLoader(this);
Map<String, Control> controls = loader.load(CONFIG_PATH);
```

Then it resolves only the facades that participate in behavior:

```java
sldValue = Util.getControl(controls, "sldValue", Slider.class);
numValue = Util.getControl(controls, "numValue", NumericField.class);
lblCurrentValue = Util.getControl(controls, "lblCurrentValue", Label.class);
```

Other controls from the same JSON document, such as title and help labels, can stay in the generic collection and still be drawn uniformly:

```java
controls.values().forEach(Control::draw);
```

This is the key boundary:

- JSON defines structure
- the sketch defines synchronization

---

## Unidirectional Example

`JsonMultiControlUnidirectionalBindingTest` is the base pattern.

It uses the same visual structure as the bidirectional example:

- `Slider sldValue`
- `NumericField numValue`
- `Label lblCurrentValue`
- the same `Map<String, Control>` loading flow
- the same typed facade recovery
- the same uniform render pass with `Control::draw`

The data flow has one source:

```text
Slider -> NumericField
Slider -> Label
```

The binding is just one listener:

```java
sldValue.setChangeListener(value -> syncFromSlider());
```

And one synchronization routine:

```java
private void syncFromSlider() {
    numValue.setValue(sldValue.getValue());
    refreshDerivedLabels();
}
```

The initial state is set explicitly in `setup()`:

```java
numValue.setValue(sldValue.getValue());
refreshDerivedLabels();
```

The numeric field is still registered for input, because it remains a real control in the composed UI. In this example, however, typing into it does not update the slider. It is only a visual destination synchronized from the slider.

No guard is needed because there is only one synchronization direction.

---

## Evolution To Bidirectional

`JsonMultiControlBindingTest` is the controlled bidirectional extension of the unidirectional sketch.

It keeps the same JSON, same controls, same input setup, same derived label refresh, and same render pass. The extra behavior is intentionally small:

- one additional listener on `NumericField`
- one additional synchronization routine, `syncFromNumericField()`
- one local anti-loop guard, `internalUpdate`

The extra listener is:

```java
numValue.setChangeListener(value -> syncFromNumericField());
```

The numeric-field path validates and commits through the slider:

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

That re-applies the final slider value back into `NumericField` after the slider has normalized it. This matters because the slider owns range and step normalization, so the sketch uses the final slider value as the committed synchronized value.

If the numeric field is in an intermediate invalid state, the sketch does not push that state into the slider. It only refreshes derived UI state.

---

## Guard Anti-Loop

Bidirectional synchronization needs a local guard because:

- changing the slider updates the numeric field
- changing the numeric field can update the slider
- programmatic updates can trigger listeners

The bidirectional sketch solves that with a sketch-local field:

```java
private boolean internalUpdate;
```

Each synchronization path returns early when the current change was caused by sketch code:

```java
if (internalUpdate) return;
```

Then it wraps programmatic updates:

```java
internalUpdate = true;
try {
    // synchronized updates
} finally {
    internalUpdate = false;
}
```

This guard is part of the sketch composition. It is not a framework-level binding feature and it is not declared in JSON.

---

## Derived State

Derived UI state stays explicit too.

Both examples use a dedicated method:

```java
private void refreshDerivedLabels() {
    lblCurrentValue.setText("Current value: " + sldValue.getFormattedValue());
}
```

This keeps visible text inside `Label` controls instead of drawing ad hoc text directly in the sketch.

The important distinction is:

- source state is read from the public facade that owns it
- derived state is updated by sketch code
- JSON only provides the initial label text and visual configuration

---

## Current Model

For the current public facade architecture, the primary learning path is:

1. compose controls through facades
2. load them from JSON when useful
3. use `Map<String, Control>` as the common surface
4. resolve typed facades needed by behavior
5. start with unidirectional sketch synchronization
6. add the bidirectional layer only when the UI needs it

This keeps the public API focused on facades, explicit composition, and sketch-side behavior.

---

## Historical Note

Earlier iterations of the project explored a lower-level binding helper around ViewModel synchronization.

That approach was removed to enforce a single public composition model based on closed facades, `Control`, optional JSON multi-control loading, and explicit synchronization in the sketch.
