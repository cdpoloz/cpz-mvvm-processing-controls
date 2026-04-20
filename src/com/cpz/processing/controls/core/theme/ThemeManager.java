package com.cpz.processing.controls.core.theme;

/**
 * Sketch-owned theme provider used by styles during rendering.
 *
 * <p>The manager is not a singleton and does not own any global state. Host
 * sketches create the manager they need and pass it to public styles. Calling
 * {@link #setTheme(Theme)} replaces the current theme and rebuilds the cached
 * {@link ThemeSnapshot}; styles read that snapshot during rendering.</p>
 *
 * @author CPZ
 */
public final class ThemeManager implements ThemeProvider {
   private Theme currentTheme;
   private ThemeSnapshot snapshot;

   /**
    * Creates a manager initialized with {@link LightTheme}.
    */
   public ThemeManager() {
      this(new LightTheme());
   }

   /**
    * Creates a manager initialized with the supplied theme.
    *
    * @param theme initial theme; {@code null} falls back to {@link LightTheme}
    */
   public ThemeManager(Theme theme) {
      this.setTheme(theme == null ? new LightTheme() : theme);
   }

   /**
    * Returns the cached immutable snapshot consumed by styles.
    *
    * @return current snapshot
    */
   public ThemeSnapshot getSnapshot() {
      return this.snapshot;
   }

   /**
    * Replaces the current theme and rebuilds the cached snapshot.
    *
    * @param theme new theme, never {@code null}
    * @throws IllegalArgumentException when {@code theme} is {@code null}
    */
   public void setTheme(Theme theme) {
      if (theme == null) {
         throw new IllegalArgumentException("theme must not be null");
      } else {
         this.currentTheme = theme.copy();
         this.snapshot = new ThemeSnapshot(this.currentTheme);
      }
   }
}
