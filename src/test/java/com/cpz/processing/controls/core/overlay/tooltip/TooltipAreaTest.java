package com.cpz.processing.controls.core.overlay.tooltip;

import com.cpz.processing.controls.controls.button.Button;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TooltipAreaTest {
    @Test
    void containsPointsInsideCurrentBounds() {
        TooltipArea area = new TooltipArea(10.0F, 20.0F, 100.0F, 50.0F);

        assertTrue(area.getTooltipBounds().contains(10.0F, 20.0F));
        assertTrue(area.getTooltipBounds().contains(60.0F, 45.0F));
        assertFalse(area.getTooltipBounds().contains(9.0F, 20.0F));
        assertFalse(area.getTooltipBounds().contains(60.0F, 71.0F));
    }

    @Test
    void updatesBoundsForManualRegions() {
        TooltipArea area = new TooltipArea(0.0F, 0.0F, 10.0F, 10.0F);

        area.setBounds(100.0F, 80.0F, 160.0F, 120.0F);

        assertTrue(area.getTooltipBounds().contains(120.0F, 100.0F));
        assertFalse(area.getTooltipBounds().contains(20.0F, 20.0F));
    }

    @Test
    void supportsDynamicBoundsSupplier() {
        TooltipArea area = new TooltipArea(() -> new TooltipBounds(5.0F, 5.0F, 20.0F, 20.0F));

        assertTrue(area.getTooltipBounds().contains(10.0F, 10.0F));
        assertFalse(area.getTooltipBounds().contains(30.0F, 30.0F));
    }

    @Test
    void setTooltipTextUpdatesCurrentTooltip() {
        TooltipArea area = new TooltipArea(0.0F, 0.0F, 10.0F, 10.0F).setTooltip("Initial");

        area.setTooltipText("Updated");

        assertEquals("Updated", area.getTooltip().getText());
    }

    @Test
    void tooltipAttachableCanRepresentControlsAndAreas() {
        TooltipAttachable area = new TooltipArea(0.0F, 0.0F, 10.0F, 10.0F).setTooltip("Area");
        TooltipAttachable button = new Button(new PApplet(), "btn", "Button", 50.0F, 50.0F, 100.0F, 40.0F)
                .setTooltip("Button");

        area.setTooltipText("Area updated");
        button.setTooltipText("Button updated");

        assertEquals("Area updated", area.getTooltip().getText());
        assertEquals("Button updated", button.getTooltip().getText());
    }
}
