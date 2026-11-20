package com.cpz.processing.controls.controls.floatinglist;

import com.cpz.processing.controls.controls.button.Button;
import com.cpz.processing.controls.controls.geometry.ControlBounds;
import com.cpz.processing.controls.controls.geometry.ControlMeasure;
import com.cpz.processing.controls.controls.geometry.ResolvedBounds;
import com.cpz.processing.controls.controls.panel.Panel;
import com.cpz.processing.controls.core.input.DefaultInputLayer;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.KeyboardEvent;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.processing.controls.core.overlay.OverlayEntry;
import com.cpz.processing.controls.core.overlay.OverlayManager;
import com.cpz.processing.controls.core.util.FontLoader;
import processing.core.PApplet;
import processing.core.PFont;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.cpz.processing.controls.controls.floatinglist.FloatingListStyle.color;
import static com.cpz.processing.controls.controls.floatinglist.FloatingListStyle.nonNegative;
import static com.cpz.processing.controls.controls.floatinglist.FloatingListStyle.pixels;
import static com.cpz.processing.controls.controls.floatinglist.FloatingListStyle.positive;
import static com.cpz.processing.controls.controls.geometry.ControlMeasure.absolute;

/**
 * Anchored, pointer-scrollable list rendered in the sketch's P2D overlay pass.
 * Use from the sketch thread, in canvas coordinates with the default screen
 * projection and without an enclosing clip. Row drawing must balance any
 * matrix/style scopes of its own and leave clip and projection ownership here.
 */
public final class FloatingListPanel<K, T> implements AutoCloseable {
    /** All sizes are positive logical pixels. Max height includes the header. */
    public record Dimensions(int width, int maxHeight, int rowHeight, int headerHeight,
                             int scrollbarWidth, int closeButtonWidth) {
        public Dimensions {
            if (width <= 0 || rowHeight <= 0 || headerHeight <= 0 || maxHeight <= headerHeight
                    || scrollbarWidth <= 0 || scrollbarWidth >= width
                    || closeButtonWidth <= 0 || closeButtonWidth >= width) {
                throw new IllegalArgumentException("Invalid floating list dimensions");
            }
        }
    }

    /**
     * Declared geometry in absolute or canvas-height-relative measures.
     * Null close button height uses the header height. Null minimum thumb size
     * uses the legacy width-based minimum.
     */
    public record Measures(ControlMeasure width, ControlMeasure maxHeight, ControlMeasure rowHeight,
                           ControlMeasure headerHeight, ControlMeasure scrollbarWidth, ControlMeasure closeButtonWidth,
                           ControlMeasure closeButtonHeight, ControlMeasure canvasMargin, ControlMeasure anchorGap,
                           ControlMeasure padding, ControlMeasure minThumbSize) {
        public Measures {
            positive(width, "width");
            positive(maxHeight, "maxHeight");
            positive(rowHeight, "rowHeight");
            positive(headerHeight, "headerHeight");
            positive(scrollbarWidth, "scrollbarWidth");
            positive(closeButtonWidth, "closeButtonWidth");
            if (closeButtonHeight != null) positive(closeButtonHeight, "closeButtonHeight");
            nonNegative(canvasMargin, "canvasMargin");
            nonNegative(anchorGap, "anchorGap");
            nonNegative(padding, "padding");
            if (minThumbSize != null) nonNegative(minThumbSize, "minThumbSize");
            if (maxHeight.mode() == headerHeight.mode() && maxHeight.value() <= headerHeight.value()) {
                throw new IllegalArgumentException("maxHeight must exceed headerHeight when both use the same mode");
            }
        }

        /** Converts the legacy dimensions without changing their absolute units. */
        public static Measures fromDimensions(Dimensions dimensions) {
            Objects.requireNonNull(dimensions, "dimensions");
            return new Measures(absolute(dimensions.width()), absolute(dimensions.maxHeight()),
                    absolute(dimensions.rowHeight()), absolute(dimensions.headerHeight()),
                    absolute(dimensions.scrollbarWidth()), absolute(dimensions.closeButtonWidth()), null,
                    absolute(0), absolute(0), absolute(0), null);
        }
    }

