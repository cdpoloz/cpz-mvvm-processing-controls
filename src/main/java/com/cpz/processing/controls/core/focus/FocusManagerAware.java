package com.cpz.processing.controls.core.focus;

/**
 * Optional capability for controls and input layers that participate in a
 * host-owned focus scope.
 *
 * <p>{@code InputManager} uses this contract when layers enter or leave its
 * routing registry. Implementations must release any focus owned through the
 * supplied manager when detached.</p>
 *
 * @author CPZ
 */
public interface FocusManagerAware {
    /**
     * Attaches this participant to a focus authority.
     *
     * @param focusManager focus authority owned by the routing host
     */
    void attachFocusManager(FocusManager focusManager);

    /**
     * Detaches this participant from a focus authority.
     *
     * @param focusManager focus authority previously supplied on attachment
     */
    void detachFocusManager(FocusManager focusManager);
}
