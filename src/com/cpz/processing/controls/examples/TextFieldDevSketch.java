package com.cpz.processing.controls.examples;

import com.cpz.processing.controls.controls.textfield.config.TextFieldStyleConfig;
import com.cpz.processing.controls.controls.textfield.model.TextFieldModel;
import com.cpz.processing.controls.controls.textfield.style.DefaultTextFieldStyle;
import com.cpz.processing.controls.controls.textfield.input.TextFieldInputAdapter;
import com.cpz.processing.controls.controls.textfield.view.TextFieldView;
import com.cpz.processing.controls.controls.textfield.viewmodel.TextFieldViewModel;
import com.cpz.processing.controls.core.focus.FocusManager;
import com.cpz.processing.controls.core.input.DefaultInputLayer;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.KeyboardEvent;
import com.cpz.processing.controls.core.input.KeyboardInputAdapter;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.processing.controls.core.util.Colors;
import com.cpz.processing.controls.input.KeyboardState;
import com.cpz.processing.controls.input.ProcessingKeyboardAdapter;
import java.util.Objects;
import processing.core.PApplet;
import processing.core.PFont;

/**
 * Development sketch for the text field dev flow.
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
public class TextFieldDevSketch extends PApplet {
   private final FocusManager focusManager = new FocusManager();
   private final InputManager inputManager = new InputManager();
   private TextFieldView customFontView;
   private TextFieldView defaultFontView;
   private TextFieldInputAdapter customFontInput;
   private TextFieldInputAdapter defaultFontInput;
   private KeyboardInputAdapter keyboardAdapter;
   private KeyboardState keyboardState;
   private ProcessingKeyboardAdapter processingKeyboardAdapter;

   /**
    * Updates tings.
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void settings() {
      this.size(980, 420);
      this.smooth(4);
   }

   /**
    * Updates up.
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setup() {
      PFont var1 = this.createFont("data/font/abel-regular.ttf", 16.0F, true);
      this.keyboardAdapter = new KeyboardInputAdapter(this.focusManager);
      this.keyboardState = new KeyboardState();
      this.processingKeyboardAdapter = new ProcessingKeyboardAdapter(this.keyboardState, this.inputManager);
      TextFieldViewModel var2 = new TextFieldViewModel(new TextFieldModel());
      var2.setText("Custom font field");
      this.customFontView = new TextFieldView(this, var2, (float)this.width * 0.5F, 140.0F, 420.0F, 46.0F);
      this.customFontView.setStyle(new DefaultTextFieldStyle(this.createCustomFontStyle(var1)));
      this.customFontInput = new TextFieldInputAdapter(this.customFontView, var2, this.focusManager);
      TextFieldViewModel var3 = new TextFieldViewModel(new TextFieldModel());
      var3.setText("Default font field");
      this.defaultFontView = new TextFieldView(this, var3, (float)this.width * 0.5F, 250.0F, 420.0F, 46.0F);
      this.defaultFontView.setStyle(new DefaultTextFieldStyle(this.createDefaultFontStyle()));
      this.defaultFontInput = new TextFieldInputAdapter(this.defaultFontView, var3, this.focusManager);
      this.inputManager.registerLayer(new TextFieldRootInputLayer());
   }

   /**
    * Draws the current frame.
    *
    * Behavior:
    * - Uses already available state and does not define business rules.
    */
   public void draw() {
      this.background(242);
      this.drawTitles();
      this.customFontView.draw();
      this.defaultFontView.draw();
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
    * Performs mouse dragged.
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public void mouseDragged() {
      this.inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.DRAG, (float)this.mouseX, (float)this.mouseY, this.mouseButton));
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
      this.fill(36);
      this.textAlign(3, 3);
      this.textSize(20.0F);
      this.text("TextField Dev Sketch", (float)this.width * 0.5F, 52.0F);
      this.textSize(14.0F);
      this.text("Custom font: Abel from data/font/abel-regular.ttf", (float)this.width * 0.5F, 88.0F);
      this.text("Default font: Processing fallback when font is null", (float)this.width * 0.5F, 198.0F);
      this.popStyle();
   }

   private TextFieldStyleConfig createCustomFontStyle(PFont var1) {
      TextFieldStyleConfig var2 = new TextFieldStyleConfig();
      var2.backgroundColor = Colors.rgb(248, 245, 238);
      var2.borderColor = Colors.rgb(167, 93, 68);
      var2.textColor = Colors.rgb(48, 34, 28);
      var2.cursorColor = Colors.rgb(208, 96, 48);
      var2.selectionColor = Colors.rgb(228, 197, 168);
      var2.selectionTextColor = Colors.rgb(48, 34, 28);
      var2.textSize = 18.0F;
      var2.font = var1;
      return var2;
   }

   private TextFieldStyleConfig createDefaultFontStyle() {
      TextFieldStyleConfig var1 = new TextFieldStyleConfig();
      var1.backgroundColor = Colors.rgb(236, 242, 248);
      var1.borderColor = Colors.rgb(72, 116, 156);
      var1.textColor = Colors.rgb(28, 44, 62);
      var1.cursorColor = Colors.rgb(38, 132, 212);
      var1.selectionColor = Colors.rgb(182, 217, 248);
      var1.selectionTextColor = Colors.rgb(28, 44, 62);
      var1.textSize = 16.0F;
      var1.font = null;
      return var1;
   }

   private final class TextFieldRootInputLayer extends DefaultInputLayer {
      private TextFieldRootInputLayer() {
         Objects.requireNonNull(TextFieldDevSketch.this);
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
            case PRESS:
               boolean var2 = TextFieldDevSketch.this.customFontInput.handleMousePress(var1.getX(), var1.getY());
               if (!var2) {
                  var2 = TextFieldDevSketch.this.defaultFontInput.handleMousePress(var1.getX(), var1.getY());
               }

               if (!var2) {
                  TextFieldDevSketch.this.focusManager.clearFocus();
               }

               return true;
            case DRAG:
               TextFieldDevSketch.this.customFontInput.handleMouseDrag(var1.getX(), var1.getY());
               TextFieldDevSketch.this.defaultFontInput.handleMouseDrag(var1.getX(), var1.getY());
               return true;
            case RELEASE:
               TextFieldDevSketch.this.customFontInput.handleMouseRelease();
               TextFieldDevSketch.this.defaultFontInput.handleMouseRelease();
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
         switch (var1.getType()) {
            case PRESS:
               TextFieldDevSketch.this.keyboardAdapter.handleKeyboardEvent(var1);
               return true;
            case TYPE:
               TextFieldDevSketch.this.keyboardAdapter.handleKeyboardEvent(var1);
               return true;
            default:
               return false;
         }
      }
   }
}
