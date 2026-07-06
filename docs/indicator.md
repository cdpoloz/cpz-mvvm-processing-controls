# Indicator

`Indicator` is a public non-interactive LED-style control for displaying an
application-driven on/off state.

It implements `Control`, `ParentSizeAwareControl`, and `TooltipAttachable`. It
does not implement `PointerRoutableControl` or `KeyboardRoutableControl`
because this first version does not consume pointer or keyboard input.

---

## Purpose

Use `Indicator` when a sketch needs a simple status light:

- connection active or inactive
- hardware or service state
- validation status
- application state that is changed from code

`Indicator` is not a button and does not toggle itself. The sketch changes its
state programmatically with `setOn(boolean)`.

---

## Basic API

Absolute setup:

```java
Indicator indicator = new Indicator(this, "indServer", 40, 40, 24, 24);

indicator.setOn(true);
boolean on = indicator.isOn();

indicator.setOnColor(0xFF2ECC71);
indicator.setOffColor(0xFF30343A);
indicator.setStrokeColor(0xFFFFFFFF);
indicator.setStrokeWeight(2.0F);

indicator.setTooltip("Server status");
indicator.setTooltipText("Server online");
```

The same absolute values can be passed through `ControlBounds.absolute(...)`:

```java
Indicator absoluteBoundsIndicator = new Indicator(this, "indAbsolute",
        ControlBounds.absolute(40, 40, 24, 24));
```

Relative setup:

```java
Indicator relativeIndicator = new Indicator(this, "indRelative",
        ControlBounds.relative(0.1f, 0.1f, 0.05f, 0.05f));

Indicator mixedIndicator = new Indicator(this, "indMixed",
        ControlBounds.of(
                ControlMeasure.relative(0.1f),
                ControlMeasure.relative(0.1f),
                ControlMeasure.absolute(24.0f),
                ControlMeasure.absolute(24.0f)));
```

SVG setup uses constructor overloads with a path:

```java
Indicator svgIndicator = new Indicator(this, "indSvg",
        ControlBounds.relative(0.1f, 0.1f, 0.08f, 0.08f),
        "data/img/test.svg");
```

Minimum public state API:

```java
boolean isOn();
void setOn(boolean on);
int getOnColor();
void setOnColor(int color);
int getOffColor();
void setOffColor(int color);
int getStrokeColor();
void setStrokeColor(int color);
float getStrokeWeight();
void setStrokeWeight(float weight);
```

The control also supports the common `Control` methods for code, drawing,
visibility, enabled state, and position.

---

## Geometry

The absolute constructor uses top-left logical bounds:

```java
Indicator indicator = new Indicator(this, 40, 40, 24, 24);
```

The LED is drawn as a circle centered inside the full bounds rectangle. The
circle diameter is `min(width, height)`. The full bounds rectangle remains the
logical area used for layout and tooltips.

Relative bounds use the same `ControlBounds` rules introduced for other
relative-aware controls:

```java
Indicator indicator = new Indicator(this, "indStatus",
        ControlBounds.relative(0.1f, 0.1f, 0.05f, 0.05f));
```

Resolution rules:

- relative `x` uses `parentWidth * factor`
- relative `y` uses `parentHeight * factor`
- relative `width` uses `parentHeight * factor`
- relative `height` uses `parentHeight * factor`

Root indicators resolve against the sketch canvas. Indicators added to a
`Panel` resolve against the panel's resolved width and height.

`setPosition(x, y)` makes only the position absolute and preserves the current
size measures. `setSize(width, height)` makes only the size absolute and
preserves the current position measures.

---

## SVG

`Indicator` can render an SVG instead of the default LED circle:

```java
Indicator svgIndicator = new Indicator(this, "indSvg", 40, 40, 32, 32,
        "data/img/test.svg");

svgIndicator.setOn(true);
svgIndicator.setOnColor(0xFF2ECC71);
svgIndicator.setOffColor(0xFF30343A);
```

The SVG is loaded once when the indicator is created. The path resolution
matches the existing Button/Toggle SVG renderers: first the configured path is
loaded, then a second attempt is made without the leading `data/` prefix when
the original path starts with `data/`.

The SVG is drawn centered and scaled to the full indicator bounds with
Processing `shapeMode(CENTER)`, matching the Button/Toggle SVG behavior. The
full bounds remain the logical area for layout and tooltips.

Color follows the existing SVG renderer convention: the SVG's internal style is
disabled and the current indicator state controls the fill. `on=true` uses
`onColor`; `on=false` uses `offColor`. The simple indicator border color is
used as stroke.

If the SVG cannot be loaded, the SVG renderer path is kept consistent with
Button/Toggle: no exception is thrown and no SVG is drawn. Indicators without
an SVG path continue to use the default circular LED rendering.

---

## Stroke

`Indicator` exposes a configurable visual stroke for the circular LED and SVG
rendering modes:

```java
indicator.setStrokeColor(0xFFFFFFFF);
indicator.setStrokeWeight(2.0F);
```

The defaults are:

- `strokeColor = 0xFF1F2328`
- `strokeWeight = 1.0F`

