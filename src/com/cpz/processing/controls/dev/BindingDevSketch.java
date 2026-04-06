package com.cpz.processing.controls.dev;

import com.cpz.processing.controls.common.binding.Binding;
import com.cpz.processing.controls.controls.label.model.LabelModel;
import com.cpz.processing.controls.controls.label.view.LabelView;
import com.cpz.processing.controls.controls.label.viewmodel.LabelViewModel;
import com.cpz.processing.controls.controls.slider.input.SliderInputAdapter;
import com.cpz.processing.controls.controls.slider.model.SliderModel;
import com.cpz.processing.controls.controls.slider.model.SliderOrientation;
import com.cpz.processing.controls.controls.slider.viewmodel.SliderViewModel;
import com.cpz.processing.controls.controls.slider.view.SliderView;
import com.cpz.processing.controls.core.input.DefaultInputLayer;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.KeyboardEvent;
import com.cpz.processing.controls.core.input.PointerEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import processing.core.PApplet;
import processing.event.MouseEvent;

public final class BindingDevSketch extends PApplet {
   private final InputManager inputManager = new InputManager();
   private SliderView sliderView;
   private SliderViewModel sliderViewModel;
   private SliderInputAdapter sliderInputAdapter;
   private LabelView valueLabelView;
   private LabelViewModel valueLabelViewModel;

   public void settings() {
      this.size(960, 420);
      this.smooth(4);
   }

   public void setup() {
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
      this.valueLabelView = new LabelView(this, this.valueLabelViewModel, (float)this.width * 0.5F - 88.0F, 144.0F);
      Binding.bind(this.sliderViewModel::getValue, (var0) -> this.valueLabelViewModel.setText("Value: " + var0.setScale(1, RoundingMode.HALF_UP).toPlainString()), this.sliderViewModel::addListener);
      this.inputManager.registerLayer(new BindingRootInputLayer());
   }

   public void draw() {
      this.background(242);
      this.drawTitles();
      this.valueLabelView.draw();
      this.sliderView.draw();
   }

   public void keyReleased() {
      if (key == ESC) key = 0;
      this.inputManager.dispatchKeyboard(new KeyboardEvent(KeyboardEvent.Type.RELEASE, this.key, this.keyCode, this.keyEvent != null && this.keyEvent.isShiftDown(), this.keyEvent != null && this.keyEvent.isControlDown(), this.keyEvent != null && this.keyEvent.isAltDown()));
   }

   public void keyPressed() {
      if (key == ESC) key = 0;
      this.inputManager.dispatchKeyboard(new KeyboardEvent(KeyboardEvent.Type.PRESS, this.key, this.keyCode, this.keyEvent != null && this.keyEvent.isShiftDown(), this.keyEvent != null && this.keyEvent.isControlDown(), this.keyEvent != null && this.keyEvent.isAltDown()));
   }

   public void keyTyped() {
      if (key == ESC) key = 0;
      this.inputManager.dispatchKeyboard(new KeyboardEvent(KeyboardEvent.Type.TYPE, this.key, this.keyCode, this.keyEvent != null && this.keyEvent.isShiftDown(), this.keyEvent != null && this.keyEvent.isControlDown(), this.keyEvent != null && this.keyEvent.isAltDown()));
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
      this.text("Minimal unidirectional binding: SliderViewModel -> LabelViewModel", (float)this.width * 0.5F, 96.0F);
      this.text("Drag the slider or use the mouse wheel while hovering to update the label in real time", (float)this.width * 0.5F, 118.0F);
      this.popStyle();
   }

   private final class BindingRootInputLayer extends DefaultInputLayer {
      private BindingRootInputLayer() {
         Objects.requireNonNull(BindingDevSketch.this);
         super(0);
      }

      public boolean handlePointerEvent(PointerEvent var1) {
         switch (var1.getType()) {
            case MOVE:
               BindingDevSketch.this.sliderInputAdapter.handleMouseMove(var1.getX(), var1.getY());
               return true;
            case DRAG:
               BindingDevSketch.this.sliderInputAdapter.handleMouseDrag(var1.getX(), var1.getY());
               return true;
            case PRESS:
               BindingDevSketch.this.sliderInputAdapter.handleMousePress(var1.getX(), var1.getY());
               return true;
            case RELEASE:
               BindingDevSketch.this.sliderInputAdapter.handleMouseRelease(var1.getX(), var1.getY());
               return true;
            case WHEEL:
               BindingDevSketch.this.sliderInputAdapter.handleMouseWheel(var1.getWheelDelta(), var1.isShiftDown(), var1.isControlDown());
               return true;
            default:
               return false;
         }
      }

      public boolean handleKeyboardEvent(KeyboardEvent var1) {
         return false;
      }
   }
}
