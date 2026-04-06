package com.cpz.processing.controls.core.theme;

/**
 * Theme component for theme.
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
public class Theme {
   private final ThemeTokens tokens;

   /**
    * Creates a theme.
    *
    * @param var1 parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public Theme(ThemeTokens var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("tokens must not be null");
      } else {
         this.tokens = var1.copy();
      }
   }

   /**
    * Performs tokens.
    *
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public ThemeTokens tokens() {
      return this.tokens.copy();
   }

   /**
    * Returns a copy of state.
    *
    * @return copied value
    *
    * Behavior:
    * - Produces the public result required by the surrounding pipeline.
    */
   public Theme copy() {
      return new Theme(this.tokens);
   }
}
