package com.cpz.processing.controls.config;

import com.cpz.processing.controls.controls.label.Label;
import com.cpz.processing.controls.controls.label.LabelFactory;
import com.cpz.processing.controls.controls.label.config.LabelConfig;
import com.cpz.processing.controls.controls.label.config.LabelConfigLoader;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;
import processing.data.JSONObject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

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

    private static LabelConfig labelConfig(String json) {
        return new LabelConfigLoader(new PApplet()).loadFromJson(JSONObject.parse(json), "label.json");
    }

    private static PApplet sketch(int width, int height) {
        PApplet sketch = new PApplet();
        sketch.width = width;
        sketch.height = height;
        return sketch;
    }
}
