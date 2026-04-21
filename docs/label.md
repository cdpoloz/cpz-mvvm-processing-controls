# Label

`Label` is the public facade recommended for the simple non-interactive text label case.

The facade keeps the public API ergonomic and closed, while reusing the existing internal control pipeline of the framework.

---

## Overview

`Label` is intended for static or application-driven text output:

- it renders text without pointer or keyboard interaction
- it exposes a small public API for text, layout, visibility, enabled state, and style
- it exposes the stable control identity through `getCode()`
- it does not register input layers because the control is non-interactive

---

## Basic usage

Programmatic setup:

```java
private Label label;

public void setup() {
    float x = 120f;
    float y = 70f;
    float w = 360f;
    float h = 100f;
    label = new Label(this, "lblPrimary", "Label facade\nprogrammatic example", x, y, w, h);
}
```

The facade creates the internal control composition for you.

The constructor already applies the initial text, position, and reserved size used by the label.

---

## Positioning and size

The public facade exposes position and reserved size directly:

```java
label.setPosition(120f, 70f);
label.setSize(360f, 100f);
```

For `Label`, width and height define the reserved text area used during rendering.

When both values are greater than `0`, the label renders within that box. This is the recommended public path when you want centered or block-based text placement.

---

## Text handling

The facade exposes text through `getText()` and `setText(String)`:

```java
label.setText("Label facade\nprogrammatic example");
String currentText = label.getText();
```

Null text is normalized internally to an empty string.

Multi-line text is supported by passing line breaks in the string.

---

## Visibility and enabled state

The public facade exposes the standard control state methods:

```java
label.setVisible(true);
label.setEnabled(true);
```

- `visible` controls whether the label is drawn
- `enabled` does not add interaction, but it still affects visual resolution such as disabled alpha in the style

Because `Label` is non-interactive, there are no click listeners, change listeners, or input adapters involved in the public API.

---

## Styling

`Label` uses the existing label styling pipeline:

```java
LabelStyleConfig lsc = new LabelStyleConfig();
lsc.textSize = 24.0f;
PFont font = createFont("data/font/abel-regular.ttf", 24);
lsc.font = font;
lsc.textColor = Colors.rgb(210, 228, 255);
lsc.lineSpacingMultiplier = 1.2f;
lsc.alignX = HorizontalAlign.CENTER;
lsc.alignY = VerticalAlign.CENTER;
lsc.disabledAlpha = 80;

label.setStyle(new DefaultLabelStyle(lsc));
```

This configuration controls the visual appearance:

- `textSize` controls font size
- `font` optionally assigns a specific `PFont`
- `textColor` controls the resolved text color
- `lineSpacingMultiplier` controls spacing between lines
- `alignX` controls horizontal alignment
- `alignY` controls vertical alignment
- `disabledAlpha` controls disabled transparency

The sketch owns programmatic resource loading. When a custom font is needed, load it with Processing's `createFont(...)` and assign the resulting `PFont` to `LabelStyleConfig.font`.

If no font is assigned, the label keeps the previous behavior and renders with the default Processing text font for the current sketch.

The facade uses the existing style mechanism. It does not introduce a new styling path.

---

## JSON configuration

`Label` also supports the same minimal config-driven path used by the other closed controls:

```text
JSON -> controls[] -> LabelConfigLoader -> LabelConfig -> LabelFactory -> Label facade
```

Minimal JSON:

```json
{
  "code": "lblJsonTest",
  "text": "Label facade\nJSON example",
  "x": 120.0,
  "y": 70.0,
  "width": 360.0,
  "height": 100.0,
  "enabled": true,
  "visible": true
}
```

JSON style can also provide a custom font path:

```json
"style": {
  "textSize": 24.0,
  "font": "data/font/abel-regular.ttf",
  "textColor": "#D2E4FF",
  "lineSpacingMultiplier": 1.2,
  "alignX": "center",
  "alignY": "center",
  "disabledAlpha": 80
}
```

In the JSON flow, `LabelConfigLoader` reads the `font` path and `LabelFactory` materializes it with `sketch.createFont(...)`. The size passed to `createFont(...)` is `textSize` when present, or `12.0f` otherwise. If `font` is omitted or `null`, the previous default-font behavior is preserved.

Minimal Java sketch flow:

```java
private static final String LABEL_CONFIG_PATH = "data" + File.separator + "config" + File.separator + "label-test.json";

private Label label;

public void setup() {
    LabelConfigLoader loader = new LabelConfigLoader(this);
    LabelConfig config = loader.load(LABEL_CONFIG_PATH);
    label = LabelFactory.create(this, config);
}
```

The current JSON validation is intentionally small and explicit:

- `code` is required
- `text` defaults to `""` when omitted
- `width` must be greater than `0`
- `height` must be greater than `0`

For simple single-control examples, `LabelConfigLoader` accepts the wrapped `controls` document when it contains exactly one `label` entry. Binding and listeners still remain outside JSON.

---

## SVG support

`Label` does not provide SVG renderer support in the current iteration.

Unlike `Button`, `Checkbox`, and `Slider`, the label control currently uses the existing text renderer path only. There is no `label-svg.md` document because no separate SVG configuration path exists for this control at this stage.

---

## Full example

```java
import com.cpz.processing.controls.controls.label.Label;
import com.cpz.processing.controls.controls.label.config.LabelStyleConfig;
import com.cpz.processing.controls.controls.label.style.DefaultLabelStyle;
import com.cpz.processing.controls.controls.label.style.HorizontalAlign;
import com.cpz.processing.controls.controls.label.style.VerticalAlign;
import com.cpz.utils.color.Colors;
import processing.core.PApplet;
import processing.core.PFont;

public class LabelTest extends PApplet {
    private Label label;

    public void settings() {
        size(600, 260);
        smooth(8);
    }

    public void setup() {
        float x = 120f;
        float y = 70f;
        float w = 360f;
        float h = 100f;
        label = new Label(this, "lblTest", "Label facade\nprogrammatic example", x, y, w, h);
        // style
        LabelStyleConfig lsc = new LabelStyleConfig();
        lsc.textSize = 24.0f;
        PFont font = createFont("data/font/abel-regular.ttf", 24);
        lsc.font = font;
        lsc.textColor = Colors.rgb(210, 228, 255);
        lsc.lineSpacingMultiplier = 1.2f;
        lsc.alignX = HorizontalAlign.CENTER;
        lsc.alignY = VerticalAlign.CENTER;
        lsc.disabledAlpha = 80;
        label.setStyle(new DefaultLabelStyle(lsc));
    }

    public void draw() {
        background(28);
        label.draw();
        fill(180);
        textAlign(CENTER, CENTER);
        text(label.getCode() + " | non-interactive label", 300, 215);
    }
}
```

---

## Notes / limitations

- `Label` is non-interactive and does not use `InputManager`
- the facade does not expose `getView()` or `getViewModel()`
- the public API is intentionally small and aligned with the other closed control facades
- SVG is not part of the label public surface in this iteration

---

## See also

- [JSON Configuration](json-configuration.md)
- [Architecture](architecture.md)
