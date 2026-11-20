package com.cpz.processing.controls.controls.floatinglist;

import com.cpz.processing.controls.controls.geometry.MeasureMode;
import com.cpz.processing.controls.controls.geometry.ResolvedBounds;
import com.cpz.processing.controls.core.input.DefaultInputLayer;
import com.cpz.processing.controls.core.input.InputLayer;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.KeyboardEvent;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.processing.controls.core.overlay.OverlayManager;
import com.cpz.processing.controls.testsupport.ProcessingTestSupport;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;
import processing.core.PFont;
import processing.data.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.stream.IntStream;

import static com.cpz.processing.controls.core.input.PointerEvent.Type.*;
import static org.junit.jupiter.api.Assertions.*;

class FloatingListPanelTest {
    private final DrawingApplet sketch = new DrawingApplet();
    private final OverlayManager overlays = new OverlayManager();
    private final TrackingInput input = new TrackingInput();
    private final List<PointerEvent.Type> lowerPointer = new ArrayList<>();
    private final List<Character> lowerKeys = new ArrayList<>();
    private final List<Integer> drawnItems = new ArrayList<>();
    private final List<ResolvedBounds> drawnBounds = new ArrayList<>();
    private final List<Integer> items = new ArrayList<>(IntStream.range(0, 12).boxed().toList());
    private final FloatingListPanel<Integer, Integer> panel;
    private ResolvedBounds anchor = new ResolvedBounds(20, 20, 30, 20);
    private boolean failRendering;

    FloatingListPanelTest() {
        sketch.width = 300;
        sketch.height = 260;
        input.registerLayer(new DefaultInputLayer(0) {
            @Override public boolean handlePointerEvent(PointerEvent event) {
                lowerPointer.add(event.getType());
                return true;
            }

            @Override public boolean handleKeyboardEvent(KeyboardEvent event) {
                lowerKeys.add(event.getKey());
                return true;
            }
        });
        panel = new FloatingListPanel<>(sketch, overlays, input, 10,
                new FloatingListPanel.Dimensions(120, 76, 20, 24, 10, 36),
                "List", "X", "Empty", item -> item < 0 ? null : item, (item, bounds) -> {
                    assertEquals(bounds.x(), sketch.activeClip.x());
                    assertEquals(bounds.width(), sketch.activeClip.width());
                    assertTrue(bounds.y() < sketch.activeClip.y() + sketch.activeClip.height());
                    assertTrue(bounds.y() + bounds.height() > sketch.activeClip.y());
                    drawnItems.add(item);
                    drawnBounds.add(bounds);
                    if (failRendering) throw new IllegalStateException("row failed");
                });
        panel.setItems(items);
        panel.open(() -> anchor);
    }

    @Test
    void scrollClampsBothEndsAndDrawsOnlyRowsIntersectingTheViewport() {
        wheel(2.25f);
        render();
        assertEquals(List.of(2, 3, 4), drawnItems);
        assertEquals(59, drawnBounds.get(0).y());
        assertEquals(new ResolvedBounds(20, 64, 110, 52), sketch.clips.get(sketch.clips.size() - 1));

        wheel(10_000);
        render();
        assertEquals(List.of(9, 10, 11), drawnItems);
        assertEquals(56, drawnBounds.get(0).y());

        wheel(-10_000);
        render();
        assertEquals(List.of(0, 1, 2), drawnItems);
        assertEquals(64, drawnBounds.get(0).y());
        assertTrue(lowerPointer.isEmpty());
    }

    @Test
    void preservesPartialFirstRowAndUsesNextThenPreviousSurvivor() {
        wheel(2.25f);
        items.add(0, 40);
        panel.setItems(items);
        render();
        assertEquals(2, drawnItems.get(0));
        assertEquals(59, drawnBounds.get(0).y());

        items.remove(Integer.valueOf(0));
        items.remove(Integer.valueOf(2));
        panel.setItems(items);
        render();
        assertEquals(3, drawnItems.get(0));
        assertEquals(59, drawnBounds.get(0).y());

        // Put all the survivors of the old suffix before the viewport out of reach.
        panel.setItems(List.of(40, 1, 50, 51, 52, 53));
        render();
        assertEquals(1, drawnItems.get(0));
        assertEquals(59, drawnBounds.get(0).y());
    }

