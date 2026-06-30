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
PFont jetBrainsMono = createFont("data/font/JetBrainsMono.ttf", 14.0f);

TooltipStyleConfig darkTooltipStyle = new TooltipStyleConfig()
        .setFont(jetBrainsMono)
        .setTextSize(14.0f)
        .setBackgroundColor(0xE61B1F26)
        .setTextColor(0xFFFFFFFF)
        .setBorderColor(0x668A94A6);

button.setTooltip("Button tooltip")
      .setTooltipStyle(darkTooltipStyle);

label.setTooltip("Label tooltip")
     .setTooltipStyle(darkTooltipStyle);
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
    TooltipStyleConfig darkTooltipStyle = new TooltipStyleConfig()
            .setFont(jetBrainsMono)
            .setTextSize(14.0f)
            .setBackgroundColor(0xE61B1F26);

    serverArea = new TooltipArea(100, 80, 160, 120)
            .setTooltip("Servidor principal")
            .setTooltipStyle(darkTooltipStyle);

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
- stroke weight
- font
- text size

`TooltipStyleConfig` is mutable for fluent setup. `Tooltip`, controls, and
`TooltipArea` copy the config when `setStyle(...)` or `setTooltipStyle(...)`
is called, so later edits to a shared preset do not mutate already configured
tooltips.

By default, the tooltip appears above the target and is centered horizontally.
If there is not enough room above the target, it appears below. A basic clamp
keeps the tooltip inside the sketch bounds.

---

## JSON

Control JSON entries can include an optional `tooltip` block. This configures
the tooltip owned by that control entry:

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

Reusable tooltip styles can be defined once at the root of a multi-control
JSON document and referenced with `tooltip.styleRef`:

```json
{
  "tooltipStyles": {
    "dark": {
      "backgroundColor": "#E61B1F26",
      "textColor": "#FFFFFFFF",
      "borderColor": "#668A94A6",
      "font": "data/font/JetBrainsMono.ttf",
      "textSize": 14.0,
      "textPadding": 10.0,
      "cornerRadius": 8.0,
      "offset": 10.0,
      "strokeWeight": 1.0
    }
  },
  "controls": [
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
        "styleRef": "dark"
      }
    }
  ]
}
```

`tooltip.style` can still be used locally. When both `styleRef` and local
`style` exist, the preset is copied first and the local fields override only
the properties they define. An unknown `styleRef` fails JSON loading with an
`IllegalArgumentException`.

The JSON block configures the tooltip assigned to the control. It does not
register the control automatically with a `TooltipOverlayController`; the
sketch must still register each target and route pointer events through
`TooltipInputLayer`.

The font path is loaded during config/control creation. Fonts from
`tooltipStyles` are materialized once per preset and copied into each tooltip;
tooltip fonts are not loaded inside `draw()`.

Standalone tooltip JSON can also be loaded directly:

```java
Tooltip tooltip = TooltipFactory.loadFromJson(
        this,
        "data/config/server-tooltip.json"
);
```

```json
{
  "text": "Servidor principal",
  "enabled": true,
  "style": {
    "backgroundColor": "#E61B1F26",
    "textColor": "#FFFFFFFF",
    "borderColor": "#668A94A6",
    "font": "data/font/JetBrainsMono.ttf",
    "textSize": 14.0
  }
}
```

This standalone path creates a `Tooltip`, not a control. It is useful for
manual targets such as `TooltipArea`, `PImage` regions, icons, server drawings,
or other sketch-owned geometry:

```java
TooltipArea serverArea = new TooltipArea(520, 230, 190, 92)
        .setTooltip(TooltipFactory.loadFromJson(this, "data/config/server-tooltip.json"));
```

`TooltipArea` regions are not part of `controls[]`; the sketch creates and
registers them explicitly.

The dedicated JSON tooltip demo is:

- `src/main/java/com/cpz/processing/controls/examples/tooltip/TooltipVisualJsonTest.java`
- `data/config/tooltip-visual-test.json`
- `data/config/server-tooltip.json`
