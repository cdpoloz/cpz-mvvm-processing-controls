# Button (PNG)

This tutorial shows how to use a PNG alpha mask as the complete visual shape of
a `Button`.

For the base control architecture and input pipeline, see [Button](button.md).
For the vector variant, see [Button (SVG)](button-svg.md).

---

## Rendering contract

`PngButtonRenderer` follows the same renderer-complete model as
`SvgButtonRenderer`:

- the PNG replaces the default rectangular button shape;
- it is not a separate icon and no rectangle is drawn behind it;
- the button text is drawn afterwards, centered on top of the PNG;
- the button's existing style resolves normal, hover, pressed, and disabled
  colors before the renderer runs.

The PNG is interpreted as an alpha mask. Its original alpha channel is
preserved, its RGB channels are discarded, and `tint(...)` applies the resolved
`ButtonRenderStyle.fillColor()` for the current frame. A colored source PNG and
a white source PNG therefore produce the same button colors when their alpha
channels match.

The image is scaled uniformly to fit completely inside the button bounds and is
centered without cropping or deformation. `strokeColor`, `strokeWeight`, and
`cornerRadius` do not affect the PNG renderer.

---

## Runtime setup

```java
Button button = new Button(this, "btnPng", "PNG Button", 210, 130, 220, 120);

ButtonStyleConfig style = new ButtonStyleConfig();
style.baseColor = Colors.rgb(48, 98, 219);
style.textColor = Colors.gray(255);
style.disabledAlpha = 90;
style.hoverBlendWithWhite = 0.12F;
style.pressedBlendWithBlack = 0.25F;
style.setRenderer(new PngButtonRenderer(this, "data/img/button-mask.png"));

button.setStyle(new DefaultButtonStyle(style));
```

The facade does not need a PNG-specific constructor or renderer setter. Runtime
selection remains part of `ButtonStyleConfig`, exactly as for SVG.

`PngButtonRenderer` loads and normalizes the resource once in its constructor.
A `null` or empty runtime path creates an inert renderer. If Processing cannot
load the configured resource, no PNG shape is drawn; the renderer does not fall
back to the default rectangle. Text remains part of the normal button text path.

---

## JSON setup

```json
{
  "controls": [
    {
      "type": "button",
      "code": "btnPng",
      "text": "PNG Button",
      "x": 210.0,
      "y": 130.0,
      "width": 220.0,
      "height": 120.0,
      "style": {
        "baseColor": "#3062DB",
        "textColor": "#FFFFFF",
        "disabledAlpha": 90,
        "hoverBlendWithWhite": 0.12,
        "pressedBlendWithBlack": 0.25,
        "renderer": {
          "type": "png",
          "path": "data/img/button-mask.png"
        }
      }
    }
  ]
}
```

`style.renderer.type` selects the renderer. Button renderer selection does not
infer or validate the file extension, so `type` remains the source of truth.
Type matching is trimmed and case-insensitive. JSON paths must be non-empty.

---

## State colors

The PNG renderer introduces no separate palette:

| State | PNG tint source |
| --- | --- |
| normal | resolved base fill color |
| hover | existing lightened hover fill |
| pressed | existing darkened pressed fill |
| disabled | existing base fill with disabled alpha |

Pressed continues to take precedence over hover. Disabling a button clears its
transient hover and pressed state through the existing input/ViewModel flow.

---

## Examples

Runtime:

```text
src/main/java/com/cpz/processing/controls/examples/button/ButtonPngTest.java
```

JSON:

```text
src/main/java/com/cpz/processing/controls/examples/button/ButtonPngJsonTest.java
data/config/button-png-test.json
```

Shared example mask:

```text
data/img/button-mask.png
```
