package com.cpz.processing.controls.dev;

import com.cpz.processing.controls.controls.switchcontrol.config.SwitchStyleConfig;
import com.cpz.processing.controls.controls.switchcontrol.model.SwitchModel;
import com.cpz.processing.controls.controls.switchcontrol.style.ParametricSwitchStyle;
import com.cpz.processing.controls.controls.switchcontrol.style.render.CircleShapeRenderer;
import com.cpz.processing.controls.controls.switchcontrol.style.render.SvgShapeRenderer;
import com.cpz.processing.controls.controls.switchcontrol.view.SwitchInputAdapter;
import com.cpz.processing.controls.controls.switchcontrol.view.SwitchView;
import com.cpz.processing.controls.controls.switchcontrol.viewmodel.SwitchViewModel;
import com.cpz.processing.controls.core.input.DefaultInputLayer;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.KeyboardEvent;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.processing.controls.core.input.hit.CircleHitTest;
import com.cpz.processing.controls.core.input.hit.RectHitTest;
import com.cpz.processing.controls.core.util.Colors;
import java.util.Objects;
import processing.core.PApplet;

public class SwitchDevSketch extends PApplet {
   private final InputManager inputManager = new InputManager();
   private SwitchViewModel swViewModel1;
   private SwitchViewModel swViewModel2;
   private SwitchView swView1;
   private SwitchView swView2;
   private SwitchInputAdapter swInput1;
   private SwitchInputAdapter swInput2;

   public void settings() {
      this.size(760, 440);
      this.smooth(4);
   }

   public void setup() {
      this.noStroke();
      this.swViewModel1 = new SwitchViewModel(new SwitchModel());
      this.swViewModel1.setTotalStates(2);
      this.swView1 = new SwitchView(this, this.swViewModel1, 220.0F, 220.0F, 76.0F);
      this.swView1.setHitTest(new CircleHitTest());
      this.swView1.setStyle(new ParametricSwitchStyle(this.createCircularConfig()));
      this.swInput1 = new SwitchInputAdapter(this.swView1, this.swViewModel1);
      this.swViewModel2 = new SwitchViewModel(new SwitchModel());
      this.swViewModel2.setTotalStates(3);
      this.swView2 = new SwitchView(this, this.swViewModel2, 540.0F, 220.0F, 120.0F, 100.0F);
      this.swView2.setStyle(new ParametricSwitchStyle(this.createSvgConfig()));
      this.swView2.setHitTest(new RectHitTest());
      this.swInput2 = new SwitchInputAdapter(this.swView2, this.swViewModel2);
      this.inputManager.registerLayer(new SwitchRootInputLayer());
   }

   public void draw() {
      this.update();
      this.dibujar();
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
      String var1 = "Checks:\n- Click changes state on release\n- Dragging outside cancels\n- Hover/pressed modify final fill\n- SVG switch is disabled while circle is OFF\n- SVG scales using SwitchView width/height";
      this.text(var1, 24.0F, (float)(this.height - 108));
      this.text("SVG enabled: " + this.swViewModel2.isEnabled(), 420.0F, 344.0F);
      this.text("Circle hovered: " + this.swViewModel1.isHovered(), 420.0F, 366.0F);
      this.text("SVG pressed: " + this.swViewModel2.isPressed(), 420.0F, 388.0F);
      this.popStyle();
   }

   private SwitchStyleConfig createCircularConfig() {
      SwitchStyleConfig var1 = new SwitchStyleConfig();
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

   private SwitchStyleConfig createSvgConfig() {
      SwitchStyleConfig var1 = new SwitchStyleConfig();
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

   private final class SwitchRootInputLayer extends DefaultInputLayer {
      private SwitchRootInputLayer() {
         Objects.requireNonNull(SwitchDevSketch.this);
         super(0);
      }

      public boolean handlePointerEvent(PointerEvent var1) {
         switch (var1.getType()) {
            case MOVE:
            case DRAG:
               SwitchDevSketch.this.swInput1.handleMouseMove(var1.getX(), var1.getY());
               SwitchDevSketch.this.swInput2.handleMouseMove(var1.getX(), var1.getY());
               return true;
            case PRESS:
               SwitchDevSketch.this.swInput1.handleMousePress(var1.getX(), var1.getY());
               SwitchDevSketch.this.swInput2.handleMousePress(var1.getX(), var1.getY());
               return true;
            case RELEASE:
               SwitchDevSketch.this.swInput1.handleMouseRelease(var1.getX(), var1.getY());
               SwitchDevSketch.this.swInput2.handleMouseRelease(var1.getX(), var1.getY());
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
