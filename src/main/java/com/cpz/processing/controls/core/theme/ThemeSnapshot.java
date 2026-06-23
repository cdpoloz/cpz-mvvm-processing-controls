package com.cpz.processing.controls.core.theme;

/**
 * Immutable rendering snapshot of a theme.
 *
 * <p>Styles consume snapshots instead of resolving theme objects during every
 * render operation. {@link ThemeManager} rebuilds this snapshot only when its
 * current theme changes.</p>
 *
 * @author CPZ
 */
public final class ThemeSnapshot {
   public final ThemeTokens tokens;

   /**
    * Captures the tokens of the supplied theme.
    *
    * @param theme source theme
    * @throws IllegalArgumentException when {@code theme} is {@code null}
    */
   public ThemeSnapshot(Theme theme) {
      if (theme == null) {
         throw new IllegalArgumentException("theme must not be null");
      } else {
         this.tokens = theme.tokens();
      }
   }
}
