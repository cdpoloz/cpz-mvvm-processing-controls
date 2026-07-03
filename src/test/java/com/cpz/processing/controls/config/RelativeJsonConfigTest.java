package com.cpz.processing.controls.config;

import com.cpz.processing.controls.controls.Control;
import com.cpz.processing.controls.controls.button.Button;
import com.cpz.processing.controls.controls.button.config.ButtonConfig;
import com.cpz.processing.controls.controls.button.config.ButtonConfigLoader;
import com.cpz.processing.controls.controls.config.ControlConfigLoader;
import com.cpz.processing.controls.controls.dropdown.DropDown;
import com.cpz.processing.controls.controls.geometry.ControlMeasure;
import com.cpz.processing.controls.controls.geometry.MeasureMode;
import com.cpz.processing.controls.controls.label.Label;
import com.cpz.processing.controls.controls.radiogroup.RadioGroup;
import com.cpz.processing.controls.controls.slider.Slider;
import com.cpz.processing.controls.controls.textfield.TextField;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.overlay.OverlayManager;
import com.cpz.processing.controls.core.overlay.tooltip.TooltipAttachable;
import com.cpz.processing.controls.core.overlay.tooltip.TooltipBounds;
import com.cpz.processing.controls.testsupport.ProcessingTestSupport;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;
import processing.core.PShape;
import processing.data.JSONObject;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RelativeJsonConfigTest {
    @Test
    void legacyBoundsLoadAsAbsolute() {
        ButtonConfig config = buttonConfig("{\"code\":\"b\",\"text\":\"Button\",\"x\":100,\"y\":50,\"width\":200,\"height\":40}");

        assertMeasure(config.getBounds().x(), MeasureMode.ABSOLUTE, 100.0F);
        assertMeasure(config.getBounds().y(), MeasureMode.ABSOLUTE, 50.0F);
        assertMeasure(config.getBounds().width(), MeasureMode.ABSOLUTE, 200.0F);
        assertMeasure(config.getBounds().height(), MeasureMode.ABSOLUTE, 40.0F);
    }

    @Test
    void explicitAbsoluteBoundsLoadLikeLegacy() {
        ButtonConfig config = buttonConfig("{\"code\":\"b\",\"text\":\"Button\","
                + "\"bounds\":{\"mode\":\"absolute\",\"x\":100,\"y\":50,\"width\":200,\"height\":40}}");

        assertMeasure(config.getBounds().x(), MeasureMode.ABSOLUTE, 100.0F);
        assertMeasure(config.getBounds().height(), MeasureMode.ABSOLUTE, 40.0F);
    }

    @Test
    void explicitRelativeBoundsAndTextSizeLoadAsRelative() {
        ButtonConfig config = buttonConfig("{\"code\":\"b\",\"text\":\"Button\","
                + "\"bounds\":{\"mode\":\"relative\",\"x\":0.1,\"y\":0.2,\"width\":0.35,\"height\":0.08},"
                + "\"textSize\":{\"mode\":\"relative\",\"value\":0.035}}");

        assertMeasure(config.getBounds().x(), MeasureMode.RELATIVE, 0.1F);
        assertMeasure(config.getBounds().height(), MeasureMode.RELATIVE, 0.08F);
        assertMeasure(config.getTextSizeMeasure(), MeasureMode.RELATIVE, 0.035F);
    }

    @Test
    void explicitAbsoluteTextSizeLoadsAsAbsolute() {
        ButtonConfig config = buttonConfig("{\"code\":\"b\",\"text\":\"Button\",\"x\":1,\"y\":2,\"width\":30,\"height\":10,"
                + "\"textSize\":{\"mode\":\"absolute\",\"value\":18}}");

        assertMeasure(config.getTextSizeMeasure(), MeasureMode.ABSOLUTE, 18.0F);
    }

    @Test
    void explicitMeasureModesAreCaseInsensitive() {
        ButtonConfig config = buttonConfig("{\"code\":\"b\",\"text\":\"Button\","
                + "\"bounds\":{\"mode\":\"Relative\",\"x\":0.1,\"y\":0.2,\"width\":0.35,\"height\":0.08},"
                + "\"textSize\":{\"mode\":\"ABSOLUTE\",\"value\":18}}");

        assertMeasure(config.getBounds().x(), MeasureMode.RELATIVE, 0.1F);
        assertMeasure(config.getTextSizeMeasure(), MeasureMode.ABSOLUTE, 18.0F);
    }

    @Test
    void explicitBoundsTakePrecedenceOverLegacyGeometry() {
        ButtonConfig config = buttonConfig("{\"code\":\"b\",\"text\":\"Button\","
                + "\"x\":100,\"y\":50,\"width\":200,\"height\":40,"
                + "\"bounds\":{\"mode\":\"absolute\",\"x\":10,\"y\":20,\"width\":30,\"height\":12}}");

        assertMeasure(config.getBounds().x(), MeasureMode.ABSOLUTE, 10.0F);
        assertMeasure(config.getBounds().width(), MeasureMode.ABSOLUTE, 30.0F);
    }

    @Test
    void topLevelTextSizeTakesPrecedenceOverLegacyStyleTextSize() {
        JSONObject root = JSONObject.parse("{\"controls\":[{\"type\":\"button\",\"code\":\"b\",\"text\":\"Button\","
                + "\"x\":100,\"y\":50,\"width\":200,\"height\":40,"
                + "\"style\":{\"textSize\":12},"
                + "\"textSize\":{\"mode\":\"relative\",\"value\":0.05}}]}");
        JsonApplet sketch = new JsonApplet(root);
        sketch.width = 800;
        sketch.height = 600;
        ProcessingTestSupport.RecordingGraphics graphics = ProcessingTestSupport.graphics(sketch);

        Button button = assertInstanceOf(Button.class, new ControlConfigLoader(sketch).load("controls.json").get("b"));
        button.draw();

        assertTrue(graphics.appliedSizes().contains(30.0F));
        assertFalse(graphics.appliedSizes().contains(12.0F));
    }

    @Test
    void invalidBoundsModeFailsClearly() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> buttonConfig("{\"code\":\"b\",\"text\":\"Button\","
                        + "\"bounds\":{\"mode\":\"fluid\",\"x\":0.1,\"y\":0.2,\"width\":0.3,\"height\":0.1}}")
        );

        assertTrue(exception.getMessage().contains("bounds.mode"));
        assertTrue(exception.getMessage().contains("absolute"));
        assertTrue(exception.getMessage().contains("relative"));
    }

    @Test
    void invalidTextSizeModeFailsClearly() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> buttonConfig("{\"code\":\"b\",\"text\":\"Button\",\"x\":1,\"y\":2,\"width\":30,\"height\":10,"
                        + "\"textSize\":{\"mode\":\"fluid\",\"value\":0.035}}")
        );

        assertTrue(exception.getMessage().contains("textSize"));
        assertTrue(exception.getMessage().contains("absolute"));
        assertTrue(exception.getMessage().contains("relative"));
    }

    @Test
    void missingModeInBoundsOrTextSizeFailsClearly() {
        IllegalArgumentException boundsException = assertThrows(
                IllegalArgumentException.class,
                () -> buttonConfig("{\"code\":\"b\",\"text\":\"Button\","
                        + "\"bounds\":{\"x\":0.1,\"y\":0.2,\"width\":0.3,\"height\":0.1}}")
        );
        IllegalArgumentException textSizeException = assertThrows(
                IllegalArgumentException.class,
                () -> buttonConfig("{\"code\":\"b\",\"text\":\"Button\",\"x\":1,\"y\":2,\"width\":30,\"height\":10,"
                        + "\"textSize\":{\"value\":0.035}}")
        );

        assertTrue(boundsException.getMessage().contains("'mode'"));
        assertTrue(textSizeException.getMessage().contains("'mode'"));
    }

    @Test
    void buttonLabelAndTextFieldLoadRelativeBoundsAndTextSizeThroughControlLoader() {
        assertRelativeControl("button", "\"text\":\"Button\"", Button.class, bounds ->
                assertBounds(bounds, 50.0F, 0.0F, 300.0F, 120.0F));
        assertRelativeControl("label", "\"text\":\"Label\"", Label.class, bounds ->
                assertBounds(bounds, 200.0F, 60.0F, 300.0F, 120.0F));
        assertRelativeControl("textfield", "\"text\":\"Text\"", TextField.class, bounds ->
                assertBounds(bounds, 50.0F, 0.0F, 300.0F, 120.0F));
    }

    @Test
    void radioGroupSliderAndDropDownLoadRelativeBoundsAndTextSizeThroughControlLoader() {
        assertRelativeControl("radiogroup", "\"options\":[\"A\",\"B\"]", RadioGroup.class, bounds -> {
            assertEquals(300.0F, bounds.width());
            assertEquals(248.0F, bounds.height());
        });
        assertRelativeControl("slider", "\"min\":0,\"max\":1,\"step\":0.1,\"value\":0.5", Slider.class, bounds ->
                assertBounds(bounds, 50.0F, 0.0F, 300.0F, 120.0F));
        assertRelativeControl("dropdown", "\"items\":[\"A\",\"B\"],\"selectedIndex\":0", DropDown.class, bounds ->
                assertBounds(bounds, 50.0F, 0.0F, 300.0F, 120.0F));
    }

    @Test
    void existingControlExampleJsonFilesStillLoad() throws Exception {
        try (var paths = Files.list(Path.of("data/config"))) {
            for (Path path : paths.filter(candidate -> candidate.toString().endsWith(".json")).toList()) {
                JSONObject root = JSONObject.parse(Files.readString(path));
                if (!root.hasKey("controls")) {
                    continue;
                }

                JsonApplet sketch = new JsonApplet(root);
                sketch.width = 800;
                sketch.height = 600;

                Map<String, Control> controls = new ControlConfigLoader(sketch, new OverlayManager(), new InputManager()).load(path.toString());

                assertFalse(controls.isEmpty(), path.toString());
            }
        }
    }

    private static <T extends Control & TooltipAttachable> void assertRelativeControl(
            String type,
            String controlSpecificJson,
            Class<T> expectedType,
            Consumer<TooltipBounds> boundsAssertion
    ) {
        JSONObject root = JSONObject.parse("{\"controls\":[{\"type\":\"" + type + "\",\"code\":\"c\","
                + controlSpecificJson + ","
                + "\"bounds\":{\"mode\":\"relative\",\"x\":0.25,\"y\":0.1,\"width\":0.5,\"height\":0.2},"
                + "\"textSize\":{\"mode\":\"relative\",\"value\":0.05}}]}");
        JsonApplet sketch = new JsonApplet(root);
        sketch.width = 800;
        sketch.height = 600;
        ProcessingTestSupport.RecordingGraphics graphics = ProcessingTestSupport.graphics(sketch);

        Map<String, Control> controls = new ControlConfigLoader(sketch, new OverlayManager(), new InputManager()).load("controls.json");
        T control = assertInstanceOf(expectedType, controls.get("c"));

        boundsAssertion.accept(control.getTooltipBounds());
        control.draw();
        assertTrue(
                graphics.appliedSizes().contains(30.0F),
                () -> "Expected relative text size 30.0 in " + graphics.appliedSizes()
        );
    }

    private static ButtonConfig buttonConfig(String json) {
        return new ButtonConfigLoader(new PApplet()).loadFromJson(JSONObject.parse(json), "button.json");
    }

    private static void assertMeasure(ControlMeasure measure, MeasureMode mode, float value) {
        assertEquals(mode, measure.mode());
        assertEquals(value, measure.value());
    }

    private static void assertBounds(TooltipBounds bounds, float x, float y, float width, float height) {
        assertEquals(x, bounds.x());
        assertEquals(y, bounds.y());
        assertEquals(width, bounds.width());
        assertEquals(height, bounds.height());
    }

    private static final class JsonApplet extends ProcessingTestSupport.FontApplet {
        private final JSONObject root;

        private JsonApplet(JSONObject root) {
            super(ProcessingTestSupport.font("Dialog", 16));
            this.root = root;
        }

        @Override
        public JSONObject loadJSONObject(String filename) {
            return this.root;
        }

        @Override
        public PShape loadShape(String filename) {
            return new PShape();
        }
    }
}
