# Slider (SVG)

This tutorial shows the SVG variant of the public `Slider` API using the existing slider thumb rendering path.

For the base control architecture, input pipeline, and recommended non-SVG setup, see [slider.md](slider.md).

---

## What changes when using SVG

The control architecture and input flow stay the same.

The only change is the thumb configuration applied through the slider style:

- the slider is still created through the public `Slider` facade
- input is still routed through `InputManager` and `SliderInputLayer`
- visuals are still resolved by `SliderStyle` and drawn by `SliderRenderer`
- the thumb is rendered from an SVG shape instead of the fallback ellipse path

So this document only focuses on the SVG-specific part of the setup. The public API remains identical to the base slider example.

---

## Programmatic setup

### 1. Create the slider

```java
private InputManager inputManager;
private Slider slider;

public void setup() {
    float x = 300f;
    float y = 130f;
    float w = 320f;
    float h = 72f;
    slider = new Slider(
            this,
            "sldSvgTest",
            new BigDecimal("0"),
            new BigDecimal("1"),
            new BigDecimal("0.05"),
            new BigDecimal("0.35"),
            x,
            y,
            w,
            h
    );
}
```

This is the same public setup used in the regular slider example.

---

### 2. Assign the change action

```java
slider.setChangeListener(value -> {
    System.out.println("Slider value = " + value);
});
```

---

### 3. Configure the style and SVG thumb

```java
SliderStyleConfig ssc = new SliderStyleConfig();
ssc.trackColor = Colors.rgb(62, 72, 86);
ssc.trackHoverColor = Colors.rgb(86, 98, 116);
ssc.trackPressedColor = Colors.rgb(44, 52, 64);
ssc.trackStrokeColor = Colors.gray(220);
ssc.trackStrokeWeight = 1.5f;
ssc.trackStrokeWeightHover = 2.5f;
ssc.trackThickness = 10.0f;
ssc.activeTrackColor = Colors.rgb(56, 159, 232);
ssc.activeTrackHoverColor = Colors.rgb(98, 184, 244);
ssc.activeTrackPressedColor = Colors.rgb(38, 124, 184);
ssc.thumbColor = Colors.gray(255);
ssc.thumbHoverColor = Colors.rgb(198, 234, 255);
ssc.thumbPressedColor = Colors.rgb(170, 220, 255);
ssc.thumbStrokeColor = Colors.rgb(32, 78, 120);
ssc.thumbStrokeWeight = 2.0f;
ssc.thumbStrokeWeightHover = 3.0f;
ssc.thumbSize = 28.0f;
ssc.textColor = Colors.gray(245);
ssc.disabledAlpha = 90;
ssc.showValueText = true;
ssc.svgColorMode = SvgColorMode.USE_RENDER_STYLE;
ssc.thumbShape = loadShape("data" + File.separator + "img" + File.separator + "test.svg");

slider.setStyle(new SliderStyle(ssc));
```

This is the key SVG-specific step.

The cross-platform asset path matches the example sketch:

`"data" + File.separator + "img" + File.separator + "test.svg"`

In the slider control, SVG support applies to the thumb only.

The same internal slider pipeline is reused in both cases:

- programmatic setup assigns `thumbShape` and `svgColorMode` directly in `SliderStyleConfig`
- JSON setup uses the `renderer` block and the optional `svgColorMode` field described below

Both approaches end in the same internal `SliderStyle` and `SliderRenderer` behavior. SVG is not a separate control architecture.

---

## JSON setup

The config-driven route keeps the same public flow:

```text
JSON → SliderConfigLoader → SliderConfig → SliderFactory → Slider facade → MVVM internals
```

Minimal SVG JSON:

