package com.cpz.processing.controls.core.theme;

/**
 * Theme component for theme manager.
 *
 * Responsibilities:
 * - Represent theme data or theme access for the rendering pipeline.
 * - Keep theme concerns explicit and reusable.
 *
 * Behavior:
 * - Keeps the public role isolated from unrelated concerns.
 *
 * Notes:
 * - This type is part of the public project surface.
 */
public final class ThemeManager implements ThemeProvider {
   private Theme currentTheme;
   private ThemeSnapshot snapshot;

   /**
    * Creates a theme manager.
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public ThemeManager() {
      this(new LightTheme());
   }

   /**
    * Creates a theme manager.
    *
    * @param var1 parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public ThemeManager(Theme var1) {
      this.setTheme(var1 == null ? new LightTheme() : var1);
   }

   /**
    * Returns snapshot.
    *
    * @return current snapshot
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public ThemeSnapshot getSnapshot() {
      return this.snapshot;
   }

   /**
    * Updates theme.
    *
    * @param var1 new theme
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setTheme(Theme var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("Theme null");
      } else {
         this.currentTheme = var1.copy();
         this.snapshot = new ThemeSnapshot(this.currentTheme);
      }
   }
}
