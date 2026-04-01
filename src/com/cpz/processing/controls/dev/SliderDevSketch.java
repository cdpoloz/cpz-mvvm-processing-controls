package com.cpz.processing.controls.dev;

import com.cpz.processing.controls.slidercontrol.SnapMode;
import com.cpz.processing.controls.slidercontrol.SliderOrientation;
import com.cpz.processing.controls.slidercontrol.input.SliderInputAdapter;
import com.cpz.processing.controls.slidercontrol.model.SliderModel;
import com.cpz.processing.controls.slidercontrol.style.SliderStyle;
import com.cpz.processing.controls.slidercontrol.style.SliderStyleConfig;
import com.cpz.processing.controls.slidercontrol.style.SvgColorMode;
import com.cpz.processing.controls.slidercontrol.view.SliderView;
import com.cpz.processing.controls.slidercontrol.viewmodel.SliderViewModel;
import com.cpz.processing.controls.util.Colors;
import processing.core.PApplet;
import processing.core.PShape;
import processing.event.MouseEvent;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SliderDevSketch extends PApplet {

    private SliderView horizontalSliderView;
    private SliderView verticalSliderView;
    private SliderView fallbackSliderView;
    private SliderView releaseSnapSliderView;
    private SliderViewModel horizontalSliderViewModel;
    private SliderViewModel verticalSliderViewModel;
    private SliderViewModel fallbackSliderViewModel;
    private SliderViewModel releaseSnapSliderViewModel;
    private SliderInputAdapter horizontalInputAdapter;
    private SliderInputAdapter verticalInputAdapter;
    private SliderInputAdapter fallbackInputAdapter;
    private SliderInputAdapter releaseSnapInputAdapter;

    @Override
    public void settings() {
        size(1100, 620);
        smooth(4);
    }

    @Override
    public void setup() {
        PShape svgThumb1 = loadSvg("data/img/test.svg");
        PShape svgThumb2 = loadSvg("data/img/test.svg");

        horizontalSliderViewModel = new SliderViewModel(createModel(
                new BigDecimal("0"),
                new BigDecimal("1"),
                new BigDecimal("0.01"),
                new BigDecimal("0.35")
        ));
        horizontalSliderViewModel.setFormatter(value -> "Horizontal: " + value.setScale(2, RoundingMode.HALF_UP).toPlainString());
        //horizontalSliderViewModel.setOnValueChanged(value -> println("Horizontal -> " + value));
        horizontalSliderView = new SliderView(this, horizontalSliderViewModel, width * 0.5f, height * 0.33f, 420f, 72f, SliderOrientation.HORIZONTAL);
        horizontalSliderView.setStyle(new SliderStyle(createHorizontalStyle(svgThumb1)));
        horizontalInputAdapter = new SliderInputAdapter(horizontalSliderView, horizontalSliderViewModel);

        verticalSliderViewModel = new SliderViewModel(createModel(
                new BigDecimal("-10"),
                new BigDecimal("10"),
                new BigDecimal("0.5"),
                new BigDecimal("2.0")
        ));
        verticalSliderViewModel.setFormatter(value -> "Vertical: " + value.setScale(1, RoundingMode.HALF_UP).toPlainString());
        //verticalSliderViewModel.setOnValueChanged(value -> println("Vertical -> " + value));
        verticalSliderView = new SliderView(this, verticalSliderViewModel, width * 0.72f, height * 0.58f, 96f, 300f, SliderOrientation.VERTICAL);
        verticalSliderView.setStyle(new SliderStyle(createVerticalStyle(svgThumb2)));
        verticalInputAdapter = new SliderInputAdapter(verticalSliderView, verticalSliderViewModel);

        fallbackSliderViewModel = new SliderViewModel(createModel(
                new BigDecimal("0"),
                new BigDecimal("100"),
                new BigDecimal("5"),
                new BigDecimal("40")
        ));
        fallbackSliderViewModel.setFormatter(null);
        //fallbackSliderViewModel.setOnValueChanged(value -> println("Fallback -> " + value));
        fallbackSliderView = new SliderView(this, fallbackSliderViewModel, width * 0.34f, height * 0.72f, 360f, 68f, SliderOrientation.HORIZONTAL);
        fallbackSliderView.setStyle(new SliderStyle(createFallbackStyle()));
        fallbackInputAdapter = new SliderInputAdapter(fallbackSliderView, fallbackSliderViewModel);

        releaseSnapSliderViewModel = new SliderViewModel(createModel(
                new BigDecimal("0"),
                new BigDecimal("1"),
                new BigDecimal("0.1"),
                new BigDecimal("0.4")
        ));
        releaseSnapSliderViewModel.setSnapMode(SnapMode.ON_RELEASE);
        releaseSnapSliderViewModel.setFormatter(value -> "Release snap: " + value.setScale(1, RoundingMode.HALF_UP).toPlainString());
        releaseSnapSliderView = new SliderView(this, releaseSnapSliderViewModel, width * 0.5f, height * 0.88f, 420f, 60f, SliderOrientation.HORIZONTAL);
        releaseSnapSliderView.setStyle(new SliderStyle(createHorizontalStyle(svgThumb1)));
        releaseSnapInputAdapter = new SliderInputAdapter(releaseSnapSliderView, releaseSnapSliderViewModel);
    }

    @Override
    public void draw() {
        background(24);
        drawTitles();
        horizontalSliderView.draw();
        verticalSliderView.draw();
        fallbackSliderView.draw();
        releaseSnapSliderView.draw();
        drawDebugPanel();
    }

    @Override
    public void keyReleased() {
        if (key == ESC) key = 0;
    }

    @Override
    public void mouseMoved() {
        forwardMove(mouseX, mouseY);
    }

    @Override
    public void mouseDragged() {
        forwardDrag(mouseX, mouseY);
    }

    @Override
    public void mousePressed() {
        horizontalInputAdapter.handleMousePress(mouseX, mouseY);
        verticalInputAdapter.handleMousePress(mouseX, mouseY);
        fallbackInputAdapter.handleMousePress(mouseX, mouseY);
        releaseSnapInputAdapter.handleMousePress(mouseX, mouseY);
    }

    @Override
    public void mouseReleased() {
        horizontalInputAdapter.handleMouseRelease(mouseX, mouseY);
        verticalInputAdapter.handleMouseRelease(mouseX, mouseY);
        fallbackInputAdapter.handleMouseRelease(mouseX, mouseY);
        releaseSnapInputAdapter.handleMouseRelease(mouseX, mouseY);
    }

    @Override
    public void mouseWheel(MouseEvent event) {
        float delta = event.getCount();
        boolean shiftDown = event.isShiftDown();
        boolean ctrlDown = event.isControlDown();
        horizontalInputAdapter.handleMouseWheel(delta, shiftDown, ctrlDown);
        verticalInputAdapter.handleMouseWheel(delta, shiftDown, ctrlDown);
        fallbackInputAdapter.handleMouseWheel(delta, shiftDown, ctrlDown);
        releaseSnapInputAdapter.handleMouseWheel(delta, shiftDown, ctrlDown);
    }

    private void forwardMove(float mx, float my) {
        horizontalInputAdapter.handleMouseMove(mx, my);
        verticalInputAdapter.handleMouseMove(mx, my);
        fallbackInputAdapter.handleMouseMove(mx, my);
        releaseSnapInputAdapter.handleMouseMove(mx, my);
    }

    private void forwardDrag(float mx, float my) {
        horizontalInputAdapter.handleMouseDrag(mx, my);
        verticalInputAdapter.handleMouseDrag(mx, my);
        fallbackInputAdapter.handleMouseDrag(mx, my);
        releaseSnapInputAdapter.handleMouseDrag(mx, my);
    }

    private void drawTitles() {
        pushStyle();
        fill(232);
        textAlign(CENTER, CENTER);
        textSize(18);
        text("Slider SVG Render Style", width * 0.5f, 72);
        text("Slider SVG Original", width * 0.72f, 72);
        text("Slider Fallback Geometry", width * 0.34f, height * 0.62f);
        text("Slider Snap On Release", width * 0.5f, height * 0.80f);
        popStyle();
    }

    private void drawDebugPanel() {
        pushStyle();
        fill(220);
        textAlign(LEFT, TOP);
        text("Checks:", 40, 420);
        text("- Drag remains active outside bounds once started", 40, 446);
        text("- Mouse wheel uses step / SHIFT x10 / CTRL x0.1", 40, 468);
        text("- Null formatter falls back to step-based plain text", 40, 490);
        text("- Top slider uses SVG with render-style colors", 40, 512);
        text("- Right slider preserves original SVG colors", 40, 534);
        text("- Bottom slider draws ellipse fallback with no SVG", 40, 556);
        text("- Lower slider uses SnapMode.ON_RELEASE for visual check", 40, 578);
        text("Horizontal value: " + horizontalSliderViewModel.getFormattedValue(), 560, 420);
        text("Vertical value: " + verticalSliderViewModel.getFormattedValue(), 560, 446);
        text("Fallback value: " + fallbackSliderViewModel.getFormattedValue(), 560, 472);
        text("Release snap value: " + releaseSnapSliderViewModel.getFormattedValue(), 560, 498);
        text("Horizontal dragging: " + horizontalSliderViewModel.isDragging(), 560, 530);
        text("Vertical dragging: " + verticalSliderViewModel.isDragging(), 560, 556);
        text("Fallback dragging: " + fallbackSliderViewModel.isDragging(), 560, 582);
        popStyle();
    }

    private SliderModel createModel(BigDecimal min, BigDecimal max, BigDecimal step, BigDecimal value) {
        SliderModel model = new SliderModel();
        model.setMin(min);
        model.setMax(max);
        model.setStep(step);
        model.setValue(value);
        return model;
    }

    private SliderStyleConfig createHorizontalStyle(PShape thumbShape) {
        SliderStyleConfig config = new SliderStyleConfig();
        config.trackColor = Colors.rgb(62, 72, 86);
        config.trackHoverColor = Colors.rgb(86, 98, 116);
        config.trackPressedColor = Colors.rgb(44, 52, 64);
        config.trackStrokeColor = Colors.gray(220);
        config.trackStrokeWeight = 1.5f;
        config.trackStrokeWeightHover = 2.5f;
        config.trackThickness = 10f;
        config.activeTrackColor = Colors.rgb(56, 159, 232);
        config.activeTrackHoverColor = Colors.rgb(98, 184, 244);
        config.activeTrackPressedColor = Colors.rgb(38, 124, 184);
        config.thumbColor = Colors.rgb(255, 255, 255);
        config.thumbHoverColor = Colors.rgb(198, 234, 255);
        config.thumbPressedColor = Colors.rgb(168, 214, 244);
        config.thumbStrokeColor = Colors.rgb(56, 159, 232);
        config.thumbStrokeWeight = 2f;
        config.thumbStrokeWeightHover = 3f;
        config.thumbSize = 34f;
        config.textColor = Colors.gray(245);
        config.disabledAlpha = 90;
        config.showValueText = true;
        config.thumbShape = thumbShape;
        config.svgColorMode = SvgColorMode.USE_RENDER_STYLE;
        return config;
    }

    private SliderStyleConfig createVerticalStyle(PShape thumbShape) {
        SliderStyleConfig config = new SliderStyleConfig();
        config.trackColor = Colors.rgb(70, 70, 70);
        config.trackHoverColor = Colors.rgb(92, 92, 92);
        config.trackPressedColor = Colors.rgb(48, 48, 48);
        config.trackStrokeColor = Colors.gray(240);
        config.trackStrokeWeight = 1.5f;
        config.trackStrokeWeightHover = 2.5f;
        config.trackThickness = 12f;
        config.activeTrackColor = Colors.rgb(242, 149, 68);
        config.activeTrackHoverColor = Colors.rgb(250, 177, 112);
        config.activeTrackPressedColor = Colors.rgb(216, 124, 48);
        config.thumbColor = Colors.gray(255);
        config.thumbHoverColor = Colors.gray(255);
        config.thumbPressedColor = Colors.gray(255);
        config.thumbStrokeColor = Colors.rgb(242, 149, 68);
        config.thumbStrokeWeight = 2f;
        config.thumbStrokeWeightHover = 3f;
        config.thumbSize = 42f;
        config.textColor = Colors.gray(245);
        config.disabledAlpha = 90;
        config.showValueText = true;
        config.thumbShape = thumbShape;
        config.svgColorMode = SvgColorMode.USE_SVG_ORIGINAL;
        return config;
    }

    private SliderStyleConfig createFallbackStyle() {
        SliderStyleConfig config = new SliderStyleConfig();
        config.trackColor = Colors.rgb(58, 64, 72);
        config.trackHoverColor = Colors.rgb(82, 90, 102);
        config.trackPressedColor = Colors.rgb(42, 46, 54);
        config.trackStrokeColor = Colors.gray(225);
        config.trackStrokeWeight = 1.5f;
        config.trackStrokeWeightHover = 2.5f;
        config.trackThickness = 9f;
        config.activeTrackColor = Colors.rgb(120, 210, 92);
        config.activeTrackHoverColor = Colors.rgb(154, 228, 132);
        config.activeTrackPressedColor = Colors.rgb(88, 176, 62);
        config.thumbColor = Colors.rgb(255, 240, 120);
        config.thumbHoverColor = Colors.rgb(255, 248, 164);
        config.thumbPressedColor = Colors.rgb(244, 220, 72);
        config.thumbStrokeColor = Colors.rgb(28, 32, 36);
        config.thumbStrokeWeight = 2f;
        config.thumbStrokeWeightHover = 3f;
        config.thumbSize = 28f;
        config.textColor = Colors.gray(245);
        config.disabledAlpha = 90;
        config.showValueText = true;
        config.thumbShape = null;
        config.svgColorMode = SvgColorMode.USE_RENDER_STYLE;
        return config;
    }

    private PShape loadSvg(String path) {
        PShape shape = loadShape(path);
        if (shape == null && path.startsWith("data/")) {
            shape = loadShape(path.substring("data/".length()));
        }
        return shape;
    }
}