    /** Draws one row using absolute sketch coordinates. Bounds may extend past the clip. */
    @FunctionalInterface
    public interface RowRenderer<T> {
        void draw(T item, ResolvedBounds bounds);
    }

    private record Row<K, T>(K key, T item) {}

    private record Layout(ResolvedBounds frame, ResolvedBounds header, ResolvedBounds title, ResolvedBounds close,
                          ResolvedBounds viewport, ResolvedBounds track, int rowHeight, float minThumbSize) {}

    private final PApplet sketch;
    private final OverlayManager overlays;
    private final InputManager input;
    private Measures measures;
    private FloatingListStyle style;
    private FontLoader.FontResolver fontResolver;
    private final Panel frame;
    private final Button closeButton;
    private final FloatingListCloseStyle closeStyle = new FloatingListCloseStyle();
    private final Layer layer;
    private final OverlayEntry overlay;
    private String title;
    private final String emptyText;
    private Function<? super T, ? extends K> keyOf;
    private RowRenderer<? super T> renderer;
    private List<Row<K, T>> rows = List.of();
    private Supplier<ResolvedBounds> anchor;
    private Boolean opensBelow;
    private double scrollRows;
    private PointerEvent lastPointer;
    private Integer capturedButton;
    private boolean closingButtonPressed;
    private float thumbGrab = Float.NaN;
    private boolean disposed;

    /** Uses the same priority for overlay Z index and input dispatch. */
    public FloatingListPanel(PApplet sketch, OverlayManager overlays, InputManager input, int priority,
                             Dimensions dimensions, String title, String closeText, String emptyText,
                             Function<? super T, ? extends K> keyOf, RowRenderer<? super T> renderer) {
        this(sketch, overlays, input, priority, Measures.fromDimensions(dimensions), FloatingListStyle.defaults(),
                title, closeText, emptyText, keyOf, renderer);
    }

    /** Creates a panel with explicit declared measures and chrome. */
    public FloatingListPanel(PApplet sketch, OverlayManager overlays, InputManager input, int priority,
                             Measures measures, FloatingListStyle style, String title, String closeText, String emptyText,
                             Function<? super T, ? extends K> keyOf, RowRenderer<? super T> renderer) {
        this.sketch = Objects.requireNonNull(sketch, "sketch");
        this.overlays = Objects.requireNonNull(overlays, "overlays");
        this.input = Objects.requireNonNull(input, "input");
        this.measures = Objects.requireNonNull(measures, "measures");
        this.style = Objects.requireNonNull(style, "style");
        this.title = Objects.requireNonNull(title, "title");
        this.emptyText = Objects.requireNonNull(emptyText, "emptyText");
        this.keyOf = Objects.requireNonNull(keyOf, "keyOf");
        this.renderer = Objects.requireNonNull(renderer, "renderer");
        this.frame = new Panel(sketch, 0, 0, 0, 0);
        this.closeButton = new Button(sketch, "floating-list-close", Objects.requireNonNull(closeText, "closeText"),
                0, 0, 0, 0);
        this.closeButton.setStyle(this.closeStyle);
        this.closeButton.setClickListener(this::dismiss);
        this.layer = new Layer(priority);
        this.overlay = new OverlayEntry(priority, this::draw, this.layer, this::dismiss);
    }

    /** Returns the declared measures, before canvas constraints are applied. */
    public Measures getMeasures() {
        return this.measures;
    }

    /** Replaces geometry while preserving the current visible row where the new limits allow. */
    public void setMeasures(Measures measures) {
        this.requireUsable();
        this.measures = Objects.requireNonNull(measures, "measures");
    }

    /** Replaces geometry with the legacy absolute sizes and positional defaults. */
    public void setDimensions(Dimensions dimensions) {
        this.setMeasures(Measures.fromDimensions(dimensions));
    }

    public FloatingListStyle getStyle() {
        return this.style;
    }

    /** Replaces the style and uses its Java fonts in place of any JSON font. */
    public void setStyle(FloatingListStyle style) {
        this.requireUsable();
        this.style = Objects.requireNonNull(style, "style");
        this.fontResolver = null;
    }

