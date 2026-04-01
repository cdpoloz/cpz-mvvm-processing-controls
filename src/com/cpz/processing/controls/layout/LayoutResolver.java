package com.cpz.processing.controls.layout;

public final class LayoutResolver {

    private LayoutResolver() {
    }

    public static float resolveX(LayoutConfig config, float width, float parentWidth) {
        float x = config.getNormalizedX() * parentWidth;

        switch (config.getAnchor()) {
            case TOP_CENTER:
            case CENTER:
            case BOTTOM_CENTER:
                return x - width / 2f;

            case TOP_RIGHT:
            case BOTTOM_RIGHT:
                return x - width;

            default:
                return x;
        }
    }

    public static float resolveY(LayoutConfig config, float height, float parentHeight) {
        float y = config.getNormalizedY() * parentHeight;

        switch (config.getAnchor()) {
            case CENTER:
                return y - height / 2f;

            case BOTTOM_CENTER:
            case BOTTOM_LEFT:
            case BOTTOM_RIGHT:
                return y - height;

            case TOP_CENTER:
            case TOP_LEFT:
            case TOP_RIGHT:
                return y;

            default:
                return y;
        }
    }
}
