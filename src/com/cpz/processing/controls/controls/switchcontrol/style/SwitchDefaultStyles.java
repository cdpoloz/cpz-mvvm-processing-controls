package com.cpz.processing.controls.controls.switchcontrol.style;

import com.cpz.processing.controls.controls.switchcontrol.config.SwitchStyleConfig;
import com.cpz.processing.controls.controls.switchcontrol.style.render.CircleShapeRenderer;

public final class SwitchDefaultStyles {
   private SwitchDefaultStyles() {
   }

   public static SwitchStyle circular() {
      SwitchStyleConfig var0 = new SwitchStyleConfig();
      var0.shape = new CircleShapeRenderer();
      var0.strokeWidth = 2.0F;
      var0.strokeWidthHover = 3.0F;
      return new ParametricSwitchStyle(var0);
   }
}
