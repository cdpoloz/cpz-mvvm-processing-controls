package com.cpz.processing.controls.core.overlay.notification;

import com.cpz.processing.controls.controls.geometry.ControlMeasure;
import com.cpz.processing.controls.controls.geometry.MeasureMode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NotificationPositionTest {
    @Test
    void absoluteFactoryCreatesAbsoluteMeasures() {
        NotificationPosition position = NotificationPosition.absolute(120.0F, 80.0F);

        assertMeasure(position.x(), MeasureMode.ABSOLUTE, 120.0F);
        assertMeasure(position.y(), MeasureMode.ABSOLUTE, 80.0F);
    }

    @Test
    void relativeFactoryCreatesRelativeMeasures() {
        NotificationPosition position = NotificationPosition.relative(0.5F, 0.25F);

        assertMeasure(position.x(), MeasureMode.RELATIVE, 0.5F);
        assertMeasure(position.y(), MeasureMode.RELATIVE, 0.25F);
    }

    @Test
    void ofPreservesMixedMeasureInstances() {
        ControlMeasure x = ControlMeasure.relative(0.5F);
        ControlMeasure y = ControlMeasure.absolute(120.0F);

        NotificationPosition position = NotificationPosition.of(x, y);

        assertSame(x, position.x());
        assertSame(y, position.y());
    }

    @Test
    void nullCoordinatesFailFast() {
        assertThrows(NullPointerException.class, () -> NotificationPosition.of(null, ControlMeasure.absolute(1.0F)));
        assertThrows(NullPointerException.class, () -> NotificationPosition.of(ControlMeasure.absolute(1.0F), null));
    }

    @Test
    void nonFiniteCoordinatesFailFast() {
        assertThrows(IllegalArgumentException.class, () -> NotificationPosition.absolute(Float.NaN, 0.0F));
        assertThrows(IllegalArgumentException.class, () -> NotificationPosition.absolute(0.0F, Float.NaN));
        assertThrows(IllegalArgumentException.class, () -> NotificationPosition.relative(Float.POSITIVE_INFINITY, 0.0F));
        assertThrows(IllegalArgumentException.class, () -> NotificationPosition.relative(0.0F, Float.NEGATIVE_INFINITY));
    }

    @Test
    void negativeAndOutOfRangeRelativeCoordinatesAreAccepted() {
        NotificationPosition absolute = NotificationPosition.absolute(-20.0F, -10.0F);
        NotificationPosition relative = NotificationPosition.relative(-0.25F, 1.5F);

        assertEquals(-20.0F, absolute.x().value());
        assertEquals(-10.0F, absolute.y().value());
        assertEquals(-0.25F, relative.x().value());
        assertEquals(1.5F, relative.y().value());
    }

    private static void assertMeasure(ControlMeasure measure, MeasureMode mode, float value) {
        assertEquals(mode, measure.mode());
        assertEquals(value, measure.value());
    }
}
