package com.cpz.processing.controls.dev;

import com.cpz.processing.controls.common.focus.FocusManager;
import com.cpz.processing.controls.common.input.KeyboardInputAdapter;
import com.cpz.processing.controls.textfieldcontrol.input.TextFieldInputAdapter;
import com.cpz.processing.controls.textfieldcontrol.model.TextFieldModel;
import com.cpz.processing.controls.textfieldcontrol.style.DefaultTextFieldStyle;
import com.cpz.processing.controls.textfieldcontrol.style.TextFieldStyleConfig;
import com.cpz.processing.controls.textfieldcontrol.view.TextFieldView;
import com.cpz.processing.controls.textfieldcontrol.viewmodel.TextFieldViewModel;
import com.cpz.processing.controls.util.Colors;
import processing.core.PApplet;
import processing.core.PFont;

public class TextFieldDevSketch extends PApplet {

    private final FocusManager focusManager = new FocusManager();

    private TextFieldView customFontView;
    private TextFieldView defaultFontView;
    private TextFieldInputAdapter customFontInput;
    private TextFieldInputAdapter defaultFontInput;
    private KeyboardInputAdapter keyboardAdapter;

    @Override
    public void settings() {
        size(980, 420);
        smooth(4);
    }

    @Override
    public void setup() {
        PFont abel = createFont("data/font/abel-regular.ttf", 16, true);
        keyboardAdapter = new KeyboardInputAdapter(focusManager);

        TextFieldViewModel customFontViewModel = new TextFieldViewModel(new TextFieldModel());
        customFontViewModel.setText("Custom font field");
        customFontView = new TextFieldView(this, customFontViewModel, width * 0.5f, 140f, 420f, 46f);
        customFontView.setStyle(new DefaultTextFieldStyle(createCustomFontStyle(abel)));
        customFontInput = new TextFieldInputAdapter(customFontView, customFontViewModel, focusManager);

        TextFieldViewModel defaultFontViewModel = new TextFieldViewModel(new TextFieldModel());
        defaultFontViewModel.setText("Default font field");
        defaultFontView = new TextFieldView(this, defaultFontViewModel, width * 0.5f, 250f, 420f, 46f);
        defaultFontView.setStyle(new DefaultTextFieldStyle(createDefaultFontStyle()));
        defaultFontInput = new TextFieldInputAdapter(defaultFontView, defaultFontViewModel, focusManager);
    }

    @Override
    public void draw() {
        background(242);
        drawTitles();
        customFontView.draw();
        defaultFontView.draw();
    }

    @Override
    public void keyReleased() {
        if (key == ESC) key = 0;
    }

    @Override
    public void mousePressed() {
        boolean handled = customFontInput.handleMousePress(mouseX, mouseY);
        if (!handled) {
            handled = defaultFontInput.handleMousePress(mouseX, mouseY);
        }
        if (!handled) {
            focusManager.clearFocus();
        }
    }

    @Override
    public void mouseDragged() {
        customFontInput.handleMouseDrag(mouseX, mouseY);
        defaultFontInput.handleMouseDrag(mouseX, mouseY);
    }

    @Override
    public void mouseReleased() {
        customFontInput.handleMouseRelease();
        defaultFontInput.handleMouseRelease();
    }

    @Override
    public void keyPressed() {
        keyboardAdapter.onKeyPressed(key, keyCode, keyEvent);
    }

    @Override
    public void keyTyped() {
        keyboardAdapter.onKeyTyped(key);
    }

    private void drawTitles() {
        pushStyle();
        fill(36);
        textAlign(CENTER, CENTER);
        textSize(20);
        text("TextField Dev Sketch", width * 0.5f, 52f);
        textSize(14);
        text("Custom font: Abel from data/font/abel-regular.ttf", width * 0.5f, 88f);
        text("Default font: Processing fallback when font is null", width * 0.5f, 198f);
        popStyle();
    }

    private TextFieldStyleConfig createCustomFontStyle(PFont font) {
        TextFieldStyleConfig config = new TextFieldStyleConfig();
        config.backgroundColor = Colors.rgb(248, 245, 238);
        config.borderColor = Colors.rgb(167, 93, 68);
        config.textColor = Colors.rgb(48, 34, 28);
        config.cursorColor = Colors.rgb(208, 96, 48);
        config.selectionColor = Colors.rgb(228, 197, 168);
        config.selectionTextColor = Colors.rgb(48, 34, 28);
        config.textSize = 18f;
        config.font = font;
        return config;
    }

    private TextFieldStyleConfig createDefaultFontStyle() {
        TextFieldStyleConfig config = new TextFieldStyleConfig();
        config.backgroundColor = Colors.rgb(236, 242, 248);
        config.borderColor = Colors.rgb(72, 116, 156);
        config.textColor = Colors.rgb(28, 44, 62);
        config.cursorColor = Colors.rgb(38, 132, 212);
        config.selectionColor = Colors.rgb(182, 217, 248);
        config.selectionTextColor = Colors.rgb(28, 44, 62);
        config.textSize = 16f;
        config.font = null;
        return config;
    }
}
