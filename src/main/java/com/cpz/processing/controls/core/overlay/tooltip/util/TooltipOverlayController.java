package com.cpz.processing.controls.core.overlay.tooltip.util;

import com.cpz.processing.controls.core.input.DefaultInputLayer;
import com.cpz.processing.controls.core.overlay.OverlayEntry;
import com.cpz.processing.controls.core.overlay.OverlayManager;
import com.cpz.processing.controls.core.overlay.tooltip.Tooltip;
import com.cpz.processing.controls.core.overlay.tooltip.TooltipBounds;
import com.cpz.processing.controls.core.overlay.tooltip.TooltipTarget;
import com.cpz.processing.controls.core.overlay.tooltip.config.TooltipStyleConfig;
import com.cpz.processing.controls.core.overlay.tooltip.model.TooltipModel;
import com.cpz.processing.controls.core.overlay.tooltip.style.DefaultTooltipStyle;
import com.cpz.processing.controls.core.overlay.tooltip.view.TooltipView;
import com.cpz.processing.controls.core.overlay.tooltip.viewmodel.TooltipViewModel;
import java.util.ArrayList;
import java.util.List;
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
   private final OverlayEntry overlayEntry;
   private final List<TooltipTarget> targets = new ArrayList<>();
   private boolean registered;
   private boolean pointerKnown;
   private float mouseX;
   private float mouseY;
   private TooltipTarget activeTarget;
   private boolean legacySupplierMode;
   private Supplier legacyHoverSupplier;
   private Supplier legacyTextSupplier;
   private AnchorBoundsProvider legacyAnchorBoundsProvider;
   private DefaultTooltipStyle legacyStyle;

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
      this(sketch, overlayManager);
      this.legacySupplierMode = true;
      this.legacyHoverSupplier = hoverSupplier;
      this.legacyTextSupplier = textSupplier;
      this.legacyAnchorBoundsProvider = anchorBoundsProvider;
      this.legacyStyle = style == null ? new DefaultTooltipStyle(new TooltipStyleConfig()) : style;
      this.view.setStyle(this.legacyStyle);
   }

   public TooltipOverlayController(PApplet sketch, OverlayManager overlayManager) {
      Objects.requireNonNull(sketch, "sketch");
      this.overlayManager = Objects.requireNonNull(overlayManager, "overlayManager");
      this.viewModel = new TooltipViewModel(new TooltipModel(""));
      this.view = new TooltipView(sketch, this.viewModel);
      PassiveTooltipInputLayer passiveTooltipInputLayer = new PassiveTooltipInputLayer();
      TooltipView tooltipView = this.view;
      this.overlayEntry = new OverlayEntry(10, tooltipView::draw, passiveTooltipInputLayer, this::hideTooltip);
   }

   /**
    * Performs sync.
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public void sync() {
      if (this.legacySupplierMode) {
         this.syncLegacySuppliers();
         return;
      }
      if (this.pointerKnown) {
         this.showIfMouseOver(this.mouseX, this.mouseY);
      } else {
         this.hideTooltip();
      }
   }

   public void registerTarget(TooltipTarget target) {
      if (target != null && !this.targets.contains(target)) {
         this.targets.add(target);
      }
   }

   public void unregisterTarget(TooltipTarget target) {
      this.targets.remove(target);
      this.sync();
   }

   public void clearTargets() {
      this.targets.clear();
      this.hideTooltip();
   }

   public void showIfMouseOver(float mouseX, float mouseY) {
      this.pointerKnown = true;
      this.mouseX = mouseX;
      this.mouseY = mouseY;
      TooltipTarget target = this.findTarget(mouseX, mouseY);
      if (target == null) {
         this.hideTooltip();
         return;
      }

      Tooltip tooltip = target.getTooltip();
      TooltipBounds bounds = target.getTooltipBounds();
      this.activeTarget = target;
      this.viewModel.setText(tooltip.getText());
      this.viewModel.setVisible(true);
      this.viewModel.setEnabled(tooltip.isEnabled());
      this.view.setStyle(new DefaultTooltipStyle(tooltip.getStyleConfig()));
      this.view.setTargetBounds(bounds);
      if (!this.registered) {
         this.overlayManager.register(this.overlayEntry);
         this.registered = true;
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
      this.targets.clear();
   }

   private void hideTooltip() {
      this.viewModel.setVisible(false);
      this.activeTarget = null;
      if (this.registered) {
         this.overlayManager.unregister(this.overlayEntry);
         this.registered = false;
      }

   }

   private void syncLegacySuppliers() {
      boolean active = this.legacyHoverSupplier != null && Boolean.TRUE.equals(this.legacyHoverSupplier.get());
      String text = this.legacyTextSupplier == null ? "" : (String)this.legacyTextSupplier.get();
      if (!active || text == null || text.isBlank() || this.legacyAnchorBoundsProvider == null) {
         this.hideTooltip();
         return;
      }

      this.viewModel.setText(text);
      this.viewModel.setVisible(true);
      this.viewModel.setEnabled(true);
      this.view.setStyle(this.legacyStyle);
      this.view.setTargetBounds(new TooltipBounds(this.legacyAnchorBoundsProvider.getCenterX() - 0.5F, this.legacyAnchorBoundsProvider.getTopY(), 1.0F, 1.0F));
      if (!this.registered) {
         this.overlayManager.register(this.overlayEntry);
         this.registered = true;
      }
   }

   TooltipTarget getActiveTarget() {
      return this.activeTarget;
   }

   private TooltipTarget findTarget(float mouseX, float mouseY) {
      for(int i = this.targets.size() - 1; i >= 0; --i) {
         TooltipTarget target = this.targets.get(i);
         if (this.canShow(target, mouseX, mouseY)) {
            return target;
         }
      }
      return null;
   }

   private boolean canShow(TooltipTarget target, float mouseX, float mouseY) {
      if (target == null || !target.isTooltipTargetVisible() || !target.isTooltipTargetEnabled()) {
         return false;
      }
      Tooltip tooltip = target.getTooltip();
      if (tooltip == null || !tooltip.isEnabled() || tooltip.getText() == null || tooltip.getText().isBlank()) {
         return false;
      }
      TooltipBounds bounds = target.getTooltipBounds();
      return bounds != null && bounds.contains(mouseX, mouseY);
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
