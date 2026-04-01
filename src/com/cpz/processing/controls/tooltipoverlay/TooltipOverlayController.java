package com.cpz.processing.controls.tooltipoverlay;

import com.cpz.processing.controls.input.DefaultInputLayer;
import com.cpz.processing.controls.input.InputLayer;
import com.cpz.processing.controls.overlay.OverlayEntry;
import com.cpz.processing.controls.overlay.OverlayManager;
import processing.core.PApplet;

import java.util.function.Supplier;

public final class TooltipOverlayController {

    public interface AnchorBoundsProvider {
        float getCenterX();
        float getTopY();
    }

    private final TooltipView view;
    private final TooltipViewModel viewModel;
    private final OverlayManager overlayManager;
    private final Supplier<Boolean> hoverSupplier;
    private final Supplier<String> textSupplier;
    private final AnchorBoundsProvider anchorBoundsProvider;
    private final OverlayEntry overlayEntry;
    private boolean registered;

    public TooltipOverlayController(PApplet sketch,
                                    OverlayManager overlayManager,
                                    Supplier<Boolean> hoverSupplier,
                                    Supplier<String> textSupplier,
                                    AnchorBoundsProvider anchorBoundsProvider) {
        this(sketch, overlayManager, hoverSupplier, textSupplier, anchorBoundsProvider, new DefaultTooltipStyle(new TooltipStyleConfig()));
    }

    public TooltipOverlayController(PApplet sketch,
                                    OverlayManager overlayManager,
                                    Supplier<Boolean> hoverSupplier,
                                    Supplier<String> textSupplier,
                                    AnchorBoundsProvider anchorBoundsProvider,
                                    DefaultTooltipStyle style) {
        this.overlayManager = overlayManager;
        this.hoverSupplier = hoverSupplier;
        this.textSupplier = textSupplier;
        this.anchorBoundsProvider = anchorBoundsProvider;
        this.viewModel = new TooltipViewModel(new TooltipModel(""));
        this.view = new TooltipView(sketch, viewModel);
        this.view.setStyle(style);
        InputLayer passiveLayer = new PassiveTooltipInputLayer();
        this.overlayEntry = new OverlayEntry(10, view::draw, passiveLayer, this::hideTooltip);
    }

    public void sync() {
        boolean shouldShow = hoverSupplier != null && Boolean.TRUE.equals(hoverSupplier.get());
        String text = textSupplier == null ? "" : textSupplier.get();
        if (!shouldShow || text == null || text.isBlank()) {
            hideTooltip();
            return;
        }
        viewModel.setText(text);
        viewModel.setVisible(true);
        view.setAnchorBounds(anchorBoundsProvider.getCenterX(), anchorBoundsProvider.getTopY());
        if (!registered) {
            overlayManager.register(overlayEntry);
            registered = true;
        }
    }

    public void dispose() {
        hideTooltip();
    }

    private void hideTooltip() {
        viewModel.setVisible(false);
        if (registered) {
            overlayManager.unregister(overlayEntry);
            registered = false;
        }
    }

    private static final class PassiveTooltipInputLayer extends DefaultInputLayer {

        private PassiveTooltipInputLayer() {
            super(10);
        }

        @Override
        public boolean isPointerCaptureEnabled() {
            return false;
        }

        @Override
        public boolean isKeyboardCaptureEnabled() {
            return false;
        }
    }
}
