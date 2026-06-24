package com.cpz.processing.controls.controls;

import com.cpz.processing.controls.controls.label.Label;
import com.cpz.processing.controls.controls.label.config.LabelStyleConfig;
import com.cpz.processing.controls.controls.label.style.DefaultLabelStyle;
import com.cpz.processing.controls.controls.label.view.LabelView;
import com.cpz.processing.controls.controls.numericfield.NumericField;
import com.cpz.processing.controls.controls.numericfield.config.NumericFieldStyleConfig;
import com.cpz.processing.controls.controls.numericfield.style.NumericFieldStyle;
import com.cpz.processing.controls.controls.numericfield.view.NumericFieldView;
import com.cpz.processing.controls.controls.textfield.TextField;
import com.cpz.processing.controls.controls.textfield.config.TextFieldStyleConfig;
import com.cpz.processing.controls.controls.textfield.style.DefaultTextFieldStyle;
import com.cpz.processing.controls.controls.textfield.view.TextFieldView;
import com.cpz.processing.controls.testsupport.ProcessingTestSupport;
import org.junit.jupiter.api.Test;
import processing.core.PFont;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MeasurementTypographyConsistencyTest {
    @Test
    void labelMeasurementAndRenderingUseTheSameTypography() throws Exception {
        PFont custom = ProcessingTestSupport.font("Monospaced", 18);
        ProcessingTestSupport.FontApplet sketch = new ProcessingTestSupport.FontApplet(custom);
        ProcessingTestSupport.RecordingGraphics graphics = ProcessingTestSupport.graphics(sketch);
        Label label = new Label(sketch, "label", "Measure me", 10.0F, 10.0F);
        LabelStyleConfig config = new LabelStyleConfig();
        config.font = custom;
        config.textSize = 18.0F;
        label.setStyle(new DefaultLabelStyle(config));
        LabelView view = privateField(label, "view", LabelView.class);

        view.getWidth();
        label.draw();

        assertTypographyWasAppliedForMeasurementAndRendering(graphics, custom, 18.0F);
    }

    @Test
    void textFieldMeasurementAndRenderingUseTheSameTypography() throws Exception {
        PFont custom = ProcessingTestSupport.font("Monospaced", 18);
        ProcessingTestSupport.FontApplet sketch = new ProcessingTestSupport.FontApplet(custom);
        ProcessingTestSupport.RecordingGraphics graphics = ProcessingTestSupport.graphics(sketch);
        TextField field = new TextField(sketch, "text", "Measure me", 50.0F, 20.0F, 100.0F, 30.0F);
        TextFieldStyleConfig config = new TextFieldStyleConfig();
        config.font = custom;
        config.textSize = 18.0F;
        field.setStyle(new DefaultTextFieldStyle(config));
        TextFieldView view = privateField(field, "view", TextFieldView.class);

        view.positionToCursorIndex(75.0F);
        field.draw();

        assertTypographyWasAppliedForMeasurementAndRendering(graphics, custom, 18.0F);
    }

    @Test
    void numericFieldMeasurementAndRenderingUseTheSameTypography() throws Exception {
        PFont custom = ProcessingTestSupport.font("Monospaced", 18);
        ProcessingTestSupport.FontApplet sketch = new ProcessingTestSupport.FontApplet(custom);
        ProcessingTestSupport.RecordingGraphics graphics = ProcessingTestSupport.graphics(sketch);
        NumericField field = new NumericField(sketch, "number", "12.5", 50.0F, 20.0F, 100.0F, 30.0F);
        NumericFieldStyleConfig config = new NumericFieldStyleConfig();
        config.font = custom;
        config.textSize = 18.0F;
        field.setStyle(new NumericFieldStyle(config));
        NumericFieldView view = privateField(field, "view", NumericFieldView.class);

        view.positionToCursorIndex(75.0F);
        field.draw();

        assertTypographyWasAppliedForMeasurementAndRendering(graphics, custom, 18.0F);
    }

    private static void assertTypographyWasAppliedForMeasurementAndRendering(
            ProcessingTestSupport.RecordingGraphics graphics,
            PFont font,
            float size
    ) {
        long fontApplications = graphics.appliedFonts().stream().filter(applied -> applied == font).count();
        long sizeApplications = graphics.appliedSizes().stream().filter(applied -> applied == size).count();
        assertTrue(fontApplications >= 2, "expected the font in both measurement and rendering");
        assertTrue(sizeApplications >= 2, "expected the size in both measurement and rendering");
    }

    private static <T> T privateField(Object owner, String name, Class<T> type) throws Exception {
        Field field = owner.getClass().getDeclaredField(name);
        field.setAccessible(true);
        return type.cast(field.get(owner));
    }
}
