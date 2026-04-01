package com.cpz.processing.controls.dev;

import com.cpz.processing.controls.controls.slider.config.SliderStyleConfig;
import com.cpz.processing.controls.controls.slider.model.SliderModel;
import com.cpz.processing.controls.controls.slider.model.SliderOrientation;
import com.cpz.processing.controls.controls.slider.model.SnapMode;
import com.cpz.processing.controls.controls.slider.style.SliderStyle;
import com.cpz.processing.controls.controls.slider.style.SvgColorMode;
import com.cpz.processing.controls.controls.slider.view.SliderInputAdapter;
import com.cpz.processing.controls.controls.slider.view.SliderView;
import com.cpz.processing.controls.controls.slider.viewmodel.SliderViewModel;
import com.cpz.processing.controls.core.input.DefaultInputLayer;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.KeyboardEvent;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.processing.controls.core.util.Colors;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.function.Function;
import processing.core.PApplet;
import processing.core.PShape;
import processing.event.MouseEvent;

public class SliderDevSketch extends PApplet {
   private final InputManager inputManager = new InputManager();
   private SliderView horizontalSliderView;
   private SliderView verticalSliderView;
   private SliderView fallbackSliderView;
   private SliderView releaseSnapSliderView;
   private SliderViewModel horizontalSliderViewModel;
   private SliderViewModel verticalSliderViewModel;
   private SliderViewModel fallbackSliderViewModel;
   private SliderViewModel releaseSnapSliderViewModel;
   private SliderInputAdapter horizontalInputAdapter;
   private SliderInputAdapter verticalInputAdapter;
   private SliderInputAdapter fallbackInputAdapter;
   private SliderInputAdapter releaseSnapInputAdapter;

   public void settings() {
      this.size(1100, 620);
      this.smooth(4);
   }

   public void setup() {
      PShape var1 = this.loadSvg("data/img/test.svg");
      PShape var2 = this.loadSvg("data/img/test.svg");
      this.horizontalSliderViewModel = new SliderViewModel(this.createModel(new BigDecimal("0"), new BigDecimal("1"), new BigDecimal("0.01"), new BigDecimal("0.35")));
      this.horizontalSliderViewModel.setFormatter((var0) -> "Horizontal: " + var0.setScale(2, RoundingMode.HALF_UP).toPlainString());
      this.horizontalSliderView = new SliderView(this, this.horizontalSliderViewModel, (float)this.width * 0.5F, (float)this.height * 0.33F, 420.0F, 72.0F, SliderOrientation.HORIZONTAL);
      this.horizontalSliderView.setStyle(new SliderStyle(this.createHorizontalStyle(var1)));
      this.horizontalInputAdapter = new SliderInputAdapter(this.horizontalSliderView, this.horizontalSliderViewModel);
      this.verticalSliderViewModel = new SliderViewModel(this.createModel(new BigDecimal("-10"), new BigDecimal("10"), new BigDecimal("0.5"), new BigDecimal("2.0")));
      this.verticalSliderViewModel.setFormatter((var0) -> "Vertical: " + var0.setScale(1, RoundingMode.HALF_UP).toPlainString());
      this.verticalSliderView = new SliderView(this, this.verticalSliderViewModel, (float)this.width * 0.72F, (float)this.height * 0.58F, 96.0F, 300.0F, SliderOrientation.VERTICAL);
      this.verticalSliderView.setStyle(new SliderStyle(this.createVerticalStyle(var2)));
      this.verticalInputAdapter = new SliderInputAdapter(this.verticalSliderView, this.verticalSliderViewModel);
      this.fallbackSliderViewModel = new SliderViewModel(this.createModel(new BigDecimal("0"), new BigDecimal("100"), new BigDecimal("5"), new BigDecimal("40")));
      this.fallbackSliderViewModel.setFormatter((Function)null);
      this.fallbackSliderView = new SliderView(this, this.fallbackSliderViewModel, (float)this.width * 0.34F, (float)this.height * 0.72F, 360.0F, 68.0F, SliderOrientation.HORIZONTAL);
      this.fallbackSliderView.setStyle(new SliderStyle(this.createFallbackStyle()));
      this.fallbackInputAdapter = new SliderInputAdapter(this.fallbackSliderView, this.fallbackSliderViewModel);
      this.releaseSnapSliderViewModel = new SliderViewModel(this.createModel(new BigDecimal("0"), new BigDecimal("1"), new BigDecimal("0.1"), new BigDecimal("0.4")));
      this.releaseSnapSliderViewModel.setSnapMode(SnapMode.ON_RELEASE);
      this.releaseSnapSliderViewModel.setFormatter((var0) -> "Release snap: " + var0.setScale(1, RoundingMode.HALF_UP).toPlainString());
      this.releaseSnapSliderView = new SliderView(this, this.releaseSnapSliderViewModel, (float)this.width * 0.5F, (float)this.height * 0.88F, 420.0F, 60.0F, SliderOrientation.HORIZONTAL);
      this.releaseSnapSliderView.setStyle(new SliderStyle(this.createHorizontalStyle(var1)));
      this.releaseSnapInputAdapter = new SliderInputAdapter(this.releaseSnapSliderView, this.releaseSnapSliderViewModel);
      this.inputManager.registerLayer(new SliderRootInputLayer());
   }

