package com.cpz.processing.controls.controls.dropdown.style;

import com.cpz.processing.controls.controls.dropdown.config.DropDownStyleConfig;
import com.cpz.processing.controls.controls.dropdown.state.DropDownViewState;
import com.cpz.processing.controls.controls.dropdown.style.render.DefaultDropDownRenderer;
import com.cpz.processing.controls.core.style.InteractiveStyleHelper;
import com.cpz.processing.controls.core.theme.ThemeManager;
import com.cpz.processing.controls.core.theme.ThemeProvider;
import com.cpz.processing.controls.core.theme.ThemeSnapshot;
import com.cpz.processing.controls.core.theme.ThemeTokens;
import com.cpz.processing.controls.core.util.Colors;
import processing.core.PApplet;

/**
 * Style component for default drop down style.
 *
 * Responsibilities:
 * - Resolve visual values from immutable state and theme data.
 * - Keep interaction rules outside the rendering layer.
 *
 * Behavior:
 * - Does not process raw input or mutate the backing model.
 *
 * Notes:
 * - This type belongs to the visual styling pipeline.
 */
public final class DefaultDropDownStyle {
   private final DropDownStyleConfig config;
   private final DefaultDropDownRenderer renderer;
   private final ThemeProvider themeProvider;

   /**
    * Creates a default drop down style.
    *
    * @param var1 parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public DefaultDropDownStyle(DropDownStyleConfig var1) {
      this(var1, new DefaultDropDownRenderer());
   }

   /**
    * Creates a default drop down style.
    *
    * @param var1 parameter used by this operation
    * @param var2 parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public DefaultDropDownStyle(DropDownStyleConfig var1, DefaultDropDownRenderer var2) {
      if (var1 == null) {
         throw new IllegalArgumentException("config must not be null");
      } else {
         this.config = var1;
         this.renderer = var2 == null ? new DefaultDropDownRenderer() : var2;
         this.themeProvider = var1.themeProvider != null ? var1.themeProvider : new ThemeManager();
      }
   }

   /**
    * Renders the current frame.
    *
    * @param var1 parameter used by this operation
    * @param var2 parameter used by this operation
    * @param var3 parameter used by this operation
    *
    * Behavior:
    * - Uses already available state and does not define business rules.
    */
   public void render(PApplet var1, DropDownViewState var2, ThemeSnapshot var3) {
      this.renderer.render(var1, var2, this.resolveRenderStyle(var2, var3));
   }

   /**
    * Returns corner radius.
    *
    * @return current corner radius
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public float getCornerRadius() {
      return this.config.cornerRadius;
   }

   /**
    * Returns list corner radius.
    *
    * @return current list corner radius
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public float getListCornerRadius() {
      return this.config.listCornerRadius;
   }

   /**
    * Returns stroke weight.
    *
    * @return current stroke weight
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public float getStrokeWeight() {
      return this.config.strokeWeight;
   }

   /**
    * Returns focused stroke weight.
    *
    * @return current focused stroke weight
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public float getFocusedStrokeWeight() {
      return this.config.focusedStrokeWeight;
   }

   /**
    * Returns item height.
    *
    * @return current item height
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public float getItemHeight() {
      return this.config.itemHeight;
   }

   /**
    * Returns text padding.
    *
    * @return current text padding
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public float getTextPadding() {
      return this.config.textPadding;
   }

   /**
    * Returns arrow padding.
    *
    * @return current arrow padding
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public float getArrowPadding() {
      return this.config.arrowPadding;
   }

   /**
    * Returns max visible items.
    *
    * @return current max visible items
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public int getMaxVisibleItems() {
      return Math.max(1, this.config.maxVisibleItems);
   }

   /**
    * Resolves render style.
    *
    * @param var1 parameter used by this operation
    * @param var2 parameter used by this operation
    * @return resolved render style
    *
    * Behavior:
    * - Produces the public result required by the surrounding pipeline.
    */
   public DropDownRenderStyle resolveRenderStyle(DropDownViewState var1, ThemeSnapshot var2) {
      ThemeTokens var3 = var2.tokens;
      int var4 = this.resolveColor(var3.surface, this.config.baseFillOverride);
      int var5 = this.resolveColor(var3.surfaceVariant, this.config.listFillOverride);
      int var6 = this.resolveColor(var3.onSurface, this.config.textOverride);
      int var7 = this.resolveColor(var3.border, this.config.borderOverride);
      int var8 = this.resolveColor(var3.primary, this.config.focusedBorderOverride);
      int var9 = this.resolveColor(var3.hoverOverlay, this.config.hoverItemOverlayOverride);
      int var10 = this.resolveColor(Colors.alpha(36, var3.primary), this.config.selectedItemOverlayOverride);
      int var11 = this.resolveBaseFill(var4, var9, var3.pressedOverlay, var1);
      int var12 = var1.focused() ? this.blend(var7, var8, 0.35F) : var7;
      float var13 = !var1.hovered() && !var1.focused() ? this.config.strokeWeight : this.config.focusedStrokeWeight;
      int var14 = InteractiveStyleHelper.applyOverlay(var5, var9);
      int var15 = InteractiveStyleHelper.applyOverlay(var5, var10);
      int var16 = this.config.disabledAlpha != null ? this.config.disabledAlpha : var3.disabledAlpha;
      return new DropDownRenderStyle(InteractiveStyleHelper.applyDisabledAlpha(var11, var1.enabled(), var16), InteractiveStyleHelper.applyDisabledAlpha(var5, var1.enabled(), var16), InteractiveStyleHelper.applyDisabledAlpha(var14, var1.enabled(), var16), InteractiveStyleHelper.applyDisabledAlpha(var15, var1.enabled(), var16), InteractiveStyleHelper.applyDisabledAlpha(var12, var1.enabled(), var16), var13, InteractiveStyleHelper.applyDisabledAlpha(var6, var1.enabled(), var16), InteractiveStyleHelper.applyDisabledAlpha(var6, var1.enabled(), var16), var1.cornerRadius(), var1.listCornerRadius(), this.config.textSize, var1.itemHeight(), var1.textPadding(), var1.arrowPadding(), Math.max(1, var1.maxVisibleItems()), this.config.font);
   }

   private int resolveBaseFill(int var1, int var2, int var3, DropDownViewState var4) {
      return InteractiveStyleHelper.resolveFillColorWithOverlays(var1, var2, var3, var4.hovered(), var4.pressed());
   }

   private int resolveColor(int var1, Integer var2) {
      return var2 != null ? var2 : var1;
   }

   private int blend(int var1, int var2, float var3) {
      float var4 = Math.max(0.0F, Math.min(1.0F, var3));
      int var5 = this.blendChannel(var1 >>> 24 & 255, var2 >>> 24 & 255, var4);
      int var6 = this.blendChannel(var1 >>> 16 & 255, var2 >>> 16 & 255, var4);
      int var7 = this.blendChannel(var1 >>> 8 & 255, var2 >>> 8 & 255, var4);
      int var8 = this.blendChannel(var1 & 255, var2 & 255, var4);
      return Colors.argb(var5, var6, var7, var8);
   }

   private int blendChannel(int var1, int var2, float var3) {
      return Math.round((float)var1 + (float)(var2 - var1) * var3);
   }

   /**
    * Returns theme snapshot.
    *
    * @return current theme snapshot
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public ThemeSnapshot getThemeSnapshot() {
      return this.themeProvider.getSnapshot();
   }
}
