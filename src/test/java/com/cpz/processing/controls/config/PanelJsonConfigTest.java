package com.cpz.processing.controls.config;

import com.cpz.processing.controls.controls.Control;
import com.cpz.processing.controls.controls.button.Button;
import com.cpz.processing.controls.controls.button.input.ButtonInputLayer;
import com.cpz.processing.controls.controls.config.ControlConfigLoader;
import com.cpz.processing.controls.controls.dropdown.DropDown;
import com.cpz.processing.controls.controls.dropdown.util.DropDownOverlayController;
import com.cpz.processing.controls.controls.geometry.ControlBounds;
import com.cpz.processing.controls.controls.geometry.MeasureMode;
import com.cpz.processing.controls.controls.panel.Panel;
import com.cpz.processing.controls.controls.panel.config.PanelConfig;
import com.cpz.processing.controls.controls.panel.config.PanelConfigLoader;
import com.cpz.processing.controls.controls.panel.config.PanelStyleConfig;
import com.cpz.processing.controls.controls.panel.input.PanelInputLayer;
import com.cpz.processing.controls.controls.panel.style.PanelStyle;
import com.cpz.processing.controls.core.theme.DarkTheme;
import com.cpz.processing.controls.core.theme.LightTheme;
import com.cpz.processing.controls.core.theme.ThemeManager;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.processing.controls.core.overlay.OverlayEntry;
import com.cpz.processing.controls.core.overlay.OverlayManager;
import com.cpz.processing.controls.testsupport.ProcessingTestSupport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import processing.core.PShape;
import processing.data.JSONObject;

