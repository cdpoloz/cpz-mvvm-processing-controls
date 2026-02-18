package com.cpz.processing.controls.labelcontrol;

/**
 * @author CPZ
 */

public final class LabelModel {

    private String text = "";
    private boolean display = true;

    public void setText(String text) {
        this.text = text != null ? text : "";
    }

    public String getText() {
        return text;
    }

    public void setDisplay(boolean display) {
        this.display = display;
    }

    public boolean isDisplay(){
        return display;
    }
}
