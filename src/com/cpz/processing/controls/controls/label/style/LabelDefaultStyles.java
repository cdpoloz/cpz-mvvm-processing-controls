package com.cpz.processing.controls.controls.label.style;

import com.cpz.processing.controls.controls.label.config.LabelStyleConfig;

public final class LabelDefaultStyles {
   private LabelDefaultStyles() {
   }

   public static LabelStyle defaultText() {
      LabelStyleConfig var0 = new LabelStyleConfig();
      var0.font = null;
      var0.textSize = 12.0F;
      var0.textColor = null;
      var0.lineSpacingMultiplier = 1.0F;
      var0.alignX = HorizontalAlign.START;
      var0.alignY = VerticalAlign.BASELINE;
      var0.disabledAlpha = null;
      return new DefaultLabelStyle(var0);
   }
}
