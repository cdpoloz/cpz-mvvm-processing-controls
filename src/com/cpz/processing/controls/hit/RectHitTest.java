package com.cpz.processing.controls.hit;

import com.cpz.processing.controls.hit.interfaces.HitTest;

/**
 * @author CPZ
 */
public class RectHitTest implements HitTest {

    private float x, y, w, h;

    public RectHitTest() {
    }

    public RectHitTest(float centerX, float centerY, float width, float height) {
        this.w = width;
        this.h = height;
        this.x = centerX - width * 0.5f;
        this.y = centerY - height * 0.5f;
    }

    @Override
    public void onLayout(float centerX, float centerY, float width, float height) {
        this.w = width;
        this.h = height;
        this.x = centerX - width * 0.5f;
        this.y = centerY - height * 0.5f;
    }

    @Override
    public boolean contains(float px, float py) {
        return px >= x && px <= x + w
                && py >= y && py <= y + h;
    }

}
