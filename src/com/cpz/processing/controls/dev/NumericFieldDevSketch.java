package com.cpz.processing.controls.dev;

import com.cpz.processing.controls.controls.numericfield.model.NumericFieldModel;
import com.cpz.processing.controls.controls.numericfield.style.NumericFieldDefaultStyles;
import com.cpz.processing.controls.controls.numericfield.view.NumericFieldInputAdapter;
import com.cpz.processing.controls.controls.numericfield.view.NumericFieldView;
import com.cpz.processing.controls.controls.numericfield.viewmodel.NumericFieldViewModel;
import com.cpz.processing.controls.core.focus.FocusManager;
import com.cpz.processing.controls.core.input.DefaultInputLayer;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.KeyboardEvent;
import com.cpz.processing.controls.core.input.KeyboardInputAdapter;
import com.cpz.processing.controls.core.input.PointerEvent;
import java.math.BigDecimal;
import java.util.Objects;
import processing.core.PApplet;
import processing.event.MouseEvent;

public class NumericFieldDevSketch extends PApplet {
   private final FocusManager focusManager = new FocusManager();
   private final InputManager inputManager = new InputManager();
   private NumericFieldView primaryFieldView;
   private NumericFieldView secondaryFieldView;
   private NumericFieldViewModel primaryFieldViewModel;
   private NumericFieldViewModel secondaryFieldViewModel;
   private NumericFieldInputAdapter primaryFieldInput;
   private NumericFieldInputAdapter secondaryFieldInput;
   private KeyboardInputAdapter keyboardAdapter;
   private String lastChangeMessage = "No committed change yet";
   private int externalUpdateCount;

   public void settings() {
      this.size(980, 460);
      this.smooth(4);
   }

   public void setup() {
      this.keyboardAdapter = new KeyboardInputAdapter(this.focusManager);
      this.primaryFieldViewModel = new NumericFieldViewModel(new NumericFieldModel(new BigDecimal("12.50"), BigDecimal.ZERO, new BigDecimal("99.99"), new BigDecimal("0.25"), false, true, 2));
      this.primaryFieldView = new NumericFieldView(this, this.primaryFieldViewModel, (float)this.width * 0.5F, 145.0F, 380.0F, 46.0F);
      this.primaryFieldView.setStyle(NumericFieldDefaultStyles.standard());
      this.primaryFieldInput = new NumericFieldInputAdapter(this.primaryFieldView, this.primaryFieldViewModel, this.focusManager);
      this.primaryFieldViewModel.setOnValueChanged((var1) -> this.lastChangeMessage = "Primary committed -> " + var1.toPlainString());
      this.secondaryFieldViewModel = new NumericFieldViewModel(new NumericFieldModel(new BigDecimal("-5"), new BigDecimal("-10"), new BigDecimal("10"), BigDecimal.ONE, true, false, 0));
      this.secondaryFieldView = new NumericFieldView(this, this.secondaryFieldViewModel, (float)this.width * 0.5F, 260.0F, 380.0F, 46.0F);
      this.secondaryFieldView.setStyle(NumericFieldDefaultStyles.standard());
      this.secondaryFieldInput = new NumericFieldInputAdapter(this.secondaryFieldView, this.secondaryFieldViewModel, this.focusManager);
      this.secondaryFieldViewModel.setOnValueChanged((var1) -> this.lastChangeMessage = "Secondary committed -> " + var1.toPlainString());
      this.inputManager.registerLayer(new NumericFieldRootInputLayer());
   }

   public void draw() {
      this.background(242);
      this.drawTitles();
      this.primaryFieldView.draw();
      this.secondaryFieldView.draw();
      this.drawDebug();
   }

