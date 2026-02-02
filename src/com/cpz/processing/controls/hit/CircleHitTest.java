package com.cpz.processing.controls.hit;

import com.cpz.processing.controls.hit.interfaces.HitTest;

public final class CircleHitTest implements HitTest {

    private float cx, cy, r;

    public CircleHitTest() {
    }

    public CircleHitTest(float cx, float cy, float r) {
        set(cx, cy, r);
    }

    @Override
    public void onLayout(float x, float y, float width, float height) {
        float diameter = Math.min(width, height);
        set(x, y, diameter * 0.5f);
    }

    public void set(float cx, float cy, float r) {
        this.cx = cx;
        this.cy = cy;
        this.r = r;
    }

    @Override
    public boolean contains(float px, float py) {
        float dx = px - cx;
        float dy = py - cy;
        return dx * dx + dy * dy <= r * r;
    }
}

