package com.cpz.processing.controls.hit;

/**
 * @author CPZ
 */
public interface HitTest {

    boolean contains(float px, float py);

    default void onLayout(float x, float y, float size) {
        // por defecto, no hace nada
    }

}
