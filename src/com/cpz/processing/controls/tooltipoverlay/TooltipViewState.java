package com.cpz.processing.controls.tooltipoverlay;

public record TooltipViewState(float x,
                               float y,
                               float width,
                               float height,
                               String text,
                               boolean enabled,
                               float textPadding,
                               float cornerRadius) {
}