   public void draw() {
      this.background(24);
      this.drawTitles();
      this.horizontalSliderView.draw();
      this.verticalSliderView.draw();
      this.fallbackSliderView.draw();
      this.releaseSnapSliderView.draw();
      this.drawDebugPanel();
   }

   public void keyReleased() {
      if (key == ESC) key = 0;
      this.inputManager.dispatchKeyboard(new KeyboardEvent(KeyboardEvent.Type.RELEASE, this.key, this.keyCode, this.keyEvent != null && this.keyEvent.isShiftDown(), this.keyEvent != null && this.keyEvent.isControlDown(), this.keyEvent != null && this.keyEvent.isAltDown()));
   }

   public void keyPressed() {
      if (key == ESC) key = 0;
   }

   public void keyTyped() {
      if (key == ESC) key = 0;
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
      this.fill(232);
      this.textAlign(3, 3);
      this.textSize(18.0F);
      this.text("Slider SVG Render Style", (float)this.width * 0.5F, 72.0F);
      this.text("Slider SVG Original", (float)this.width * 0.72F, 72.0F);
      this.text("Slider Fallback Geometry", (float)this.width * 0.34F, (float)this.height * 0.62F);
      this.text("Slider Snap On Release", (float)this.width * 0.5F, (float)this.height * 0.8F);
      this.popStyle();
   }

   private void drawDebugPanel() {
      this.pushStyle();
      this.fill(220);
      this.textAlign(37, 101);
      this.text("Checks:", 40.0F, 420.0F);
      this.text("- Drag remains active outside bounds once started", 40.0F, 446.0F);
      this.text("- Mouse wheel uses step / SHIFT x10 / CTRL x0.1", 40.0F, 468.0F);
      this.text("- Null formatter falls back to step-based plain text", 40.0F, 490.0F);
      this.text("- Top slider uses SVG with render-style colors", 40.0F, 512.0F);
      this.text("- Right slider preserves original SVG colors", 40.0F, 534.0F);
      this.text("- Bottom slider draws ellipse fallback with no SVG", 40.0F, 556.0F);
      this.text("- Lower slider uses SnapMode.ON_RELEASE for visual check", 40.0F, 578.0F);
      this.text("Horizontal value: " + this.horizontalSliderViewModel.getFormattedValue(), 560.0F, 420.0F);
      this.text("Vertical value: " + this.verticalSliderViewModel.getFormattedValue(), 560.0F, 446.0F);
      this.text("Fallback value: " + this.fallbackSliderViewModel.getFormattedValue(), 560.0F, 472.0F);
      this.text("Release snap value: " + this.releaseSnapSliderViewModel.getFormattedValue(), 560.0F, 498.0F);
      this.text("Horizontal dragging: " + this.horizontalSliderViewModel.isDragging(), 560.0F, 530.0F);
      this.text("Vertical dragging: " + this.verticalSliderViewModel.isDragging(), 560.0F, 556.0F);
      this.text("Fallback dragging: " + this.fallbackSliderViewModel.isDragging(), 560.0F, 582.0F);
      this.popStyle();
   }