    @Test
    void copiesDataAndRejectsInvalidKeysWithoutChangingTheList() {
        items.clear();
        assertThrows(IllegalArgumentException.class, () -> panel.setItems(List.of(7, 7)));
        assertThrows(NullPointerException.class, () -> panel.setItems(List.of(-1)));
        assertThrows(NullPointerException.class, () -> panel.setItems(java.util.Arrays.asList(5, null)));
        render();
        assertEquals(List.of(0, 1, 2), drawnItems);
        assertThrows(IllegalArgumentException.class,
                () -> new FloatingListPanel.Dimensions(120, 24, 20, 24, 10, 36));
        assertThrows(IllegalArgumentException.class,
                () -> panel.open(() -> new ResolvedBounds(Float.NaN, 20, 30, 20)));
        assertTrue(panel.isOpen());
    }

    @Test
    void keepsEmptyPanelOpenAndResetsOffsetOnRefill() {
        wheel(6);
        panel.setItems(List.of());
        render();
        assertTrue(panel.isOpen());
        assertTrue(drawnItems.isEmpty());
        assertTrue(sketch.boxedText.contains("Empty"));
        wheel(3);
        assertTrue(lowerPointer.isEmpty());

        panel.setItems(items);
        render();
        assertEquals(0, drawnItems.get(0));
    }

    @Test
    void anchorsInsideCanvasAndAllowsPartialRowOrHeaderOnlyAfterResize() {
        anchor = new ResolvedBounds(280, 100, 15, 20);
        sketch.height = 125;
        render();
        assertEquals(new ResolvedBounds(180, 48, 110, 52), sketch.clips.get(sketch.clips.size() - 1));

        anchor = new ResolvedBounds(280, 37, 15, 20);
        sketch.height = 80;
        render();
        assertEquals(new ResolvedBounds(180, 24, 110, 13), sketch.clips.get(sketch.clips.size() - 1));
        assertEquals(List.of(0), drawnItems);

        sketch.height = 20;
        render();
        assertTrue(panel.isOpen());
        assertEquals(List.of(new ResolvedBounds(180, 0, 84, 20),
                new ResolvedBounds(264, 0, 36, 20)), sketch.clips);
        assertTrue(drawnItems.isEmpty());
    }

    @Test
    void thumbDragAndTrackPagingUseTheSameOffsetAsWheel() {
        pointer(PRESS, 135, 66);
        pointer(DRAG, 160, 140);
        pointer(RELEASE, 160, 140);
        render();
        assertEquals(List.of(9, 10, 11), drawnItems);

        wheel(-100);
        pointer(PRESS, 135, 106);
        pointer(RELEASE, 135, 106);
        render();
        assertEquals(List.of(2, 3, 4, 5), drawnItems);
        assertEquals(52, drawnBounds.get(0).y());
        assertTrue(lowerPointer.isEmpty());
    }

    @Test
    void closesOutsideButConsumesTheWholeGestureThroughRelease() {
        pointer(PRESS, 200, 100);
        assertFalse(panel.isOpen());
        assertTrue(overlays.getActiveOverlays().isEmpty());
        assertEquals(2, input.registered.size());
        pointer(DRAG, 201, 101);
        pointer(RELEASE, 202, 103);
        assertEquals(1, input.registered.size());
        assertTrue(lowerPointer.isEmpty());
        pointer(PRESS, 202, 103);
        assertEquals(List.of(PRESS), lowerPointer);
    }

    @Test
    void openingAgainBeforeReleaseKeepsTheGestureOwnedWithoutClosingAgain() {
        pointer(PRESS, 200, 100);
        panel.open(() -> anchor);
        pointer(DRAG, 201, 101);
        pointer(RELEASE, 202, 103);
        assertTrue(lowerPointer.isEmpty());
        assertTrue(panel.isOpen());
        assertEquals(2, input.registered.size());
        assertEquals(1, overlays.getActiveOverlays().size());
    }

