package com.cpz.processing.controls.controls;

import com.cpz.processing.controls.controls.button.ButtonFactory;
import com.cpz.processing.controls.controls.button.Button;
import com.cpz.processing.controls.controls.button.config.ButtonConfigLoader;
import com.cpz.processing.controls.controls.button.style.ButtonRenderStyle;
import com.cpz.processing.controls.controls.button.style.render.SvgButtonRenderer;
import com.cpz.processing.controls.controls.dropdown.DropDownFactory;
import com.cpz.processing.controls.controls.dropdown.config.DropDownConfigLoader;
import com.cpz.processing.controls.controls.label.LabelFactory;
import com.cpz.processing.controls.controls.label.config.LabelConfigLoader;
import com.cpz.processing.controls.controls.numericfield.NumericFieldFactory;
import com.cpz.processing.controls.controls.numericfield.config.NumericFieldConfigLoader;
import com.cpz.processing.controls.controls.radiogroup.RadioGroupFactory;
import com.cpz.processing.controls.controls.radiogroup.config.RadioGroupConfigLoader;
import com.cpz.processing.controls.controls.slider.SliderFactory;
import com.cpz.processing.controls.controls.slider.config.SliderConfigLoader;
import com.cpz.processing.controls.controls.textfield.TextFieldFactory;
import com.cpz.processing.controls.controls.textfield.config.TextFieldConfigLoader;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.overlay.OverlayManager;
import com.cpz.processing.controls.testsupport.ProcessingTestSupport;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import processing.core.PFont;
import processing.data.JSONObject;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FontFactoryPropagationTest {
    @TestFactory
    Stream<DynamicTest> factoriesLoadOnceAndRenderWithTheExactFontInstance() {
        return specs().stream().map(spec -> DynamicTest.dynamicTest(spec.name, () -> {
            PFont font = ProcessingTestSupport.font("Monospaced", 16);
            ProcessingTestSupport.FontApplet sketch = new ProcessingTestSupport.FontApplet(font);
            ProcessingTestSupport.RecordingGraphics graphics = ProcessingTestSupport.graphics(sketch);

            Control control = spec.create.apply(sketch);
            assertEquals(1, sketch.getCreateFontCalls());

            control.draw();

            assertEquals(1, sketch.getCreateFontCalls(), "font loading must not occur in draw()");
            assertTrue(graphics.appliedFonts().stream().anyMatch(applied -> applied == font));
        }));
    }

    @Test
    void svgButtonRendererAppliesTheConfiguredTypography() {
        PFont font = ProcessingTestSupport.font("Monospaced", 16);
        ProcessingTestSupport.FontApplet sketch = new ProcessingTestSupport.FontApplet(font);
        ProcessingTestSupport.RecordingGraphics graphics = ProcessingTestSupport.graphics(sketch);
        SvgButtonRenderer renderer = new SvgButtonRenderer(sketch, "missing-shape.svg");

        renderer.render(
                sketch,
                20.0F,
                20.0F,
                40.0F,
                20.0F,
                new ButtonRenderStyle(1, 2, 1.0F, 3, 4.0F, true, "SVG", font, 18.0F)
        );

        assertTrue(graphics.appliedFonts().stream().anyMatch(applied -> applied == font));
        assertTrue(graphics.appliedSizes().contains(18.0F));
    }

    @Test
    void controlMeasureTextSizeRefreshesJsonFontBeforeButtonDrawAndThenReusesIt() {
        PFont font = ProcessingTestSupport.font("Monospaced", 21);
        ProcessingTestSupport.FontApplet sketch = new ProcessingTestSupport.FontApplet(font);
        ProcessingTestSupport.RecordingGraphics graphics = ProcessingTestSupport.graphics(sketch);

        Button button = ButtonFactory.create(
                sketch,
                new ButtonConfigLoader(sketch).loadFromJson(
                        parse("{\"code\":\"b\",\"text\":\"Button\",\"x\":20,\"y\":20,\"width\":40,\"height\":20,"
                                + "\"textSize\":{\"mode\":\"absolute\",\"value\":21},"
                                + "\"style\":{\"font\":\"font.ttf\"}}"),
                        "controls.json"
                )
        );
        int callsAfterFactory = sketch.getCreateFontCalls();
        button.draw();

        assertTrue(sketch.getCreateFontSizes().contains(21.0F), sketch.getCreateFontSizes().toString());
        assertTrue(graphics.appliedSizes().contains(21.0F));
        assertEquals(callsAfterFactory, sketch.getCreateFontCalls(), "draw must reuse the size-resolved JSON font");
    }

    private List<Spec> specs() {
        return List.of(
                new Spec("button", sketch -> ButtonFactory.create(
                        sketch,
                        new ButtonConfigLoader(sketch).loadFromJson(
                                parse("{\"code\":\"b\",\"text\":\"Button\",\"x\":20,\"y\":20,\"width\":40,\"height\":20,\"style\":{\"font\":\"font.ttf\",\"textSize\":16}}"),
                                "controls.json"
                        )
                )),
                new Spec("label", sketch -> LabelFactory.create(
                        sketch,
                        new LabelConfigLoader(sketch).loadFromJson(
                                parse("{\"code\":\"l\",\"text\":\"Label\",\"x\":1,\"y\":1,\"width\":40,\"height\":20,\"style\":{\"font\":\"font.ttf\",\"textSize\":16}}"),
                                "controls.json"
                        )
                )),
                new Spec("horizontal slider", sketch -> SliderFactory.create(
                        sketch,
                        new SliderConfigLoader(sketch).loadFromJson(
                                parse("{\"code\":\"s1\",\"min\":0,\"max\":1,\"step\":0.1,\"value\":0.5,\"x\":20,\"y\":20,\"width\":40,\"height\":20,\"orientation\":\"horizontal\",\"style\":{\"font\":\"font.ttf\",\"textSize\":16}}"),
                                "controls.json"
                        )
                )),
                new Spec("vertical slider", sketch -> SliderFactory.create(
                        sketch,
                        new SliderConfigLoader(sketch).loadFromJson(
                                parse("{\"code\":\"s2\",\"min\":0,\"max\":1,\"step\":0.1,\"value\":0.5,\"x\":20,\"y\":20,\"width\":20,\"height\":40,\"orientation\":\"vertical\",\"style\":{\"font\":\"font.ttf\",\"textSize\":16}}"),
                                "controls.json"
                        )
                )),
                new Spec("radio group", sketch -> RadioGroupFactory.create(
                        sketch,
                        new RadioGroupConfigLoader(sketch).loadFromJson(
                                parse("{\"code\":\"r\",\"options\":[\"A\"],\"x\":20,\"y\":20,\"width\":40,\"style\":{\"font\":\"font.ttf\",\"textSize\":16}}"),
                                "controls.json"
                        )
                )),
                new Spec("text field", sketch -> TextFieldFactory.create(
                        sketch,
                        new TextFieldConfigLoader(sketch).loadFromJson(
                                parse("{\"code\":\"t\",\"text\":\"Text\",\"x\":20,\"y\":20,\"width\":40,\"height\":20,\"style\":{\"font\":\"font.ttf\",\"textSize\":16}}"),
                                "controls.json"
                        )
                )),
                new Spec("numeric field", sketch -> NumericFieldFactory.create(
                        sketch,
                        new NumericFieldConfigLoader(sketch).loadFromJson(
                                parse("{\"code\":\"n\",\"text\":\"12.5\",\"x\":20,\"y\":20,\"width\":40,\"height\":20,\"style\":{\"font\":\"font.ttf\",\"textSize\":16}}"),
                                "controls.json"
                        )
                )),
                new Spec("drop down", sketch -> DropDownFactory.create(
                        sketch,
                        new OverlayManager(),
                        new InputManager(),
                        new DropDownConfigLoader(sketch).loadFromJson(
                                parse("{\"code\":\"d\",\"items\":[\"A\"],\"selectedIndex\":0,\"x\":20,\"y\":20,\"width\":40,\"height\":20,\"style\":{\"font\":\"font.ttf\",\"textSize\":16}}"),
                                "controls.json"
                        )
                ))
        );
    }

    private static JSONObject parse(String json) {
        return JSONObject.parse(json);
    }

    private record Spec(String name, Function<ProcessingTestSupport.FontApplet, Control> create) {
    }
}
