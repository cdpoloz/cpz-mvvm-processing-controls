package com.cpz.processing.controls.controls;

/**
 * Optional contract for controls whose geometry can resolve against a parent.
 *
 * @author CPZ
 */
public interface ParentSizeAwareControl extends Control {
    void setParentSize(float width, float height);

    void clearParentSize();
}
