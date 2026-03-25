package com.cpz.processing.controls.common.viewmodel;

import com.cpz.processing.controls.common.Enableable;
import com.cpz.processing.controls.common.Visible;

public abstract class AbstractControlViewModel<M extends Enableable> implements Visible, Enableable {

    protected final M model;
    private boolean visible = true;

    protected AbstractControlViewModel(M model) {
        this.model = model;
    }

    @Override
    public final boolean isVisible() {
        return visible;
    }

    @Override
    public final void setVisible(boolean visible) {
        this.visible = visible;
        onAvailabilityChanged();
    }

    @Override
    public boolean isEnabled() {
        return model.isEnabled();
    }

    @Override
    public void setEnabled(boolean enabled) {
        model.setEnabled(enabled);
        onAvailabilityChanged();
    }

    protected void onAvailabilityChanged() {
        // default implementation does nothing
    }
}
