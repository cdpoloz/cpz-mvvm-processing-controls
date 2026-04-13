# Button (SVG)

This tutorial shows how to create a `Button` using an SVG rendered via `SvgButtonRenderer`.

> For the base `Button` setup, architecture, and input system, see [button.md](button.md)

---

## What changes when using SVG

The overall control architecture remains the same:

- `Model`
- `ViewModel`
- `View`
- `InputAdapter`
- `InputLayer`

The only difference is the visual representation layer:

- instead of rendering only primitive shapes, the button uses an SVG rendered via `SvgButtonRenderer`.

---

## Step-by-step setup

This section focuses only on what changes compared to the base button example. The control architecture and input handling remain unchanged.

---

### 1. Create the model and view model

This step is identical to the base button example:

```java
ButtonViewModel buttonViewModel = new ButtonViewModel(new ButtonModel("SVG Button"));

buttonViewModel.setClickListener(() -> {
    System.out.println("You clicked the SVG button!");
});
```

---

### 2. Create the view

```java
float x = 300f;
float y = 150f;
float w = 200f;
float h = 173f;

buttonView = new ButtonView(this, buttonViewModel, x, y, w, h);
```

---

### 3. Apply the SVG to the button

This is the key difference.

Use the existing API from the framework to assign the SVG to the button.

```java
ButtonStyleConfig bsc = new ButtonStyleConfig();
bsc.baseColor = Colors.rgb(48, 98, 219);
bsc.textColor = Colors.gray(255);
bsc.strokeColor = Colors.gray(255);
bsc.strokeWeight = 2.0f;
bsc.strokeWeightHover = 4.0f;
bsc.cornerRadius = 18.0f;
bsc.disabledAlpha = 90;
bsc.hoverBlendWithWhite = 0.12f;
bsc.pressedBlendWithBlack = 0.25f;
bsc.setRenderer(new SvgButtonRenderer(this, "data" + File.separator + "img" + File.separator + "test.svg"));

buttonView.setStyle(new DefaultButtonStyle(bsc));
```

---

### 4. Input handling

Input handling is identical to the base button example.

```java
ButtonInputAdapter buttonInput = new ButtonInputAdapter(buttonView, buttonViewModel);

InputManager inputManager = new InputManager();
inputManager.registerLayer(new ButtonRootInputLayer());
```

---

### 5. Draw

```java
public void draw() {
    background(28);
    buttonView.draw();
}
```

---

## Full example

```java
import com.cpz.processing.controls.controls.button.config.ButtonStyleConfig;
import com.cpz.processing.controls.controls.button.input.ButtonInputAdapter;
import com.cpz.processing.controls.controls.button.model.ButtonModel;
import com.cpz.processing.controls.controls.button.style.DefaultButtonStyle;
import com.cpz.processing.controls.controls.button.style.render.SvgButtonRenderer;
import com.cpz.processing.controls.controls.button.view.ButtonView;
import com.cpz.processing.controls.controls.button.viewmodel.ButtonViewModel;
import com.cpz.processing.controls.core.input.DefaultInputLayer;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.KeyboardEvent;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.processing.controls.core.util.Colors;
import processing.core.PApplet;

import java.io.File;
import java.util.Objects;

public class ButtonSvgTest extends PApplet {

    private InputManager inputManager;
    private ButtonView buttonView;
    private ButtonViewModel buttonViewModel;
    private ButtonInputAdapter buttonInput;

    public void settings() {
        size(600, 300);
        smooth(8);
    }

    public void setup() {
        // viewModel
        buttonViewModel = new ButtonViewModel(new ButtonModel("SVG Button"));
        buttonViewModel.setClickListener(() -> {
            // the code that executes after a button click goes here, for example:
            System.out.println("You clicked the SVG button!");
        });
        // view
        float x = 300f;
        float y = 150f;
        float w = 200f;
        float h = 173f;
        buttonView = new ButtonView(this, buttonViewModel, x, y, w, h);
        // style (optional)
        ButtonStyleConfig bsc = new ButtonStyleConfig();
        bsc.baseColor = Colors.rgb(48, 98, 219);
        bsc.textColor = Colors.gray(255);
        bsc.strokeColor = Colors.gray(255);
        bsc.strokeWeight = 2.0f;
        bsc.strokeWeightHover = 4.0f;
        bsc.cornerRadius = 18.0f;
        bsc.disabledAlpha = 90;
        bsc.hoverBlendWithWhite = 0.12f;
        bsc.pressedBlendWithBlack = 0.25f;
        bsc.setRenderer(new SvgButtonRenderer(this, "data" + File.separator + "img" + File.separator + "test.svg"));
        buttonView.setStyle(new DefaultButtonStyle(bsc));
        // inputAdapter
        buttonInput = new ButtonInputAdapter(buttonView, buttonViewModel);
        // inputManager
        inputManager = new InputManager();
        inputManager.registerLayer(new ButtonRootInputLayer());
    }

    public void draw() {
        background(28);
        buttonView.draw();
    }

    // mouse events
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

    // input layer
    private final class ButtonRootInputLayer extends DefaultInputLayer {
        private ButtonRootInputLayer() {
            Objects.requireNonNull(ButtonSvgTest.this);
            super(0);
        }

        public boolean handlePointerEvent(PointerEvent pointerEvent) {
            switch (pointerEvent.getType()) {
                case MOVE:
                case DRAG:
                    buttonInput.handleMouseMove(pointerEvent.getX(), pointerEvent.getY());
                    return true;
                case PRESS:
                    buttonInput.handleMousePress(pointerEvent.getX(), pointerEvent.getY());
                    return true;
                case RELEASE:
                    buttonInput.handleMouseRelease(pointerEvent.getX(), pointerEvent.getY());
                    return true;
                default:
                    return false;
            }
        }

        public boolean handleKeyboardEvent(KeyboardEvent keyboardEvent) {
            return false;
        }
    }

}
```

---

## Notes

- The SVG is rendered through `SvgButtonRenderer`, which handles shape loading and rendering.
- Prefer simple, single-color SVGs for best visual consistency.
- SVG rendering depends on Processing's `PShape` behavior.
- Keep SVG assets lightweight to avoid performance issues.

---

## See also

- [Button](button.md)
- [Input system](docs/input-system.md)
- [Architecture](docs/architecture.md)
