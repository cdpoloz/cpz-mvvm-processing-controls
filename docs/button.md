# Button

`ButtonView` builds a plain `ButtonViewState`, reads the cached `ThemeSnapshot` once from its style, and passes that
snapshot into `DefaultButtonStyle`. The style reads tokens from the snapshot instead of resolving theme objects during
render.

---

## Performance note

Themed button rendering no longer allocates from theme resolution in `draw()`. Theme snapshots are reused until the
sketch changes theme.

---

## Responsabilities

The `Button` control follows the standard MVVM separation used across the framework:

- Model → stores base state (e.g. text, enabled)
- ViewModel → handles interaction (hover, pressed, click)
- View → layout, hit testing, and `ViewState` construction
- Style → resolves visual appearance (no business logic)
- Renderer → performs drawing only

Rendering pipeline:

```text
ViewModel → ViewState → Style → RenderStyle → Renderer
```

---

## Minimal example

A minimal button requieres:

1. `ButtonModel`
2. `ButtonViewModel`
3. `ButtonView`
4. `ButtonInputAdapter`
5. An `InputLayer`
6. Forwarded pointer events

---

## Step-by-step setup

This section shows how to build a `Button` step by step usaing a Processing sketch.

---

### 1. Create the model and view model

``` Java
ButtonViewModel buttonViewModel = new ButtonViewModel(new ButtonModel("Text-on-Button"));
```

- `ButtonModel` stores the base state (text, enabled state).
- `ButtonViewModel` manages interaction state and exposes behavior such as click handling.

---

### 2. Assign the click action

``` Java
buttonViewModel.setClickListener(() -> {
    // the code that executes after a button click goes here, for example:
    System.out.println("You clicked the button!")
});
```

This defines what happens when the button is clicked.

---

### 3. Create the view

``` Java
float x = 210f;
float y = 150f;
float w = 190f;
float h = 60f;
buttonView = new ButtonView(this, buttonViewModel, x, y, w, h);
```

The `View` is responsible for:

- layout (position and size)
- hit testing (`contains(x, y)`)
- building the `ViewState`

It does not contain business logic.

---

### 4. Optional: customize the style

``` Java
ButtonStyleConfig bsc = new ButtonStyleConfig();
bsc.baseColor = Colors.rgb(219, 98, 48);
bsc.textColor = Colors.gray(255);
bsc.strokeColor = Colors.gray(255);
bsc.strokeWeight = 2.0f;
bsc.strokeWeightHover = 4.0f;
bsc.cornerRadius = 18.0f;
bsc.disabledAlpha = 90;
bsc.hoverBlendWithWhite = 0.12f;
bsc.pressedBlendWithBlack = 0.25f;

buttonView.setStyle(bsc);
```

This configuration controls the visual appearance:

- `baseColor` → main color
- `textColor` → text color
- `strokeColor` → border color
- `strokeWeight` → default border width
- `strokeWeightHover` → border width on hover
- `cornerRadius` → rounded corners
- `disabledAlpha` → transparency when disabled
- `hoverBlendWithWhite` → lighten effect on hover
- `pressedBlendWithBlack` → darken effect on press

Styles resolve visuals only. They do not contain logic.

---

### 5. Create the input adapter

``` Java
ButtonInputAdapter buttonInput = new ButtonInputAdapter(buttonView, buttonViewModel);
```

The adapter:

- receives pointer input
- checks if the pointer is inside the view
- updates the `ViewModel` (hover, pressed, click)

> The adapter is independent from Processing.
> It only consumes normalized input data.

---

### 6. Register an input layer

``` Java
InputManager inputManager = new InputManager();
inputManager.registerLayer(new ButtonRootInputLayer());
```

`InputManager` is responsible for dispatching events.

It does not know about buttons or controls.

Instead:

* it receives normalized events
* it forwards them to registered `InputLayer`
* each layer decides how to route them

---

### 7. Draw the button

``` Java
public void draw() {
    background(28);
    buttonView.draw();
}
```

During `draw()`:

1. `View` reads state from `ViewModel`
2. Builds `ViewState`
3. Style resolves `RenderStyle`
4. Renderer draws

---

## Input flow