    public String getTitle() {
        return this.title;
    }

    /** Changes the visible title without changing registration, scroll or pointer capture. */
    public void setTitle(String title) {
        this.requireUsable();
        this.title = Objects.requireNonNull(title, "title");
    }

    void applyPresentation(Measures measures, FloatingListStyle style, String title,
                           FontLoader.FontResolver fontResolver) {
        this.setMeasures(measures);
        this.style = style;
        this.title = title;
        if (fontResolver != null) this.fontResolver = fontResolver;
    }

    /**
     * Opens or moves the panel to a live anchor in canvas coordinates. The
     * supplier must return finite bounds with nonnegative dimensions.
     */
    public void open(Supplier<ResolvedBounds> anchorInSketch) {
        this.requireUsable();
        Objects.requireNonNull(anchorInSketch, "anchorInSketch");
        ResolvedBounds bounds = validateAnchor(anchorInSketch.get());
        this.anchor = anchorInSketch;
        this.layout(bounds);
        this.overlays.register(this.overlay);
        this.input.registerLayer(this.layer);
    }

    /**
     * Replaces the ordered list with a shallow defensive copy. Items and keys
     * must be nonnull and keys unique. A rejected update leaves the old list intact.
     */
    public void setItems(List<? extends T> items) {
        this.requireUsable();
        List<Row<K, T>> replacement = new ArrayList<>();
        Map<K, Integer> positions = new HashMap<>();
        for (T item : List.copyOf(items)) {
            K key = Objects.requireNonNull(this.keyOf.apply(item), "row key");
            if (positions.putIfAbsent(key, replacement.size()) != null) {
                throw new IllegalArgumentException("Duplicate floating list key: " + key);
            }
            replacement.add(new Row<>(key, item));
        }

        double nextOffset = 0;
        if (!this.rows.isEmpty() && !replacement.isEmpty()) {
            int first = Math.min(this.rows.size() - 1, (int) this.scrollRows);
            Integer next = this.findSurvivor(positions, first, this.rows.size(), 1);
            if (next == null) next = this.findSurvivor(positions, first - 1, -1, -1);
            if (next != null) nextOffset = next + (this.scrollRows - first);
        }
        this.rows = List.copyOf(replacement);
        this.scrollRows = nextOffset;
        if (this.isOpen()) this.layout();
    }

    private Integer findSurvivor(Map<K, Integer> positions, int start, int end, int step) {
        for (int i = start; i != end; i += step) {
            Integer position = positions.get(this.rows.get(i).key());
            if (position != null) return position;
        }
        return null;
    }

    /** Returns whether the overlay is open, independently of pending pointer capture. */
    public boolean isOpen() {
        return this.anchor != null;
    }

    /** Hides the overlay, preserving scroll and any pointer gesture awaiting release. */
    public void dismiss() {
        if (!this.isOpen()) return;
        this.anchor = null;
        this.opensBelow = null;
        this.lastPointer = null;
        this.cancelPointerAction();
        this.overlays.unregister(this.overlay);
        if (this.capturedButton == null) this.input.unregisterLayer(this.layer);
    }

    /** Permanently releases the panel, including any pending pointer capture and row data. */
    @Override
    public void close() {
        if (this.disposed) return;
        this.dismiss();
        this.capturedButton = null;
        this.input.unregisterLayer(this.layer);
        this.rows = List.of();
        this.keyOf = null;
        this.renderer = null;
        this.fontResolver = null;
        this.closeStyle.update(this.style.closeButton(), null, false);
        this.disposed = true;
    }

    private void requireUsable() {
        if (this.disposed) throw new IllegalStateException("Floating list panel is closed");
    }

    private static ResolvedBounds validateAnchor(ResolvedBounds bounds) {
        Objects.requireNonNull(bounds, "anchor bounds");
        if (!Float.isFinite(bounds.x()) || !Float.isFinite(bounds.y())
                || !Float.isFinite(bounds.x() + bounds.width()) || !Float.isFinite(bounds.y() + bounds.height())
                || bounds.width() < 0 || bounds.height() < 0) {
            throw new IllegalArgumentException("Invalid floating list anchor");
        }
        return bounds;
    }

