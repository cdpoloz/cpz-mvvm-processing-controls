package com.cpz.processing.controls.controls;

import com.cpz.processing.controls.controls.button.ButtonFactory;
import com.cpz.processing.controls.controls.button.config.ButtonConfigLoader;
import com.cpz.processing.controls.controls.button.style.ButtonRenderStyle;
import com.cpz.processing.controls.controls.button.style.render.ButtonRenderer;
import com.cpz.processing.controls.controls.button.style.render.DefaultButtonRenderer;
import com.cpz.processing.controls.controls.button.style.render.SvgButtonRenderer;
import com.cpz.processing.controls.controls.slider.SliderFactory;
import com.cpz.processing.controls.controls.slider.config.SliderConfigLoader;
import com.cpz.processing.controls.controls.slider.model.SliderOrientation;
import com.cpz.processing.controls.controls.slider.state.SliderViewState;
import com.cpz.processing.controls.controls.slider.style.SliderRenderStyle;
import com.cpz.processing.controls.controls.slider.style.SvgColorMode;
import com.cpz.processing.controls.controls.slider.style.render.SliderRenderer;
import com.cpz.processing.controls.controls.slider.view.SliderGeometry;
import com.cpz.processing.controls.core.style.TypographySupport;
import com.cpz.processing.controls.testsupport.ProcessingTestSupport;
import org.junit.jupiter.api.Test;
import processing.core.PFont;
import processing.data.JSONObject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ButtonSliderTypographyMatrixTest {
    @Test
    void buttonRendererImplementsTheFourTypographyCombinations() {
        PFont ambient = ProcessingTestSupport.font("Dialog", 13);
        PFont custom = ProcessingTestSupport.font("Monospaced", 16);
        ProcessingTestSupport.FontApplet sketch = new ProcessingTestSupport.FontApplet(custom);
        ProcessingTestSupport.RecordingGraphics graphics = ProcessingTestSupport.graphics(sketch);

        for (ButtonRenderer renderer : new ButtonRenderer[]{
                new DefaultButtonRenderer(),
                new SvgButtonRenderer(sketch, "missing.svg")
        }) {
            assertButtonCase(renderer, sketch, graphics, ambient, custom, null, null, false, null);
            assertButtonCase(renderer, sketch, graphics, ambient, custom, null, 19.0F, false, 19.0F);
            assertButtonCase(renderer, sketch, graphics, ambient, custom, custom, 18.0F, true, 18.0F);
            assertButtonCase(
                    renderer,
                    sketch,
                    graphics,
                    ambient,
                    custom,
                    custom,
                    null,
                    true,
                    TypographySupport.DEFAULT_CUSTOM_FONT_SIZE
            );
        }
    }

    @Test
    void sliderRendererImplementsTheFourTypographyCombinations() {
        PFont ambient = ProcessingTestSupport.font("Dialog", 13);
        PFont custom = ProcessingTestSupport.font("Monospaced", 16);
        ProcessingTestSupport.FontApplet sketch = new ProcessingTestSupport.FontApplet(custom);
        ProcessingTestSupport.RecordingGraphics graphics = ProcessingTestSupport.graphics(sketch);
        SliderRenderer renderer = new SliderRenderer();

        for (SliderOrientation orientation : SliderOrientation.values()) {
            assertSliderCase(renderer, sketch, graphics, ambient, custom, orientation, null, null, false, null);
            assertSliderCase(renderer, sketch, graphics, ambient, custom, orientation, null, 19.0F, false, 19.0F);
            assertSliderCase(renderer, sketch, graphics, ambient, custom, orientation, custom, 18.0F, true, 18.0F);
            assertSliderCase(
                    renderer,
                    sketch,
                    graphics,
                    ambient,
                    custom,
                    orientation,
                    custom,
                    null,
                    true,
                    TypographySupport.DEFAULT_CUSTOM_FONT_SIZE
            );
        }
    }

    @Test
    void factoriesUseTheNamedDefaultSizeWhenFontHasNoTextSize() {
        PFont custom = ProcessingTestSupport.font("Monospaced", 16);

        ProcessingTestSupport.FontApplet buttonSketch = new ProcessingTestSupport.FontApplet(custom);
        ButtonFactory.create(
                buttonSketch,
                new ButtonConfigLoader(buttonSketch).loadFromJson(
                        JSONObject.parse("{\"code\":\"b\",\"text\":\"B\",\"x\":1,\"y\":1,\"width\":10,\"height\":10,\"style\":{\"font\":\"font.ttf\"}}"),
                        "button.json"
                )
        );
        assertEquals(TypographySupport.DEFAULT_CUSTOM_FONT_SIZE, buttonSketch.getLastCreateFontSize());

        ProcessingTestSupport.FontApplet sliderSketch = new ProcessingTestSupport.FontApplet(custom);
        SliderFactory.create(
                sliderSketch,
                new SliderConfigLoader(sliderSketch).loadFromJson(
                        JSONObject.parse("{\"code\":\"s\",\"min\":0,\"max\":1,\"step\":0.1,\"value\":0.5,\"x\":1,\"y\":1,\"width\":10,\"height\":10,\"style\":{\"font\":\"font.ttf\"}}"),
                        "slider.json"
                )
        );
        assertEquals(TypographySupport.DEFAULT_CUSTOM_FONT_SIZE, sliderSketch.getLastCreateFontSize());
    }

    private static void assertButtonCase(
            ButtonRenderer renderer,
            ProcessingTestSupport.FontApplet sketch,
            ProcessingTestSupport.RecordingGraphics graphics,
            PFont ambient,
            PFont custom,
            PFont controlFont,
            Float textSize,
            boolean expectCustomFont,
            Float expectedSize
    ) {
        graphics.textFont(ambient, 13.0F);
        graphics.clearTypographyHistory();

        renderer.render(
                sketch,
                20.0F,
                20.0F,
                40.0F,
                20.0F,
                new ButtonRenderStyle(1, 2, 1.0F, 3, 4.0F, true, "Button", controlFont, textSize)
        );

        assertEquals(expectCustomFont, graphics.appliedFonts().stream().anyMatch(font -> font == custom));
        if (expectedSize != null) {
            assertTrue(graphics.appliedSizes().contains(expectedSize));
        } else {
            assertFalse(graphics.appliedSizes().stream().anyMatch(size -> size != 13.0F));
        }
        assertEquals(ambient, graphics.textFont);
        assertEquals(13.0F, graphics.textSize);
    }

    private static void assertSliderCase(
            SliderRenderer renderer,
            ProcessingTestSupport.FontApplet sketch,
            ProcessingTestSupport.RecordingGraphics graphics,
            PFont ambient,
            PFont custom,
            SliderOrientation orientation,
            PFont controlFont,
            Float textSize,
            boolean expectCustomFont,
            Float expectedSize
    ) {
        graphics.textFont(ambient, 13.0F);
        graphics.clearTypographyHistory();

        renderer.render(
                sketch,
                new SliderGeometry(20.0F, 20.0F, 40.0F, 20.0F, orientation, 0.0F, 40.0F),
                new SliderViewState(0.5F, false, false, false, true, "0.5", true),
                new SliderRenderStyle(
                        1, 2, 1.0F, 4.0F, 3, 4, 5, 1.0F,
                        10.0F, 6, SvgColorMode.USE_RENDER_STYLE, null, true,
                        controlFont, textSize
                )
        );

        assertEquals(expectCustomFont, graphics.appliedFonts().stream().anyMatch(font -> font == custom));
        if (expectedSize != null) {
            assertTrue(graphics.appliedSizes().contains(expectedSize));
        } else {
            assertFalse(graphics.appliedSizes().stream().anyMatch(size -> size != 13.0F));
        }
        assertEquals(ambient, graphics.textFont);
        assertEquals(13.0F, graphics.textSize);
    }
}