```json
{
  "code": "sldSvgJsonTest",
  "min": 0.0,
  "max": 1.0,
  "step": 0.05,
  "value": 0.35,
  "x": 300.0,
  "y": 130.0,
  "width": 320.0,
  "height": 72.0,
  "orientation": "horizontal",
  "snapMode": "always",
  "enabled": true,
  "visible": true,
  "style": {
    "trackColor": "#3E4856",
    "trackHoverColor": "#566274",
    "trackPressedColor": "#2C3440",
    "trackStrokeColor": "#DCDCDC",
    "trackStrokeWeight": 1.5,
    "trackStrokeWeightHover": 2.5,
    "trackThickness": 10.0,
    "activeTrackColor": "#389FE8",
    "activeTrackHoverColor": "#62B8F4",
    "activeTrackPressedColor": "#267CB8",
    "thumbColor": "#FFFFFF",
    "thumbHoverColor": "#C6EAFF",
    "thumbPressedColor": "#AADCFF",
    "thumbStrokeColor": "#204E78",
    "thumbStrokeWeight": 2.0,
    "thumbStrokeWeightHover": 3.0,
    "thumbSize": 28.0,
    "textColor": "#F5F5F5",
    "disabledAlpha": 90,
    "showValueText": true,
    "svgColorMode": "use_render_style",
    "renderer": {
      "type": "svg",
      "path": "data/img/test.svg"
    }
  }
}
```

The `renderer` block is the JSON-side declaration of the same thumb SVG resource.

Supported slider SVG fields in the current iteration:

- `style.renderer.type`
- `style.renderer.path`
- `style.svgColorMode`

`svgColorMode` accepts:

- `use_render_style`
- `use_svg_original`

`use_render_style` recolors the SVG thumb using the resolved slider thumb fill and stroke.

`use_svg_original` preserves the colors defined inside the SVG itself.

---

## Full examples

### 1. Programmatic SVG slider

```java
import com.cpz.processing.controls.controls.slider.Slider;
import com.cpz.processing.controls.controls.slider.config.SliderStyleConfig;
import com.cpz.processing.controls.controls.slider.input.SliderInputLayer;
import com.cpz.processing.controls.controls.slider.style.SliderStyle;
import com.cpz.processing.controls.controls.slider.style.SvgColorMode;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.processing.controls.core.util.Colors;
import processing.core.PApplet;
import processing.core.PShape;
import processing.event.MouseEvent;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class SliderSvgTest extends PApplet {
    private InputManager inputManager;
    private Slider slider;
    private String currentValue;

    public void settings() {
        size(600, 340);
        smooth(8);
    }

    public void setup() {
        float x = 300f;
        float y = 130f;
        float w = 320f;
        float h = 72f;
        slider = new Slider(
                this,
                "sldSvgTest",
                new BigDecimal("0"),
                new BigDecimal("1"),
                new BigDecimal("0.05"),
                new BigDecimal("0.35"),
                x,
                y,
                w,
                h
        );
        slider.setFormatter(value -> value.setScale(2, RoundingMode.HALF_UP).toPlainString());
        slider.setChangeListener(value -> currentValue = slider.getFormattedValue());
        currentValue = slider.getFormattedValue();
        // style
        SliderStyleConfig ssc = new SliderStyleConfig();
        ssc.trackColor = Colors.rgb(62, 72, 86);
        ssc.trackHoverColor = Colors.rgb(86, 98, 116);
        ssc.trackPressedColor = Colors.rgb(44, 52, 64);
        ssc.trackStrokeColor = Colors.gray(220);
        ssc.trackStrokeWeight = 1.5f;
        ssc.trackStrokeWeightHover = 2.5f;
        ssc.trackThickness = 10.0f;
        ssc.activeTrackColor = Colors.rgb(56, 159, 232);
        ssc.activeTrackHoverColor = Colors.rgb(98, 184, 244);
        ssc.activeTrackPressedColor = Colors.rgb(38, 124, 184);
        ssc.thumbColor = Colors.gray(255);
        ssc.thumbHoverColor = Colors.rgb(198, 234, 255);
        ssc.thumbPressedColor = Colors.rgb(170, 220, 255);
        ssc.thumbStrokeColor = Colors.rgb(32, 78, 120);
        ssc.thumbStrokeWeight = 2.0f;
        ssc.thumbStrokeWeightHover = 3.0f;
        ssc.thumbSize = 28.0f;
        ssc.textColor = Colors.gray(245);
        ssc.disabledAlpha = 90;
        ssc.showValueText = true;
        ssc.svgColorMode = SvgColorMode.USE_RENDER_STYLE;
        ssc.thumbShape = loadThumbShape("data" + File.separator + "img" + File.separator + "test.svg");
        slider.setStyle(new SliderStyle(ssc));
        // input manager
        inputManager = new InputManager();
        inputManager.registerLayer(new SliderInputLayer(0, slider));
        // text output
        textAlign(CENTER, CENTER);
    }

    public void draw() {
        background(28);
        slider.draw();
        text(slider.getCode() + " | value = " + currentValue, 300, 240);
        text("SVG thumb rendered through the same slider pipeline", 300, 275);
    }

    public void mouseMoved() {
        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.MOVE, (float) mouseX, (float) mouseY, mouseButton));
    }

    public void mouseDragged() {
        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.DRAG, (float) mouseX, (float) mouseY, mouseButton));
    }

    public void mousePressed() {
        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.PRESS, (float) mouseX, (float) mouseY, mouseButton));
    }

    public void mouseReleased() {
        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.RELEASE, (float) mouseX, (float) mouseY, mouseButton));
    }

    public void mouseWheel(MouseEvent event) {
        inputManager.dispatchPointer(new PointerEvent(
                PointerEvent.Type.WHEEL,
                (float) mouseX,
                (float) mouseY,
                mouseButton,
                (float) event.getCount(),
                event.isShiftDown(),
                event.isControlDown()
        ));
    }

    private PShape loadThumbShape(String path) {
        PShape shape = loadShape(path);
        if (shape == null && path.startsWith("data/")) {
            shape = loadShape(path.substring("data/".length()));
        }
        return shape;
    }
}
```

