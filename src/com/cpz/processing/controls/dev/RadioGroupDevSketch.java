package com.cpz.processing.controls.dev;

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
import java.util.List;
import java.util.Objects;
import processing.core.PApplet;

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
   private String statusText = "No selection yet";

   public void settings() {
      this.size(920, 520);
      this.smooth(4);
   }

   public void setup() {
      this.keyboardAdapter = new KeyboardInputAdapter(this.focusManager);
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

   public void draw() {
      this.background(244);
      this.drawTitles();
      this.mainGroupView.draw();
      this.secondaryGroupView.draw();
      this.drawDebug();
   }

   public void mouseMoved() {
      this.inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.MOVE, (float)this.mouseX, (float)this.mouseY, this.mouseButton));
   }

   public void mouseDragged() {
      this.inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.DRAG, (float)this.mouseX, (float)this.mouseY, this.mouseButton));
   }

   public void mousePressed() {
      this.inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.PRESS, (float)this.mouseX, (float)this.mouseY, this.mouseButton));
   }

   public void mouseReleased() {
      this.inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.RELEASE, (float)this.mouseX, (float)this.mouseY, this.mouseButton));
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

      public boolean handleKeyboardEvent(KeyboardEvent var1) {
         RadioGroupDevSketch.this.keyboardAdapter.handleKeyboardEvent(var1);
         return true;
      }
   }
}