   private SliderModel createModel(BigDecimal var1, BigDecimal var2, BigDecimal var3, BigDecimal var4) {
      SliderModel var5 = new SliderModel();
      var5.setMin(var1);
      var5.setMax(var2);
      var5.setStep(var3);
      var5.setValue(var4);
      return var5;
   }

   private SliderStyleConfig createHorizontalStyle(PShape var1) {
      SliderStyleConfig var2 = new SliderStyleConfig();
      var2.trackColor = Colors.rgb(62, 72, 86);
      var2.trackHoverColor = Colors.rgb(86, 98, 116);
      var2.trackPressedColor = Colors.rgb(44, 52, 64);
      var2.trackStrokeColor = Colors.gray(220);
      var2.trackStrokeWeight = 1.5F;
      var2.trackStrokeWeightHover = 2.5F;
      var2.trackThickness = 10.0F;
      var2.activeTrackColor = Colors.rgb(56, 159, 232);
      var2.activeTrackHoverColor = Colors.rgb(98, 184, 244);
      var2.activeTrackPressedColor = Colors.rgb(38, 124, 184);
      var2.thumbColor = Colors.rgb(255, 255, 255);
      var2.thumbHoverColor = Colors.rgb(198, 234, 255);
      var2.thumbPressedColor = Colors.rgb(168, 214, 244);
      var2.thumbStrokeColor = Colors.rgb(56, 159, 232);
      var2.thumbStrokeWeight = 2.0F;
      var2.thumbStrokeWeightHover = 3.0F;
      var2.thumbSize = 34.0F;
      var2.textColor = Colors.gray(245);
      var2.disabledAlpha = 90;
      var2.showValueText = true;
      var2.thumbShape = var1;
      var2.svgColorMode = SvgColorMode.USE_RENDER_STYLE;
      return var2;
   }

   private SliderStyleConfig createVerticalStyle(PShape var1) {
      SliderStyleConfig var2 = new SliderStyleConfig();
      var2.trackColor = Colors.rgb(70, 70, 70);
      var2.trackHoverColor = Colors.rgb(92, 92, 92);
      var2.trackPressedColor = Colors.rgb(48, 48, 48);
      var2.trackStrokeColor = Colors.gray(240);
      var2.trackStrokeWeight = 1.5F;
      var2.trackStrokeWeightHover = 2.5F;
      var2.trackThickness = 12.0F;
      var2.activeTrackColor = Colors.rgb(242, 149, 68);
      var2.activeTrackHoverColor = Colors.rgb(250, 177, 112);
      var2.activeTrackPressedColor = Colors.rgb(216, 124, 48);
      var2.thumbColor = Colors.gray(255);
      var2.thumbHoverColor = Colors.gray(255);
      var2.thumbPressedColor = Colors.gray(255);
      var2.thumbStrokeColor = Colors.rgb(242, 149, 68);
      var2.thumbStrokeWeight = 2.0F;
      var2.thumbStrokeWeightHover = 3.0F;
      var2.thumbSize = 42.0F;
      var2.textColor = Colors.gray(245);
      var2.disabledAlpha = 90;
      var2.showValueText = true;
      var2.thumbShape = var1;
      var2.svgColorMode = SvgColorMode.USE_SVG_ORIGINAL;
      return var2;
   }

