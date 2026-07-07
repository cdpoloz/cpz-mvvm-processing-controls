package com.cpz.processing.controls.config;

import com.cpz.processing.controls.controls.Control;
import com.cpz.processing.controls.controls.config.ControlConfigLoader;
import com.cpz.processing.controls.controls.geometry.ControlMeasure;
import com.cpz.processing.controls.controls.geometry.MeasureMode;
import com.cpz.processing.controls.controls.progressbar.ProgressBar;
import com.cpz.processing.controls.controls.progressbar.ProgressBarFillDirection;
import com.cpz.processing.controls.controls.progressbar.ProgressBarFactory;
import com.cpz.processing.controls.controls.progressbar.config.ProgressBarConfig;
import com.cpz.processing.controls.controls.progressbar.config.ProgressBarConfigLoader;
import com.cpz.processing.controls.core.overlay.tooltip.TooltipBounds;
import java.util.Map;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;
import processing.data.JSONObject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class ProgressBarJsonConfigTest {
    @Test
    void legacyGeometryLoadsAsAbsoluteBounds() {
        ProgressBarConfig config = progressBarConfig("{\"code\":\"bar\",\"x\":40,\"y\":50,\"width\":200,\"height\":20}");

        assertMeasure(config.getBounds().x(), MeasureMode.ABSOLUTE, 40.0F);
        assertMeasure(config.getBounds().y(), MeasureMode.ABSOLUTE, 50.0F);
        assertMeasure(config.getBounds().width(), MeasureMode.ABSOLUTE, 200.0F);
        assertMeasure(config.getBounds().height(), MeasureMode.ABSOLUTE, 20.0F);
    }

    @Test
    void relativeBoundsLoadAsRelativeMeasures() {
        ProgressBarConfig config = progressBarConfig("{\"code\":\"bar\","
                + "\"bounds\":{\"mode\":\"relative\",\"x\":0.1,\"y\":0.2,\"width\":0.5,\"height\":0.05}}");

        assertMeasure(config.getBounds().x(), MeasureMode.RELATIVE, 0.1F);
        assertMeasure(config.getBounds().height(), MeasureMode.RELATIVE, 0.05F);
    }

    @Test
    void boundsTakePrecedenceOverLegacyGeometry() {
        ProgressBarConfig config = progressBarConfig("{\"code\":\"bar\","
                + "\"x\":40,\"y\":50,\"width\":200,\"height\":20,"
                + "\"bounds\":{\"mode\":\"absolute\",\"x\":10,\"y\":20,\"width\":120,\"height\":14}}");

        assertMeasure(config.getBounds().x(), MeasureMode.ABSOLUTE, 10.0F);
        assertMeasure(config.getBounds().width(), MeasureMode.ABSOLUTE, 120.0F);
    }

    @Test
    void boundsModeIsTrimmedAndCaseInsensitive() {
        ProgressBarConfig config = progressBarConfig("{\"code\":\"bar\","
                + "\"bounds\":{\"mode\":\" Relative \",\"x\":0.1,\"y\":0.2,\"width\":0.5,\"height\":0.05}}");

        assertMeasure(config.getBounds().x(), MeasureMode.RELATIVE, 0.1F);
    }

    @Test
    void valueRangeAndStyleLoadFromJson() {
        ProgressBarConfig config = progressBarConfig("{\"code\":\"bar\",\"x\":40,\"y\":50,\"width\":200,\"height\":20,"
                + "\"min\":10,\"max\":30,\"value\":20,"
                + "\"trackColor\":\"#FF111111\",\"fillColor\":\"#FF00AA00\","
                + "\"style\":{\"strokeColor\":\"#FFFFFFFF\",\"strokeWeight\":2.5}}");

        assertEquals(10.0F, config.getMin());
        assertEquals(30.0F, config.getMax());
        assertEquals(20.0F, config.getValue());
        assertEquals(0xFF111111, config.getTrackColor());
        assertEquals(0xFF00AA00, config.getFillColor());
        assertEquals(0xFFFFFFFF, config.getStrokeColor());
        assertEquals(2.5F, config.getStrokeWeight());
        assertEquals(ProgressBarFillDirection.LEFT_TO_RIGHT, config.getFillDirection());
    }

    @Test
    void styleColorAliasesLoadWhenTopLevelColorsAreAbsent() {
        ProgressBarConfig config = progressBarConfig("{\"code\":\"bar\",\"x\":40,\"y\":50,\"width\":200,\"height\":20,"
                + "\"style\":{\"trackColor\":\"#FF123456\",\"fillColor\":\"#FF654321\"}}");

        assertEquals(0xFF123456, config.getTrackColor());
        assertEquals(0xFF654321, config.getFillColor());
    }

    @Test
    void invertedRangeIsSortedAndValueIsClampedByConfig() {
        ProgressBarConfig config = progressBarConfig("{\"code\":\"bar\",\"x\":40,\"y\":50,\"width\":200,\"height\":20,"
                + "\"min\":30,\"max\":10,\"value\":50}");

        assertEquals(10.0F, config.getMin());
        assertEquals(30.0F, config.getMax());
        assertEquals(30.0F, config.getValue());
    }

    @Test
    void negativeStyleStrokeWeightIsClampedToZero() {
        ProgressBarConfig config = progressBarConfig("{\"code\":\"bar\",\"x\":40,\"y\":50,\"width\":200,\"height\":20,"
                + "\"style\":{\"strokeWeight\":-2.0}}");

        assertEquals(0.0F, config.getStrokeWeight());
    }

    @Test
    void fillDirectionDefaultsToLeftToRightWhenMissing() {
        ProgressBarConfig config = progressBarConfig("{\"code\":\"bar\",\"x\":40,\"y\":50,\"width\":200,\"height\":20}");

        assertEquals(ProgressBarFillDirection.LEFT_TO_RIGHT, config.getFillDirection());
    }

    @Test
    void fillDirectionLoadsFromStyleJson() {
        assertFillDirection("left-to-right", ProgressBarFillDirection.LEFT_TO_RIGHT);
        assertFillDirection("right-to-left", ProgressBarFillDirection.RIGHT_TO_LEFT);
        assertFillDirection("bottom-to-top", ProgressBarFillDirection.BOTTOM_TO_TOP);
        assertFillDirection("top-to-bottom", ProgressBarFillDirection.TOP_TO_BOTTOM);
    }

    @Test
    void fillDirectionAcceptsTrimCaseAndSeparatorVariants() {
        ProgressBarConfig config = progressBarConfig("{\"code\":\"bar\",\"x\":40,\"y\":50,\"width\":200,\"height\":20,"
                + "\"style\":{\"fillDirection\":\"  Bottom To Top  \"}}");

        assertEquals(ProgressBarFillDirection.BOTTOM_TO_TOP, config.getFillDirection());
    }

    @Test
    void topLevelFillDirectionTakesPrecedenceOverStyleFillDirection() {
        ProgressBarConfig config = progressBarConfig("{\"code\":\"bar\",\"x\":40,\"y\":50,\"width\":200,\"height\":20,"
                + "\"fillDirection\":\"RIGHT_TO_LEFT\","
                + "\"style\":{\"fillDirection\":\"top-to-bottom\"}}");

        assertEquals(ProgressBarFillDirection.RIGHT_TO_LEFT, config.getFillDirection());
    }

    @Test
    void invalidFillDirectionUsesDefaultDirection() {
        ProgressBarConfig config = progressBarConfig("{\"code\":\"bar\",\"x\":40,\"y\":50,\"width\":200,\"height\":20,"
                + "\"style\":{\"fillDirection\":\"diagonal\"}}");

        assertEquals(ProgressBarFillDirection.LEFT_TO_RIGHT, config.getFillDirection());
    }

    @Test
    void factoryAppliesValuesColorsVisibilityEnabledAndTooltip() {
        ProgressBarConfig config = progressBarConfig("{\"code\":\"bar\",\"x\":40,\"y\":50,\"width\":200,\"height\":20,"
                + "\"min\":10,\"max\":30,\"value\":20,"
                + "\"trackColor\":\"#FF111111\",\"fillColor\":\"#FF00AA00\","
                + "\"style\":{\"strokeColor\":\"#FFFFFFFF\",\"strokeWeight\":2.5,\"fillDirection\":\"bottom-to-top\"},"
                + "\"enabled\":false,\"visible\":false,\"tooltip\":{\"text\":\"progress status\"}}");

        ProgressBar progressBar = ProgressBarFactory.create(sketch(800, 600), config);

        assertEquals(10.0F, progressBar.getMin());
        assertEquals(30.0F, progressBar.getMax());
        assertEquals(20.0F, progressBar.getValue());
        assertEquals(0.5F, progressBar.getProgress());
        assertEquals(0xFF111111, progressBar.getTrackColor());
        assertEquals(0xFF00AA00, progressBar.getFillColor());
        assertEquals(0xFFFFFFFF, progressBar.getStrokeColor());
        assertEquals(2.5F, progressBar.getStrokeWeight());
        assertEquals(0xFF111111, progressBar.getStyle().getTrackColor());
        assertEquals(0xFF00AA00, progressBar.getStyle().getFillColor());
        assertEquals(0xFFFFFFFF, progressBar.getStyle().getStrokeColor());
        assertEquals(2.5F, progressBar.getStyle().getStrokeWeight());
        assertEquals(ProgressBarFillDirection.BOTTOM_TO_TOP, progressBar.getFillDirection());
        assertEquals(ProgressBarFillDirection.BOTTOM_TO_TOP, progressBar.getStyle().getFillDirection());
        assertFalse(progressBar.isEnabled());
        assertFalse(progressBar.isVisible());
        assertEquals("progress status", progressBar.getTooltip().getText());
    }

    @Test
    void controlConfigLoaderCreatesProgressBar() {
        JSONObject root = JSONObject.parse("{\"controls\":[{\"type\":\"progressbar\",\"code\":\"bar\","
                + "\"bounds\":{\"mode\":\"relative\",\"x\":0.1,\"y\":0.2,\"width\":0.5,\"height\":0.05},"
                + "\"min\":10,\"max\":30,\"value\":20,\"tooltip\":\"Progress\"}]}");
        JsonApplet sketch = new JsonApplet(root);
        sketch.width = 800;
        sketch.height = 600;

        Map<String, Control> controls = new ControlConfigLoader(sketch).load("controls.json");
        ProgressBar progressBar = assertInstanceOf(ProgressBar.class, controls.get("bar"));
        TooltipBounds bounds = progressBar.getTooltipBounds();

        assertEquals(0.5F, progressBar.getProgress());
        assertEquals("Progress", progressBar.getTooltip().getText());
        assertEquals(80.0F, bounds.x());
        assertEquals(120.0F, bounds.y());
        assertEquals(300.0F, bounds.width());
        assertEquals(30.0F, bounds.height());
    }

    private static ProgressBarConfig progressBarConfig(String json) {
        return new ProgressBarConfigLoader(new PApplet()).loadFromJson(JSONObject.parse(json), "progressbar.json");
    }

    private static void assertFillDirection(String raw, ProgressBarFillDirection expected) {
        ProgressBarConfig config = progressBarConfig("{\"code\":\"bar\",\"x\":40,\"y\":50,\"width\":200,\"height\":20,"
                + "\"style\":{\"fillDirection\":\"" + raw + "\"}}");

        assertEquals(expected, config.getFillDirection());
    }

    private static void assertMeasure(ControlMeasure measure, MeasureMode mode, float value) {
        assertEquals(mode, measure.mode());
        assertEquals(value, measure.value());
    }

    private static PApplet sketch(int width, int height) {
        PApplet sketch = new PApplet();
        sketch.width = width;
        sketch.height = height;
        return sketch;
    }

    private static final class JsonApplet extends PApplet {
        private final JSONObject root;

        private JsonApplet(JSONObject root) {
            this.root = root;
        }

        @Override
        public JSONObject loadJSONObject(String filename) {
            return this.root;
        }
    }
}
