package com.cpz.processing.controls.examples;

import com.cpz.processing.controls.controls.radiogroup.model.RadioGroupModel;
import com.cpz.processing.controls.controls.radiogroup.style.RadioGroupDefaultStyles;
import com.cpz.processing.controls.controls.radiogroup.input.RadioGroupInputAdapter;
import com.cpz.processing.controls.controls.radiogroup.view.RadioGroupView;
import com.cpz.processing.controls.controls.radiogroup.viewmodel.RadioGroupViewModel;
import com.cpz.processing.controls.core.focus.FocusManager;
import com.cpz.processing.controls.core.input.DefaultInputLayer;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.KeyboardEvent;
import com.cpz.processing.controls.core.input.KeyboardInputAdapter;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.processing.controls.input.KeyboardState;
import com.cpz.processing.controls.input.ProcessingKeyboardAdapter;
import java.util.List;
import java.util.Objects;
import processing.core.PApplet;

/**
 * Development sketch for the radio group dev flow.
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
public class RadioGroupDevSketch extends PApplet {
   private final FocusManager focusManager = new FocusManager();
   private final InputManager inputManager = new InputManager();
   private RadioGroupView mainGroupView;
   private RadioGroupView secondaryGroupView;
   private RadioGroupViewModel mainGroupViewModel;
   private RadioGroupViewModel secondaryGroupViewModel;
   private RadioGroupInputAdapter mainGroupInput;
   private RadioGroupInputAdapter secondaryGroupInput;
   private KeyboardInputAdapter keyboardAdapter;
   private KeyboardState keyboardState;
   private ProcessingKeyboardAdapter processingKeyboardAdapter;
   private String statusText = "No selection yet";

   /**
    * Updates tings.
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void settings() {
      this.size(920, 520);
      this.smooth(4);
   }

   /**
    * Updates up.
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setup() {
      this.keyboardAdapter = new KeyboardInputAdapter(this.focusManager);
      this.keyboardState = new KeyboardState();
      this.processingKeyboardAdapter = new ProcessingKeyboardAdapter(this.keyboardState, this.inputManager);
      this.mainGroupViewModel = new RadioGroupViewModel(new RadioGroupModel(List.of("Mercury", "Venus", "Earth", "Mars", "Jupiter"), 2));
      this.mainGroupViewModel.setOnOptionSelected((var1) -> this.statusText = "Main selected index: " + var1);
      this.mainGroupView = new RadioGroupView(this, this.mainGroupViewModel, 250.0F, 170.0F, 280.0F);
      this.mainGroupView.setStyle(RadioGroupDefaultStyles.standard());
      this.mainGroupInput = new RadioGroupInputAdapter(this.mainGroupView, this.mainGroupViewModel, this.focusManager);
      this.secondaryGroupViewModel = new RadioGroupViewModel(new RadioGroupModel(List.of("Small", "Medium", "Large"), 0));
      this.secondaryGroupViewModel.setOnOptionSelected((var1) -> this.statusText = "Secondary selected index: " + var1);
      this.secondaryGroupView = new RadioGroupView(this, this.secondaryGroupViewModel, 660.0F, 170.0F, 220.0F);
      this.secondaryGroupView.setStyle(RadioGroupDefaultStyles.standard());
      this.secondaryGroupInput = new RadioGroupInputAdapter(this.secondaryGroupView, this.secondaryGroupViewModel, this.focusManager);
      this.inputManager.registerLayer(new RadioGroupRootInputLayer());
   }

   /**
    * Draws the current frame.
    *
    * Behavior:
    * - Uses already available state and does not define business rules.
    */
   public void draw() {
      this.background(244);
      this.drawTitles();
      this.mainGroupView.draw();
      this.secondaryGroupView.draw();
      this.drawDebug();
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

   /**
    * Performs key pressed.
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public void keyPressed() {
      if (this.key == 27) {
         this.key = 0;
      }

      this.processingKeyboardAdapter.keyPressed(this.key, this.keyCode);
   }

   /**
    * Performs key released.
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public void keyReleased() {
      if (this.key == 27) {
         this.key = 0;
      }

      this.processingKeyboardAdapter.keyReleased(this.key, this.keyCode);
   }

   /**
    * Performs key typed.
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public void keyTyped() {
      this.processingKeyboardAdapter.keyTyped(this.key, this.keyCode);
   }

   private void drawTitles() {
      this.pushStyle();
      this.fill(32);
      this.textAlign(3, 3);
      this.textSize(22.0F);
      this.text("RadioGroup Dev Sketch", (float)this.width * 0.5F, 48.0F);
      this.textSize(14.0F);
      this.text("Mouse select, arrows navigate, Enter/Space select, TAB cycles focus between groups.", (float)this.width * 0.5F, 82.0F);
      this.popStyle();
   }

   private void drawDebug() {
      this.pushStyle();
      this.fill(36);
      this.textAlign(37, 101);
      this.text("Status: " + this.statusText, 70.0F, 400.0F);
      int var10001 = this.mainGroupViewModel.getSelectedIndex();
      this.text("Main selected: " + var10001 + " hovered: " + this.mainGroupViewModel.getHoveredIndex() + " pressed: " + this.mainGroupViewModel.getPressedIndex() + " focused: " + this.mainGroupViewModel.isFocused(), 70.0F, 428.0F);
      var10001 = this.secondaryGroupViewModel.getSelectedIndex();
      this.text("Secondary selected: " + var10001 + " hovered: " + this.secondaryGroupViewModel.getHoveredIndex() + " pressed: " + this.secondaryGroupViewModel.getPressedIndex() + " focused: " + this.secondaryGroupViewModel.isFocused(), 70.0F, 454.0F);
      this.popStyle();
   }

   private final class RadioGroupRootInputLayer extends DefaultInputLayer {
      private RadioGroupRootInputLayer() {
         Objects.requireNonNull(RadioGroupDevSketch.this);
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
               RadioGroupDevSketch.this.mainGroupInput.handleMouseMove(var1.getX(), var1.getY());
               RadioGroupDevSketch.this.secondaryGroupInput.handleMouseMove(var1.getX(), var1.getY());
               return true;
            case PRESS:
               RadioGroupDevSketch.this.mainGroupInput.handleMousePress(var1.getX(), var1.getY());
               RadioGroupDevSketch.this.secondaryGroupInput.handleMousePress(var1.getX(), var1.getY());
               if (!RadioGroupDevSketch.this.mainGroupView.contains(var1.getX(), var1.getY()) && !RadioGroupDevSketch.this.secondaryGroupView.contains(var1.getX(), var1.getY())) {
                  RadioGroupDevSketch.this.focusManager.clearFocus();
               }

               return true;
            case RELEASE:
               RadioGroupDevSketch.this.mainGroupInput.handleMouseRelease(var1.getX(), var1.getY());
               RadioGroupDevSketch.this.secondaryGroupInput.handleMouseRelease(var1.getX(), var1.getY());
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
         RadioGroupDevSketch.this.keyboardAdapter.handleKeyboardEvent(var1);
         return true;
      }
   }
}
