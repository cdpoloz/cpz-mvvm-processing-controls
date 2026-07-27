# ProgressBar

`ProgressBar` is a public non-interactive control for displaying an
application-driven numeric progress value.

This document describes the control as available in the current `0.9.11`
release line.

It implements `Control`, `ParentSizeAwareControl`, and `TooltipAttachable`. It
does not implement `PointerRoutableControl` or `KeyboardRoutableControl`
because the current implementation does not consume pointer or keyboard input.

---

## Purpose

Use `ProgressBar` when a sketch needs to show determinate progress:

- loading or export progress
- task completion
- capacity or quota usage
- an application value normalized to a visual range

`ProgressBar` is not interactive. The sketch updates its value with
`setValue(float)`. Use `ProgressBar` instead of `Slider` when no input should
be consumed.

---

## Basic API

```java
ProgressBar progressBar = new ProgressBar(this, "pbLoad", 40, 40, 240, 20);
ProgressBarStyle style = new ProgressBarStyle()
        .setTrackColor(0xFF30343A)
        .setFillColor(0xFF2F80ED)
        .setStrokeColor(0xFFFFFFFF)
        .setStrokeWeight(1.5F)
        .setFillDirection(ProgressBarFillDirection.LEFT_TO_RIGHT);

progressBar.setStyle(style);
progressBar.setRange(0.0F, 100.0F);
progressBar.setValue(35.0F);

float value = progressBar.getValue();
float progress = progressBar.getProgress();

progressBar.setTooltip("Loading");
```

The same geometry can be passed through `ControlBounds`:

```java
ProgressBar relativeBar = new ProgressBar(this, "pbRelative",
        ControlBounds.relative(0.1F, 0.1F, 0.5F, 0.04F));
```

Minimum public value and style API:

```java
float getValue();
void setValue(float value);
float getMin();
void setMin(float min);
float getMax();
void setMax(float max);
void setRange(float min, float max);
float getProgress();
ProgressBarFillDirection getFillDirection();
void setFillDirection(ProgressBarFillDirection direction);
int getTrackColor();
void setTrackColor(int color);
int getFillColor();
void setFillColor(int color);
int getStrokeColor();
void setStrokeColor(int color);
float getStrokeWeight();
void setStrokeWeight(float weight);
ProgressBarStyle getStyle();
void setStyle(ProgressBarStyle style);
```

---

## Value Policy

Defaults:

- `min = 0.0F`
- `max = 1.0F`
- `value = 0.0F`

`setValue(...)` clamps the stored value to the current `[min, max]` range.
It rejects `NaN` and either infinity before changing the stored value.

`getProgress()` returns a normalized value from `0.0F` to `1.0F`.

If `setRange(min, max)` receives `min > max`, the values are sorted
automatically. This keeps JSON and runtime setup tolerant while preserving a
predictable stored range.

`setRange(...)`, `setMin(...)`, and `setMax(...)` reject non-finite endpoints
before changing the range. JSON `min`, `max`, and `value` follow the same
finite-value contract.

If `min == max`, `getProgress()` avoids division by zero and returns `1.0F`
when `value >= max`, otherwise `0.0F`. Since values are clamped to the single
point in the range, normal use reports full progress for an equal range.

---

## Geometry

The absolute constructor uses top-left logical bounds:

```java
ProgressBar progressBar = new ProgressBar(this, 40, 40, 240, 20);
```

The full bounds rectangle is the logical area used for layout and tooltips.
The fill direction determines whether the bar is horizontal or vertical. The
full bounds are used for both orientations.

Relative bounds use the same `ControlBounds` rules as other relative-aware
controls:

- relative `x` uses `parentWidth * factor`
- relative `y` uses `parentHeight * factor`
- relative `width` uses `parentHeight * factor`
- relative `height` uses `parentHeight * factor`

Root progress bars resolve against the sketch canvas. Progress bars added to a
`Panel` resolve against the panel's resolved width and height.

`setPosition(x, y)` makes only the position absolute and preserves the current
size measures. `setSize(width, height)` makes only the size absolute and
preserves the current position measures.

---

## Rendering

Rendering order:

1. Track/background rectangle.
2. Fill rectangle according to `getProgress()`.
3. Optional outer stroke.

The defaults are:

- `trackColor = 0xFF30343A`
- `fillColor = 0xFF2F80ED`
- `strokeColor = 0xFF1F2328`
- `strokeWeight = 1.0F`
- `fillDirection = LEFT_TO_RIGHT`

