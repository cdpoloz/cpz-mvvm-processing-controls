package com.cpz.processing.controls.core.overlay.tooltip;

import com.cpz.processing.controls.controls.button.Button;
import com.cpz.processing.controls.controls.label.Label;
import com.cpz.processing.controls.core.overlay.tooltip.config.TooltipStyleConfig;
import com.cpz.processing.controls.testsupport.ProcessingTestSupport;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;
import processing.core.PFont;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

class TooltipSharedStyleTest {
    @Test
    void appliesSharedStyleToMultipleTooltipsWithDefensiveCopies() {
        PApplet sketch = new PApplet();
        PFont font = ProcessingTestSupport.font("Monospaced", 14);
        TooltipStyleConfig sharedStyle = new TooltipStyleConfig()
                .setFont(font)
                .setTextSize(14.0F)
                .setBackgroundColor(0xE61B1F26)
                .setTextColor(0xFFFFFFFF)
                .setBorderColor(0x668A94A6)
                .setPadding(12.0F)
                .setOffset(6.0F)
                .setCornerRadius(5.0F)
                .setStrokeWeight(2.0F);

        Button button = new Button(sketch, "btn", "Button", 1.0F, 1.0F, 40.0F, 20.0F)
                .setTooltip("Button tooltip")
                .setTooltipStyle(sharedStyle);
        Label label = new Label(sketch, "lbl", "Label", 1.0F, 30.0F, 40.0F, 20.0F)
                .setTooltip("Label tooltip")
                .setTooltipStyle(sharedStyle);
        TooltipArea area = new TooltipArea(1.0F, 60.0F, 40.0F, 20.0F)
                .setTooltip("Area tooltip")
                .setTooltipStyle(sharedStyle);

        assertStyle(button.getTooltip(), font, 0xE61B1F26, 0xFFFFFFFF, 0x668A94A6);
        assertStyle(label.getTooltip(), font, 0xE61B1F26, 0xFFFFFFFF, 0x668A94A6);
        assertStyle(area.getTooltip(), font, 0xE61B1F26, 0xFFFFFFFF, 0x668A94A6);
        assertNotSame(sharedStyle, button.getTooltip().getStyleConfig());
        assertNotSame(sharedStyle, label.getTooltip().getStyleConfig());
        assertNotSame(sharedStyle, area.getTooltip().getStyleConfig());

        sharedStyle.setBackgroundColor(0xFF000000)
                .setTextColor(0xFF000000)
                .setBorderColor(0xFF000000)
                .setTextSize(20.0F);

        assertStyle(button.getTooltip(), font, 0xE61B1F26, 0xFFFFFFFF, 0x668A94A6);
        assertStyle(label.getTooltip(), font, 0xE61B1F26, 0xFFFFFFFF, 0x668A94A6);
        assertStyle(area.getTooltip(), font, 0xE61B1F26, 0xFFFFFFFF, 0x668A94A6);
    }

    private static void assertStyle(Tooltip tooltip, PFont font, int backgroundColor, int textColor, int borderColor) {
        assertEquals(backgroundColor, tooltip.getStyleConfig().backgroundOverride);
        assertEquals(textColor, tooltip.getStyleConfig().textOverride);
        assertEquals(borderColor, tooltip.getStyleConfig().borderOverride);
        assertEquals(14.0F, tooltip.getStyleConfig().textSize);
        assertEquals(12.0F, tooltip.getStyleConfig().textPadding);
        assertEquals(6.0F, tooltip.getStyleConfig().offset);
        assertEquals(5.0F, tooltip.getStyleConfig().cornerRadius);
        assertEquals(2.0F, tooltip.getStyleConfig().strokeWeight);
        assertSame(font, tooltip.getStyleConfig().font);
    }
}
