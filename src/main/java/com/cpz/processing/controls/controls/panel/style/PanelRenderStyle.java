package com.cpz.processing.controls.controls.panel.style;

/**
 * Resolved visual values used by the panel renderer.
 *
 * <p>This record contains effective values only. Theme fallback resolution and
 * JSON/runtime precedence are handled by {@link PanelStyle} before rendering.</p>
 *
 * @param backgroundColor effective background color
 * @param backgroundVisible whether the background is rendered
 * @param strokeColor effective border color
 * @param strokeVisible whether the border is rendered
 * @param strokeWeight effective border width
 * @param cornerRadius panel corner radius
 *
 * @author CPZ
 */
public record PanelRenderStyle(
        int backgroundColor,
        boolean backgroundVisible,
        int strokeColor,
        boolean strokeVisible,
        float strokeWeight,
        float cornerRadius
) {
}