These values are stored in the control's `ProgressBarStyle`. The direct color
and stroke setters remain available and update the current style object.
Passing `null` to `setStyle(...)` restores a default style.

Recommended style setup:

```java
ProgressBarStyle style = new ProgressBarStyle()
        .setTrackColor(0xFF30343A)
        .setFillColor(0xFF2F80ED)
        .setStrokeColor(0xFFFFFFFF)
        .setStrokeWeight(1.5F)
        .setFillDirection(ProgressBarFillDirection.LEFT_TO_RIGHT);

progressBar.setStyle(style);
```

Supported fill directions:

- `LEFT_TO_RIGHT`: horizontal fill from left to right
- `RIGHT_TO_LEFT`: horizontal fill from right to left
- `BOTTOM_TO_TOP`: vertical fill from bottom to top
- `TOP_TO_BOTTOM`: vertical fill from top to bottom

`setStrokeWeight(...)` clamps negative values to `0.0F`. A stroke weight of
`0.0F` disables the outer stroke with Processing `noStroke()`.

No animation, text, stripes, indeterminate state, or events are included in
this version.

---

## Visibility And Enabled State

`visible=false` prevents drawing and hides the progress bar as a tooltip
target.

`enabled=false` does not change the logical value and does not block tooltips.
In the current implementation, `ProgressBar` keeps the same simple visual appearance
when disabled because it is non-interactive. Programmatic updates still apply
normally while disabled, including `setValue(...)`, `setRange(...)`, and the
derived `getProgress()` result.

---

## JSON

Legacy absolute geometry is supported:

```json
{
  "type": "progressbar",
  "code": "pbLoad",
  "x": 40,
  "y": 40,
  "width": 240,
  "height": 20,
  "value": 0.35
}
```

Explicit bounds can be absolute or relative. `bounds` takes precedence over
legacy `x` / `y` / `width` / `height`:

```json
{
  "type": "progressbar",
  "code": "pbLoad",
  "bounds": {
    "mode": "relative",
    "x": 0.1,
    "y": 0.1,
    "width": 0.5,
    "height": 0.04
  },
  "min": 0.0,
  "max": 100.0,
  "value": 35.0,
  "trackColor": "#FF30343A",
  "fillColor": "#FF2F80ED",
  "style": {
    "strokeColor": "#FFFFFFFF",
    "strokeWeight": 1.5,
    "fillDirection": "left-to-right"
  },
  "tooltip": "Loading"
}
```

Supported properties:

- `type`: must be `"progressbar"`
- `code`: unique control code
- `x`, `y`, `width`, `height`: legacy absolute bounds
- `bounds`: explicit `absolute` or `relative` bounds object
- `min`: optional minimum, default `0.0`
- `max`: optional maximum, default `1.0`
- `value`: optional value, default `0.0`
- `trackColor`: optional track color
- `fillColor`: optional fill color
- `style.trackColor` / `style.fillColor`: optional color aliases
- `style.strokeColor`: optional outer stroke color
- `style.strokeWeight`: optional outer stroke width; `0` disables the stroke
- `style.fillDirection`: optional fill direction, default `"left-to-right"`
- `fillDirection`: top-level fill direction alias; takes precedence over `style.fillDirection`
- `enabled`: optional enabled flag, default `true`
- `visible`: optional visible flag, default `true`
- `tooltip`: optional tooltip object or text shorthand

`style.strokeWeight` is the canonical border-width property. `borderColor` is
not a `ProgressBar` visual border property; it belongs to tooltip style blocks.
Supported JSON fill direction values are `"left-to-right"`,
`"right-to-left"`, `"bottom-to-top"`, and `"top-to-bottom"`. Values are trimmed
and case-insensitive; hyphen, underscore, and space separators are accepted.
Invalid values use the default `LEFT_TO_RIGHT` direction.

`min`, `max`, and `value` must be finite. Finite values keep the runtime range
sorting and value clamping behavior described above.

---

## Examples

- `src/main/java/com/cpz/processing/controls/examples/progressbar/ProgressBarTest.java`
- `src/main/java/com/cpz/processing/controls/examples/progressbar/ProgressBarJsonTest.java`
- `data/config/progressbar.json`

---

## See Also

- [Control](control.md)
- [Panel](panel.md)
- [JSON Configuration](json-configuration.md)
- [Tooltip](tooltip.md)
