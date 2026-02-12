package com.cpz.processing.controls.labelcontrol;

import lombok.Getter;
import lombok.Setter;

/**
 * @author CPZ
 */
@Getter
public final class LabelModel {

    private String text = "";
    @Setter
    private boolean display = true;

    public void setText(String text) {
        this.text = text != null ? text : "";
    }

}