---

### 2. SVG slider from JSON

```java
import com.cpz.processing.controls.controls.slider.Slider;
import com.cpz.processing.controls.controls.slider.SliderFactory;
import com.cpz.processing.controls.controls.slider.config.SliderConfig;
import com.cpz.processing.controls.controls.slider.config.SliderConfigLoader;
import com.cpz.processing.controls.controls.slider.input.SliderInputLayer;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.PointerEvent;
import processing.core.PApplet;
import processing.event.MouseEvent;

import java.io.File;

public class SliderSvgJsonTest extends PApplet {
    private static final String SLIDER_CONFIG_PATH = "data" + File.separator + "config" + File.separator + "slider-svg-test.json";

    private InputManager inputManager;
    private Slider slider;
    private String currentValue;

    public void settings() {
        size(600, 340);
        smooth(8);
    }

    public void setup() {
        SliderConfigLoader loader = new SliderConfigLoader(this);
        SliderConfig config = loader.load(SLIDER_CONFIG_PATH);
        slider = SliderFactory.create(this, config);
        slider.setChangeListener(value -> currentValue = slider.getFormattedValue());
        currentValue = slider.getFormattedValue();
        // input manager
        inputManager = new InputManager();
        inputManager.registerLayer(new SliderInputLayer(0, slider));
        // text output
        textAlign(CENTER, CENTER);
    }

    public void draw() {
        background(28);
        slider.draw();
        text(slider.getCode() + " | value = " + currentValue, 300, 240);
        text("SVG thumb loaded from JSON through the same slider style path", 300, 275);
    }

    public void mouseMoved() {
        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.MOVE, (float) mouseX, (float) mouseY, mouseButton));
    }

    public void mouseDragged() {
        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.DRAG, (float) mouseX, (float) mouseY, mouseButton));
    }

    public void mousePressed() {
        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.PRESS, (float) mouseX, (float) mouseY, mouseButton));
    }

    public void mouseReleased() {
        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.RELEASE, (float) mouseX, (float) mouseY, mouseButton));
    }

    public void mouseWheel(MouseEvent event) {
        inputManager.dispatchPointer(new PointerEvent(
                PointerEvent.Type.WHEEL,
                (float) mouseX,
                (float) mouseY,
                mouseButton,
                (float) event.getCount(),
                event.isShiftDown(),
                event.isControlDown()
        ));
    }
}
```

---

## Notes

- slider SVG support reuses the existing slider renderer instead of introducing a separate SVG control variant
- the SVG asset is applied to the thumb only
- simple, lightweight SVG assets usually give the most predictable results
- Processing's SVG rendering behavior still applies because the thumb shape ultimately relies on Processing shape support
- the same SVG setup is also available through JSON configuration, see [JSON Configuration](json-configuration.md)

---

## See also

- [Slider](slider.md)
- [JSON Configuration](json-configuration.md)
- [Input system](input-system.md)
- [Architecture](architecture.md)
