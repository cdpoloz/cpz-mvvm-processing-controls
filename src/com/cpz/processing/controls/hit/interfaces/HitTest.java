package com.cpz.processing.controls.hit.interfaces;

/**
 * @author CPZ
 */
public interface HitTest {

    boolean contains(float px, float py);

    default void onLayout(float x, float y, float width, float height) {
        // por defecto, no hace nada
    }

}
