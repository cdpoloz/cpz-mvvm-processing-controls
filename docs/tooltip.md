# Tooltip

`Tooltip` is a reusable overlay component. It is not a `Control` and does not
depend on `Button`, `Label`, `TextField`, or any other public facade.

It can be attached to MVVM controls or to arbitrary sketch regions such as a
`PImage`, an icon, a rack, a server drawing, or any manually rendered
rectangle. Control tooltips can be assigned from Java code or from JSON.

---

## Components

- `Tooltip`: stores text, enabled state, and visual configuration
- `TooltipTarget`: generic target contract with bounds, optional tooltip,
  visibility, and enabled state
- `TooltipArea`: rectangular manual target for arbitrary Processing content
- `TooltipOverlayController`: owns active tooltip overlay registration
- `TooltipInputLayer`: passive input layer that updates the controller without
  consuming pointer events

The host sketch still owns `InputManager` and `OverlayManager`.

---

## Controls

Existing public facades can expose tooltips without changing the common
`Control` interface:

```java
button.setTooltip("Save changes")
      .setTooltipFont(createFont("data/font/JetBrainsMono.ttf", 14))
      .setTooltipBackgroundColor(0xE61B1F26);
```

Register the controls with a shared controller:

```java
TooltipOverlayController tooltips = new TooltipOverlayController(this, overlayManager);
tooltips.registerTarget(button);
tooltips.registerTarget(label);
tooltips.registerTarget(textField);

inputManager.registerLayer(new TooltipInputLayer(1000, tooltips));
```

Draw overlays after drawing controls:

```java
for (OverlayEntry entry : overlayManager.getActiveOverlays()) {
    entry.getRender().run();
}
```

If a control has no tooltip, or its tooltip text is blank, nothing is shown and
the control behavior is unchanged.

---

## Arbitrary Areas

Use `TooltipArea` for content drawn directly in the sketch:

```java
PImage serverImage;
TooltipArea serverArea;

public void setup() {
    serverImage = loadImage("data/img/server.png");

    PFont jetBrainsMono = createFont("data/font/JetBrainsMono.ttf", 14);
    serverArea = new TooltipArea(100, 80, 160, 120)
            .setTooltip("Servidor principal")
            .setTooltipFont(jetBrainsMono)
            .setTooltipBackgroundColor(0xE61B1F26);

    tooltips.registerTarget(serverArea);
}

public void draw() {
    image(serverImage, 100, 80, 160, 120);
}
```

When the drawn region moves, update the bounds:

```java
serverArea.setBounds(x, y, width, height);
```

For dynamic layouts, `TooltipArea` can also be created with a
`Supplier<TooltipBounds>`.

---

## Styling

Tooltip styling supports:

- background color with alpha, including `0xAARRGGBB`
- text color
- border color
- text padding
- offset from the target
- corner radius
- font
- text size

By default, the tooltip appears above the target and is centered horizontally.
If there is not enough room above the target, it appears below. A basic clamp
keeps the tooltip inside the sketch bounds.

---

## JSON

Control JSON entries can include an optional `tooltip` block:

```json
{
  "type": "button",
  "code": "btnSave",
  "text": "Save",
  "x": 120.0,
  "y": 80.0,
  "width": 160.0,
  "height": 44.0,
  "tooltip": {
    "text": "Guardar cambios",
    "enabled": true,
    "style": {
      "backgroundColor": "#E61B1F26",
      "textColor": "#FFFFFFFF",
      "borderColor": "#668A94A6",
      "font": "data/font/JetBrainsMono.ttf",
      "textSize": 14.0,
      "textPadding": 10.0,
      "offset": 10.0,
      "cornerRadius": 8.0
    }
  }
}
```

The JSON block configures the tooltip assigned to the control. It does not
register the control automatically with a `TooltipOverlayController`; the
sketch must still register each target and route pointer events through
`TooltipInputLayer`.

The font path is loaded once when the control is created. Tooltip fonts are not
loaded inside `draw()`.

Arbitrary `TooltipArea` regions are configured from Java in this iteration;
they are not part of `controls[]`.
