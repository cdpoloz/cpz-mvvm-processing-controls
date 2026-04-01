package com.cpz.processing.controls.core.overlay.tooltip.util;

import com.cpz.processing.controls.core.input.DefaultInputLayer;
import com.cpz.processing.controls.core.overlay.OverlayEntry;
import com.cpz.processing.controls.core.overlay.OverlayManager;
import com.cpz.processing.controls.core.overlay.tooltip.config.TooltipStyleConfig;
import com.cpz.processing.controls.core.overlay.tooltip.model.TooltipModel;
import com.cpz.processing.controls.core.overlay.tooltip.style.DefaultTooltipStyle;
import com.cpz.processing.controls.core.overlay.tooltip.view.TooltipView;
import com.cpz.processing.controls.core.overlay.tooltip.viewmodel.TooltipViewModel;
import java.util.Objects;
import java.util.function.Supplier;
import processing.core.PApplet;

public final class TooltipOverlayController {
   private final TooltipView view;
   private final TooltipViewModel viewModel;
   private final OverlayManager overlayManager;
   private final Supplier hoverSupplier;
   private final Supplier textSupplier;
   private final AnchorBoundsProvider anchorBoundsProvider;
   private final OverlayEntry overlayEntry;
   private boolean registered;

   public TooltipOverlayController(PApplet var1, OverlayManager var2, Supplier var3, Supplier var4, AnchorBoundsProvider var5) {
      this(var1, var2, var3, var4, var5, new DefaultTooltipStyle(new TooltipStyleConfig()));
   }

   public TooltipOverlayController(PApplet var1, OverlayManager var2, Supplier var3, Supplier var4, AnchorBoundsProvider var5, DefaultTooltipStyle var6) {
      this.overlayManager = var2;
      this.hoverSupplier = var3;
      this.textSupplier = var4;
      this.anchorBoundsProvider = var5;
      this.viewModel = new TooltipViewModel(new TooltipModel(""));
      this.view = new TooltipView(var1, this.viewModel);
      this.view.setStyle(var6);
      PassiveTooltipInputLayer var7 = new PassiveTooltipInputLayer();
      TooltipView var10004 = this.view;
      Objects.requireNonNull(var10004);
      this.overlayEntry = new OverlayEntry(10, var10004::draw, var7, this::hideTooltip);
   }

   public void sync() {
      boolean var1 = this.hoverSupplier != null && Boolean.TRUE.equals(this.hoverSupplier.get());
      String var2 = this.textSupplier == null ? "" : (String)this.textSupplier.get();
      if (var1 && var2 != null && !var2.isBlank()) {
         this.viewModel.setText(var2);
         this.viewModel.setVisible(true);
         this.view.setAnchorBounds(this.anchorBoundsProvider.getCenterX(), this.anchorBoundsProvider.getTopY());
         if (!this.registered) {
            this.overlayManager.register(this.overlayEntry);
            this.registered = true;
         }

      } else {
         this.hideTooltip();
      }
   }

   public void dispose() {
      this.hideTooltip();
   }

   private void hideTooltip() {
      this.viewModel.setVisible(false);
      if (this.registered) {
         this.overlayManager.unregister(this.overlayEntry);
         this.registered = false;
      }

   }

   private static final class PassiveTooltipInputLayer extends DefaultInputLayer {
      private PassiveTooltipInputLayer() {
         super(10);
      }

      public boolean isPointerCaptureEnabled() {
         return false;
      }

      public boolean isKeyboardCaptureEnabled() {
         return false;
      }
   }

   public interface AnchorBoundsProvider {
      float getCenterX();

      float getTopY();
   }
}
