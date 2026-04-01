package com.cpz.processing.controls.dropdowncontrol;

import java.util.List;

public record DropDownViewState(float x,
                                float y,
                                float width,
                                float height,
                                boolean expanded,
                                String selectedText,
                                List<String> items,
                                int selectedIndex,
                                int hoveredIndex,
                                boolean hovered,
                                boolean pressed,
                                boolean focused,
                                boolean enabled,
                                float itemHeight,
                                int maxVisibleItems,
                                float textPadding,
                                float arrowPadding,
                                float cornerRadius,
                                float listCornerRadius,
                                float strokeWeight) {
}
