package com.cpz.processing.controls.config;

import com.cpz.processing.controls.controls.button.Button;
import com.cpz.processing.controls.controls.config.ControlConfigLoader;
import com.cpz.processing.controls.controls.panel.Panel;
import com.cpz.processing.controls.controls.panel.config.PanelConfigLoader;
import com.cpz.processing.controls.core.util.JsonConfigSupport;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import processing.core.PApplet;
import processing.data.JSONObject;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValidationContractTest {
    @Test
    void jsonIdentityHelperRejectsUnicodeWhitespace() {
        JSONObject json = new JSONObject();
        json.setString("code", "\u2003");

        assertThrows(
                IllegalArgumentException.class,
                () -> JsonConfigSupport.getRequiredNonBlankString(json, "code", "control.json", "control")
        );
    }

    @ParameterizedTest(name = "aggregate code {0}")
    @MethodSource("invalidJsonCodes")
    void aggregateLoaderRejectsNullEmptyAndBlankCodes(String label, String rawCode) {
        JSONObject root = JSONObject.parse(
                "{\"controls\":[{\"type\":\"panel\",\"code\":" + rawCode
                        + ",\"x\":10,\"y\":20,\"width\":100,\"height\":60}]}"
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new ControlConfigLoader(new JsonApplet(root)).load("controls.json")
        );
    }

    @ParameterizedTest(name = "specific code {0}")
    @MethodSource("invalidJsonCodes")
    void specificLoaderRejectsNullEmptyAndBlankCodes(String label, String rawCode) {
        JSONObject panel = JSONObject.parse(
                "{\"code\":" + rawCode + ",\"x\":10,\"y\":20,\"width\":100,\"height\":60}"
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new PanelConfigLoader(new PApplet()).loadFromJson(panel, "panel.json")
        );
    }

    @ParameterizedTest(name = "runtime code {0}")
    @MethodSource("invalidRuntimeCodes")
    void explicitRuntimeCodesRejectNullEmptyAndBlank(String label, String code) {
        PApplet sketch = sketch();

        assertThrows(
                IllegalArgumentException.class,
                () -> new Panel(sketch, code, 10.0F, 20.0F, 100.0F, 60.0F)
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new Button(sketch, code, "Button", 10.0F, 20.0F, 100.0F, 30.0F)
        );
    }

    @Test
    void legacyConstructorsStillGenerateNonBlankCodes() {
        PApplet sketch = sketch();
        Panel panel = new Panel(sketch, 10.0F, 20.0F, 100.0F, 60.0F);
        Button button = new Button(sketch, "Button", 10.0F, 20.0F, 100.0F, 30.0F);

        assertFalse(panel.getCode().isBlank());
        assertFalse(button.getCode().isBlank());
        assertTrue(panel.getCode().startsWith("panel-"));
        assertTrue(button.getCode().startsWith("button-"));
    }

    @ParameterizedTest(name = "legacy width {0}")
    @MethodSource("nonFiniteValues")
    void specificJsonLoaderRejectsNonFiniteDimensions(String label, float value) {
        JSONObject panel = new NonFinitePanelJson(value);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new PanelConfigLoader(new PApplet()).loadFromJson(panel, "panel.json")
        );
        assertTrue(exception.getMessage().contains("width"));
    }

    private static Stream<Arguments> invalidJsonCodes() {
        return Stream.of(
                Arguments.of("null", "null"),
                Arguments.of("empty", "\"\""),
                Arguments.of("blank", "\"   \""),
                Arguments.of("unicode whitespace", "\"\\u2003\"")
        );
    }

    private static Stream<Arguments> invalidRuntimeCodes() {
        return Stream.of(
                Arguments.of("null", null),
                Arguments.of("empty", ""),
                Arguments.of("blank", "   "),
                Arguments.of("unicode whitespace", "\u2003")
        );
    }

    private static Stream<Arguments> nonFiniteValues() {
        return Stream.of(
                Arguments.of("NaN", Float.NaN),
                Arguments.of("+Infinity", Float.POSITIVE_INFINITY),
                Arguments.of("-Infinity", Float.NEGATIVE_INFINITY)
        );
    }

    private static PApplet sketch() {
        PApplet sketch = new PApplet();
        sketch.width = 800;
        sketch.height = 600;
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

    private static final class NonFinitePanelJson extends JSONObject {
        private final float width;

        private NonFinitePanelJson(float width) {
            this.width = width;
            this.setString("code", "panel");
            this.setFloat("x", 10.0F);
            this.setFloat("y", 20.0F);
            this.setFloat("width", 100.0F);
            this.setFloat("height", 60.0F);
        }

        @Override
        public float getFloat(String key) {
            return "width".equals(key) ? this.width : super.getFloat(key);
        }
    }
}