    @Test
    void buttonEscapeAndGlobalClearAreIdempotentAndLeaveOtherKeysAlone() {
        pointer(PRESS, 120, 45);
        pointer(RELEASE, 95, 45);
        assertTrue(panel.isOpen());
        pointer(PRESS, 120, 45);
        pointer(RELEASE, 120, 45);
        assertFalse(panel.isOpen());
        assertEquals(1, input.registered.size());

        panel.open(() -> anchor);
        key('a');
        key(PApplet.ESC);
        panel.dismiss();
        assertEquals(List.of('a'), lowerKeys);
        assertFalse(panel.isOpen());

        panel.open(() -> anchor);
        pointer(PRESS, 30, 70);
        overlays.clearAll();
        overlays.clearAll();
        pointer(DRAG, 200, 100);
        pointer(RELEASE, 200, 100);
        assertFalse(panel.isOpen());
        assertTrue(lowerPointer.isEmpty());
        assertEquals(1, input.registered.size());
        assertTrue(overlays.getActiveOverlays().isEmpty());
    }

    @Test
    void disposalUnregistersEvenDuringCaptureAndCannotBeReopened() {
        pointer(PRESS, 200, 100);
        panel.close();
        panel.close();
        assertFalse(panel.isOpen());
        assertEquals(1, input.registered.size());
        assertTrue(overlays.getActiveOverlays().isEmpty());
        assertThrows(IllegalStateException.class, () -> panel.open(() -> anchor));
        assertThrows(IllegalStateException.class, () -> panel.setItems(items));
    }

    @Test
    void aRowFailureUnwindsTheClipAndBothDrawingStacks() {
        failRendering = true;
        assertEquals("row failed", assertThrows(IllegalStateException.class, this::render).getMessage());
        assertNull(sketch.activeClip);
        assertEquals(0, sketch.matrixDepth);
        assertEquals(0, sketch.styleDepth);
    }

    @Test
    void absoluteAndExplicitAbsoluteJsonReplaceRelativeMeasuresWithoutChangingOnResize() {
        apply("""
                {"measures":{"width":{"mode":"relative","value":0.4}},
                 "style":{"title":{"size":{"mode":"relative","value":0.06}}}}
                """);
        assertEquals(MeasureMode.RELATIVE, panel.getMeasures().width().mode());
        assertEquals(MeasureMode.RELATIVE, panel.getStyle().title().size().mode());
        panel.setDimensions(new FloatingListPanel.Dimensions(120, 76, 20, 24, 10, 36));
        apply("""
                {"title":"New","measures":{"anchorGap":{"mode":"absolute","value":6},"canvasMargin":12},
                 "style":{"title":{"size":11}}}
                """);
        assertEquals(MeasureMode.ABSOLUTE, panel.getMeasures().width().mode());
        assertEquals(MeasureMode.ABSOLUTE, panel.getStyle().title().size().mode());
        for (int height : List.of(260, 400)) {
            sketch.height = height;
            render();
            assertEquals(new ResolvedBounds(20, 70, 110, 52), viewport());
            assertEquals(20, drawnBounds.get(0).height());
            assertEquals(11, sketch.textSizes.get("New"));
        }
    }

