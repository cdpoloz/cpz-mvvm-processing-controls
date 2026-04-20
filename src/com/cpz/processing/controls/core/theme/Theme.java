package com.cpz.processing.controls.core.theme;

/**
 * Immutable theme definition backed by rendering tokens.
 *
 * <p>A theme is value-like: it copies tokens on construction and returns copies
 * to callers. {@link ThemeManager} owns the active theme for a sketch and turns
 * it into a cached {@link ThemeSnapshot} for styles.</p>
 *
 * @author CPZ
 */
public class Theme {
   private final ThemeTokens tokens;

   /**
    * Creates a theme from the supplied token set.
    *
    * @param tokens source tokens
    * @throws IllegalArgumentException when {@code tokens} is {@code null}
    */
   public Theme(ThemeTokens tokens) {
      if (tokens == null) {
         throw new IllegalArgumentException("tokens must not be null");
      } else {
         this.tokens = tokens.copy();
      }
   }

   /**
    * Returns a defensive copy of this theme's tokens.
    *
    * @return copied token set
    */
   public ThemeTokens tokens() {
      return this.tokens.copy();
   }

   /**
    * Returns a copy of this theme.
    *
    * @return copied theme
    */
   public Theme copy() {
      return new Theme(this.tokens);
   }
}