   private SliderStyleConfig createFallbackStyle() {
      SliderStyleConfig var1 = new SliderStyleConfig();
      var1.trackColor = Colors.rgb(58, 64, 72);
      var1.trackHoverColor = Colors.rgb(82, 90, 102);
      var1.trackPressedColor = Colors.rgb(42, 46, 54);
      var1.trackStrokeColor = Colors.gray(225);
      var1.trackStrokeWeight = 1.5F;
      var1.trackStrokeWeightHover = 2.5F;
      var1.trackThickness = 9.0F;
      var1.activeTrackColor = Colors.rgb(120, 210, 92);
      var1.activeTrackHoverColor = Colors.rgb(154, 228, 132);
      var1.activeTrackPressedColor = Colors.rgb(88, 176, 62);
      var1.thumbColor = Colors.rgb(255, 240, 120);
      var1.thumbHoverColor = Colors.rgb(255, 248, 164);
      var1.thumbPressedColor = Colors.rgb(244, 220, 72);
      var1.thumbStrokeColor = Colors.rgb(28, 32, 36);
      var1.thumbStrokeWeight = 2.0F;
      var1.thumbStrokeWeightHover = 3.0F;
      var1.thumbSize = 28.0F;
      var1.textColor = Colors.gray(245);
      var1.disabledAlpha = 90;
      var1.showValueText = true;
      var1.thumbShape = null;
      var1.svgColorMode = SvgColorMode.USE_RENDER_STYLE;
      return var1;
   }

   private PShape loadSvg(String var1) {
      PShape var2 = this.loadShape(var1);
      if (var2 == null && var1.startsWith("data/")) {
         var2 = this.loadShape(var1.substring("data/".length()));
      }

      return var2;
   }

   private final class SliderRootInputLayer extends DefaultInputLayer {
      private SliderRootInputLayer() {
         Objects.requireNonNull(SliderDevSketch.this);
         super(0);
      }

      public boolean handlePointerEvent(PointerEvent var1) {
         switch (var1.getType()) {
            case MOVE:
               SliderDevSketch.this.horizontalInputAdapter.handleMouseMove(var1.getX(), var1.getY());
               SliderDevSketch.this.verticalInputAdapter.handleMouseMove(var1.getX(), var1.getY());
               SliderDevSketch.this.fallbackInputAdapter.handleMouseMove(var1.getX(), var1.getY());
               SliderDevSketch.this.releaseSnapInputAdapter.handleMouseMove(var1.getX(), var1.getY());
               return true;
            case DRAG:
               SliderDevSketch.this.horizontalInputAdapter.handleMouseDrag(var1.getX(), var1.getY());
               SliderDevSketch.this.verticalInputAdapter.handleMouseDrag(var1.getX(), var1.getY());
               SliderDevSketch.this.fallbackInputAdapter.handleMouseDrag(var1.getX(), var1.getY());
               SliderDevSketch.this.releaseSnapInputAdapter.handleMouseDrag(var1.getX(), var1.getY());
               return true;
            case PRESS:
               SliderDevSketch.this.horizontalInputAdapter.handleMousePress(var1.getX(), var1.getY());
               SliderDevSketch.this.verticalInputAdapter.handleMousePress(var1.getX(), var1.getY());
               SliderDevSketch.this.fallbackInputAdapter.handleMousePress(var1.getX(), var1.getY());
               SliderDevSketch.this.releaseSnapInputAdapter.handleMousePress(var1.getX(), var1.getY());
               return true;
            case RELEASE:
               SliderDevSketch.this.horizontalInputAdapter.handleMouseRelease(var1.getX(), var1.getY());
               SliderDevSketch.this.verticalInputAdapter.handleMouseRelease(var1.getX(), var1.getY());
               SliderDevSketch.this.fallbackInputAdapter.handleMouseRelease(var1.getX(), var1.getY());
               SliderDevSketch.this.releaseSnapInputAdapter.handleMouseRelease(var1.getX(), var1.getY());
               return true;
            case WHEEL:
               SliderDevSketch.this.horizontalInputAdapter.handleMouseWheel(var1.getWheelDelta(), var1.isShiftDown(), var1.isControlDown());
               SliderDevSketch.this.verticalInputAdapter.handleMouseWheel(var1.getWheelDelta(), var1.isShiftDown(), var1.isControlDown());
               SliderDevSketch.this.fallbackInputAdapter.handleMouseWheel(var1.getWheelDelta(), var1.isShiftDown(), var1.isControlDown());
               SliderDevSketch.this.releaseSnapInputAdapter.handleMouseWheel(var1.getWheelDelta(), var1.isShiftDown(), var1.isControlDown());
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