`setStrokeWeight(...)` clamps negative values to `0.0F`. A stroke weight of
`0.0F` disables the stroke with Processing `noStroke()`.

The JSON property names follow Button's style naming:

```json
{
  "type": "indicator",
  "code": "indStatus",
  "x": 40,
  "y": 40,
  "width": 24,
  "height": 24,
  "style": {
    "strokeColor": "#FFFFFFFF",
    "strokeWeight": 2.0
  }
}
```

`style.strokeWeight` is the canonical Indicator JSON key. `style.strokeWidth`
is also accepted as an alias when `strokeWeight` is absent, for compatibility
with Toggle terminology. Do not use `borderColor` for the Indicator visual
border; `borderColor` belongs to tooltip styles.

---

## Visibility And Enabled State

`visible=false` prevents drawing and hides the indicator as a tooltip target.

`enabled=false` does not change the logical `on` state and does not block
tooltips. In this first version, `Indicator` keeps the same simple visual
appearance when disabled because there is no dedicated indicator style or
interaction state.

---

## Tooltip

`Indicator` implements `TooltipAttachable` from the first version. Tooltip
style can be configured with the same `TooltipStyleConfig` used by other
controls:

```java
TooltipStyleConfig tooltipStyle = new TooltipStyleConfig()
        .setBackgroundColor(0xF21B1F26)
        .setTextColor(0xFFFFFFFF)
        .setBorderColor(0xFF8A94A6);

Indicator indicator = new Indicator(this, "indStatus", 40, 40, 24, 24)
        .setTooltip("Status")
        .setTooltipStyle(tooltipStyle);

indicator.setTooltipText("Status changed at runtime");
```

When the indicator is a child of a `Panel`, register the panel-adjusted tooltip
target:

```java
panel.add(indicator);
tooltips.registerTarget(panel.tooltipTarget(indicator));
```

---

## JSON

Legacy absolute geometry is supported:

```json
{
  "type": "indicator",
  "code": "indServer",
  "x": 40,
  "y": 40,
  "width": 24,
  "height": 24,
  "on": true,
  "onColor": "#FF2ECC71",
  "offColor": "#FF30343A",
  "style": {
    "strokeColor": "#FFFFFFFF",
    "strokeWeight": 2.0
  },
  "tooltip": "Server status"
}
```

Relative bounds are also supported:

```json
{
  "type": "indicator",
  "code": "indServer",
  "bounds": {
    "mode": "relative",
    "x": 0.1,
    "y": 0.1,
    "width": 0.05,
    "height": 0.05
  },
  "on": false,
  "onColor": "#FF2ECC71",
  "offColor": "#FF30343A",
  "style": {
    "strokeColor": "#FFFFFFFF",
    "strokeWeight": 2.0
  },
  "tooltip": {
    "text": "Server status",
    "styleRef": "indicatorDark"
  }
}
```

SVG JSON uses the same renderer block used by Button and Toggle:

```json
{
  "type": "indicator",
  "code": "indSvg",
  "bounds": {
    "mode": "relative",
    "x": 0.1,
    "y": 0.1,
    "width": 0.08,
    "height": 0.08
  },
  "on": true,
  "onColor": "#FF2ECC71",
  "offColor": "#FF30343A",
  "style": {
    "strokeColor": "#FFFFFFFF",
    "strokeWeight": 2.0,
    "renderer": {
      "type": "svg",
      "path": "data/img/test.svg"
    }
  },
  "tooltip": "SVG indicator"
}
```

Rules:

- `bounds` takes precedence over legacy `x` / `y` / `width` / `height`
- `mode` accepts `absolute` and `relative`, trimmed and case-insensitive
- missing or invalid `mode` fails like the other relative-aware controls
- `on` defaults to `false`
- `onColor` defaults to green
- `offColor` defaults to dark gray
- colors use the shared parser: integer, `#RRGGBB`, or `#AARRGGBB`
- `style.strokeColor` configures the visual border color
- `style.strokeWeight` configures the visual border width
- `style.strokeWidth` is accepted as an alias only when `strokeWeight` is absent
- `tooltip` uses the same object block as other tooltip-capable controls
- `tooltip` may also be a string shorthand for tooltip text
- SVG uses `style.renderer.type = "svg"` and `style.renderer.path`

For consistency with the existing style-oriented JSON shape, `style.onColor`
and `style.offColor` are accepted as aliases. Top-level `onColor` and
`offColor` take precedence when both forms are present.

---

## Limitations

`Indicator` is intentionally non-interactive in this version:

- no pointer or keyboard routing
- no click listeners
- no `toggle()` as primary API
- no blinking, glow, animation, or shape variants
- no `Panel.children` JSON composition in this release

The visual example is:

```text
src/main/java/com/cpz/processing/controls/examples/indicator/IndicatorTest.java
```

The JSON example is:

```text
src/main/java/com/cpz/processing/controls/examples/indicator/IndicatorJsonTest.java
data/config/indicator.json
```

The SVG examples are:

```text
src/main/java/com/cpz/processing/controls/examples/indicator/IndicatorSvgTest.java
src/main/java/com/cpz/processing/controls/examples/indicator/IndicatorSvgJsonTest.java
data/config/indicator-svg.json
```
