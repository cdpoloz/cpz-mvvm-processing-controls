package com.cpz.processing.controls.config;

import com.cpz.processing.controls.controls.Control;
import com.cpz.processing.controls.controls.label.Label;
import com.cpz.processing.controls.controls.label.LabelFactory;
import com.cpz.processing.controls.controls.label.config.LabelConfig;
import com.cpz.processing.controls.controls.label.config.LabelConfigLoader;
import com.cpz.processing.controls.controls.config.ControlConfigLoader;
import com.cpz.processing.controls.controls.panel.Panel;
import com.cpz.processing.controls.core.overlay.tooltip.TooltipBounds;
import com.cpz.processing.controls.testsupport.ProcessingTestSupport;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;
import processing.data.JSONObject;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LabelJsonConfigTest {
    @Test
    void styleTextColorLoadsFromJson() {
        LabelConfig config = labelConfig("{\"code\":\"label\",\"text\":\"Status\",\"x\":40,\"y\":50,\"width\":160,\"height\":32,"
                + "\"style\":{\"textColor\":\"#FF50DC78\"}}");

        assertEquals(0xFF50DC78, config.getStyle().getTextColor());
    }

    @Test
    void factoryAppliesInitialTextColorFromJsonStyle() {
        LabelConfig config = labelConfig("{\"code\":\"label\",\"text\":\"Status\",\"x\":40,\"y\":50,\"width\":160,\"height\":32,"
                + "\"enabled\":false,\"visible\":false,"
                + "\"style\":{\"textColor\":\"#FFFFB428\"}}");

        Label label = LabelFactory.create(sketch(800, 600), config);

        assertEquals(0xFFFFB428, label.getTextColor());
        assertEquals(0xFFFFB428, label.getStyleConfig().textColor);
        assertEquals("Status", label.getText());
        assertFalse(label.isEnabled());
        assertFalse(label.isVisible());
    }

    @Test
    void relativeBoundsAndTopLevelRelativeTextSizeLoadThroughControlLoader() {
        JsonFontApplet sketch = jsonSketch(JSONObject.parse("{\"controls\":[{\"type\":\"label\",\"code\":\"label\",\"text\":\"Status\","
                + "\"bounds\":{\"mode\":\"relative\",\"x\":0.25,\"y\":0.1,\"width\":0.5,\"height\":0.2},"
                + "\"textSize\":{\"mode\":\"relative\",\"value\":0.035},"
                + "\"style\":{\"font\":\"data/font/JetBrainsMono.ttf\",\"textColor\":\"#FF50DC78\"}}]}"), 800, 600);
        ProcessingTestSupport.graphics(sketch);

        Label label = assertInstanceOf(Label.class, new ControlConfigLoader(sketch).load("labels.json").get("label"));
        TooltipBounds bounds = label.getTooltipBounds();
        int callsAfterLoadAndResolve = sketch.getCreateFontCalls();
        label.draw();

        assertEquals(200.0F, bounds.x());
        assertEquals(60.0F, bounds.y());
        assertEquals(300.0F, bounds.width());
        assertEquals(120.0F, bounds.height());
        assertEquals(21.0F, label.getStyleConfig().textSize);
        assertTrue(sketch.getCreateFontSizes().contains(21.0F), sketch.getCreateFontSizes().toString());
        assertEquals(callsAfterLoadAndResolve, sketch.getCreateFontCalls(), "same resolved size must reuse the cached PFont");
    }

    @Test
    void topLevelAbsoluteTextSizeCreatesJsonFontAtEffectiveSize() {
        JsonFontApplet sketch = jsonSketch(JSONObject.parse("{\"controls\":[{\"type\":\"label\",\"code\":\"label\",\"text\":\"Status\","
                + "\"x\":40,\"y\":50,\"width\":160,\"height\":32,"
                + "\"textSize\":{\"mode\":\"absolute\",\"value\":21},"
                + "\"style\":{\"font\":\"data/font/JetBrainsMono.ttf\"}}]}"), 800, 600);
        ProcessingTestSupport.graphics(sketch);

        Label label = assertInstanceOf(Label.class, new ControlConfigLoader(sketch).load("labels.json").get("label"));
        int callsAfterLoadAndResolve = sketch.getCreateFontCalls();
        label.draw();

        assertEquals(21.0F, label.getStyleConfig().textSize);
        assertTrue(sketch.getCreateFontSizes().contains(21.0F), sketch.getCreateFontSizes().toString());
        assertEquals(callsAfterLoadAndResolve, sketch.getCreateFontCalls());
    }

    @Test
    void legacyNumericStyleTextSizeStillLoadsFontAtStyleSize() {
        JsonFontApplet sketch = jsonSketch(JSONObject.parse("{\"controls\":[{\"type\":\"label\",\"code\":\"label\",\"text\":\"Status\","
                + "\"x\":40,\"y\":50,\"width\":160,\"height\":32,"
                + "\"style\":{\"font\":\"data/font/JetBrainsMono.ttf\",\"textSize\":24}}]}"), 800, 600);
        ProcessingTestSupport.graphics(sketch);

        Label label = assertInstanceOf(Label.class, new ControlConfigLoader(sketch).load("labels.json").get("label"));

        assertEquals(24.0F, label.getStyleConfig().textSize);
        assertEquals(1, sketch.getCreateFontCalls());
        assertEquals(24.0F, sketch.getLastCreateFontSize());
    }

    @Test
    void topLevelTextSizeTakesPrecedenceOverLegacyStyleTextSizeForLabel() {
        JsonFontApplet sketch = jsonSketch(JSONObject.parse("{\"controls\":[{\"type\":\"label\",\"code\":\"label\",\"text\":\"Status\","
                + "\"x\":40,\"y\":50,\"width\":160,\"height\":32,"
                + "\"textSize\":{\"mode\":\"absolute\",\"value\":21},"
                + "\"style\":{\"font\":\"data/font/JetBrainsMono.ttf\",\"textSize\":12}}]}"), 800, 600);
        ProcessingTestSupport.graphics(sketch);

        Label label = assertInstanceOf(Label.class, new ControlConfigLoader(sketch).load("labels.json").get("label"));

        assertEquals(21.0F, label.getStyleConfig().textSize);
        assertTrue(sketch.getCreateFontSizes().contains(21.0F), sketch.getCreateFontSizes().toString());
    }

    @Test
    void relativeTextSizeInsidePanelUsesPanelHeightAndRefreshesJsonFont() {
        JsonFontApplet sketch = jsonSketch(JSONObject.parse("{\"controls\":[{\"type\":\"label\",\"code\":\"label\",\"text\":\"Status\","
                + "\"bounds\":{\"mode\":\"relative\",\"x\":0.1,\"y\":0.2,\"width\":0.5,\"height\":0.1},"
                + "\"textSize\":{\"mode\":\"relative\",\"value\":0.1},"
                + "\"style\":{\"font\":\"data/font/JetBrainsMono.ttf\"}}]}"), 800, 600);
        ProcessingTestSupport.graphics(sketch);
        Map<String, Control> controls = new ControlConfigLoader(sketch).load("labels.json");
        Label label = assertInstanceOf(Label.class, controls.get("label"));
        Panel panel = new Panel(sketch, "panel", 10.0F, 20.0F, 400.0F, 200.0F);

        panel.add(label);
        panel.tooltipTarget(label).getTooltipBounds();
        int callsAfterPanelResolve = sketch.getCreateFontCalls();
        label.draw();

        assertEquals(20.0F, label.getStyleConfig().textSize);
        assertTrue(sketch.getCreateFontSizes().contains(20.0F), sketch.getCreateFontSizes().toString());
        assertEquals(callsAfterPanelResolve, sketch.getCreateFontCalls(), "panel-stable size must reuse the cached PFont");
    }

    @Test
    void resizingPanelCreatesJsonFontOnlyForNewNormalizedRelativeTextSize() {
        JsonFontApplet sketch = jsonSketch(JSONObject.parse("{\"controls\":[{\"type\":\"label\",\"code\":\"label\",\"text\":\"Status\","
                + "\"x\":20,\"y\":20,\"width\":160,\"height\":32,"
                + "\"textSize\":{\"mode\":\"relative\",\"value\":0.1},"
                + "\"style\":{\"font\":\"data/font/JetBrainsMono.ttf\"}}]}"), 800, 600);
        ProcessingTestSupport.graphics(sketch);
        Label label = assertInstanceOf(Label.class, new ControlConfigLoader(sketch).load("labels.json").get("label"));
        Panel panel = new Panel(sketch, "panel", 10.0F, 20.0F, 400.0F, 200.0F);
        panel.add(label);
        panel.tooltipTarget(label).getTooltipBounds();
        int callsAfterInitialPanelResolve = sketch.getCreateFontCalls();

        panel.setSize(400.0F, 130.0F);
        panel.tooltipTarget(label).getTooltipBounds();
        int callsAfterResize = sketch.getCreateFontCalls();
        label.draw();

        assertTrue(sketch.getCreateFontSizes().contains(20.0F), sketch.getCreateFontSizes().toString());
        assertTrue(sketch.getCreateFontSizes().contains(13.0F), sketch.getCreateFontSizes().toString());
        assertEquals(callsAfterInitialPanelResolve + 1, callsAfterResize);
        assertEquals(callsAfterResize, sketch.getCreateFontCalls());
    }

    @Test
    void styleTextSizeObjectFailsWithControlMeasureGuidance() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> labelConfig("{\"code\":\"label\",\"text\":\"Status\",\"x\":40,\"y\":50,\"width\":160,\"height\":32,"
                        + "\"style\":{\"textSize\":{\"mode\":\"relative\",\"value\":0.02}}}")
        );

        assertTrue(exception.getMessage().contains("style.textSize"));
        assertTrue(exception.getMessage().contains("numeric absolute"));
        assertTrue(exception.getMessage().contains("top-level"));
        assertTrue(exception.getMessage().contains("ControlMeasure"));
    }

    private static LabelConfig labelConfig(String json) {
        return new LabelConfigLoader(new PApplet()).loadFromJson(JSONObject.parse(json), "label.json");
    }

    private static PApplet sketch(int width, int height) {
        PApplet sketch = new PApplet();
        sketch.width = width;
        sketch.height = height;
        return sketch;
    }

    private static JsonFontApplet jsonSketch(JSONObject root, int width, int height) {
        JsonFontApplet sketch = new JsonFontApplet(root);
        sketch.width = width;
        sketch.height = height;
        return sketch;
    }

    private static final class JsonFontApplet extends ProcessingTestSupport.FontApplet {
        private final JSONObject root;

        private JsonFontApplet(JSONObject root) {
            super(ProcessingTestSupport.font("Monospaced", 16));
            this.root = root;
        }

        @Override
        public JSONObject loadJSONObject(String filename) {
            return this.root;
        }
    }
}