    @Test
    void relativeGeometryAndStyleResolveTogetherAndPreservePartialRowAcrossResize() {
        apply("""
                {"measures":{
                  "width":{"mode":"relative","value":0.5},
                  "maxHeight":{"mode":"relative","value":0.4},
                  "rowHeight":{"mode":"relative","value":0.1},
                  "headerHeight":{"mode":"relative","value":0.1},
                  "scrollbarWidth":{"mode":"relative","value":0.05},
                  "closeButtonWidth":{"mode":"relative","value":0.2},
                  "closeButtonHeight":{"mode":"relative","value":0.05},
                  "canvasMargin":{"mode":"relative","value":0.05},
                  "anchorGap":{"mode":"relative","value":0.025},
                  "padding":{"mode":"relative","value":0.02},
                  "minThumbSize":{"mode":"relative","value":0.15}},
                 "style":{
                   "font":"font/test.ttf",
                   "frame":{"borderColor":"#FFFF00","strokeWeight":{"mode":"relative","value":0.01},
                            "cornerRadius":{"mode":"relative","value":0.02}},
                   "title":{"size":{"mode":"relative","value":0.05},
                            "paddingLeft":{"mode":"relative","value":0.02}},
                   "closeButton":{"backgroundColor":"#116644","hoverColor":"#448866","pressedColor":"#224433"}}}
                """);
        var declared = panel.getMeasures();
        assertEquals(0, sketch.getCreateFontCalls());
        wheelAt(2.25f, 74, 105);
        render();
        assertEquals(new ResolvedBounds(25, 78, 107, 68), viewport());
        assertEquals(new ResolvedBounds(98, 53, 52, 13), sketch.clips.get(1));
        assertEquals(new ResolvedBounds(25, 71.5f, 107, 26), drawnBounds.get(0));
        assertEquals(2, drawnItems.get(0));
        assertEquals(13, sketch.textSizes.get("List"));
        assertEquals(25.2f, sketch.textBoxes.get("List").x(), 0.0001f);
        assertEquals(2.6f, sketch.strokeWeights.get(sketch.strokeWeights.size() - 1), 0.0001f);
        assertTrue(sketch.roundedRects.stream().anyMatch(rect -> rect.color() == 0xFF94A8B7
                && rect.bounds().height() == 39));

        sketch.width = 420;
        sketch.height = 400;
        render();
        assertSame(declared, panel.getMeasures());
        assertEquals(new ResolvedBounds(28, 98, 164, 104), viewport());
        assertEquals(new ResolvedBounds(140, 60, 80, 20), sketch.clips.get(1));
        assertEquals(new ResolvedBounds(28, 88, 164, 40), drawnBounds.get(0));
        assertEquals(2, drawnItems.get(0));
        assertEquals(20, sketch.textSizes.get("List"));
        assertEquals(28, sketch.textBoxes.get("List").x());
        assertEquals(4, sketch.strokeWeights.get(sketch.strokeWeights.size() - 1));
        assertTrue(sketch.getCreateFontSizes().containsAll(List.of(13f, 14f, 20f)));

        pointer(MOVE, 168, 76);
        render();
        assertTrue(sketch.roundedRects.stream().anyMatch(rect -> rect.color() == 0xFF448866
                && rect.bounds().equals(sketch.clips.get(1))));
        pointer(PRESS, 168, 76);
        render();
        assertTrue(sketch.roundedRects.stream().anyMatch(rect -> rect.color() == 0xFF224433));
        pointer(RELEASE, 168, 76);
        assertFalse(panel.isOpen());
        assertTrue(lowerPointer.isEmpty());
    }

    @Test
    void titleChangesInsideOwnClipAndPreservesOverlayAndThumbGesture() {
        wheel(2.25f);
        render();
        var entry = overlays.getTopOverlay().orElseThrow();
        var registered = List.copyOf(input.registered);
        var initialRow = drawnBounds.get(0);
        pointer(PRESS, 135, 80);
        String title = "Count · 12 items with a title longer than the available space";
        panel.setTitle(title);
        render();
        assertEquals(initialRow, drawnBounds.get(0));
        assertEquals(2, drawnItems.get(0));
        assertEquals(new ResolvedBounds(20, 40, 84, 24), sketch.textClips.get(title));
        assertSame(entry, overlays.getTopOverlay().orElseThrow());
        assertEquals(registered, input.registered);
        pointer(DRAG, 160, 140);
        pointer(RELEASE, 160, 140);
        render();
        assertEquals(List.of(9, 10, 11), drawnItems);
        assertTrue(lowerPointer.isEmpty());
    }

