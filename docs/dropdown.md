# DropDown

`DropDown` is the public facade recommended for the simple single-selection drop-down case.

Internally, the control still follows the framework's MVVM architecture. The facade keeps the public API closed while the overlay, focus, and input coordination remain internal implementation details.

---

## Overview

`DropDown` is a closed single-selection control:

- it exposes a small public API for items, current selection, visibility, enabled state, layout, style, and drawing
- it exposes the stable control identity through `getCode()`
- it keeps the expanded overlay internal to the control
- it integrates with the host-owned `InputManager` and `OverlayManager`

In the current iteration:

- click on the base control toggles open/close
- click on an option selects it and closes the overlay
- click outside closes the overlay
- while expanded, the overlay captures pointer input with top-layer priority
- keyboard interaction is intentionally out of scope for this version

---

## Responsibilities

The control keeps the standard MVVM separation used across the framework:

- `Model` stores durable item and selection state plus stable `code`
- `ViewModel` owns expanded state, focus state, hover state, and current selection
- `View` owns layout, hit testing, and `ViewState` construction
- `DropDownOverlayController` keeps overlay registration and top-layer capture internal
- `Style` resolves visual appearance from immutable state and theme data
- `Renderer` performs drawing only

Rendering pipeline:

```text
ViewModel → ViewState → Style → RenderStyle → Renderer
```

---

## Public facade

`DropDown` is a convenience facade for the common case:

- it composes the default internal MVVM pieces and the internal overlay controller
- it does not expose `getView()` or `getViewModel()`
- it keeps overlay registration and overlay input routing out of the public API
- it works with `DropDownInputLayer` for the base control routing case

This is the recommended entry point for normal sketch usage.

---

## Managers owned by the host

Unlike controls without overlays, `DropDown` needs both:

- `InputManager` for pointer routing
- `OverlayManager` for rendering the expanded overlay

The host still owns both managers. `DropDown` uses them internally, but it does not create or expose them.

Minimal setup:

```java
private InputManager inputManager;
private OverlayManager overlayManager;
private DropDown dropDown;

public void setup() {
    inputManager = new InputManager();
    overlayManager = new OverlayManager();

    dropDown = new DropDown(
            this,
            overlayManager,
            inputManager,
            "ddTest",
            List.of("Option Alpha", "Option Beta", "Option Gamma"),
            1,
            380f,
            110f,
            420f,
            48f
    );

    inputManager.registerLayer(new DropDownInputLayer(0, dropDown));
}
```

---

## Input flow

The framework does not own the input source. It consumes normalized pointer events provided by the application.

For the closed public flow:

```text
External Source → Adapter → InputManager → DropDownInputLayer → DropDown facade → DropDownInputAdapter
                                                         
                                                         ↳ internal overlay input layer while expanded 
```

Behavior:

- the base `DropDownInputLayer` handles the collapsed control flow
- when the control expands, the internal overlay controller registers an internal top-priority input layer
- while expanded, that overlay layer captures pointer events and prevents click-through to unrelated controls
- when the user clicks another dropdown, the current overlay closes and the interaction is transferred internally

---

## Focus behavior

`DropDown` uses `FocusManager` internally and keeps focus handling consistent with the rest of the framework:

- opening requests focus for the dropdown
- selecting an option closes the overlay and releases focus
- clicking outside closes the overlay and releases focus
- disabled or invisible state clears expanded/focused transient state

No extra host-side focus hacks are required.

---

## Keyboard

Keyboard interaction is intentionally out of scope in this iteration:

- `DropDownInputLayer` does not route keyboard events
- the internal overlay layer does not route keyboard events
- the control does not claim partial keyboard support
- host logic such as `ESC` remains an application decision

If the host wants `ESC` to close the top overlay, it can do so through `OverlayManager`. In the public examples, `ESC` is only consumed when an overlay is actually open; otherwise the sketch keeps Processing's normal `ESC` behavior:

```java
public void keyPressed() {
    if (key == ESC) {
        OverlayEntry topOverlay = overlayManager.getTopOverlay().orElse(null);
        if (topOverlay != null) {
            key = 0;
            if (topOverlay.getOnClose() != null) {
                topOverlay.getOnClose().run();
            }
        }
    }
}
```

This means:

- `ESC` is not part of the `DropDown` contract
- the host may use `ESC` to close the top overlay
- the host does not need to suppress Processing's default close behavior when no overlay is open

---

## Selection API

`DropDown` keeps the public state small:

```java
List<String> items = dropDown.getItems();
int selectedIndex = dropDown.getSelectedIndex();
String selectedItem = dropDown.getSelectedItem();
boolean expanded = dropDown.isExpanded();
```

- `getItems()` returns the current option list
- `getSelectedIndex()` returns `-1` when nothing is selected
- `getSelectedItem()` returns the selected item text or `null`
- `isExpanded()` reports whether the overlay is currently open

You can also observe selection changes:

```java
dropDown.setChangeListener(index -> {
    System.out.println("selected index = " + index);
});
```

---

## Styling

`DropDown` uses the existing drop-down styling pipeline through `DefaultDropDownStyle` and `DropDownStyleConfig`:

```java
DropDownStyleConfig style = new DropDownStyleConfig();
style.baseFillOverride = Colors.rgb(236, 242, 248);
style.listFillOverride = Colors.rgb(245, 248, 252);
style.textOverride = Colors.rgb(28, 44, 62);
style.borderOverride = Colors.rgb(72, 116, 156);
style.focusedBorderOverride = Colors.rgb(38, 132, 212);
style.textSize = 16.0f;
style.itemHeight = 38.0f;

dropDown.setStyle(new DefaultDropDownStyle(style));
```

The facade does not introduce a second styling path.

---

## Overlay rendering responsibility

The overlay is internal, but the host still renders active overlays through the shared `OverlayManager`:

```java
public void draw() {
    background(28);
    dropDown.draw();

    for (OverlayEntry entry : overlayManager.getActiveOverlays()) {
        entry.getRender().run();
    }
}
```

This keeps overlay ordering centralized and consistent with the existing infrastructure.

---

## JSON usage

`DropDown` also supports the same minimal config-driven path used by the other closed controls:

```text
JSON → DropDownConfigLoader → DropDownConfig → DropDownFactory → DropDown facade → MVVM internals
```

Minimal JSON:

```json
{
  "code": "ddJsonTest",
  "items": ["Mercury", "Venus", "Earth", "Mars", "Jupiter"],
  "selectedIndex": 2,
  "x": 380.0,
  "y": 110.0,
  "width": 420.0,
  "height": 48.0,
  "enabled": true,
  "visible": true
}
```

The loader validates the supported surface directly:

- `code` is required
- `items` is required and must contain at least one string
- `selectedIndex` must be `-1` or a valid item index
- `width` and `height` must be greater than `0`

---

## Limitations

`DropDown` intentionally stays small in the current iteration:

- single selection only
- no public access to overlay internals
- no public keyboard interaction
- no multi-select
- no editable text entry
- no listener or binding setup from JSON

This keeps the public control aligned with the closed-control pattern already used by the framework.
