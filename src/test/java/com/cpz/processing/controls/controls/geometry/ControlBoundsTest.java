package com.cpz.processing.controls.controls.geometry;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ControlBoundsTest {
    @Test
    void absoluteBoundsResolveWithoutParentScaling() {
        ResolvedBounds bounds = ControlBounds.absolute(10.0F, 20.0F, 30.0F, 40.0F)
                .resolve(800.0F, 600.0F);

        assertEquals(10.0F, bounds.x());
        assertEquals(20.0F, bounds.y());
        assertEquals(30.0F, bounds.width());
        assertEquals(40.0F, bounds.height());
    }

    @Test
    void relativeBoundsUseConfiguredParentAxes() {
        ResolvedBounds bounds = ControlBounds.relative(0.25F, 0.5F, 0.3F, 0.1F)
                .resolve(800.0F, 600.0F);

        assertEquals(200.0F, bounds.x());
        assertEquals(300.0F, bounds.y());
        assertEquals(180.0F, bounds.width());
        assertEquals(60.0F, bounds.height());
    }

    @Test
    void relativeMeasureUsesProvidedParentHeight() {
        assertEquals(30.0F, ControlMeasure.relative(0.05F).resolve(600.0F));
    }
}