    @Test
    void openingSideSurvivesShortListAndFlipsOnlyWhenTheOriginalSideLosesUsableSpace() {
        panel.dismiss();
        sketch.height = 180;
        anchor = new ResolvedBounds(20, 100, 30, 20);
        panel.open(() -> anchor);
        render();
        assertEquals(24, sketch.clips.get(0).y());
        panel.setItems(List.of(0));
        panel.setTitle("One");
        render();
        assertEquals(56, sketch.clips.get(0).y());

        sketch.height = 130;
        anchor = new ResolvedBounds(20, 10, 30, 20);
        render();
        assertEquals(30, sketch.clips.get(0).y());

        panel.dismiss();
        sketch.height = 260;
        anchor = new ResolvedBounds(20, 100, 30, 20);
        panel.open(() -> anchor);
        render();
        assertEquals(120, sketch.clips.get(0).y());

        sketch.height = 140;
        render();
        assertEquals(56, sketch.clips.get(0).y());
    }

    @Test
    void excessiveMarginLeavesAnAccessibleCloseAndSmallCanvasClampsScroll() {
        wheel(1000);
        apply("""
                {"measures":{"width":180,"rowHeight":30,"canvasMargin":500,"anchorGap":100,"padding":8}}
                """);
        sketch.width = 101;
        sketch.height = 46;
        render();
        assertEquals(new ResolvedBounds(50, 11, 1, 24), sketch.clips.get(0));
        assertTrue(drawnItems.isEmpty());
        sketch.width = 0;
        sketch.height = 0;
        render();
        assertTrue(sketch.clips.isEmpty());
        assertTrue(panel.isOpen());

        apply("""
                {"measures":{"canvasMargin":8,"anchorGap":4,"padding":0,"rowHeight":10,"headerHeight":20}}
                """);
        sketch.width = 250;
        sketch.height = 180;
        render();
        assertEquals(11, drawnItems.get(drawnItems.size() - 1));
        assertEquals(viewport().y() + viewport().height(),
                drawnBounds.get(drawnBounds.size() - 1).y() + 10, 0.0001f);
        var close = sketch.clips.get(1);
        pointer(PRESS, close.x() + 1, close.y() + 1);
        pointer(RELEASE, close.x() + 1, close.y() + 1);
        assertFalse(panel.isOpen());
    }

    @Test
    void invalidConfigIsAtomicAndOmittedFieldsKeepAppliedValues() {
        apply("""
                {"title":"Configured","measures":{"width":{"mode":"relative","value":0.55},"rowHeight":18},
                 "style":{"headerColor":"#446677","empty":{"size":10}}}
                """);
        var declared = panel.getMeasures();
        var styled = panel.getStyle();
        for (String invalid : List.of(
                "{\"width\":0}", "{\"rowHeight\":-1}", "{\"padding\":-0.1}",
                "{\"minThumbSize\":{\"mode\":\"implicit\",\"value\":1}}",
                "{\"anchorGap\":{\"mode\":\"relative\"}}",
                "{\"maxHeight\":1e999}", "{\"width\":true}")) {
            assertThrows(IllegalArgumentException.class,
                    () -> apply("{\"title\":\"Invalid\",\"measures\":" + invalid + "}"), invalid);
        }
        assertThrows(IllegalArgumentException.class, () -> apply("{\"style\":{\"closeButton\":{\"textSize\":0}}}"));
        assertThrows(IllegalArgumentException.class, () -> apply("{\"style\":{\"font\":\" \"}}"));
        assertThrows(IllegalArgumentException.class, () -> apply("{\"style\":{\"title\":false}}"));
        assertEquals("Configured", panel.getTitle());
        assertSame(declared, panel.getMeasures());
        assertSame(styled, panel.getStyle());

        apply("{\"title\":\"Still open\",\"style\":{\"headerColor\":null,\"title\":{\"size\":12}}}");
        assertTrue(panel.isOpen());
        assertSame(declared.width(), panel.getMeasures().width());
        assertEquals(18, panel.getMeasures().rowHeight().value());
        assertEquals(0xFF446677, panel.getStyle().headerColor());
        assertEquals(10, panel.getStyle().empty().size().value());
        assertEquals(12, panel.getStyle().title().size().value());
    }

