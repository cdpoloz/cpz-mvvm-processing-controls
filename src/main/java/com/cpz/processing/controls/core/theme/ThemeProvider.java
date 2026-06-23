package com.cpz.processing.controls.core.theme;

/**
 * Provider contract used by styles to obtain the current theme snapshot.
 *
 * <p>The interface allows sketches to pass a {@link ThemeManager} or another
 * explicit provider into styles without introducing global theme state.</p>
 *
 * @author CPZ
 */
public interface ThemeProvider {
   /**
    * Returns the current snapshot for visual resolution.
    *
    * @return theme snapshot
    */
   ThemeSnapshot getSnapshot();
}
