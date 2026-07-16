package com.cpz.processing.controls.config;

import com.cpz.processing.controls.controls.Control;
import com.cpz.processing.controls.controls.config.ControlConfigLoader;
import com.cpz.processing.controls.controls.geometry.ControlMeasure;
import com.cpz.processing.controls.controls.geometry.MeasureMode;
import com.cpz.processing.controls.controls.indicator.Indicator;
import com.cpz.processing.controls.controls.indicator.IndicatorFactory;
import com.cpz.processing.controls.controls.indicator.config.IndicatorConfig;
import com.cpz.processing.controls.controls.indicator.config.IndicatorConfigLoader;
import com.cpz.processing.controls.core.overlay.tooltip.TooltipBounds;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PShape;
import processing.data.JSONObject;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IndicatorJsonConfigTest {
    @Test
    void legacyGeometryLoadsAsAbsoluteBounds() {
        IndicatorConfig config = indicatorConfig("{\"code\":\"ind\",\"x\":40,\"y\":50,\"width\":24,\"height\":30}");

        assertMeasure(config.getBounds().x(), MeasureMode.ABSOLUTE, 40.0F);
        assertMeasure(config.getBounds().y(), MeasureMode.ABSOLUTE, 50.0F);
        assertMeasure(config.getBounds().width(), MeasureMode.ABSOLUTE, 24.0F);
        assertMeasure(config.getBounds().height(), MeasureMode.ABSOLUTE, 30.0F);
    }

    @Test
    void relativeBoundsLoadAsRelativeMeasures() {
        IndicatorConfig config = indicatorConfig("{\"code\":\"ind\","
                + "\"bounds\":{\"mode\":\"relative\",\"x\":0.1,\"y\":0.2,\"width\":0.05,\"height\":0.1}}");

        assertMeasure(config.getBounds().x(), MeasureMode.RELATIVE, 0.1F);
        assertMeasure(config.getBounds().height(), MeasureMode.RELATIVE, 0.1F);
    }

    @Test
    void svgRendererLoadsFromStyleRendererConfig() {
        IndicatorConfig config = indicatorConfig("{\"code\":\"ind\",\"x\":40,\"y\":50,\"width\":24,\"height\":30,"
                + "\"style\":{\"renderer\":{\"type\":\"svg\",\"path\":\"data/img/test.svg\"}}}");

        assertEquals("svg", config.getRenderer().getType());
        assertEquals("data/img/test.svg", config.getRenderer().getPath());
    }

    @Test
    void pngRendererLoadsFromStyleRendererConfig() {
        IndicatorConfig config = indicatorConfig("{\"code\":\"ind\",\"x\":40,\"y\":50,\"width\":24,\"height\":30,"
                + "\"style\":{\"renderer\":{\"type\":\"png\",\"path\":\"src/test/resources/data/img/indicator-mask.png\"}}}");

        assertEquals("png", config.getRenderer().getType());
        assertEquals("src/test/resources/data/img/indicator-mask.png", config.getRenderer().getPath());
    }

    @Test
    void pngRendererExtensionIsCaseInsensitiveInJson() {
        IndicatorConfig config = indicatorConfig("{\"code\":\"ind\",\"x\":40,\"y\":50,\"width\":24,\"height\":30,"
                + "\"style\":{\"renderer\":{\"type\":\"PNG\",\"path\":\"src/test/resources/data/img/indicator-mask.PNG\"}}}");

        assertEquals("png", config.getRenderer().getType());
        assertEquals("src/test/resources/data/img/indicator-mask.PNG", config.getRenderer().getPath());
    }

    @Test
    void unsupportedRendererFormatFailsClearly() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> indicatorConfig("{\"code\":\"ind\",\"x\":40,\"y\":50,\"width\":24,\"height\":30,"
                        + "\"style\":{\"renderer\":{\"type\":\"jpg\",\"path\":\"data/img/icon.jpg\"}}}")
        );

        assertTrue(exception.getMessage().contains("style.renderer"));
        assertTrue(exception.getMessage().contains("svg"));
        assertTrue(exception.getMessage().contains("png"));
    }

    @Test
    void rendererTypeMustMatchPathExtension() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> indicatorConfig("{\"code\":\"ind\",\"x\":40,\"y\":50,\"width\":24,\"height\":30,"
                        + "\"style\":{\"renderer\":{\"type\":\"png\",\"path\":\"data/img/test.svg\"}}}")
        );

        assertTrue(exception.getMessage().contains("does not match"));
    }

    @Test
    void blankRendererPathFailsClearly() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> indicatorConfig("{\"code\":\"ind\",\"x\":40,\"y\":50,\"width\":24,\"height\":30,"
                        + "\"style\":{\"renderer\":{\"type\":\"png\",\"path\":\" \"}}}")
        );

        assertTrue(exception.getMessage().contains("path"));
    }

    @Test
    void onDefaultsToFalse() {
        IndicatorConfig config = indicatorConfig("{\"code\":\"ind\",\"x\":40,\"y\":50,\"width\":24,\"height\":30}");

        assertFalse(config.isOn());
    }

    @Test
    void strokeDefaultsMatchIndicatorDefaults() {
        IndicatorConfig config = indicatorConfig("{\"code\":\"ind\",\"x\":40,\"y\":50,\"width\":24,\"height\":30}");

        assertEquals(Indicator.DEFAULT_BORDER_COLOR, config.getStrokeColor());
        assertEquals(1.0F, config.getStrokeWeight());
    }

    @Test
    void onTrueLoadsFromJson() {
        IndicatorConfig config = indicatorConfig("{\"code\":\"ind\",\"x\":40,\"y\":50,\"width\":24,\"height\":30,\"on\":true}");

        assertTrue(config.isOn());
    }

    @Test
    void topLevelColorsLoadFromJson() {
        IndicatorConfig config = indicatorConfig("{\"code\":\"ind\",\"x\":40,\"y\":50,\"width\":24,\"height\":30,"
                + "\"onColor\":\"#FF00AA00\",\"offColor\":\"#FF111111\"}");

        assertEquals(0xFF00AA00, config.getOnColor());
        assertEquals(0xFF111111, config.getOffColor());
    }

    @Test
    void styleStrokeLoadsFromJson() {
        IndicatorConfig config = indicatorConfig("{\"code\":\"ind\",\"x\":40,\"y\":50,\"width\":24,\"height\":30,"
                + "\"style\":{\"strokeColor\":\"#FFFFFFFF\",\"strokeWeight\":2.5}}");

        assertEquals(0xFFFFFFFF, config.getStrokeColor());
        assertEquals(2.5F, config.getStrokeWeight());
    }

    @Test
    void styleStrokeWidthAliasLoadsWhenStrokeWeightIsAbsent() {
        IndicatorConfig config = indicatorConfig("{\"code\":\"ind\",\"x\":40,\"y\":50,\"width\":24,\"height\":30,"
                + "\"style\":{\"strokeColor\":\"#FFFFFFFF\",\"strokeWidth\":3.0}}");

        assertEquals(0xFFFFFFFF, config.getStrokeColor());
        assertEquals(3.0F, config.getStrokeWeight());
    }

    @Test
    void styleStrokeWeightTakesPrecedenceOverStrokeWidthAlias() {
        IndicatorConfig config = indicatorConfig("{\"code\":\"ind\",\"x\":40,\"y\":50,\"width\":24,\"height\":30,"
                + "\"style\":{\"strokeWeight\":2.0,\"strokeWidth\":3.0}}");

        assertEquals(2.0F, config.getStrokeWeight());
    }

    @Test
    void negativeStyleStrokeWeightIsClampedToZero() {
        IndicatorConfig config = indicatorConfig("{\"code\":\"ind\",\"x\":40,\"y\":50,\"width\":24,\"height\":30,"
                + "\"style\":{\"strokeWeight\":-2.0}}");

        assertEquals(0.0F, config.getStrokeWeight());
    }

    @Test
    void styleColorsAreAcceptedWhenTopLevelColorsAreAbsent() {
        IndicatorConfig config = indicatorConfig("{\"code\":\"ind\",\"x\":40,\"y\":50,\"width\":24,\"height\":30,"
                + "\"style\":{\"onColor\":\"#FF123456\",\"offColor\":\"#FF654321\"}}");

        assertEquals(0xFF123456, config.getOnColor());
        assertEquals(0xFF654321, config.getOffColor());
    }

    @Test
    void topLevelColorsTakePrecedenceOverStyleAliases() {
        IndicatorConfig config = indicatorConfig("{\"code\":\"ind\",\"x\":40,\"y\":50,\"width\":24,\"height\":30,"
                + "\"onColor\":\"#FF00AA00\",\"style\":{\"onColor\":\"#FF123456\",\"offColor\":\"#FF654321\"}}");

        assertEquals(0xFF00AA00, config.getOnColor());
        assertEquals(0xFF654321, config.getOffColor());
    }

    @Test
    void stringTooltipLoadsAsTooltipText() {
        IndicatorConfig config = indicatorConfig("{\"code\":\"ind\",\"x\":40,\"y\":50,\"width\":24,\"height\":30,"
                + "\"tooltip\":\"Status text\"}");

        Indicator indicator = IndicatorFactory.create(sketch(800, 600), config);

        assertEquals("Status text", indicator.getTooltip().getText());
    }

    @Test
    void boundsTakePrecedenceOverLegacyGeometry() {
        IndicatorConfig config = indicatorConfig("{\"code\":\"ind\","
                + "\"x\":40,\"y\":50,\"width\":24,\"height\":30,"
                + "\"bounds\":{\"mode\":\"absolute\",\"x\":10,\"y\":20,\"width\":12,\"height\":14}}");

        assertMeasure(config.getBounds().x(), MeasureMode.ABSOLUTE, 10.0F);
        assertMeasure(config.getBounds().width(), MeasureMode.ABSOLUTE, 12.0F);
    }

    @Test
    void boundsModeIsTrimmedAndCaseInsensitive() {
        IndicatorConfig config = indicatorConfig("{\"code\":\"ind\","
                + "\"bounds\":{\"mode\":\" Relative \",\"x\":0.1,\"y\":0.2,\"width\":0.05,\"height\":0.1}}");

        assertMeasure(config.getBounds().x(), MeasureMode.RELATIVE, 0.1F);
    }

    @Test
    void missingBoundsModeFailsLikeOtherControls() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> indicatorConfig("{\"code\":\"ind\","
                        + "\"bounds\":{\"x\":0.1,\"y\":0.2,\"width\":0.05,\"height\":0.1}}")
        );

        assertTrue(exception.getMessage().contains("'mode'"));
    }

    @Test
    void invalidBoundsModeFailsLikeOtherControls() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> indicatorConfig("{\"code\":\"ind\","
                        + "\"bounds\":{\"mode\":\"fluid\",\"x\":0.1,\"y\":0.2,\"width\":0.05,\"height\":0.1}}")
        );

        assertTrue(exception.getMessage().contains("bounds.mode"));
        assertTrue(exception.getMessage().contains("absolute"));
        assertTrue(exception.getMessage().contains("relative"));
    }

    @Test
    void factoryAppliesStateColorsVisibilityEnabledAndTooltip() {
        IndicatorConfig config = indicatorConfig("{\"code\":\"ind\",\"x\":40,\"y\":50,\"width\":24,\"height\":30,"
                + "\"on\":true,\"onColor\":\"#FF00AA00\",\"offColor\":\"#FF111111\","
                + "\"style\":{\"strokeColor\":\"#FFFFFFFF\",\"strokeWeight\":2.5},"
                + "\"enabled\":false,\"visible\":false,\"tooltip\":{\"text\":\"runtime status\"}}");

        Indicator indicator = IndicatorFactory.create(sketch(800, 600), config);

        assertTrue(indicator.isOn());
        assertEquals(0xFF00AA00, indicator.getOnColor());
        assertEquals(0xFF111111, indicator.getOffColor());
        assertEquals(0xFFFFFFFF, indicator.getStrokeColor());
        assertEquals(2.5F, indicator.getStrokeWeight());
        assertEquals(0xFF00AA00, indicator.getStyle().getOnColor());
        assertEquals(0xFF111111, indicator.getStyle().getOffColor());
        assertEquals(0xFFFFFFFF, indicator.getStyle().getStrokeColor());
        assertEquals(2.5F, indicator.getStyle().getStrokeWeight());
        assertFalse(indicator.isEnabled());
        assertFalse(indicator.isVisible());
        assertEquals("runtime status", indicator.getTooltip().getText());
    }

    @Test
    void controlConfigLoaderCreatesIndicator() {
        JSONObject root = JSONObject.parse("{\"controls\":[{\"type\":\"indicator\",\"code\":\"ind\","
                + "\"bounds\":{\"mode\":\"relative\",\"x\":0.1,\"y\":0.2,\"width\":0.05,\"height\":0.1},"
                + "\"on\":true,\"tooltip\":{\"text\":\"status\"}}]}");
        JsonApplet sketch = new JsonApplet(root);
        sketch.width = 800;
        sketch.height = 600;

        Map<String, Control> controls = new ControlConfigLoader(sketch).load("controls.json");
        Indicator indicator = assertInstanceOf(Indicator.class, controls.get("ind"));
        TooltipBounds bounds = indicator.getTooltipBounds();

        assertTrue(indicator.isOn());
        assertEquals("status", indicator.getTooltip().getText());
        assertEquals(80.0F, bounds.x());
        assertEquals(120.0F, bounds.y());
        assertEquals(30.0F, bounds.width());
        assertEquals(60.0F, bounds.height());
    }

    @Test
    void controlConfigLoaderAppliesTooltipStyleRef() {
        JSONObject root = JSONObject.parse("{\"tooltipStyles\":{\"indicatorDark\":{"
                + "\"backgroundColor\":\"#F21B1F26\",\"textColor\":\"#FFFFFFFF\",\"borderColor\":\"#FF8A94A6\"}},"
                + "\"controls\":[{\"type\":\"indicator\",\"code\":\"ind\",\"x\":40,\"y\":50,\"width\":24,\"height\":30,"
                + "\"style\":{\"strokeColor\":\"#FFFFFFFF\"},"
                + "\"tooltip\":{\"text\":\"status\",\"styleRef\":\"indicatorDark\"}}]}");
        JsonApplet sketch = new JsonApplet(root);

        Map<String, Control> controls = new ControlConfigLoader(sketch).load("controls.json");
        Indicator indicator = assertInstanceOf(Indicator.class, controls.get("ind"));

        assertEquals("status", indicator.getTooltip().getText());
        assertEquals(0xF21B1F26, indicator.getTooltip().getStyleConfig().backgroundOverride);
        assertEquals(0xFFFFFFFF, indicator.getTooltip().getStyleConfig().textOverride);
        assertEquals(0xFF8A94A6, indicator.getTooltip().getStyleConfig().borderOverride);
        assertEquals(0xFFFFFFFF, indicator.getStrokeColor());
    }

    @Test
    void controlConfigLoaderCreatesSvgIndicatorWithRelativeBoundsStateColorsAndTooltip() {
        JSONObject root = JSONObject.parse("{\"controls\":[{\"type\":\"indicator\",\"code\":\"ind\","
                + "\"bounds\":{\"mode\":\"relative\",\"x\":0.1,\"y\":0.2,\"width\":0.05,\"height\":0.1},"
                + "\"on\":true,\"onColor\":\"#FF00AA00\",\"offColor\":\"#FF111111\","
                + "\"style\":{\"strokeColor\":\"#FFFFFFFF\",\"strokeWeight\":2.0,"
                + "\"renderer\":{\"type\":\"svg\",\"path\":\"data/img/test.svg\"}},"
                + "\"tooltip\":\"SVG indicator\"}]}");
        JsonApplet sketch = new JsonApplet(root);
        sketch.width = 800;
        sketch.height = 600;

        Map<String, Control> controls = new ControlConfigLoader(sketch).load("controls.json");
        Indicator indicator = assertInstanceOf(Indicator.class, controls.get("ind"));
        TooltipBounds bounds = indicator.getTooltipBounds();

        assertEquals(1, sketch.loadShapeCalls);
        assertEquals("data/img/test.svg", sketch.lastShapePath);
        assertTrue(indicator.isOn());
        assertEquals(0xFF00AA00, indicator.getOnColor());
        assertEquals(0xFF111111, indicator.getOffColor());
        assertEquals(0xFFFFFFFF, indicator.getStrokeColor());
        assertEquals(2.0F, indicator.getStrokeWeight());
        assertTrue(indicator.getStyle().isSvgRenderer());
        assertEquals("data/img/test.svg", indicator.getStyle().getRendererPath());
        assertEquals("SVG indicator", indicator.getTooltip().getText());
        assertEquals(80.0F, bounds.x());
        assertEquals(120.0F, bounds.y());
        assertEquals(30.0F, bounds.width());
        assertEquals(60.0F, bounds.height());
    }

    @Test
    void controlConfigLoaderCreatesPngIndicatorAndResolvesPath() {
        JSONObject root = JSONObject.parse("{\"controls\":[{\"type\":\"indicator\",\"code\":\"ind\","
                + "\"x\":40,\"y\":50,\"width\":24,\"height\":30,"
                + "\"style\":{\"renderer\":{\"type\":\"png\",\"path\":\"src/test/resources/data/img/indicator-mask.png\"}},"
                + "\"tooltip\":\"PNG indicator\"}]}");
        JsonApplet sketch = new JsonApplet(root);
        sketch.loadedImage = maskImage();

        Map<String, Control> controls = new ControlConfigLoader(sketch).load("controls.json");
        Indicator indicator = assertInstanceOf(Indicator.class, controls.get("ind"));

        assertEquals(1, sketch.loadImageCalls);
        assertEquals("src/test/resources/data/img/indicator-mask.png", sketch.lastImagePath);
        assertEquals("png", indicator.getStyle().getRendererType());
        assertEquals("src/test/resources/data/img/indicator-mask.png", indicator.getStyle().getRendererPath());
        assertEquals("PNG indicator", indicator.getTooltip().getText());
    }

    private static IndicatorConfig indicatorConfig(String json) {
        return new IndicatorConfigLoader(new PApplet()).loadFromJson(JSONObject.parse(json), "indicator.json");
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

    private static PImage maskImage() {
        PImage image = new PImage(2, 2, PApplet.ARGB);
        image.loadPixels();
        for (int i = 0; i < image.pixels.length; i++) {
            image.pixels[i] = 0xFF123456;
        }
        image.updatePixels();
        return image;
    }

    private static final class JsonApplet extends PApplet {
        private final JSONObject root;
        private int loadShapeCalls;
        private int loadImageCalls;
        private String lastShapePath;
        private String lastImagePath;
        private PImage loadedImage;

        private JsonApplet(JSONObject root) {
            this.root = root;
        }

        @Override
        public JSONObject loadJSONObject(String filename) {
            return this.root;
        }

        @Override
        public PShape loadShape(String filename) {
            this.loadShapeCalls++;
            this.lastShapePath = filename;
            return new PShape();
        }

        @Override
        public PImage loadImage(String filename) {
            this.loadImageCalls++;
            this.lastImagePath = filename;
            return this.loadedImage;
        }
    }
}