    private ResolvedBounds viewport() {
        return sketch.clips.get(sketch.clips.size() - 1);
    }

    private void apply(String json) {
        FloatingListConfigLoader.applyFromJson(JSONObject.parse(json), "floating-list-test.json", panel);
    }

    private void render() {
        drawnItems.clear();
        drawnBounds.clear();
        sketch.clips.clear();
        sketch.roundedRects.clear();
        sketch.strokeWeights.clear();
        overlays.getActiveOverlays().get(0).getRender().run();
    }

    private void key(char character) {
        input.dispatchKeyboard(new KeyboardEvent(KeyboardEvent.Type.PRESS, character, character, false, false, false));
    }

    private void pointer(PointerEvent.Type type, float x, float y) {
        input.dispatchPointer(new PointerEvent(type, x, y, PApplet.LEFT));
    }

    private void wheel(float delta) {
        wheelAt(delta, 40, 72);
    }

    private void wheelAt(float delta, int x, int y) {
        input.dispatchPointer(new PointerEvent(WHEEL, x, y, 0, delta, false, false));
    }

    private static final class TrackingInput extends InputManager {
        private final List<InputLayer> registered = new ArrayList<>();

        @Override public void registerLayer(InputLayer layer) {
            if (!registered.contains(layer)) registered.add(layer);
            super.registerLayer(layer);
        }

        @Override public void unregisterLayer(InputLayer layer) {
            registered.remove(layer);
            super.unregisterLayer(layer);
        }
    }

    private record RoundedRect(ResolvedBounds bounds, int color) {}

    private static final class DrawingApplet extends ProcessingTestSupport.FontApplet {
        private final List<RoundedRect> roundedRects = new ArrayList<>();
        private final List<Float> strokeWeights = new ArrayList<>();
        private final Map<String, Float> textSizes = new HashMap<>();
        private final Map<String, ResolvedBounds> textBoxes = new HashMap<>();
        private final Map<String, ResolvedBounds> textClips = new HashMap<>();
        private float currentTextSize;
        private int currentFill;
        private final List<ResolvedBounds> clips = new ArrayList<>();
        private final List<String> boxedText = new ArrayList<>();
        private ResolvedBounds activeClip;
        private int matrixDepth;
        private int styleDepth;

        private DrawingApplet() {
            super(ProcessingTestSupport.font("Dialog", 14));
            ProcessingTestSupport.graphics(this);
        }

        @Override public void pushMatrix() { matrixDepth++; }
        @Override public void popMatrix() { matrixDepth--; }
        @Override public void pushStyle() { styleDepth++; }
        @Override public void popStyle() { styleDepth--; }
        @Override public void translate(float x, float y) {}
        @Override public void textSize(float size) { currentTextSize = size; }
        @Override public void textFont(PFont font, float size) { currentTextSize = size; }
        @Override public void fill(int color) { currentFill = color; }
        @Override public void strokeWeight(float weight) { strokeWeights.add(weight); }
        @Override public void rect(float x, float y, float w, float h, float radius) {
            roundedRects.add(new RoundedRect(new ResolvedBounds(x, y, w, h), currentFill));
        }
        @Override public void text(String text, float x, float y) {}
        @Override public void text(String text, float x, float y, float w, float h) {
            boxedText.add(text);
            textSizes.put(text, currentTextSize);
            textBoxes.put(text, new ResolvedBounds(x, y, w, h));
            textClips.put(text, activeClip);
        }

        @Override public void clip(float x, float y, float w, float h) {
            assertNull(activeClip);
            activeClip = new ResolvedBounds(x, y, w, h);
            clips.add(activeClip);
        }

        @Override public void noClip() {
            assertNotNull(activeClip);
            activeClip = null;
        }
    }
}
