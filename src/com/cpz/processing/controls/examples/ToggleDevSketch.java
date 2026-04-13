package com.cpz.processing.controls.examples;

import com.cpz.processing.controls.controls.toggle.config.ToggleStyleConfig;
import com.cpz.processing.controls.controls.toggle.model.ToggleModel;
import com.cpz.processing.controls.controls.toggle.style.ParametricToggleStyle;
import com.cpz.processing.controls.controls.toggle.style.render.CircleShapeRenderer;
import com.cpz.processing.controls.controls.toggle.style.render.SvgShapeRenderer;
import com.cpz.processing.controls.controls.toggle.input.ToggleInputAdapter;
import com.cpz.processing.controls.controls.toggle.view.ToggleView;
import com.cpz.processing.controls.controls.toggle.viewmodel.ToggleViewModel;
import com.cpz.processing.controls.core.input.DefaultInputLayer;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.KeyboardEvent;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.processing.controls.core.input.hit.CircleHitTest;
import com.cpz.processing.controls.core.input.hit.RectHitTest;
import com.cpz.processing.controls.core.util.Colors;
import com.cpz.processing.controls.input.KeyboardState;
import com.cpz.processing.controls.input.ProcessingKeyboardAdapter;
import java.util.Objects;
import processing.core.PApplet;

/**
 * Development sketch for the toggle dev flow.
 *
 * Responsibilities:
 * - Exercise public controls in an interactive sketch.
 * - Provide a development-time validation surface.
 *
 * Behavior:
 * - Targets interactive validation rather than library reuse.
 *
 * Notes:
 * - This type is intended for development and demonstration flows.
 */
public class ToggleDevSketch extends PApplet {
   private final InputManager inputManager = new InputManager();
   private ToggleViewModel swViewModel1;
   private ToggleViewModel swViewModel2;
   private ToggleView swView1;
   private ToggleView swView2;
   private ToggleInputAdapter swInput1;
   private ToggleInputAdapter swInput2;
   private KeyboardState keyboardState;
   private ProcessingKeyboardAdapter processingKeyboardAdapter;

   /**
    * Updates tings.
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void settings() {
      this.size(760, 440);
      this.smooth(4);
   }

   /**
    * Updates up.
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setup() {
      this.noStroke();
      this.keyboardState = new KeyboardState();
      this.processingKeyboardAdapter = new ProcessingKeyboardAdapter(this.keyboardState, this.inputManager);
      this.swViewModel1 = new ToggleViewModel(new ToggleModel());
      this.swViewModel1.setTotalStates(2);
      this.swView1 = new ToggleView(this, this.swViewModel1, 220.0F, 220.0F, 76.0F);
      this.swView1.setHitTest(new CircleHitTest());
      this.swView1.setStyle(new ParametricToggleStyle(this.createCircularConfig()));
      this.swInput1 = new ToggleInputAdapter(this.swView1, this.swViewModel1);
      this.swViewModel2 = new ToggleViewModel(new ToggleModel());
      this.swViewModel2.setTotalStates(3);
      this.swView2 = new ToggleView(this, this.swViewModel2, 540.0F, 220.0F, 120.0F, 100.0F);
      this.swView2.setStyle(new ParametricToggleStyle(this.createSvgConfig()));
      this.swView2.setHitTest(new RectHitTest());
      this.swInput2 = new ToggleInputAdapter(this.swView2, this.swViewModel2);
      this.inputManager.registerLayer(new ToggleRootInputLayer());
   }

   /**
    * Draws the current frame.
    *
    * Behavior:
    * - Uses already available state and does not define business rules.
    */
   public void draw() {
      this.update();
      this.dibujar();
   }

