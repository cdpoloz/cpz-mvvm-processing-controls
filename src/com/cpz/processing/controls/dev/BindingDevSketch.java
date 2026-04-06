package com.cpz.processing.controls.dev;

import com.cpz.processing.controls.common.binding.Binding;
import com.cpz.processing.controls.controls.label.model.LabelModel;
import com.cpz.processing.controls.controls.label.view.LabelView;
import com.cpz.processing.controls.controls.label.viewmodel.LabelViewModel;
import com.cpz.processing.controls.controls.numericfield.input.NumericFieldInputAdapter;
import com.cpz.processing.controls.controls.numericfield.model.NumericFieldModel;
import com.cpz.processing.controls.controls.numericfield.style.NumericFieldDefaultStyles;
import com.cpz.processing.controls.controls.numericfield.view.NumericFieldView;
import com.cpz.processing.controls.controls.numericfield.viewmodel.NumericFieldViewModel;
import com.cpz.processing.controls.controls.slider.input.SliderInputAdapter;
import com.cpz.processing.controls.controls.slider.model.SliderModel;
import com.cpz.processing.controls.controls.slider.model.SliderOrientation;
import com.cpz.processing.controls.controls.slider.viewmodel.SliderViewModel;
import com.cpz.processing.controls.controls.slider.view.SliderView;
import com.cpz.processing.controls.core.focus.FocusManager;
import com.cpz.processing.controls.core.input.DefaultInputLayer;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.KeyboardEvent;
import com.cpz.processing.controls.core.input.KeyboardInputAdapter;
import com.cpz.processing.controls.core.input.PointerEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import processing.core.PApplet;
import processing.event.MouseEvent;

