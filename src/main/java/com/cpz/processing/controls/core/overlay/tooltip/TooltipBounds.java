package com.cpz.processing.controls.core.overlay.tooltip;

/**
 * Rectangular tooltip target bounds in sketch coordinates.
 *
 * <p>The coordinates use the Processing-friendly top-left convention.</p>
 *
 * @param x left coordinate
 * @param y top coordinate
 * @param width rectangle width
 * @param height rectangle height
 * @author CPZ
 */
public record TooltipBounds(float x, float y, float width, float height) {
    public TooltipBounds {
        width = Math.max(0.0F, width);
        height = Math.max(0.0F, height);
    }

    /**
     * Returns whether the point is inside the bounds.
     *
     * @param pointX point x coordinate
     * @param pointY point y coordinate
     * @return {@code true} when the point is inside
     */
    public boolean contains(float pointX, float pointY) {
        return pointX >= this.x
                && pointX <= this.x + this.width
                && pointY >= this.y
                && pointY <= this.y + this.height;
    }

    /**
     * Returns the horizontal center.
     *
     * @return center x coordinate
     */
    public float centerX() {
        return this.x + this.width * 0.5F;
    }

    /**
     * Returns the vertical center.
     *
     * @return center y coordinate
     */
    public float centerY() {
        return this.y + this.height * 0.5F;
    }

    /**
     * Returns the lower edge.
     *
     * @return bottom y coordinate
     */
    public float bottomY() {
        return this.y + this.height;
    }
}