    private Layout layout() {
        return this.layout(validateAnchor(this.anchor.get()));
    }

    private Layout layout(ResolvedBounds anchorBounds) {
        int canvasWidth = Math.max(0, this.sketch.width);
        int canvasHeight = Math.max(0, this.sketch.height);
        int requestedHeader = this.dimension(this.measures.headerHeight(), canvasHeight);
        int margin = this.wholePixels(this.measures.canvasMargin());
        int marginX = Math.min(margin, Math.max(0, (canvasWidth - 1) / 2));
        int marginY = Math.min(margin, Math.max(0, (canvasHeight - requestedHeader) / 2));
        int bottomEdge = canvasHeight - marginY;
        int width = this.dimension(this.measures.width(), canvasWidth - 2 * marginX);
        int maximum = this.dimension(this.measures.maxHeight(), bottomEdge - marginY);
        int rowHeight = Math.max(1, this.wholePixels(this.measures.rowHeight()));
        int padding = Math.min(this.wholePixels(this.measures.padding()), Math.max(0, (width - 1) / 2));
        int header = Math.min(requestedHeader, maximum);
        int wanted = (int) Math.min(maximum,
                header + 2.0 * padding + (double) rowHeight * Math.max(1, this.rows.size()));

        int gap = this.wholePixels(this.measures.anchorGap());
        int aboveEdge = (int) clamp((float) Math.floor(anchorBounds.y() - (double) gap), marginY, bottomEdge);
        int belowEdge = (int) clamp((float) Math.ceil(anchorBounds.y() + (double) anchorBounds.height() + gap),
                marginY, bottomEdge);
        int above = aboveEdge - marginY;
        int below = bottomEdge - belowEdge;
        int viable = (int) Math.min(wanted, header + 1.0);
        if (this.opensBelow == null) {
            this.opensBelow = below >= wanted || (above < wanted && below >= above);
        } else if (this.opensBelow && below < viable && above >= viable) {
            this.opensBelow = false;
        } else if (!this.opensBelow && above < viable && below >= viable) {
            this.opensBelow = true;
        }

        int available = this.opensBelow ? below : above;
        int height = Math.min(wanted, available);
        int y = this.opensBelow ? belowEdge : aboveEdge - height;
        if (available < viable) {
            // If neither side is usable, keep the panel accessible inside the canvas.
            height = wanted;
            y = (int) clamp(y, marginY, bottomEdge - height);
        }

        int x = (int) clamp(Math.round(anchorBounds.x()), marginX, canvasWidth - marginX - width);
        int headerHeight = Math.min(height, header);
        int closeWidth = this.dimension(this.measures.closeButtonWidth(), width);
        int closeHeight = this.measures.closeButtonHeight() == null ? headerHeight
                : this.dimension(this.measures.closeButtonHeight(), headerHeight);
        int verticalPadding = Math.min(padding, Math.max(0, (height - headerHeight - 1) / 2));
        int viewportY = y + headerHeight + verticalPadding;
        int viewportHeight = height - headerHeight - 2 * verticalPadding;
        int bodyWidth = width - 2 * padding;
        int trackWidth = viewportHeight > 0 && (double) rowHeight * this.rows.size() > viewportHeight
                ? this.dimension(this.measures.scrollbarWidth(), Math.max(0, bodyWidth - 1)) : 0;
        float minThumbSize = this.measures.minThumbSize() == null ? Math.min(trackWidth, viewportHeight / 2f)
                : pixels(this.measures.minThumbSize(), canvasHeight, viewportHeight);
        this.scrollRows = Math.min(this.scrollRows, this.maxScrollRows(viewportHeight, rowHeight));

        return new Layout(new ResolvedBounds(x, y, width, height),
                new ResolvedBounds(x, y, width, headerHeight),
                new ResolvedBounds(x, y, width - closeWidth, headerHeight),
                new ResolvedBounds(x + width - closeWidth, y + (headerHeight - closeHeight) / 2, closeWidth, closeHeight),
                new ResolvedBounds(x + padding, viewportY, bodyWidth - trackWidth, viewportHeight),
                new ResolvedBounds(x + padding + bodyWidth - trackWidth, viewportY, trackWidth, viewportHeight),
                rowHeight, minThumbSize);
    }