/**
 * Development sketch for the binding dev flow.
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
public final class BindingDevSketch extends PApplet {
   private final FocusManager focusManager = new FocusManager();
   private final InputManager inputManager = new InputManager();
   private boolean internalUpdate;
   private SliderView sliderView;
   private SliderViewModel sliderViewModel;
   private SliderInputAdapter sliderInputAdapter;
   private LabelView valueLabelView;
   private LabelViewModel valueLabelViewModel;
   private NumericFieldView numericFieldView;
   private NumericFieldViewModel numericViewModel;
   private NumericFieldInputAdapter numericFieldInputAdapter;
   private KeyboardInputAdapter keyboardAdapter;

   /**
    * Updates tings.
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void settings() {
      this.size(960, 420);
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
      SliderModel var1 = new SliderModel();
      var1.setMin(BigDecimal.ZERO);
      var1.setMax(new BigDecimal("100"));
      var1.setStep(new BigDecimal("0.5"));
      var1.setValue(new BigDecimal("35"));
      this.sliderViewModel = new SliderViewModel(var1);
      this.sliderViewModel.setFormatter((var0) -> "Slider: " + var0.setScale(1, RoundingMode.HALF_UP).toPlainString());
      this.sliderView = new SliderView(this, this.sliderViewModel, (float)this.width * 0.5F, 220.0F, 420.0F, 70.0F, SliderOrientation.HORIZONTAL);
      this.sliderInputAdapter = new SliderInputAdapter(this.sliderView, this.sliderViewModel);
      this.valueLabelViewModel = new LabelViewModel(new LabelModel());
      this.valueLabelView = new LabelView(this, this.valueLabelViewModel, (float)this.width * 0.5F - 88.0F, 170.0F);
      this.numericViewModel = new NumericFieldViewModel(new NumericFieldModel(new BigDecimal("35.0"), BigDecimal.ZERO, new BigDecimal("100"), new BigDecimal("0.5"), false, true, 1));
      this.numericFieldView = new NumericFieldView(this, this.numericViewModel, (float)this.width * 0.5F, 320.0F, 220.0F, 46.0F);
      this.numericFieldView.setStyle(NumericFieldDefaultStyles.standard());
      this.numericFieldInputAdapter = new NumericFieldInputAdapter(this.numericFieldView, this.numericViewModel, this.focusManager);
      Binding.bind(this.sliderViewModel::getValue, (var0) -> this.valueLabelViewModel.setText("Value: " + var0.setScale(1, RoundingMode.HALF_UP).toPlainString()), this.sliderViewModel::addListener);
      this.sliderViewModel.addListener((var1x) -> {
         if (!this.internalUpdate) {
            this.internalUpdate = true;

            try {
               this.numericViewModel.setValue(var1x);
            } finally {
               this.internalUpdate = false;
            }

         }
      });
      this.numericViewModel.setOnValueChanged((var1x) -> {
         if (!this.internalUpdate) {
            this.internalUpdate = true;

            try {
               this.sliderViewModel.setValue(var1x);
            } finally {
               this.internalUpdate = false;
            }

         }
      });
      this.inputManager.registerLayer(new BindingRootInputLayer());
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
      this.valueLabelView.draw();
      this.sliderView.draw();
      this.numericFieldView.draw();
   }

   /**
    * Performs key released.
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public void keyReleased() {
      if (key == ESC) key = 0;
      this.inputManager.dispatchKeyboard(new KeyboardEvent(KeyboardEvent.Type.RELEASE, this.key, this.keyCode, this.keyEvent != null && this.keyEvent.isShiftDown(), this.keyEvent != null && this.keyEvent.isControlDown(), this.keyEvent != null && this.keyEvent.isAltDown()));
   }

   /**
    * Performs key pressed.
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public void keyPressed() {
      if (key == ESC) key = 0;
      this.inputManager.dispatchKeyboard(new KeyboardEvent(KeyboardEvent.Type.PRESS, this.key, this.keyCode, this.keyEvent != null && this.keyEvent.isShiftDown(), this.keyEvent != null && this.keyEvent.isControlDown(), this.keyEvent != null && this.keyEvent.isAltDown()));
   }

   /**
    * Performs key typed.
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public void keyTyped() {
      if (key == ESC) key = 0;
      this.inputManager.dispatchKeyboard(new KeyboardEvent(KeyboardEvent.Type.TYPE, this.key, this.keyCode, this.keyEvent != null && this.keyEvent.isShiftDown(), this.keyEvent != null && this.keyEvent.isControlDown(), this.keyEvent != null && this.keyEvent.isAltDown()));
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
    * Performs mouse wheel.
    *
    * @param var1 parameter used by this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public void mouseWheel(MouseEvent var1) {
      this.inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.WHEEL, (float)this.mouseX, (float)this.mouseY, this.mouseButton, (float)var1.getCount(), var1.isShiftDown(), var1.isControlDown()));
   }

   private void drawTitles() {
      this.pushStyle();
      this.fill(30);
      this.textAlign(3, 3);
      this.textSize(24.0F);
      this.text("Binding Dev Sketch", (float)this.width * 0.5F, 64.0F);
      this.fill(90);
      this.textSize(14.0F);
      this.text("Bidirectional sketch binding: SliderViewModel <-> NumericFieldViewModel", (float)this.width * 0.5F, 96.0F);
      this.text("Drag the slider or commit a valid numeric value to keep slider, label and numeric field synchronized", (float)this.width * 0.5F, 118.0F);
      this.text("Intermediate numeric states remain local until commit, and a local guard prevents update loops", (float)this.width * 0.5F, 140.0F);
      this.popStyle();
   }

   private final class BindingRootInputLayer extends DefaultInputLayer {
      private BindingRootInputLayer() {
         Objects.requireNonNull(BindingDevSketch.this);
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
               BindingDevSketch.this.sliderInputAdapter.handleMouseMove(var1.getX(), var1.getY());
               BindingDevSketch.this.numericFieldInputAdapter.handleMouseMove(var1.getX(), var1.getY());
               return true;
            case DRAG:
               BindingDevSketch.this.sliderInputAdapter.handleMouseDrag(var1.getX(), var1.getY());
               BindingDevSketch.this.numericFieldInputAdapter.handleMouseDrag(var1.getX(), var1.getY());
               return true;
            case PRESS:
               boolean var2 = BindingDevSketch.this.numericFieldInputAdapter.handleMousePress(var1.getX(), var1.getY());
               if (!var2) {
                  BindingDevSketch.this.focusManager.clearFocus();
               }

               BindingDevSketch.this.sliderInputAdapter.handleMousePress(var1.getX(), var1.getY());
               return true;
            case RELEASE:
               BindingDevSketch.this.sliderInputAdapter.handleMouseRelease(var1.getX(), var1.getY());
               BindingDevSketch.this.numericFieldInputAdapter.handleMouseRelease(var1.getX(), var1.getY());
               return true;
            case WHEEL:
               BindingDevSketch.this.sliderInputAdapter.handleMouseWheel(var1.getWheelDelta(), var1.isShiftDown(), var1.isControlDown());
               BindingDevSketch.this.numericFieldInputAdapter.handleMouseWheel(var1.getWheelDelta(), var1.isShiftDown(), var1.isControlDown());
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
         BindingDevSketch.this.keyboardAdapter.handleKeyboardEvent(var1);
         return true;
      }
   }
}
