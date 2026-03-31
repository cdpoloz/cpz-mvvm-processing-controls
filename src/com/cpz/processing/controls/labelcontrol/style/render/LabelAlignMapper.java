package com.cpz.processing.controls.labelcontrol.style.render;

import com.cpz.processing.controls.labelcontrol.style.HorizontalAlign;
import com.cpz.processing.controls.labelcontrol.style.VerticalAlign;
import processing.core.PConstants;

public final class LabelAlignMapper {

    private LabelAlignMapper() {
    }

    public static int mapHorizontal(HorizontalAlign align) {
        HorizontalAlign resolvedAlign = align == null ? HorizontalAlign.START : align;
        switch (resolvedAlign) {
            case CENTER:
                return PConstants.CENTER;
            case END:
                return PConstants.RIGHT;
            case START:
            default:
                return PConstants.LEFT;
        }
    }

    public static int mapVertical(VerticalAlign align) {
        VerticalAlign resolvedAlign = align == null ? VerticalAlign.BASELINE : align;
        switch (resolvedAlign) {
            case TOP:
                return PConstants.TOP;
            case CENTER:
                return PConstants.CENTER;
            case BOTTOM:
                return PConstants.BOTTOM;
            case BASELINE:
            default:
                return PConstants.BASELINE;
        }
    }

    public static HorizontalAlign fromProcessingHorizontal(int align) {
        switch (align) {
            case PConstants.CENTER:
                return HorizontalAlign.CENTER;
            case PConstants.RIGHT:
                return HorizontalAlign.END;
            case PConstants.LEFT:
            default:
                return HorizontalAlign.START;
        }
    }

    public static VerticalAlign fromProcessingVertical(int align) {
        switch (align) {
            case PConstants.TOP:
                return VerticalAlign.TOP;
            case PConstants.CENTER:
                return VerticalAlign.CENTER;
            case PConstants.BOTTOM:
                return VerticalAlign.BOTTOM;
            case PConstants.BASELINE:
            default:
                return VerticalAlign.BASELINE;
        }
    }
}