    private int wholePixels(ControlMeasure measure) {
        return Math.round(measure.resolve(Math.max(0, this.sketch.height)));
    }

    private int dimension(ControlMeasure measure, int available) {
        return Math.min(available, Math.max(1, this.wholePixels(measure)));
    }

    private double maxScrollRows(float viewportHeight, int rowHeight) {
        // At zero height keep a real first-row anchor for a later resize.
        return Math.max(0, this.rows.size() - (double) Math.max(1, viewportHeight) / rowHeight);
    }

    private ResolvedBounds thumb(Layout layout) {
        ResolvedBounds track = layout.track();
        float proportional = (float) (track.height() * track.height() / ((double) this.rows.size() * layout.rowHeight()));
        float maxThumb = Math.max(1, track.height() - 1);
        float thumbHeight = clamp(Math.max(layout.minThumbSize(), proportional), 1, maxThumb);
        float travel = track.height() - thumbHeight;
        float position = (float) (travel * this.scrollRows / this.maxScrollRows(track.height(), layout.rowHeight()));
        return new ResolvedBounds(track.x(), track.y() + position, track.width(), thumbHeight);
    }

    private void scrollTo(double rows, Layout layout) {
        this.scrollRows = Math.max(0, Math.min(rows, this.maxScrollRows(layout.viewport().height(), layout.rowHeight())));
    }

    private void draw() {
        if (!this.isOpen()) return;
        Layout layout = this.layout();
        ResolvedBounds outer = layout.frame();
        if (outer.width() <= 0 || outer.height() <= 0) return;

        // Processing cannot restore a null font via popStyle().
        var graphics = this.sketch.getGraphics();
        if (graphics != null && graphics.textFont == null) this.sketch.textSize(graphics.textSize);
        this.sketch.pushMatrix();
        this.sketch.pushStyle();
        try {
            this.sketch.imageMode(PApplet.CORNER);
            this.sketch.rectMode(PApplet.CORNER);
            var chrome = this.style.frame();
            float edge = Math.min(outer.width(), outer.height());
            float radius = pixels(chrome.cornerRadius(), this.sketch.height, edge / 2);
            this.frame.getStyle().setBackgroundVisible(chrome.backgroundColor() != null).setCornerRadius(radius);
            if (chrome.backgroundColor() != null) this.frame.setBackgroundColor(chrome.backgroundColor());
            this.frame.setBounds(ControlBounds.absolute(outer.x(), outer.y(), outer.width(), outer.height()));
            this.frame.draw();
            this.drawHeader(layout, radius);
            this.drawViewport(layout.viewport(), layout.rowHeight());
            this.drawScrollbar(layout);
            this.drawBorder(outer, chrome, radius);
        } finally {
            this.sketch.popStyle();
            this.sketch.popMatrix();
        }
    }

    private void drawHeader(Layout layout, float frameRadius) {
        ResolvedBounds header = layout.header();
        if (this.style.headerColor() != null) {
            float radius = Math.min(frameRadius, header.height() / 2);
            this.sketch.noStroke();
            this.sketch.fill(this.style.headerColor());
            this.sketch.rect(header.x(), header.y(), header.width(), header.height(), radius, radius, 0, 0);
        }
        if (layout.title().width() > 0) {
            this.clip(layout.title());
            try {
                this.drawText(this.title, layout.title(), this.style.title());
            } finally {
                this.sketch.noClip();
            }
        }
        ResolvedBounds close = layout.close();
        this.setCloseBounds(close);
        this.closeStyle.update(this.style.closeButton(), this.fontResolver,
                this.lastPointer != null && contains(close, this.lastPointer));
        this.clip(close);
        try {
            this.closeButton.draw();
        } finally {
            this.sketch.noClip();
        }
    }