Processing provides callbacks like `mouseMoved`, `mousePressed`, etc.

These are converted into `PointerEvent` and sent to the `InputManager`.

``` Java
// mouseX, mouseY and mouseButton are public Processing variables

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
```

Event roles:

* `MOVE` → hover updates
* `DRAG` → continuous interaction
* `PRESS` → start of click
* `RELEASE` → end of click (may trigger action)

> This decouples the framework from Processing-specific APIs.

---

## Input layer

Example layer inside `ButtonDevSketch` (our Processing test sketch):

``` Java
private final class ButtonRootInputLayer extends DefaultInputLayer {
    private ButtonRootInputLayer() {
        Objects.requireNonNull(ButtonDevSketch.this);
        super(0);
    }

    public boolean handlePointerEvent(PointerEvent pointerEvent) {
        switch (pointerEvent.getType()) {
            case MOVE:
                break;
            case DRAG:
                ButtonDevSketch.this.buttonInput.handleMouseMove(pointerEvent.getX(), pointerEvent.getY());
                return true;
            case PRESS:
                ButtonDevSketch.this.buttonInput.handleMousePress(pointerEvent.getX(), pointerEvent.getY());
                return true;
            case RELEASE:
                ButtonDevSketch.this.buttonInput.handleMouseRelease(pointerEvent.getX(), pointerEvent.getY());
                return true;
            default:
                return false;
        }
    }

    public boolean handleKeyboardEvent(KeyboardEvent keyboardEvent) {
        return false;
    }
}
```

### Why is this needed?

The input layer:

* centralizes event routing
* connects `InputManager` with adapters
* groups multiple controls
* keeps the system scalable
* allows priority-based input handling

In this example, the layer forwards events to:

* `buttonInput`

> Separation of responsibilities:
>
>* `InputManager` → dispatch
>* `InputLayer` → routing
>* `Adapter` → control logic
>* `ViewModel` → state update

---

## Full example

``` Java
public class ButtonDevSketch extends PApplet {

    private InputManager inputManager;
    private ButtonView buttonView;
    private ButtonViewModel buttonViewModel;
    private ButtonInputAdapter buttonInput;

    public void settings() {
        size(820, 440);
        smooth(4);
    }

    public void setup() {
        // viewModel
        buttonViewModel = new ButtonViewModel(new ButtonModel("Text-on-Button"));
        buttonViewModel.setClickListener(() -> {
            // the code that executes after a button click goes here, for example:
            System.out.println("You clicked the button!")
        });
        // view
        float x = 210f;
        float y = 150f;
        float w = 190f;
        float h = 60f;
        buttonView = new ButtonView(this, buttonViewModel, x, y, w, h);
        // style (optional)
        ButtonStyleConfig bsc = new ButtonStyleConfig();
        bsc.baseColor = Colors.rgb(219, 98, 48);
        bsc.textColor = Colors.gray(255);
        bsc.strokeColor = Colors.gray(255);
        bsc.strokeWeight = 2.0f;
        bsc.strokeWeightHover = 4.0f;
        bsc.cornerRadius = 18.0f;
        bsc.disabledAlpha = 90;
        bsc.hoverBlendWithWhite = 0.12f;
        bsc.pressedBlendWithBlack = 0.25f;
        buttonView.setStyle(bsc);
        // inputAdapter
        ButtonInputAdapter buttonInput = new ButtonInputAdapter(buttonView, buttonViewModel);
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
            Objects.requireNonNull(ButtonDevSketch.this);
            super(0);
        }

        public boolean handlePointerEvent(PointerEvent pointerEvent) {
            switch (pointerEvent.getType()) {
                case MOVE:
                    break;
                case DRAG:
                    ButtonDevSketch.this.buttonInput.handleMouseMove(pointerEvent.getX(), pointerEvent.getY());
                    return true;
                case PRESS:
                    ButtonDevSketch.this.buttonInput.handleMousePress(pointerEvent.getX(), pointerEvent.getY());
                    return true;
                case RELEASE:
                    ButtonDevSketch.this.buttonInput.handleMouseRelease(pointerEvent.getX(), pointerEvent.getY());
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

## See also

* [InputSystem](docs/input-system.md)
* [Architecture](docs/architecture.md)
