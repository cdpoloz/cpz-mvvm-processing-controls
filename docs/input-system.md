# Input System

## Overview

Input is centralized on purpose. Processing events enter the sketch once, then the framework redistributes them through `InputManager`.

## Core pieces

- `InputManager`: dispatches pointer and keyboard events by priority
- `InputLayer`: capture boundary with priority and activation rules
- `PointerInputAdapter`: translates pointer events into control intentions
- `KeyboardInputAdapter`: routes keyboard events through `FocusManager`
- `FocusManager`: owns the focused keyboard target
- `OverlayManager`: lets overlays temporarily sit above base layers

## Flow

```text
Processing event
-> InputManager
-> InputLayer
-> Control adapter
-> ViewModel
-> ViewState
-> Style
-> Renderer
```

## Notes

- Input, focus, and overlay managers are instance-based and should be owned per sketch.
- Theme resolution is separate from input and is handled through `ThemeProvider`.
- `ThemeDevSketch` demonstrates per-sketch theme ownership without changing the input architecture.

## Related

- [Architecture](/C:/Users/carlos.polo/Software/CPZ/cpz-mvvm-processing-controls/docs/architecture.md)
- [ThemeDevSketch.java](/C:/Users/carlos.polo/Software/CPZ/cpz-mvvm-processing-controls/src/com/cpz/processing/controls/dev/ThemeDevSketch.java)
- [DropDownDevSketch.java](/C:/Users/carlos.polo/Software/CPZ/cpz-mvvm-processing-controls/src/com/cpz/processing/controls/dev/DropDownDevSketch.java)