    private void drawScrollbar(Layout layout) {
        if (layout.track().width() <= 0) return;
        var scrollbar = this.style.scrollbar();
        ResolvedBounds thumb = this.thumb(layout);
        boolean hovering = this.lastPointer != null && contains(thumb, this.lastPointer);
        int thumbColor = scrollbar.thumbColor();
        if (!Float.isNaN(this.thumbGrab)) thumbColor = color(scrollbar.pressedColor(), thumbColor);
        else if (hovering) thumbColor = color(scrollbar.hoverColor(), thumbColor);
        this.drawRect(layout.track(), scrollbar.trackColor(), scrollbar.cornerRadius());
        this.drawRect(thumb, thumbColor, scrollbar.cornerRadius());
    }

    private void drawBorder(ResolvedBounds outer, FloatingListStyle.Frame chrome, float radius) {
        if (chrome.borderColor() == null) return;
        float edge = Math.min(outer.width(), outer.height());
        float weight = pixels(chrome.strokeWeight(), this.sketch.height, edge / 2);
        if (weight <= 0) return;
        this.sketch.noFill();
        this.sketch.stroke(chrome.borderColor());
        this.sketch.strokeWeight(weight);
        this.sketch.rect(outer.x() + weight / 2, outer.y() + weight / 2,
                outer.width() - weight, outer.height() - weight, Math.max(0, radius - weight / 2));
    }

    private void drawViewport(ResolvedBounds viewport, int rowHeight) {
        if (viewport.height() <= 0 || viewport.width() <= 0) return;
        this.clip(viewport);
        try {
            if (this.rows.isEmpty()) {
                this.drawText(this.emptyText, viewport, this.style.empty());
                return;
            }
            List<Row<K, T>> drawnRows = this.rows;
            RowRenderer<? super T> drawRow = this.renderer;
            double drawnOffset = this.scrollRows * rowHeight;
            int first = (int) (drawnOffset / rowHeight);
            int end = Math.min(drawnRows.size(), (int) Math.ceil((drawnOffset + viewport.height()) / rowHeight));
            for (int i = first; i < end; i++) {
                float y = viewport.y() + (float) ((double) i * rowHeight - drawnOffset);
                this.sketch.pushMatrix();
                this.sketch.pushStyle();
                try {
                    drawRow.draw(drawnRows.get(i).item(), new ResolvedBounds(viewport.x(), y, viewport.width(), rowHeight));
                } finally {
                    this.sketch.popStyle();
                    this.sketch.popMatrix();
                }
            }
        } finally {
            this.sketch.noClip();
        }
    }

    private void clip(ResolvedBounds bounds) {
        // Layout uses whole logical pixels. P2D scissor is independent of the model matrix.
        this.sketch.clip(bounds.x(), bounds.y(), bounds.width(), bounds.height());
    }

    private void setCloseBounds(ResolvedBounds bounds) {
        this.closeButton.setBounds(ControlBounds.absolute(bounds.x() + bounds.width() / 2,
                bounds.y() + bounds.height() / 2, bounds.width(), bounds.height()));
    }

    private void drawText(String text, ResolvedBounds bounds, FloatingListStyle.Text style) {
        float left = pixels(style.paddingLeft(), this.sketch.height, bounds.width());
        float right = pixels(style.paddingRight(), this.sketch.height, bounds.width() - left);
        float width = bounds.width() - left - right;
        if (width <= 0 || bounds.height() <= 0 || text.isEmpty()) return;
        float size = pixels(style.size(), this.sketch.height, bounds.height());
        if (size <= 0) return;
        this.sketch.pushStyle();
        try {
            this.sketch.fill(style.color());
            PFont font = FontLoader.resolve(this.fontResolver, this.sketch, size, style.font());
            if (font != null) this.sketch.textFont(font, size);
            else this.sketch.textSize(size);
            this.sketch.textAlign(PApplet.LEFT, PApplet.CENTER);
            this.sketch.text(text, bounds.x() + left, bounds.y(), width, bounds.height());
        } finally {
            this.sketch.popStyle();
        }
    }

    private void drawRect(ResolvedBounds bounds, int color, ControlMeasure cornerRadius) {
        this.sketch.noStroke();
        this.sketch.fill(color);
        float radius = pixels(cornerRadius, this.sketch.height, Math.min(bounds.width(), bounds.height()) / 2);
        this.sketch.rect(bounds.x(), bounds.y(), bounds.width(), bounds.height(), radius);
    }

