package com.cpz.processing.controls.labelcontrol.view;

import com.cpz.processing.controls.common.ControlView;
import com.cpz.processing.controls.labelcontrol.style.LabelDefaultStyles;
import com.cpz.processing.controls.labelcontrol.style.LabelTypography;
import com.cpz.processing.controls.labelcontrol.style.render.LabelAlignMapper;
import com.cpz.processing.controls.labelcontrol.style.interfaces.LabelStyle;
import processing.core.PApplet;
import java.util.Objects;

/**
 * @author CPZ
 */
public final class LabelView implements ControlView {

    private final PApplet sketch;
    private final LabelViewModel viewModel;
    private float x;
    private float y;
    private LabelStyle style;
    private float cachedWidth;
    private float cachedHeight;
    private boolean metricsDirty = true;
    private String lastMeasuredText = "";

    public LabelView(PApplet sketch, LabelViewModel viewModel, float x, float y) {
        this.sketch = sketch;
        this.viewModel = viewModel;
        this.x = x;
        this.y = y;
        this.style = LabelDefaultStyles.defaultText();
        String initialText = viewModel.getText();
        this.lastMeasuredText = initialText == null ? "" : initialText;
    }

    /**
     * Draws the control using the active style.
     */
    public void draw() {
        if (!viewModel.isVisible()) {
            return;
        }
        style.render(sketch, buildViewState());
    }

    private void updateTextMetrics() {
        String currentText = viewModel.getText();
        if (!Objects.equals(currentText, lastMeasuredText)) {
            metricsDirty = true;
        }
        if (!metricsDirty) {
            return;
        }

        String text = currentText;
        if (text == null) {
            text = "";
        }

        sketch.pushStyle();
        LabelTypography typography = style.resolveTypography();
        if (typography.font() != null) {
            sketch.textFont(typography.font());
        }
        sketch.textSize(typography.textSize());
        sketch.textAlign(
                LabelAlignMapper.mapHorizontal(typography.textAlignHorizontal()),
                LabelAlignMapper.mapVertical(typography.textAlignVertical())
        );

        String[] lines = text.split("\n", -1);
        float maxWidth = 0;
        float lineHeight = (sketch.textAscent() + sketch.textDescent()) * typography.lineSpacingMultiplier();
        for (String line : lines) {
            float w = sketch.textWidth(line);
            if (w > maxWidth) {
                maxWidth = w;
            }
        }

        cachedWidth = maxWidth;
        cachedHeight = lines.length * lineHeight;
        sketch.popStyle();
        lastMeasuredText = currentText;
        metricsDirty = false;
    }

    public float getWidth() {
        if (metricsDirty) {
            updateTextMetrics();
        }
        return cachedWidth;
    }

    public float getHeight() {
        if (metricsDirty) {
            updateTextMetrics();
        }
        return cachedHeight;
    }

    public void centerBlockAround(float centerX, float centerY) {
        if (metricsDirty) {
            updateTextMetrics();
        }
        this.x = centerX - (cachedWidth * 0.5f);
        this.y = centerY - (cachedHeight * 0.5f);
    }

    private LabelViewState buildViewState() {
        return new LabelViewState(x, y, viewModel.getText(), viewModel.isEnabled());
    }

    public void setStyle(LabelStyle style) {
        if (style != null) {
            this.style = style;
            this.metricsDirty = true;
        }
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        this.metricsDirty = true;
    }

    public void drawDebugBounds() {
        if (metricsDirty) {
            updateTextMetrics();
        }
        sketch.pushStyle();
        sketch.noFill();
        sketch.stroke(255, 0, 0);
        sketch.rect(x, y, cachedWidth, cachedHeight);
        sketch.popStyle();
    }
}
