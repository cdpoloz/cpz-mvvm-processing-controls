package com.cpz.processing.controls.core.theme;

/**
 * Theme component for theme snapshot.
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
public final class ThemeSnapshot {
   public final ThemeTokens tokens;

   /**
    * Creates a theme snapshot.
    *
    * @param var1 parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public ThemeSnapshot(Theme var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("theme must not be null");
      } else {
         this.tokens = var1.tokens();
      }
   }
}