    private static boolean contains(ResolvedBounds bounds, PointerEvent event) {
        return event.getX() >= bounds.x() && event.getX() < bounds.x() + bounds.width()
                && event.getY() >= bounds.y() && event.getY() < bounds.y() + bounds.height();
    }

    private static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(value, max));
    }

    private void cancelPointerAction() {
        if (this.closingButtonPressed) {
            this.closeButton.handlePointerEvent(new PointerEvent(PointerEvent.Type.RELEASE, -1, -1));
            this.closingButtonPressed = false;
        }
        this.thumbGrab = Float.NaN;
    }

    private boolean press(PointerEvent event, Layout layout, boolean inside) {
        this.capturedButton = event.getButton();
        if (!inside) {
            this.dismiss();
        } else if (event.getButton() == 0 || event.getButton() == PApplet.LEFT) {
            if (contains(layout.close(), event)) {
                this.setCloseBounds(layout.close());
                this.closingButtonPressed = true;
                this.closeButton.handlePointerEvent(event);
            } else if (contains(layout.track(), event)) {
                ResolvedBounds thumb = this.thumb(layout);
                if (contains(thumb, event)) {
                    this.thumbGrab = (event.getY() - thumb.y()) / thumb.height();
                } else {
                    double direction = event.getY() < thumb.y() ? -1 : 1;
                    this.scrollTo(this.scrollRows + direction * layout.viewport().height() / layout.rowHeight(), layout);
                }
            }
        }
        return true;
    }

    private boolean captured(PointerEvent event, Layout layout) {
        if (event.getType() == PointerEvent.Type.RELEASE
                && (this.capturedButton == 0 || event.getButton() == 0 || this.capturedButton == event.getButton())) {
            this.capturedButton = null;
            if (this.closingButtonPressed) {
                if (contains(layout.close(), event)) {
                    this.closingButtonPressed = false;
                    this.setCloseBounds(layout.close());
                    this.closeButton.handlePointerEvent(event);
                }
            }
            this.cancelPointerAction();
            if (!this.isOpen()) this.input.unregisterLayer(this.layer);
        } else if (event.getType() == PointerEvent.Type.DRAG && layout != null
                && !Float.isNaN(this.thumbGrab) && layout.track().width() > 0) {
            ResolvedBounds thumb = this.thumb(layout);
            float travel = layout.track().height() - thumb.height();
            if (travel > 0) {
                double fraction = (event.getY() - layout.track().y() - this.thumbGrab * thumb.height()) / travel;
                this.scrollTo(fraction * this.maxScrollRows(layout.track().height(), layout.rowHeight()), layout);
            }
        }
        return true;
    }

    private final class Layer extends DefaultInputLayer {
        private Layer(int priority) {
            super(priority);
        }

        @Override
        public boolean isKeyboardCaptureEnabled() {
            return FloatingListPanel.this.isOpen();
        }

        @Override
        public boolean handleKeyboardEvent(KeyboardEvent event) {
            if (event != null && event.getType() == KeyboardEvent.Type.PRESS
                    && (event.getKey() == PApplet.ESC || event.getKeyCode() == PApplet.ESC)) {
                FloatingListPanel.this.dismiss();
                return true;
            }
            return false;
        }

        @Override
        public boolean handlePointerEvent(PointerEvent event) {
            if (event == null) return false;
            Layout layout = isOpen() ? layout() : null;
            if (layout != null) lastPointer = event;
            if (event.getType() != PointerEvent.Type.WHEEL && capturedButton != null) {
                return captured(event, layout);
            }
            if (layout == null) return false;

            boolean inside = contains(layout.frame(), event);
            if (event.getType() == PointerEvent.Type.PRESS) return press(event, layout, inside);
            if (event.getType() == PointerEvent.Type.WHEEL && Float.isFinite(event.getWheelDelta())
                    && (contains(layout.viewport(), event) || contains(layout.track(), event))) {
                scrollTo(scrollRows + event.getWheelDelta(), layout);
            }
            return inside;
        }
    }
}