   public void mousePressed() {
      this.inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.PRESS, (float)this.mouseX, (float)this.mouseY, this.mouseButton));
   }

   public void mouseDragged() {
      this.inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.DRAG, (float)this.mouseX, (float)this.mouseY, this.mouseButton));
   }

   public void mouseReleased() {
      this.inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.RELEASE, (float)this.mouseX, (float)this.mouseY, this.mouseButton));
   }

   public void mouseMoved() {
      this.inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.MOVE, (float)this.mouseX, (float)this.mouseY, this.mouseButton));
   }

   public void mouseWheel(MouseEvent var1) {
      this.inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.WHEEL, (float)this.mouseX, (float)this.mouseY, this.mouseButton, (float)var1.getCount(), var1.isShiftDown(), var1.isControlDown()));
   }

   public void keyPressed() {
      if (this.key == 27) {
         this.key = 0;
      }

      this.inputManager.dispatchKeyboard(new KeyboardEvent(KeyboardEvent.Type.PRESS, this.key, this.keyCode, this.keyEvent != null && this.keyEvent.isShiftDown(), this.keyEvent != null && this.keyEvent.isControlDown(), this.keyEvent != null && this.keyEvent.isAltDown()));
   }

   public void keyReleased() {
      if (this.key == 27) {
         this.key = 0;
      }

      this.inputManager.dispatchKeyboard(new KeyboardEvent(KeyboardEvent.Type.RELEASE, this.key, this.keyCode, this.keyEvent != null && this.keyEvent.isShiftDown(), this.keyEvent != null && this.keyEvent.isControlDown(), this.keyEvent != null && this.keyEvent.isAltDown()));
   }

   public void keyTyped() {
      this.inputManager.dispatchKeyboard(new KeyboardEvent(KeyboardEvent.Type.TYPE, this.key, this.keyCode, this.keyEvent != null && this.keyEvent.isShiftDown(), this.keyEvent != null && this.keyEvent.isControlDown(), this.keyEvent != null && this.keyEvent.isAltDown()));
   }

   private void drawTitles() {
      this.pushStyle();
      this.fill(36);
      this.textAlign(3, 3);
      this.textSize(20.0F);
      this.text("NumericField Dev Sketch", (float)this.width * 0.5F, 52.0F);
      this.textSize(14.0F);
      this.text("Type numbers, try -, ., -., 0., use arrows, wheel, SHIFT x10, CTRL /10, and press 'u' for external update.", (float)this.width * 0.5F, 86.0F);
      this.popStyle();
   }

   private void drawDebug() {
      this.pushStyle();
      this.fill(36);
      this.textAlign(37, 101);
      this.text("Primary value: " + this.primaryFieldViewModel.getValue().toPlainString(), 90.0F, 330.0F);
      this.text("Primary text: " + this.primaryFieldViewModel.getText(), 90.0F, 352.0F);
      this.text("Primary focused/editing: " + this.primaryFieldViewModel.isFocused() + " / " + this.primaryFieldViewModel.isEditing(), 90.0F, 374.0F);
      this.text("Secondary value: " + this.secondaryFieldViewModel.getValue().toPlainString(), 90.0F, 404.0F);
      this.text("Secondary text: " + this.secondaryFieldViewModel.getText(), 420.0F, 330.0F);
      this.text("Secondary focused/editing: " + this.secondaryFieldViewModel.isFocused() + " / " + this.secondaryFieldViewModel.isEditing(), 420.0F, 352.0F);
      this.text("Last change: " + this.lastChangeMessage, 420.0F, 378.0F, 460.0F, 32.0F);
      this.text("External updates: " + this.externalUpdateCount, 420.0F, 404.0F);
      this.text("Checks: '-', '.', '-.', '0.' stay editable, invalid chars rejected, commit fallback, arrows/wheel clamp, external sync when not editing.", 420.0F, 426.0F, 460.0F, 48.0F);
      this.popStyle();
   }

   private final class NumericFieldRootInputLayer extends DefaultInputLayer {
      private NumericFieldRootInputLayer() {
         Objects.requireNonNull(NumericFieldDevSketch.this);
         super(0);
      }

      public boolean handlePointerEvent(PointerEvent var1) {
         switch (var1.getType()) {
            case MOVE:
               NumericFieldDevSketch.this.primaryFieldInput.handleMouseMove(var1.getX(), var1.getY());
               NumericFieldDevSketch.this.secondaryFieldInput.handleMouseMove(var1.getX(), var1.getY());
               return true;
            case DRAG:
               NumericFieldDevSketch.this.primaryFieldInput.handleMouseDrag(var1.getX(), var1.getY());
               NumericFieldDevSketch.this.secondaryFieldInput.handleMouseDrag(var1.getX(), var1.getY());
               return true;
            case PRESS:
               boolean var2 = NumericFieldDevSketch.this.primaryFieldInput.handleMousePress(var1.getX(), var1.getY());
               if (!var2) {
                  var2 = NumericFieldDevSketch.this.secondaryFieldInput.handleMousePress(var1.getX(), var1.getY());
               }

               if (!var2) {
                  NumericFieldDevSketch.this.focusManager.clearFocus();
               }

               return true;
            case RELEASE:
               NumericFieldDevSketch.this.primaryFieldInput.handleMouseRelease(var1.getX(), var1.getY());
               NumericFieldDevSketch.this.secondaryFieldInput.handleMouseRelease(var1.getX(), var1.getY());
               return true;
            case WHEEL:
               NumericFieldDevSketch.this.primaryFieldInput.handleMouseWheel(var1.getWheelDelta(), var1.isShiftDown(), var1.isControlDown());
               NumericFieldDevSketch.this.secondaryFieldInput.handleMouseWheel(var1.getWheelDelta(), var1.isShiftDown(), var1.isControlDown());
               return true;
            default:
               return false;
         }
      }

      public boolean handleKeyboardEvent(KeyboardEvent var1) {
         if (var1.getType() == KeyboardEvent.Type.PRESS && (var1.getKey() == 'u' || var1.getKey() == 'U')) {
            ++NumericFieldDevSketch.this.externalUpdateCount;
            if (NumericFieldDevSketch.this.primaryFieldViewModel.isFocused()) {
               NumericFieldDevSketch.this.primaryFieldViewModel.setValue(new BigDecimal("42.75"));
               NumericFieldDevSketch.this.lastChangeMessage = "External primary update #" + NumericFieldDevSketch.this.externalUpdateCount;
            } else {
               NumericFieldDevSketch.this.secondaryFieldViewModel.setValue(new BigDecimal("-3"));
               NumericFieldDevSketch.this.lastChangeMessage = "External secondary update #" + NumericFieldDevSketch.this.externalUpdateCount;
            }

            return true;
         } else {
            NumericFieldDevSketch.this.keyboardAdapter.handleKeyboardEvent(var1);
            return true;
         }
      }
   }
}
