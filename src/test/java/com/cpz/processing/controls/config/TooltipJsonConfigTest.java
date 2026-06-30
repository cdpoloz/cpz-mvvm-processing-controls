package com.cpz.processing.controls.config;

import com.cpz.processing.controls.controls.Control;
import com.cpz.processing.controls.controls.config.ControlConfigLoader;
import com.cpz.processing.controls.controls.button.Button;
import com.cpz.processing.controls.controls.button.ButtonFactory;
import com.cpz.processing.controls.controls.button.config.ButtonConfig;
import com.cpz.processing.controls.controls.button.config.ButtonConfigLoader;
import com.cpz.processing.controls.core.overlay.tooltip.Tooltip;
import com.cpz.processing.controls.core.overlay.tooltip.TooltipFactory;
import com.cpz.processing.controls.testsupport.ProcessingTestSupport;
import org.junit.jupiter.api.Test;
import processing.core.PFont;
import processing.data.JSONObject;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Test
    void controlLoaderResolvesStyleRefAndLoadsPresetFontOnceOutsideDraw() {
        PFont font = ProcessingTestSupport.font("Monospaced", 14);
        JSONObject root = parse("{\"tooltipStyles\":{\"dark\":{\"backgroundColor\":\"#E61B1F26\",\"textColor\":\"#FFFFFFFF\",\"borderColor\":\"#668A94A6\",\"font\":\"font.ttf\",\"textSize\":14,\"textPadding\":10,\"cornerRadius\":8,\"offset\":10,\"strokeWeight\":2}},"
                + "\"controls\":["
                + "{\"type\":\"button\",\"code\":\"btnSave\",\"text\":\"Save\",\"x\":1,\"y\":1,\"width\":40,\"height\":20,\"tooltip\":{\"text\":\"Guardar\",\"styleRef\":\"dark\"}},"
                + "{\"type\":\"button\",\"code\":\"btnCancel\",\"text\":\"Cancel\",\"x\":50,\"y\":1,\"width\":40,\"height\":20,\"tooltip\":{\"text\":\"Cancelar\",\"styleRef\":\"dark\"}}"
                + "]}");
        JsonFontApplet sketch = new JsonFontApplet(font, root);
        ProcessingTestSupport.graphics(sketch);

        Map<String, Control> controls = new ControlConfigLoader(sketch).load("controls.json");
        Button save = assertInstanceOf(Button.class, controls.get("btnSave"));
        Button cancel = assertInstanceOf(Button.class, controls.get("btnCancel"));

        assertEquals(1, sketch.getCreateFontCalls());
        assertTooltipStyle(save.getTooltip(), font, (int) 0xE61B1F26L, (int) 0xFFFFFFFFL, (int) 0x668A94A6L);
        assertTooltipStyle(cancel.getTooltip(), font, (int) 0xE61B1F26L, (int) 0xFFFFFFFFL, (int) 0x668A94A6L);
        assertEquals(10.0F, save.getTooltip().getStyleConfig().textPadding);
        assertEquals(8.0F, save.getTooltip().getStyleConfig().cornerRadius);
        assertEquals(10.0F, save.getTooltip().getStyleConfig().offset);
        assertEquals(2.0F, save.getTooltip().getStyleConfig().strokeWeight);

        save.draw();
        cancel.draw();

        assertEquals(1, sketch.getCreateFontCalls(), "tooltip font loading must not occur in draw()");
    }

    @Test
    void localTooltipStyleOverridesOnlyDefinedStyleRefFields() {
        PFont font = ProcessingTestSupport.font("Monospaced", 14);
        JSONObject root = parse("{\"tooltipStyles\":{\"dark\":{\"backgroundColor\":\"#E61B1F26\",\"textColor\":\"#FFFFFFFF\",\"borderColor\":\"#668A94A6\",\"font\":\"font.ttf\",\"textSize\":14,\"textPadding\":10}},"
                + "\"controls\":[{\"type\":\"button\",\"code\":\"btnSave\",\"text\":\"Save\",\"x\":1,\"y\":1,\"width\":40,\"height\":20,"
                + "\"tooltip\":{\"text\":\"Guardar\",\"styleRef\":\"dark\",\"style\":{\"textColor\":\"#FF111111\",\"textSize\":18,\"padding\":4}}}]}");
        JsonFontApplet sketch = new JsonFontApplet(font, root);

        Map<String, Control> controls = new ControlConfigLoader(sketch).load("controls.json");
        Button save = assertInstanceOf(Button.class, controls.get("btnSave"));
        Tooltip tooltip = save.getTooltip();

        assertEquals((int) 0xE61B1F26L, tooltip.getStyleConfig().backgroundOverride);
        assertEquals((int) 0xFF111111L, tooltip.getStyleConfig().textOverride);
        assertEquals((int) 0x668A94A6L, tooltip.getStyleConfig().borderOverride);
        assertEquals(18.0F, tooltip.getStyleConfig().textSize);
        assertEquals(4.0F, tooltip.getStyleConfig().textPadding);
        assertSame(font, tooltip.getStyleConfig().font);
        assertEquals(1, sketch.getCreateFontCalls());
    }

    @Test
    void unknownStyleRefFailsWithClearDiagnostic() {
        PFont font = ProcessingTestSupport.font("Monospaced", 14);
        JSONObject root = parse("{\"tooltipStyles\":{\"dark\":{\"textColor\":\"#FFFFFFFF\"}},"
                + "\"controls\":[{\"type\":\"button\",\"code\":\"btnSave\",\"text\":\"Save\",\"x\":1,\"y\":1,\"width\":40,\"height\":20,"
                + "\"tooltip\":{\"text\":\"Guardar\",\"styleRef\":\"missing\"}}]}");
        JsonFontApplet sketch = new JsonFontApplet(font, root);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new ControlConfigLoader(sketch).load("controls.json")
        );

        assertTrue(exception.getMessage().contains("styleRef"));
        assertTrue(exception.getMessage().contains("missing"));
        assertTrue(exception.getMessage().contains("tooltipStyles"));
    }

    @Test
    void standaloneTooltipLoadsFromExternalJson() {
        PFont font = ProcessingTestSupport.font("Monospaced", 14);
        JSONObject root = parse("{\"text\":\"Servidor principal\",\"enabled\":true,"
                + "\"style\":{\"backgroundColor\":\"#E61B1F26\",\"textColor\":\"#FFFFFFFF\",\"borderColor\":\"#668A94A6\",\"font\":\"font.ttf\",\"textSize\":14}}");
        JsonFontApplet sketch = new JsonFontApplet(font, root);

        Tooltip tooltip = TooltipFactory.loadFromJson(sketch, "server-tooltip.json");

        assertEquals("Servidor principal", tooltip.getText());
        assertTooltipStyle(tooltip, font, (int) 0xE61B1F26L, (int) 0xFFFFFFFFL, (int) 0x668A94A6L);
        assertEquals(1, sketch.getCreateFontCalls());
    }

    private static void assertTooltipStyle(Tooltip tooltip, PFont font, int backgroundColor, int textColor, int borderColor) {
        assertNotNull(tooltip);
        assertEquals(backgroundColor, tooltip.getStyleConfig().backgroundOverride);
        assertEquals(textColor, tooltip.getStyleConfig().textOverride);
        assertEquals(borderColor, tooltip.getStyleConfig().borderOverride);
        assertSame(font, tooltip.getStyleConfig().font);
    }

    private static JSONObject parse(String json) {
        return JSONObject.parse(json);
    }

    private static final class JsonFontApplet extends ProcessingTestSupport.FontApplet {
        private final JSONObject root;

        private JsonFontApplet(PFont font, JSONObject root) {
            super(font);
            this.root = root;
        }

        @Override
        public JSONObject loadJSONObject(String filename) {
            return this.root;
        }
    }
}