   /**
    * Performs key released.
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public void keyReleased() {
      if (key == ESC) key = 0;
      this.processingKeyboardAdapter.keyReleased(this.key, this.keyCode);
   }

   /**
    * Performs key pressed.
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public void keyPressed() {
      if (key == ESC) key = 0;
      this.processingKeyboardAdapter.keyPressed(this.key, this.keyCode);
   }

   /**
    * Performs key typed.
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public void keyTyped() {
      if (key == ESC) key = 0;
      this.processingKeyboardAdapter.keyTyped(this.key, this.keyCode);
   }

   /**
    * Performs mouse moved.
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public void mouseMoved() {
      this.inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.MOVE, (float)this.mouseX, (float)this.mouseY, this.mouseButton));
   }

   /**
    * Performs mouse dragged.
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public void mouseDragged() {
      this.inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.DRAG, (float)this.mouseX, (float)this.mouseY, this.mouseButton));
   }

   /**
    * Performs mouse pressed.
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public void mousePressed() {
      this.inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.PRESS, (float)this.mouseX, (float)this.mouseY, this.mouseButton));
   }

   /**
    * Performs mouse released.
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public void mouseReleased() {
      this.inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.RELEASE, (float)this.mouseX, (float)this.mouseY, this.mouseButton));
   }

   private void update() {
      this.swViewModel2.setEnabled(this.swViewModel1.getState() == 1);
   }

   private void dibujar() {
      this.background(32);
      this.dibujarElementos();
      this.dibujarDebug();
   }

   private void dibujarElementos() {
      this.swView1.draw();
      this.swView2.draw();
   }

   private void dibujarDebug() {
      this.pushStyle();
      this.fill(220);
      this.textAlign(3);
      this.text("Circle Renderer", 220.0F, 110.0F);
      this.text("State: " + this.swViewModel1.getState(), 220.0F, 132.0F);
      this.text("SVG Renderer", 540.0F, 110.0F);
      this.text("State: " + this.swViewModel2.getState(), 540.0F, 132.0F);
      this.textAlign(37);
      String var1 = "Checks:\n- Click changes state on release\n- Dragging outside cancels\n- Hover/pressed modify final fill\n- SVG toggle is disabled while circle is OFF\n- SVG scales using ToggleView width/height";
      this.text(var1, 24.0F, (float)(this.height - 108));
      this.text("SVG enabled: " + this.swViewModel2.isEnabled(), 420.0F, 344.0F);
      this.text("Circle hovered: " + this.swViewModel1.isHovered(), 420.0F, 366.0F);
      this.text("SVG pressed: " + this.swViewModel2.isPressed(), 420.0F, 388.0F);
      this.popStyle();
   }

   private ToggleStyleConfig createCircularConfig() {
      ToggleStyleConfig var1 = new ToggleStyleConfig();
      var1.setShapeRenderer(new CircleShapeRenderer());
      var1.stateColors = new Integer[]{Colors.gray(60), Colors.rgb(48, 186, 96)};
      var1.strokeColor = Colors.gray(255);
      var1.strokeWidth = 2.0F;
      var1.strokeWidthHover = 4.0F;
      var1.hoverBlendWithWhite = 0.18F;
      var1.pressedBlendWithBlack = 0.2F;
      var1.disabledAlpha = 70;
      return var1;
   }

   private ToggleStyleConfig createSvgConfig() {
      ToggleStyleConfig var1 = new ToggleStyleConfig();
      var1.setShapeRenderer(new SvgShapeRenderer(this, "data/img/test.svg"));
      var1.stateColors = new Integer[]{Colors.rgb(80, 100, 220), Colors.rgb(235, 160, 40), Colors.rgb(32, 188, 176)};
      var1.strokeColor = Colors.gray(255);
      var1.strokeWidth = 1.5F;
      var1.strokeWidthHover = 3.5F;
      var1.hoverBlendWithWhite = 0.14F;
      var1.pressedBlendWithBlack = 0.24F;
      var1.disabledAlpha = 70;
      return var1;
   }

   private final class ToggleRootInputLayer extends DefaultInputLayer {
      private ToggleRootInputLayer() {
         Objects.requireNonNull(ToggleDevSketch.this);
         super(0);
      }

      /**
       * Handles pointer event.
       *
       * @param var1 parameter used by this operation
       * @return result of this operation
       *
       * Behavior:
       * - Applies the public interaction flow exposed by this type.
       */
      public boolean handlePointerEvent(PointerEvent var1) {
         switch (var1.getType()) {
            case MOVE:
            case DRAG:
               ToggleDevSketch.this.swInput1.handleMouseMove(var1.getX(), var1.getY());
               ToggleDevSketch.this.swInput2.handleMouseMove(var1.getX(), var1.getY());
               return true;
            case PRESS:
               ToggleDevSketch.this.swInput1.handleMousePress(var1.getX(), var1.getY());
               ToggleDevSketch.this.swInput2.handleMousePress(var1.getX(), var1.getY());
               return true;
            case RELEASE:
               ToggleDevSketch.this.swInput1.handleMouseRelease(var1.getX(), var1.getY());
               ToggleDevSketch.this.swInput2.handleMouseRelease(var1.getX(), var1.getY());
               return true;
            default:
               return false;
         }
      }

      /**
       * Handles keyboard event.
       *
       * @param var1 parameter used by this operation
       * @return result of this operation
       *
       * Behavior:
       * - Applies the public interaction flow exposed by this type.
       */
      public boolean handleKeyboardEvent(KeyboardEvent var1) {
         return false;
      }
   }
}