import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PanelJsonConfigTest {
    @AfterEach
    void tearDown() {
        clearDropDownControllers();
    }

    @Test
    void panelConfigLoaderReadsStructuralPanelConfig() {
        PanelConfig config = new PanelConfigLoader(new RecordingApplet(JSONObject.parse(
                "{\"code\":\"p\",\"x\":100,\"y\":80,\"width\":320,\"height\":220,\"enabled\":false,\"visible\":false}"
        ))).loadFromJson(JSONObject.parse(
                "{\"code\":\"p\",\"x\":100,\"y\":80,\"width\":320,\"height\":220,\"enabled\":false,\"visible\":false}"
        ), "panel.json");

        assertEquals("p", config.getCode());
        assertEquals(100.0F, config.getBounds().x().value());
        assertEquals(80.0F, config.getBounds().y().value());
        assertEquals(320.0F, config.getBounds().width().value());
        assertEquals(220.0F, config.getBounds().height().value());
        assertFalse(config.isEnabled());
        assertFalse(config.isVisible());
        assertNull(config.getStyle());
    }

    @Test
    void controlConfigLoaderCreatesPanel() {
        JSONObject root = JSONObject.parse("{\"controls\":[{\"type\":\"panel\",\"code\":\"panel\","
                + "\"x\":100,\"y\":80,\"width\":320,\"height\":220}]}");
        RecordingApplet sketch = new RecordingApplet(root);
        sketch.width = 800;
        sketch.height = 600;

        Map<String, Control> controls = new ControlConfigLoader(sketch).load("controls.json");
        Panel panel = assertInstanceOf(Panel.class, controls.get("panel"));

        assertEquals(100.0F, panel.getX());
        assertEquals(80.0F, panel.getY());
        assertEquals(320.0F, panel.getWidth());
        assertEquals(220.0F, panel.getHeight());
        assertTrue(panel.isEnabled());
        assertTrue(panel.isVisible());
        assertFalse(panel.isBackgroundVisible());
        assertFalse(panel.isStrokeVisible());
        assertEquals(1.0F, panel.getStrokeWeight());
        assertEquals(0.0F, panel.getCornerRadius());
    }

    @Test
    void panelConfigLoaderReadsCompleteStyleConfig() {
        JSONObject json = JSONObject.parse("{\"code\":\"panel\",\"x\":100,\"y\":80,\"width\":320,\"height\":220,"
                + "\"style\":{\"backgroundVisible\":true,\"backgroundColor\":\"#20242A\","
                + "\"strokeVisible\":true,\"strokeColor\":\"#806D7682\",\"strokeWeight\":2.5,\"cornerRadius\":10}}");

        PanelConfig config = new PanelConfigLoader(new RecordingApplet(json)).loadFromJson(json, "panel.json");
        PanelStyleConfig style = config.getStyle();

        assertNotNull(style);
        assertTrue(style.getBackgroundVisible());
        assertEquals(0xFF20242A, style.getBackgroundColor());
        assertTrue(style.getStrokeVisible());
        assertEquals(0x806D7682, style.getStrokeColor());
        assertEquals(2.5F, style.getStrokeWeight());
        assertEquals(10.0F, style.getCornerRadius());
    }

    @Test
    void controlConfigLoaderAppliesCompletePanelStyle() {
        JSONObject root = JSONObject.parse("{\"controls\":[{\"type\":\"panel\",\"code\":\"panel\","
                + "\"x\":100,\"y\":80,\"width\":320,\"height\":220,"
                + "\"style\":{\"backgroundVisible\":true,\"backgroundColor\":\"#20242A\","
                + "\"strokeVisible\":true,\"strokeColor\":\"#6D7682\",\"strokeWeight\":2,\"cornerRadius\":10}}]}");
        RecordingApplet sketch = new RecordingApplet(root);
        sketch.width = 800;
        sketch.height = 600;

        Panel panel = assertInstanceOf(Panel.class, new ControlConfigLoader(sketch).load("controls.json").get("panel"));

        assertTrue(panel.isBackgroundVisible());
        assertEquals(0xFF20242A, panel.getBackgroundColor());
        assertTrue(panel.isStrokeVisible());
        assertEquals(0xFF6D7682, panel.getStrokeColor());
        assertEquals(2.0F, panel.getStrokeWeight());
        assertEquals(10.0F, panel.getCornerRadius());
    }

    @Test
    void partialStyleUsesThemeFallbacksAndKeepsDefaults() {
        JSONObject root = JSONObject.parse("{\"controls\":[{\"type\":\"panel\",\"code\":\"panel\","
                + "\"x\":100,\"y\":80,\"width\":320,\"height\":220,"
                + "\"style\":{\"backgroundVisible\":true}}]}");
        RecordingApplet sketch = new RecordingApplet(root);
        sketch.width = 800;
        sketch.height = 600;
        Panel panel = assertInstanceOf(Panel.class, new ControlConfigLoader(sketch).load("controls.json").get("panel"));
        ThemeManager themeManager = new ThemeManager(new LightTheme());
        panel.getStyle().setThemeProvider(themeManager);

        assertTrue(panel.isBackgroundVisible());
        assertFalse(panel.isStrokeVisible());
        assertEquals(1.0F, panel.getStrokeWeight());
        assertEquals(0.0F, panel.getCornerRadius());
        assertEquals(new LightTheme().tokens().surface, panel.getBackgroundColor());

        themeManager.setTheme(new DarkTheme());

        assertEquals(new DarkTheme().tokens().surface, panel.getBackgroundColor());
    }

    @Test
    void strokePartialUsesThemeBorderFallback() {
        JSONObject root = JSONObject.parse("{\"controls\":[{\"type\":\"panel\",\"code\":\"panel\","
                + "\"x\":100,\"y\":80,\"width\":320,\"height\":220,"
                + "\"style\":{\"strokeVisible\":true,\"strokeWeight\":3}}]}");
        RecordingApplet sketch = new RecordingApplet(root);
        sketch.width = 800;
        sketch.height = 600;
        Panel panel = assertInstanceOf(Panel.class, new ControlConfigLoader(sketch).load("controls.json").get("panel"));
        ThemeManager themeManager = new ThemeManager(new LightTheme());
        panel.getStyle().setThemeProvider(themeManager);

        assertTrue(panel.isStrokeVisible());
        assertEquals(3.0F, panel.getStrokeWeight());
        assertEquals(new LightTheme().tokens().border, panel.getStrokeColor());

        themeManager.setTheme(new DarkTheme());

        assertEquals(new DarkTheme().tokens().border, panel.getStrokeColor());
    }

    @Test
    void emptyStyleAndExplicitFalseAreValid() {
        Panel empty = loadSinglePanel("{\"style\":{}}");
        Panel explicitFalse = loadSinglePanel("{\"style\":{\"backgroundVisible\":false,\"strokeVisible\":false}}");

        assertFalse(empty.isBackgroundVisible());
        assertFalse(empty.isStrokeVisible());
        assertEquals(1.0F, empty.getStrokeWeight());
        assertEquals(0.0F, empty.getCornerRadius());
        assertFalse(explicitFalse.isBackgroundVisible());
        assertFalse(explicitFalse.isStrokeVisible());
    }

    @Test
    void explicitColorsSurviveThemeChangesAndRuntimeStillOverrides() {
        Panel panel = loadSinglePanel("{\"style\":{\"backgroundVisible\":true,\"backgroundColor\":\"#20242A\","
                + "\"strokeVisible\":true,\"strokeColor\":\"#6D7682\"}}");
        ThemeManager themeManager = new ThemeManager(new LightTheme());
        panel.getStyle().setThemeProvider(themeManager);

        themeManager.setTheme(new DarkTheme());

        assertEquals(0xFF20242A, panel.getBackgroundColor());
        assertEquals(0xFF6D7682, panel.getStrokeColor());

        panel.setStrokeVisible(false);
        panel.setCornerRadius(20.0F);

        assertFalse(panel.isStrokeVisible());
        assertEquals(20.0F, panel.getCornerRadius());
    }

    @Test
    void negativePanelStyleNumbersNormalizeInFactory() {
        Panel panel = loadSinglePanel("{\"style\":{\"strokeWeight\":-2,\"cornerRadius\":-4}}");

        assertEquals(0.0F, panel.getStrokeWeight());
        assertEquals(0.0F, panel.getCornerRadius());
    }

    @Test
    void invalidPanelStyleValuesReportConfigErrors() {
        assertConfigError("{\"style\":\"bad\"}", "Invalid 'style' value");
        assertConfigError("{\"style\":{\"backgroundVisible\":\"yes\"}}", "Invalid 'backgroundVisible' value");
        assertConfigError("{\"style\":{\"strokeVisible\":1}}", "Invalid 'strokeVisible' value");
        assertConfigError("{\"style\":{\"strokeWeight\":\"heavy\"}}", "Invalid 'strokeWeight' value");
        assertConfigError("{\"style\":{\"cornerRadius\":\"round\"}}", "Invalid 'cornerRadius' value");
        assertConfigError("{\"style\":{\"backgroundColor\":\"not-a-color\"}}", "Unsupported color format");
        assertConfigError("{\"style\":{\"strokeColor\":{}}}", "Unsupported color value");
    }

    @Test
    void jsonStyleAndRuntimeStyleProduceSamePanelChrome() {
        JSONObject root = JSONObject.parse("{\"controls\":[{\"type\":\"panel\",\"code\":\"panel\","
                + "\"x\":100,\"y\":80,\"width\":320,\"height\":220,"
                + "\"style\":{\"backgroundVisible\":true,\"backgroundColor\":\"#20242A\","
                + "\"strokeVisible\":true,\"strokeColor\":\"#6D7682\",\"strokeWeight\":2,\"cornerRadius\":10}}]}");
        RecordingApplet jsonSketch = new RecordingApplet(root);
        jsonSketch.width = 800;
        jsonSketch.height = 600;
        Panel jsonPanel = assertInstanceOf(Panel.class, new ControlConfigLoader(jsonSketch).load("controls.json").get("panel"));
        RecordingApplet runtimeSketch = new RecordingApplet(root);
        runtimeSketch.width = 800;
        runtimeSketch.height = 600;
        Panel runtimePanel = new Panel(runtimeSketch, "panel", 100.0F, 80.0F, 320.0F, 220.0F);
        runtimePanel.setStyle(new PanelStyle()
                .setBackgroundVisible(true)
                .setBackgroundColor(0xFF20242A)
                .setStrokeVisible(true)
                .setStrokeColor(0xFF6D7682)
                .setStrokeWeight(2.0F)
                .setCornerRadius(10.0F));

        jsonPanel.draw();
        runtimePanel.draw();

        assertEquals(runtimeSketch.panelFillColor, jsonSketch.panelFillColor);
        assertEquals(runtimeSketch.panelStrokeColor, jsonSketch.panelStrokeColor);
        assertEquals(runtimeSketch.panelStrokeWeight, jsonSketch.panelStrokeWeight);
        assertEquals(runtimeSketch.panelRectRadius, jsonSketch.panelRectRadius);
        assertEquals(runtimeSketch.panelRectX, jsonSketch.panelRectX);
        assertEquals(runtimeSketch.panelRectY, jsonSketch.panelRectY);
        assertEquals(runtimeSketch.panelRectWidth, jsonSketch.panelRectWidth);
        assertEquals(runtimeSketch.panelRectHeight, jsonSketch.panelRectHeight);
    }

    @Test
    void relativePanelAndRelativeDropDownLoadedFromJsonComposeAtRuntimeAndUseGlobalOverlayCoordinates() {
        JSONObject root = JSONObject.parse("{\"controls\":["
                + "{\"type\":\"panel\",\"code\":\"panel\","
                + "\"bounds\":{\"mode\":\"relative\",\"x\":0.25,\"y\":0.1,\"width\":0.5,\"height\":0.2}},"
                + "{\"type\":\"dropdown\",\"code\":\"dropDown\","
                + "\"items\":[\"Alpha\",\"Beta\",\"Gamma\",\"Delta\",\"Epsilon\",\"Zeta\"],"
                + "\"selectedIndex\":0,"
                + "\"bounds\":{\"mode\":\"relative\",\"x\":0.2,\"y\":0.25,\"width\":0.5,\"height\":0.1},"
                + "\"style\":{\"itemHeight\":20}},"
                + "{\"type\":\"button\",\"code\":\"behind\",\"text\":\"Behind\",\"x\":260,\"y\":126,\"width\":120,\"height\":24}"
                + "]}");
        RecordingApplet sketch = new RecordingApplet(root);
        sketch.width = 800;
        sketch.height = 600;
        ProcessingTestSupport.graphics(sketch);
        InputManager inputManager = new InputManager();
        OverlayManager overlayManager = new OverlayManager();

        Map<String, Control> controls = new ControlConfigLoader(sketch, overlayManager, inputManager).load("controls.json");
        Panel panel = assertInstanceOf(Panel.class, controls.get("panel"));
        DropDown dropDown = assertInstanceOf(DropDown.class, controls.get("dropDown"));
        Button behindButton = assertInstanceOf(Button.class, controls.get("behind"));
        AtomicInteger behindClicks = new AtomicInteger();
        behindButton.setClickListener(behindClicks::incrementAndGet);

        assertMeasure(relativeBoundsOf(dropDown).x().mode(), MeasureMode.RELATIVE);
        assertMeasure(relativeBoundsOf(dropDown).y().mode(), MeasureMode.RELATIVE);
        assertMeasure(relativeBoundsOf(dropDown).width().mode(), MeasureMode.RELATIVE);
        assertMeasure(relativeBoundsOf(dropDown).height().mode(), MeasureMode.RELATIVE);
        assertEquals(10.0F, dropDown.getTooltipBounds().x(), 0.001F);
        assertEquals(120.0F, dropDown.getTooltipBounds().y(), 0.001F);
        assertEquals(300.0F, dropDown.getTooltipBounds().width(), 0.001F);
        assertEquals(60.0F, dropDown.getTooltipBounds().height(), 0.001F);

        panel.add(dropDown);
        inputManager.registerLayer(new PanelInputLayer(0, panel));
        inputManager.registerLayer(new ButtonInputLayer(-1, behindButton));

        assertEquals(200.0F, panel.getX(), 0.001F);
        assertEquals(60.0F, panel.getY(), 0.001F);
        assertEquals(300.0F, panel.getWidth(), 0.001F);
        assertEquals(120.0F, panel.getHeight(), 0.001F);
        assertEquals(30.0F, dropDown.getTooltipBounds().x(), 0.001F);
        assertEquals(24.0F, dropDown.getTooltipBounds().y(), 0.001F);
        assertEquals(60.0F, dropDown.getTooltipBounds().width(), 0.001F);
        assertEquals(12.0F, dropDown.getTooltipBounds().height(), 0.001F);
        assertEquals(230.0F, panel.tooltipTarget(dropDown).getTooltipBounds().x(), 0.001F);
        assertEquals(84.0F, panel.tooltipTarget(dropDown).getTooltipBounds().y(), 0.001F);
        assertEquals(60.0F, panel.tooltipTarget(dropDown).getTooltipBounds().width(), 0.001F);
        assertEquals(12.0F, panel.tooltipTarget(dropDown).getTooltipBounds().height(), 0.001F);
        assertTrue(dropDown.canConsumePointerEvent(new PointerEvent(PointerEvent.Type.PRESS, 60.0F, 30.0F)));
        assertTrue(panel.canConsumePointerEvent(new PointerEvent(PointerEvent.Type.PRESS, 260.0F, 90.0F)));

        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.PRESS, 260.0F, 90.0F));

        assertTrue(dropDown.isExpanded());
        assertEquals(1, overlayManager.getActiveOverlays().size());

        panel.draw();
        renderOverlays(overlayManager);

        assertEquals(230.0F, sketch.listRectX, 0.001F);
        assertEquals(96.0F, sketch.listRectY, 0.001F);
        assertEquals(60.0F, sketch.listRectWidth, 0.001F);

        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.PRESS, 260.0F, 126.0F));

        assertEquals("Beta", dropDown.getSelectedItem());
        assertFalse(dropDown.isExpanded());
        assertEquals(0, overlayManager.getActiveOverlays().size());
        assertEquals(0, behindClicks.get());

        dropDown.dispose();
        overlayManager.clearAll();
    }

    @Test
    void panelDropDownExampleJsonLoadsAndComposesWithoutStandaloneInputLayer() throws Exception {
        JSONObject root = JSONObject.parse(Files.readString(Path.of("data/config/panel-dropdown.json")));
        RecordingApplet sketch = new RecordingApplet(root);
        sketch.width = 900;
        sketch.height = 520;
        ProcessingTestSupport.graphics(sketch);
        InputManager inputManager = new InputManager();
        OverlayManager overlayManager = new OverlayManager();

        Map<String, Control> controls = new ControlConfigLoader(sketch, overlayManager, inputManager).load("data/config/panel-dropdown.json");
        Panel panel = assertInstanceOf(Panel.class, controls.get("pnlJsonDropDown"));
        DropDown dropDown = assertInstanceOf(DropDown.class, controls.get("ddPanelMode"));
        Button panelButton = assertInstanceOf(Button.class, controls.get("btnPanelApply"));
        Button behindButton = assertInstanceOf(Button.class, controls.get("btnBehindPanel"));
        AtomicInteger behindClicks = new AtomicInteger();
        behindButton.setClickListener(behindClicks::incrementAndGet);
        panel.add(dropDown).add(panelButton);
        inputManager.registerLayer(new PanelInputLayer(0, panel));
        inputManager.registerLayer(new ButtonInputLayer(-1, behindButton));

        assertTrue(panel.isBackgroundVisible());
        assertEquals(0xEA20242A, panel.getBackgroundColor());
        assertTrue(panel.isStrokeVisible());
        assertEquals(0xFF6D7682, panel.getStrokeColor());
        assertEquals(3.0F, panel.getStrokeWeight());
        assertEquals(12.0F, panel.getCornerRadius());
        assertEquals("Preview", dropDown.getSelectedItem());
        assertTrue(panel.canConsumePointerEvent(new PointerEvent(PointerEvent.Type.PRESS, 338.0F, 152.0F)));
        assertEquals(78.0F, dropDown.getTooltipBounds().x(), 0.001F);
        assertEquals(27.0F, dropDown.getTooltipBounds().y(), 0.001F);
        assertEquals(180.0F, dropDown.getTooltipBounds().width(), 0.001F);
        assertEquals(30.0F, dropDown.getTooltipBounds().height(), 0.001F);
        assertTrue(dropDown.canConsumePointerEvent(new PointerEvent(PointerEvent.Type.PRESS, 168.0F, 42.0F)));

        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.PRESS, 338.0F, 152.0F));

        assertTrue(dropDown.isExpanded());
        assertEquals(1, overlayManager.getActiveOverlays().size());

        panel.draw();
        renderOverlays(overlayManager);

        assertEquals(170.0F, sketch.panelRectX, 0.001F);
        assertEquals(110.0F, sketch.panelRectY, 0.001F);
        assertEquals(320.0F, sketch.panelRectWidth, 0.001F);
        assertEquals(150.0F, sketch.panelRectHeight, 0.001F);
        assertEquals(12.0F, sketch.panelRectRadius, 0.001F);
        assertEquals(0xEA20242A, sketch.panelFillColor);
        assertEquals(0xFF6D7682, sketch.panelStrokeColor);
        assertEquals(3.0F, sketch.panelStrokeWeight);
        assertEquals(248.0F, sketch.listRectX, 0.001F);
        assertEquals(167.0F, sketch.listRectY, 0.001F);
        assertEquals(180.0F, sketch.listRectWidth, 0.001F);

        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.PRESS, 338.0F, 300.0F));

        assertEquals("Review", dropDown.getSelectedItem());
        assertFalse(dropDown.isExpanded());
        assertEquals(0, overlayManager.getActiveOverlays().size());
        assertEquals(0, behindClicks.get());

        dropDown.dispose();
        overlayManager.clearAll();
    }

    private static Panel loadSinglePanel(String panelTailJson) {
        String tail = panelTailJson == null ? "" : panelTailJson.trim();
        if (tail.startsWith("{") && tail.endsWith("}")) {
            tail = tail.substring(1, tail.length() - 1);
        }
        String optionalComma = tail.isEmpty() ? "" : "," + tail;
        JSONObject root = JSONObject.parse("{\"controls\":[{\"type\":\"panel\",\"code\":\"panel\","
                + "\"x\":100,\"y\":80,\"width\":320,\"height\":220" + optionalComma + "}]}");
        RecordingApplet sketch = new RecordingApplet(root);
        sketch.width = 800;
        sketch.height = 600;
        return assertInstanceOf(Panel.class, new ControlConfigLoader(sketch).load("controls.json").get("panel"));
    }

    private static void assertConfigError(String panelTailJson, String expectedMessagePart) {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> loadSinglePanel(panelTailJson));
        assertTrue(ex.getMessage().contains(expectedMessagePart), ex.getMessage());
    }

    private static void renderOverlays(OverlayManager overlayManager) {
        for (OverlayEntry entry : overlayManager.getActiveOverlays()) {
            entry.getRender().run();
        }
    }

    private static ControlBounds relativeBoundsOf(DropDown dropDown) {
        try {
            Field field = DropDown.class.getDeclaredField("bounds");
            field.setAccessible(true);
            return (ControlBounds) field.get(dropDown);
        } catch (ReflectiveOperationException ex) {
            throw new AssertionError(ex);
        }
    }

    private static void assertMeasure(MeasureMode actual, MeasureMode expected) {
        assertEquals(expected, actual);
    }

    @SuppressWarnings("unchecked")
    private static void clearDropDownControllers() {
        try {
            Field field = DropDownOverlayController.class.getDeclaredField("CONTROLLERS");
            field.setAccessible(true);
            for (DropDownOverlayController controller : new java.util.ArrayList<>((java.util.List<DropDownOverlayController>) field.get(null))) {
                controller.dispose();
            }
        } catch (ReflectiveOperationException ex) {
            throw new AssertionError(ex);
        }
    }

    private static final class RecordingApplet extends ProcessingTestSupport.FontApplet {
        private final JSONObject root;
        private final Deque<float[]> translationStack = new ArrayDeque<>();
        private float translateX;
        private float translateY;
        private float panelRectX;
        private float panelRectY;
        private float panelRectWidth;
        private float panelRectHeight;
        private float panelRectRadius;
        private int panelFillColor;
        private int panelStrokeColor;
        private float panelStrokeWeight;
        private int currentFillColor;
        private int currentStrokeColor;
        private float currentStrokeWeight;
        private float listRectX;
        private float listRectY;
        private float listRectWidth;

        private RecordingApplet(JSONObject root) {
            super(ProcessingTestSupport.font("Dialog", 16));
            this.root = root;
        }

        @Override
        public JSONObject loadJSONObject(String filename) {
            return this.root;
        }

        @Override
        public PShape loadShape(String filename) {
            return new PShape();
        }

        @Override
        public void pushMatrix() {
            this.translationStack.push(new float[]{this.translateX, this.translateY});
        }

        @Override
        public void popMatrix() {
            float[] previous = this.translationStack.pop();
            this.translateX = previous[0];
            this.translateY = previous[1];
        }

        @Override
        public void translate(float x, float y) {
            this.translateX += x;
            this.translateY += y;
        }

        @Override
        public void pushStyle() {
        }

        @Override
        public void popStyle() {
        }

        @Override
        public void rectMode(int mode) {
        }

        @Override
        public void stroke(int rgb) {
            this.currentStrokeColor = rgb;
        }

        @Override
        public void strokeWeight(float weight) {
            this.currentStrokeWeight = weight;
        }

        @Override
        public void fill(int rgb) {
            this.currentFillColor = rgb;
        }

        @Override
        public void noFill() {
        }

        @Override
        public void noStroke() {
        }

        @Override
        public void textAlign(int horiz, int vert) {
        }

        @Override
        public void text(String str, float x, float y) {
        }

        @Override
        public void beginShape() {
        }

        @Override
        public void vertex(float x, float y) {
        }

        @Override
        public void endShape(int mode) {
        }

        @Override
        public void textSize(float size) {
        }

        @Override
        public void rect(float a, float b, float c, float d, float r) {
            if (c == 320.0F && d >= 100.0F) {
                this.panelRectX = a + this.translateX;
                this.panelRectY = b + this.translateY;
                this.panelRectWidth = c;
                this.panelRectHeight = d;
                this.panelRectRadius = r;
                this.panelFillColor = this.currentFillColor;
                this.panelStrokeColor = this.currentStrokeColor;
                this.panelStrokeWeight = this.currentStrokeWeight;
            } else if (d > 100.0F) {
                this.listRectX = a + this.translateX;
                this.listRectY = b + this.translateY;
                this.listRectWidth = c;
            }
        }
    }
}
