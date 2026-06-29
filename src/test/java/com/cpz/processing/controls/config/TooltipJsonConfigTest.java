package com.cpz.processing.controls.config;

import com.cpz.processing.controls.controls.button.Button;
import com.cpz.processing.controls.controls.button.ButtonFactory;
import com.cpz.processing.controls.controls.button.config.ButtonConfig;
import com.cpz.processing.controls.controls.button.config.ButtonConfigLoader;
import com.cpz.processing.controls.core.overlay.tooltip.Tooltip;
import com.cpz.processing.controls.testsupport.ProcessingTestSupport;
import org.junit.jupiter.api.Test;
import processing.core.PFont;
import processing.data.JSONObject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class TooltipJsonConfigTest {
    @Test
    void tooltipIsOptional() {
        ButtonConfig config = new ButtonConfigLoader(new ProcessingTestSupport.FontApplet(ProcessingTestSupport.font("Dialog", 16)))
                .loadFromJson(parse("{\"code\":\"b\",\"text\":\"Button\",\"x\":1,\"y\":1,\"width\":10,\"height\":10}"), "button.json");

        assertNull(config.getTooltip());
    }

    @Test
    void readsTooltipTextStyleAndArgbColors() {
        ButtonConfig config = new ButtonConfigLoader(new ProcessingTestSupport.FontApplet(ProcessingTestSupport.font("Dialog", 16)))
                .loadFromJson(parse("{\"code\":\"b\",\"text\":\"Button\",\"x\":1,\"y\":1,\"width\":10,\"height\":10,"
                        + "\"tooltip\":{\"text\":\"Save\",\"style\":{\"backgroundColor\":\"#E61B1F26\",\"textColor\":\"#FFFFFFFF\",\"borderColor\":\"#668A94A6\",\"textSize\":14,\"font\":\"data/font/JetBrainsMono.ttf\"}}}"), "button.json");

        assertNotNull(config.getTooltip());
        assertEquals("Save", config.getTooltip().getText());
        assertEquals((int)0xE61B1F26L, config.getTooltip().getStyle().getBackgroundColor());
        assertEquals((int)0xFFFFFFFFL, config.getTooltip().getStyle().getTextColor());
        assertEquals((int)0x668A94A6L, config.getTooltip().getStyle().getBorderColor());
        assertEquals("data/font/JetBrainsMono.ttf", config.getTooltip().getStyle().getFontPath());
    }

    @Test
    void factoryLoadsTooltipFontOnceAndAssignsTooltip() {
        PFont font = ProcessingTestSupport.font("Monospaced", 14);
        ProcessingTestSupport.FontApplet sketch = new ProcessingTestSupport.FontApplet(font);
        ButtonConfig config = new ButtonConfigLoader(sketch)
                .loadFromJson(parse("{\"code\":\"b\",\"text\":\"Button\",\"x\":1,\"y\":1,\"width\":10,\"height\":10,"
                        + "\"tooltip\":{\"text\":\"Save\",\"style\":{\"font\":\"data/font/JetBrainsMono.ttf\",\"textSize\":14}}}"), "button.json");

        Button button = ButtonFactory.create(sketch, config);
        Tooltip tooltip = button.getTooltip();

        assertNotNull(tooltip);
        assertSame(font, tooltip.getStyleConfig().font);
        assertEquals(1, sketch.getCreateFontCalls());
    }

    private static JSONObject parse(String json) {
        return JSONObject.parse(json);
    }
}
