package com.cpz.processing.controls.config;

import com.cpz.processing.controls.controls.button.config.ButtonConfigLoader;
import com.cpz.processing.controls.controls.dropdown.config.DropDownConfigLoader;
import com.cpz.processing.controls.controls.label.config.LabelConfigLoader;
import com.cpz.processing.controls.controls.numericfield.config.NumericFieldConfigLoader;
import com.cpz.processing.controls.controls.radiogroup.config.RadioGroupConfigLoader;
import com.cpz.processing.controls.controls.slider.config.SliderConfigLoader;
import com.cpz.processing.controls.controls.textfield.config.TextFieldConfigLoader;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import processing.core.PApplet;
import processing.data.JSONObject;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FontJsonConfigTest {
    private final PApplet sketch = new PApplet();

    @TestFactory
    Stream<DynamicTest> fontIsOptionalAndAcceptsJsonNullForEveryTextControl() {
        return specs().stream().flatMap(spec -> Stream.of(
                DynamicTest.dynamicTest(spec.name + " accepts absent font", () ->
                        assertNull(spec.fontPath.apply(parse(spec.jsonWithStyle("{}"))))),
                DynamicTest.dynamicTest(spec.name + " accepts null font", () ->
                        assertNull(spec.fontPath.apply(parse(spec.jsonWithStyle("{\"font\":null}")))))
        ));
    }

    @TestFactory
    Stream<DynamicTest> readsAndTrimsFontPathsForEveryTextControl() {
        return specs().stream().map(spec -> DynamicTest.dynamicTest(spec.name, () ->
                assertEquals(
                        "data/font/JetBrainsMono.ttf",
                        spec.fontPath.apply(parse(spec.jsonWithStyle(
                                "{\"font\":\"  data/font/JetBrainsMono.ttf  \"}"
                        )))
                )
        ));
    }

    @TestFactory
    Stream<DynamicTest> rejectsBlankFontPathsWithControlPropertySourceAndCause() {
        return specs().stream().flatMap(spec -> Stream.of("", "   ").map(value ->
                DynamicTest.dynamicTest(spec.name + " rejects '" + value + "'", () -> {
                    IllegalArgumentException exception = assertThrows(
                            IllegalArgumentException.class,
                            () -> spec.fontPath.apply(parse(spec.jsonWithStyle(
                                    "{\"font\":\"" + value + "\"}"
                            )))
                    );
                    assertTrue(exception.getMessage().contains(spec.context));
                    assertTrue(exception.getMessage().contains("'font'"));
                    assertTrue(exception.getMessage().contains(spec.sourcePath));
                    assertTrue(exception.getMessage().contains("Cause"));
                    assertTrue(exception.getMessage().contains("blank"));
                })
        ));
    }

    @TestFactory
    Stream<DynamicTest> currentExampleJsonFilesRemainLoadable() {
        return specs().stream().map(spec -> DynamicTest.dynamicTest(spec.name, () -> {
            JSONObject document = JSONObject.parse(Files.readString(Path.of(spec.examplePath)));
            JSONObject control = document.getJSONArray("controls").getJSONObject(0);
            assertEquals("data/font/JetBrainsMono.ttf", spec.fontPath.apply(control));
        }));
    }

    private List<Spec> specs() {
        return List.of(
                new Spec(
                        "button",
                        "button style",
                        "button-source.json",
                        "data/config/button-test.json",
                        "{\"code\":\"b\",\"text\":\"Button\",\"x\":1,\"y\":1,\"width\":10,\"height\":10,\"style\":STYLE}",
                        json -> new ButtonConfigLoader(this.sketch)
                                .loadFromJson(json, "button-source.json").getStyle().getFontPath()
                ),
                new Spec(
                        "label",
                        "label style",
                        "label-source.json",
                        "data/config/label-test.json",
                        "{\"code\":\"l\",\"text\":\"Label\",\"x\":1,\"y\":1,\"width\":10,\"height\":10,\"style\":STYLE}",
                        json -> new LabelConfigLoader(this.sketch)
                                .loadFromJson(json, "label-source.json").getStyle().getFontPath()
                ),
                new Spec(
                        "slider",
                        "slider style",
                        "slider-source.json",
                        "data/config/slider-test.json",
                        "{\"code\":\"s\",\"min\":0,\"max\":1,\"step\":0.1,\"value\":0.5,\"x\":1,\"y\":1,\"width\":10,\"height\":10,\"style\":STYLE}",
                        json -> new SliderConfigLoader(this.sketch)
                                .loadFromJson(json, "slider-source.json").getStyle().getFontPath()
                ),
                new Spec(
                        "radio group",
                        "radio group style",
                        "radio-source.json",
                        "data/config/radiogroup-test.json",
                        "{\"code\":\"r\",\"options\":[\"A\"],\"x\":1,\"y\":1,\"width\":10,\"style\":STYLE}",
                        json -> new RadioGroupConfigLoader(this.sketch)
                                .loadFromJson(json, "radio-source.json").getStyle().getFontPath()
                ),
                new Spec(
                        "text field",
                        "text field style",
                        "text-source.json",
                        "data/config/textfield-test.json",
                        "{\"code\":\"t\",\"text\":\"Text\",\"x\":1,\"y\":1,\"width\":10,\"height\":10,\"style\":STYLE}",
                        json -> new TextFieldConfigLoader(this.sketch)
                                .loadFromJson(json, "text-source.json").getStyle().getFontPath()
                ),
                new Spec(
                        "numeric field",
                        "numeric field style",
                        "numeric-source.json",
                        "data/config/numericfield-test.json",
                        "{\"code\":\"n\",\"text\":\"12.5\",\"x\":1,\"y\":1,\"width\":10,\"height\":10,\"style\":STYLE}",
                        json -> new NumericFieldConfigLoader(this.sketch)
                                .loadFromJson(json, "numeric-source.json").getStyle().getFontPath()
                ),
                new Spec(
                        "drop down",
                        "drop down style",
                        "dropdown-source.json",
                        "data/config/dropdown-test.json",
                        "{\"code\":\"d\",\"items\":[\"A\"],\"selectedIndex\":0,\"x\":1,\"y\":1,\"width\":10,\"height\":10,\"style\":STYLE}",
                        json -> new DropDownConfigLoader(this.sketch)
                                .loadFromJson(json, "dropdown-source.json").getStyle().getFontPath()
                )
        );
    }

    private static JSONObject parse(String json) {
        return JSONObject.parse(json);
    }

    private record Spec(
            String name,
            String context,
            String sourcePath,
            String examplePath,
            String baseJson,
            Function<JSONObject, String> fontPath
    ) {
        private String jsonWithStyle(String style) {
            return this.baseJson.replace("STYLE", style);
        }
    }
}
