package com.cpz.processing.controls.controls;

/**
 * Optional contract for controls that need parent context beyond size.
 *
 * <p>The control keeps its own coordinates local to the parent while this
 * contract supplies the resolved parent offset in sketch space. Controls that
 * show global overlays can use it to compute an effective global anchor without
 * coupling the parent container to a concrete implementation.</p>
 *
 * @author CPZ
 */
public interface ParentContextAwareControl extends Control {
    /**
     * Supplies the resolved parent offset in sketch/global coordinates.
     *
     * <p>The control must keep its own coordinates local to the parent. The
     * offset is additional context for features that need sketch-space anchors,
     * such as global overlays or tooltip bounds.</p>
     *
     * @param x resolved parent x offset
     * @param y resolved parent y offset
     */
    void setParentOffset(float x, float y);

    /**
     * Clears any parent offset previously supplied by a container.
     */
    void clearParentOffset();

    /**
     * Notification hook invoked before a container clears the parent context.
     *
     * <p>Controls with transient parent-scoped state, such as active overlays,
     * can close or reset that state here. The default implementation has no
     * effect.</p>
     */
    default void onRemovedFromParent() {
    }
}
