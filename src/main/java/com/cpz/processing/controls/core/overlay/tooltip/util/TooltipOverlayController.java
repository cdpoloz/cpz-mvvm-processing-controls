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

/**
 * Overlay component for tooltip overlay controller.
 *
 * Responsibilities:
 * - Coordinate overlay-specific state or drawing flow.
 * - Keep overlay behavior isolated from base controls.
 *
 * Behavior:
 * - Keeps the public role isolated from unrelated concerns.
 *
 * Notes:
 * - This type is part of the public project surface.
 *
 * @author CPZ
 */
public final class TooltipOverlayController {
   private final TooltipView view;
   private final TooltipViewModel viewModel;
   private final OverlayManager overlayManager;
   private final Supplier hoverSupplier;
   private final Supplier textSupplier;
   private final AnchorBoundsProvider anchorBoundsProvider;
   private final OverlayEntry overlayEntry;
   private boolean registered;

   /**
    * Creates a tooltip overlay controller.
    *
    * @param sketch parameter used by this operation
    * @param overlayManager parameter used by this operation
    * @param hoverSupplier supplier for hover state
    * @param textSupplier supplier for tooltip text
    * @param anchorBoundsProvider parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public TooltipOverlayController(PApplet sketch, OverlayManager overlayManager, Supplier hoverSupplier, Supplier textSupplier, AnchorBoundsProvider anchorBoundsProvider) {
      this(sketch, overlayManager, hoverSupplier, textSupplier, anchorBoundsProvider, new DefaultTooltipStyle(new TooltipStyleConfig()));
   }

   /**
    * Creates a tooltip overlay controller.
    *
    * @param sketch parameter used by this operation
    * @param overlayManager parameter used by this operation
    * @param hoverSupplier supplier for hover state
    * @param textSupplier supplier for tooltip text
    * @param anchorBoundsProvider parameter used by this operation
    * @param style parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public TooltipOverlayController(PApplet sketch, OverlayManager overlayManager, Supplier hoverSupplier, Supplier textSupplier, AnchorBoundsProvider anchorBoundsProvider, DefaultTooltipStyle style) {
      this.overlayManager = overlayManager;
      this.hoverSupplier = hoverSupplier;
      this.textSupplier = textSupplier;
      this.anchorBoundsProvider = anchorBoundsProvider;
      this.viewModel = new TooltipViewModel(new TooltipModel(""));
      this.view = new TooltipView(sketch, this.viewModel);
      this.view.setStyle(style);
      PassiveTooltipInputLayer passiveTooltipInputLayer = new PassiveTooltipInputLayer();
      TooltipView view = this.view;
      Objects.requireNonNull(view);
      this.overlayEntry = new OverlayEntry(10, view::draw, passiveTooltipInputLayer, this::hideTooltip);
   }

   /**
    * Performs sync.
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public void sync() {
      boolean active = this.hoverSupplier != null && Boolean.TRUE.equals(this.hoverSupplier.get());
      String text = this.textSupplier == null ? "" : (String)this.textSupplier.get();
      if (active && text != null && !text.isBlank()) {
         this.viewModel.setText(text);
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

   /**
    * Performs dispose.
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
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

      /**
       * Returns whether pointer capture enabled.
       *
       * @return whether the current condition is satisfied
       *
       * Behavior:
       * - Returns the current value without applying side effects.
       */
      public boolean isPointerCaptureEnabled() {
         return false;
      }

      /**
       * Returns whether keyboard capture enabled.
       *
       * @return whether the current condition is satisfied
       *
       * Behavior:
       * - Returns the current value without applying side effects.
       */
      public boolean isKeyboardCaptureEnabled() {
         return false;
      }
   }

   /**
    * Overlay component for anchor bounds provider.
    *
    * Responsibilities:
    * - Coordinate overlay-specific state or drawing flow.
    * - Keep overlay behavior isolated from base controls.
    *
    * Behavior:
    * - Declares the contract without prescribing implementation details.
    *
    * Notes:
    * - This type is part of the public project surface.
    */
   public interface AnchorBoundsProvider {
      float getCenterX();

      float getTopY();
   }
}
