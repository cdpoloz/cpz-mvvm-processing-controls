package com.cpz.processing.controls.core.overlay.notification.config;

import com.cpz.processing.controls.controls.config.ControlConfigLoader;
import com.cpz.processing.controls.controls.geometry.MeasureMode;
import com.cpz.processing.controls.core.overlay.OverlayManager;
import com.cpz.processing.controls.core.overlay.notification.NotificationManager;
import com.cpz.processing.controls.core.overlay.notification.NotificationPlacement;
import com.cpz.processing.controls.core.overlay.notification.NotificationPosition;
import com.cpz.processing.controls.core.overlay.notification.NotificationSeverity;
import com.cpz.processing.controls.core.overlay.notification.NotificationStyle;
import com.cpz.processing.controls.testsupport.ProcessingTestSupport;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;
import processing.core.PFont;
import processing.data.JSONObject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NotificationConfigLoaderTest {
    @Test
    void loadReadsPlacementFromJson() {
        NotificationManager manager = manager();

        config("{\"placement\":\"bottom-left\"}").applyTo(manager);

        assertEquals(NotificationPlacement.BOTTOM_LEFT, manager.getPlacement());
    }

    @Test
    void loadAcceptsFlexiblePlacementValues() {
        assertPlacement("top-right", NotificationPlacement.TOP_RIGHT);
        assertPlacement("top_right", NotificationPlacement.TOP_RIGHT);
        assertPlacement("TOP_RIGHT", NotificationPlacement.TOP_RIGHT);
        assertPlacement("top right", NotificationPlacement.TOP_RIGHT);
        assertPlacement(" bottom center ", NotificationPlacement.BOTTOM_CENTER);
    }

    @Test
    void loadReadsAbsolutePosition() {
        NotificationConfig config = config("{\"position\":{"
                + "\"x\":{\"mode\":\"absolute\",\"value\":120},"
                + "\"y\":{\"mode\":\"absolute\",\"value\":80}}}");
        NotificationManager manager = manager();

        config.applyTo(manager);

        assertTrue(config.isPositionSpecified());
        assertPosition(manager.getPosition(), MeasureMode.ABSOLUTE, 120.0F, MeasureMode.ABSOLUTE, 80.0F);
    }

    @Test
    void loadReadsRelativePositionAndHalfMeansFiftyPercent() {
        NotificationManager manager = manager();

        config("{\"position\":{"
                + "\"x\":{\"mode\":\"relative\",\"value\":0.5},"
                + "\"y\":{\"mode\":\"relative\",\"value\":0.25}}}").applyTo(manager);

        assertPosition(manager.getPosition(), MeasureMode.RELATIVE, 0.5F, MeasureMode.RELATIVE, 0.25F);
        assertEquals(250.0F, manager.getPosition().x().resolve(500.0F));
        assertEquals(100.0F, manager.getPosition().y().resolve(400.0F));
    }

    @Test
    void loadReadsMixedPositionMeasures() {
        NotificationManager manager = manager();

        config("{\"position\":{"
                + "\"x\":{\"mode\":\"relative\",\"value\":0.5},"
                + "\"y\":{\"mode\":\"absolute\",\"value\":120}}}").applyTo(manager);

        assertPosition(manager.getPosition(), MeasureMode.RELATIVE, 0.5F, MeasureMode.ABSOLUTE, 120.0F);
    }

    @Test
    void placementAndPositionAreBothStoredAndPlacementReturnsAfterClear() {
        NotificationManager manager = manager();

        config("{\"placement\":\"bottom-left\",\"position\":{"
                + "\"x\":{\"mode\":\"absolute\",\"value\":40},"
                + "\"y\":{\"mode\":\"absolute\",\"value\":50}}}").applyTo(manager);

        assertEquals(NotificationPlacement.BOTTOM_LEFT, manager.getPlacement());
        assertPosition(manager.getPosition(), MeasureMode.ABSOLUTE, 40.0F, MeasureMode.ABSOLUTE, 50.0F);

        manager.clearPosition();
        assertNull(manager.getPosition());
        assertEquals(NotificationPlacement.BOTTOM_LEFT, manager.getPlacement());
    }

    @Test
    void successiveConfigsDistinguishOmittedConfiguredAndNullPosition() {
        NotificationManager manager = manager();
        manager.setPosition(10.0F, 20.0F);

        NotificationConfig omitted = config("{}");
        omitted.applyTo(manager);
        assertFalse(omitted.isPositionSpecified());
        assertPosition(manager.getPosition(), MeasureMode.ABSOLUTE, 10.0F, MeasureMode.ABSOLUTE, 20.0F);

        config("{\"position\":{"
                + "\"x\":{\"mode\":\"relative\",\"value\":0.25},"
                + "\"y\":{\"mode\":\"absolute\",\"value\":90}}}").applyTo(manager);
        assertPosition(manager.getPosition(), MeasureMode.RELATIVE, 0.25F, MeasureMode.ABSOLUTE, 90.0F);

        NotificationConfig cleared = config("{\"position\":null}");
        cleared.applyTo(manager);
        assertTrue(cleared.isPositionSpecified());
        assertNull(cleared.getPosition());
        assertNull(manager.getPosition());
    }

    @Test
    void partialOrNullPositionCoordinatesFailClearly() {
        IllegalArgumentException missingY = assertThrows(
                IllegalArgumentException.class,
                () -> config("{\"position\":{\"x\":{\"mode\":\"absolute\",\"value\":10}}}")
        );
        IllegalArgumentException nullX = assertThrows(
                IllegalArgumentException.class,
                () -> config("{\"position\":{\"x\":null,\"y\":{\"mode\":\"absolute\",\"value\":20}}}")
        );

        assertTrue(missingY.getMessage().contains("'y'"));
        assertTrue(nullX.getMessage().contains("'x'"));
    }

    @Test
    void invalidPositionModesAndValuesFailClearly() {
        IllegalArgumentException mode = assertThrows(
                IllegalArgumentException.class,
                () -> config("{\"position\":{"
                        + "\"x\":{\"mode\":\"fluid\",\"value\":0.5},"
                        + "\"y\":{\"mode\":\"absolute\",\"value\":20}}}")
        );
        IllegalArgumentException value = assertThrows(
                IllegalArgumentException.class,
                () -> config("{\"position\":{"
                        + "\"x\":{\"mode\":\"relative\",\"value\":\"half\"},"
                        + "\"y\":{\"mode\":\"absolute\",\"value\":20}}}")
        );

        assertTrue(mode.getMessage().contains("'x'"));
        assertTrue(mode.getMessage().contains("relative"));
        assertTrue(value.getMessage().contains("'x'"));
    }

    @Test
    void nakedNumericCoordinatesAndNonObjectPositionFailClearly() {
        IllegalArgumentException naked = assertThrows(
                IllegalArgumentException.class,
                () -> config("{\"position\":{\"x\":0.5,\"y\":0.25}}")
        );
        IllegalArgumentException nonObject = assertThrows(
                IllegalArgumentException.class,
                () -> config("{\"position\":42}")
        );

        assertTrue(naked.getMessage().contains("explicit 'mode' and 'value'"));
        assertTrue(nonObject.getMessage().contains("expected an object"));
    }

    @Test
    void invalidPositionPreventsPartialApplication() {
        NotificationManager manager = manager();
        manager.setPlacement(NotificationPlacement.BOTTOM_LEFT);
        manager.setPosition(10.0F, 20.0F);
        JSONObject root = JSONObject.parse("{\"placement\":\"top-center\",\"position\":{"
                + "\"x\":{\"mode\":\"absolute\",\"value\":30}}}");

        assertThrows(
                IllegalArgumentException.class,
                () -> NotificationConfigLoader.apply(new JsonApplet(root), "notification.json", manager)
        );

        assertEquals(NotificationPlacement.BOTTOM_LEFT, manager.getPlacement());
        assertPosition(manager.getPosition(), MeasureMode.ABSOLUTE, 10.0F, MeasureMode.ABSOLUTE, 20.0F);
    }

    @Test
    void invalidPlacementFallsBackToTopRight() {
        NotificationManager manager = manager();
        manager.setPlacement(NotificationPlacement.BOTTOM_LEFT);

        config("{\"placement\":\"diagonal\"}").applyTo(manager);

        assertEquals(NotificationPlacement.TOP_RIGHT, manager.getPlacement());
    }

    @Test
    void loadReadsMaxVisible() {
        NotificationManager manager = manager();

        config("{\"maxVisible\":7}").applyTo(manager);

        assertEquals(7, manager.getMaxVisible());
    }

    @Test
    void loadReadsDefaultDurationMillis() {
        NotificationManager manager = manager();

        config("{\"defaultDurationMillis\":2500}").applyTo(manager);

        assertEquals(2500L, manager.getSeverityDurationMillis(NotificationSeverity.INFO));
        assertEquals(2500L, manager.getSeverityDurationMillis(NotificationSeverity.SUCCESS));
        assertEquals(2500L, manager.getSeverityDurationMillis(NotificationSeverity.WARNING));
        assertEquals(2500L, manager.getSeverityDurationMillis(NotificationSeverity.ERROR));
    }

    @Test
    void loadReadsSeverityDurations() {
        NotificationManager manager = manager();

        config("{\"severityDurations\":{\"info\":1000,\"SUCCESS\":2000,\"warning\":3000,\"error\":4000}}")
                .applyTo(manager);

        assertEquals(1000L, manager.getSeverityDurationMillis(NotificationSeverity.INFO));
        assertEquals(2000L, manager.getSeverityDurationMillis(NotificationSeverity.SUCCESS));
        assertEquals(3000L, manager.getSeverityDurationMillis(NotificationSeverity.WARNING));
        assertEquals(4000L, manager.getSeverityDurationMillis(NotificationSeverity.ERROR));
    }

    @Test
    void defaultDurationAppliesBeforeSeverityDurations() {
        NotificationManager manager = manager();

        config("{\"defaultDurationMillis\":3000,"
                + "\"severityDurations\":{\"warning\":4500,\"error\":6000}}")
                .applyTo(manager);

        assertEquals(3000L, manager.getSeverityDurationMillis(NotificationSeverity.INFO));
        assertEquals(3000L, manager.getSeverityDurationMillis(NotificationSeverity.SUCCESS));
        assertEquals(4500L, manager.getSeverityDurationMillis(NotificationSeverity.WARNING));
        assertEquals(6000L, manager.getSeverityDurationMillis(NotificationSeverity.ERROR));
    }

    @Test
    void unknownSeverityKeysAreIgnored() {
        NotificationManager manager = manager();
        manager.setDefaultDurationMillis(5000L);

        config("{\"severityDurations\":{\"critical\":1,\"info\":1200}}").applyTo(manager);

        assertEquals(1200L, manager.getSeverityDurationMillis(NotificationSeverity.INFO));
        assertEquals(5000L, manager.getSeverityDurationMillis(NotificationSeverity.ERROR));
    }

    @Test
    void invalidDurationValuesAreIgnoredSafely() {
        NotificationManager manager = manager();
        manager.setDefaultDurationMillis(2222L);

        config("{\"defaultDurationMillis\":0,"
                + "\"severityDurations\":{\"info\":-1,\"warning\":\"slow\",\"error\":6000},"
                + "\"maxVisible\":0}")
                .applyTo(manager);

        assertEquals(2222L, manager.getSeverityDurationMillis(NotificationSeverity.INFO));
        assertEquals(2222L, manager.getSeverityDurationMillis(NotificationSeverity.WARNING));
        assertEquals(6000L, manager.getSeverityDurationMillis(NotificationSeverity.ERROR));
        assertEquals(NotificationManager.DEFAULT_MAX_VISIBLE, manager.getMaxVisible());
    }

    @Test
    void loadReadsBaseStyleColors() {
        NotificationManager manager = manager();

        config("{\"style\":{"
                + "\"backgroundColor\":\"#102030\","
                + "\"textColor\":-1,"
                + "\"borderColor\":\"#FF040506\""
                + "}}").applyTo(manager);

        assertEquals(0xFF102030, manager.getStyle().getBackgroundColor());
        assertEquals(0xFFFFFFFF, manager.getStyle().getTextColor());
        assertEquals(0xFF040506, manager.getStyle().getBorderColor());
    }

    @Test
    void absentSeverityBackgroundColorsLeaveAllSeveritiesOnGeneralBackground() {
        NotificationManager manager = manager();

        config("{\"style\":{\"backgroundColor\":\"#102030\"}}").applyTo(manager);

        assertEquals(0xFF102030, manager.getStyle().getBackgroundColor());
        for (NotificationSeverity severity : NotificationSeverity.values()) {
            assertNull(manager.getStyle().getSeverityBackgroundColor(severity));
        }
    }

    @Test
    void loadReadsSeverityAccentColors() {
        NotificationManager manager = manager();

        config("{\"style\":{"
                + "\"infoAccentColor\":\"#010203\","
                + "\"successAccentColor\":\"#040506\","
                + "\"warningAccentColor\":\"#070809\","
                + "\"errorAccentColor\":\"#0A0B0C\""
                + "}}").applyTo(manager);

        assertEquals(0xFF010203, manager.getStyle().getInfoAccentColor());
        assertEquals(0xFF040506, manager.getStyle().getSuccessAccentColor());
        assertEquals(0xFF070809, manager.getStyle().getWarningAccentColor());
        assertEquals(0xFF0A0B0C, manager.getStyle().getErrorAccentColor());
    }

    @Test
    void loadReadsStyleNumericValues() {
        NotificationManager manager = manager();

        config("{\"style\":{"
                + "\"strokeWeight\":2.0,"
                + "\"cornerRadius\":7.0,"
                + "\"textSize\":15.0,"
                + "\"textPadding\":11.0,"
                + "\"gap\":9.0,"
                + "\"margin\":21.0,"
                + "\"width\":310.0,"
                + "\"minHeight\":52.0,"
                + "\"accentWidth\":6.0"
                + "}}").applyTo(manager);

        NotificationStyle style = manager.getStyle();
        assertEquals(2.0F, style.getStrokeWeight());
        assertEquals(7.0F, style.getCornerRadius());
        assertEquals(15.0F, style.getTextSize());
        assertEquals(11.0F, style.getTextPadding());
        assertEquals(9.0F, style.getGap());
        assertEquals(21.0F, style.getMargin());
        assertEquals(310.0F, style.getWidth());
        assertEquals(52.0F, style.getMinHeight());
        assertEquals(6.0F, style.getAccentWidth());
    }

    @Test
    void loadReadsIconMetricsAndPartialSeverityIcons() {
        NotificationManager manager = manager();

        config("{\"style\":{"
                + "\"iconSize\":26.0,"
                + "\"iconTextGap\":9.0,"
                + "\"severityIcons\":{"
                + "\"warning\":\" data/img/warning.svg \","
                + "\"ERROR\":\"data/img/error.svg\""
                + "}}}").applyTo(manager);

        NotificationStyle style = manager.getStyle();
        assertEquals(26.0F, style.getIconSize());
        assertEquals(9.0F, style.getIconTextGap());
        assertNull(style.getSeverityIcon(NotificationSeverity.INFO));
        assertNull(style.getSeverityIcon(NotificationSeverity.SUCCESS));
        assertEquals("data/img/warning.svg", style.getSeverityIcon(NotificationSeverity.WARNING));
        assertEquals("data/img/error.svg", style.getSeverityIcon(NotificationSeverity.ERROR));
    }

    @Test
    void loadReadsCompleteSeverityBackgroundColors() {
        NotificationManager manager = manager();

        config("{\"style\":{\"severityBackgroundColors\":{"
                + "\"info\":\"#102A38\",\"SUCCESS\":\"#123023\","
                + "\"warning\":\"#382A12\",\"error\":\"#38151D\"}}}").applyTo(manager);

        NotificationStyle style = manager.getStyle();
        assertEquals(0xFF102A38, style.getSeverityBackgroundColor(NotificationSeverity.INFO));
        assertEquals(0xFF123023, style.getSeverityBackgroundColor(NotificationSeverity.SUCCESS));
        assertEquals(0xFF382A12, style.getSeverityBackgroundColor(NotificationSeverity.WARNING));
        assertEquals(0xFF38151D, style.getSeverityBackgroundColor(NotificationSeverity.ERROR));
    }

    @Test
    void partialSeverityBackgroundColorsPreserveOtherAssociationsAndAcceptNormalizedKeys() {
        NotificationManager manager = manager();
        manager.setStyle(new NotificationStyle()
                .setSeverityBackgroundColor(NotificationSeverity.INFO, 0xFF010203)
                .setSeverityBackgroundColor(NotificationSeverity.SUCCESS, 0xFF040506));

        config("{\"style\":{\"severityBackgroundColors\":{\"warning\":\"#382A12\",\"ERROR\":\"#38151D\"}}}")
                .applyTo(manager);

        NotificationStyle style = manager.getStyle();
        assertEquals(0xFF010203, style.getSeverityBackgroundColor(NotificationSeverity.INFO));
        assertEquals(0xFF040506, style.getSeverityBackgroundColor(NotificationSeverity.SUCCESS));
        assertEquals(0xFF382A12, style.getSeverityBackgroundColor(NotificationSeverity.WARNING));
        assertEquals(0xFF38151D, style.getSeverityBackgroundColor(NotificationSeverity.ERROR));
    }

    @Test
    void nullSeverityBackgroundColorClearsExistingAssociationWhileOmittedColorsArePreserved() {
        NotificationManager manager = manager();
        manager.setStyle(new NotificationStyle()
                .setSeverityBackgroundColor(NotificationSeverity.INFO, 0xFF010203)
                .setSeverityBackgroundColor(NotificationSeverity.ERROR, 0xFF040506));

        config("{\"style\":{\"severityBackgroundColors\":{\"info\":null,\"warning\":\"#382A12\"}}}")
                .applyTo(manager);

        assertNull(manager.getStyle().getSeverityBackgroundColor(NotificationSeverity.INFO));
        assertEquals(0xFF040506, manager.getStyle().getSeverityBackgroundColor(NotificationSeverity.ERROR));
        assertEquals(0xFF382A12, manager.getStyle().getSeverityBackgroundColor(NotificationSeverity.WARNING));
    }

    @Test
    void absentSeverityBackgroundColorsPreserveCurrentStyleAssociations() {
        NotificationManager manager = manager();
        manager.setStyle(new NotificationStyle().setSeverityBackgroundColor(NotificationSeverity.ERROR, 0xFF38151D));

        config("{\"style\":{\"iconSize\":30.0}}").applyTo(manager);

        assertEquals(0xFF38151D, manager.getStyle().getSeverityBackgroundColor(NotificationSeverity.ERROR));
    }

    @Test
    void invalidSeverityBackgroundColorUsesSharedColorValidationAndUnknownKeysAreIgnored() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> config("{\"style\":{\"severityBackgroundColors\":{\"error\":\"not-a-color\"}}}")
        );
        assertTrue(exception.getMessage().contains("Unsupported color format"));

        NotificationManager manager = manager();
        config("{\"style\":{\"severityBackgroundColors\":{\"critical\":\"#010203\",\"info\":\"#102A38\"}}}")
                .applyTo(manager);
        assertEquals(0xFF102A38, manager.getStyle().getSeverityBackgroundColor(NotificationSeverity.INFO));
        assertNull(manager.getStyle().getSeverityBackgroundColor(NotificationSeverity.ERROR));
    }

    @Test
    void nullSeverityIconClearsExistingIconWhileOmittedIconsArePreserved() {
        NotificationManager manager = manager();
        manager.setStyle(new NotificationStyle()
                .setSeverityIcon(NotificationSeverity.INFO, "data/img/info.svg")
                .setSeverityIcon(NotificationSeverity.SUCCESS, "data/img/success.svg"));

        config("{\"style\":{\"severityIcons\":{\"info\":null,\"warning\":\"data/img/warning.svg\"}}}")
                .applyTo(manager);

        assertNull(manager.getStyle().getSeverityIcon(NotificationSeverity.INFO));
        assertEquals("data/img/success.svg", manager.getStyle().getSeverityIcon(NotificationSeverity.SUCCESS));
        assertEquals("data/img/warning.svg", manager.getStyle().getSeverityIcon(NotificationSeverity.WARNING));
    }

    @Test
    void absentSeverityIconsPreserveCurrentStyleAssociations() {
        NotificationManager manager = manager();
        manager.setStyle(new NotificationStyle().setSeverityIcon(NotificationSeverity.ERROR, "data/img/error.svg"));

        config("{\"style\":{\"iconSize\":30.0}}").applyTo(manager);

        assertEquals(30.0F, manager.getStyle().getIconSize());
        assertEquals("data/img/error.svg", manager.getStyle().getSeverityIcon(NotificationSeverity.ERROR));
    }

    @Test
    void blankSeverityIconPathUsesOptionalStringValidation() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> config("{\"style\":{\"severityIcons\":{\"info\":\"  \"}}}")
        );

        assertTrue(exception.getMessage().contains("severityIcons"));
        assertTrue(exception.getMessage().contains("blank"));
    }

    @Test
    void loadFromJsonReadsAndTrimsNotificationFontPathAndSourcePath() {
        NotificationConfig config = config("{\"style\":{\"font\":\"  data/font/JetBrainsMono.ttf  \"}}");

        assertEquals("data/font/JetBrainsMono.ttf", config.getStyle().getFontPath());
        assertEquals("notification.json", config.getStyle().getSourcePath());
    }

    @Test
    void loadWithSketchAppliesFontUsingConfiguredTextSize() {
        PFont expected = ProcessingTestSupport.font("Monospaced", 16);
        JsonFontApplet sketch = new JsonFontApplet(
                JSONObject.parse("{\"style\":{\"font\":\"data/font/test.ttf\",\"textSize\":18.0}}"),
                expected
        );
        NotificationManager manager = manager();

        NotificationConfigLoader.load(sketch, "notification.json").applyTo(manager);

        assertSame(expected, manager.getStyle().getFont());
        assertEquals(18.0F, manager.getStyle().getTextSize());
        assertEquals(1, sketch.getCreateFontCalls());
        assertEquals(18.0F, sketch.getLastCreateFontSize());
    }

    @Test
    void loadWithSketchUsesCurrentManagerTextSizeWhenJsonOmitsIt() {
        PFont expected = ProcessingTestSupport.font("Dialog", 19);
        JsonFontApplet sketch = new JsonFontApplet(
                JSONObject.parse("{\"style\":{\"font\":\"data/font/test.ttf\"}}"),
                expected
        );
        NotificationManager manager = manager();
        manager.setStyle(new NotificationStyle().setTextSize(19.0F));

        NotificationConfigLoader.load(sketch, "notification.json").applyTo(manager);

        assertSame(expected, manager.getStyle().getFont());
        assertEquals(19.0F, manager.getStyle().getTextSize());
        assertEquals(19.0F, sketch.getLastCreateFontSize());
    }

    @Test
    void parseOnlyLoadFromJsonCanApplyFontWhenSketchIsProvidedLater() {
        PFont expected = ProcessingTestSupport.font("Dialog", 17);
        NotificationConfig config = config("{\"style\":{\"font\":\"data/font/test.ttf\",\"textSize\":17.0}}");
        JsonFontApplet sketch = new JsonFontApplet(JSONObject.parse("{}"), expected);
        NotificationManager manager = manager();

        config.applyTo(sketch, manager);

        assertSame(expected, manager.getStyle().getFont());
        assertEquals(17.0F, sketch.getLastCreateFontSize());
    }

    @Test
    void applyLoadsAndAppliesNotificationFont() {
        PFont expected = ProcessingTestSupport.font("Monospaced", 15);
        JsonFontApplet sketch = new JsonFontApplet(
                JSONObject.parse("{\"style\":{\"font\":\"data/font/test.ttf\",\"textSize\":15.0}}"),
                expected
        );
        NotificationManager manager = manager();

        NotificationConfigLoader.apply(sketch, "notification.json", manager);

        assertSame(expected, manager.getStyle().getFont());
        assertEquals(15.0F, sketch.getLastCreateFontSize());
    }

    @Test
    void missingStyleFieldsPreserveCurrentValues() {
        NotificationManager manager = manager();
        manager.setStyle(new NotificationStyle()
                .setBackgroundColor(0xFF111111)
                .setTextColor(0xFF222222));

        config("{\"style\":{\"backgroundColor\":\"#333333\"}}").applyTo(manager);

        assertEquals(0xFF333333, manager.getStyle().getBackgroundColor());
        assertEquals(0xFF222222, manager.getStyle().getTextColor());
    }

    @Test
    void missingFontPreservesCurrentFont() {
        PFont existing = ProcessingTestSupport.font("Dialog", 14);
        NotificationManager manager = manager();
        manager.setStyle(new NotificationStyle()
                .setFont(existing)
                .setTextSize(14.0F));

        config("{\"style\":{\"backgroundColor\":\"#333333\"}}").applyTo(manager);

        assertSame(existing, manager.getStyle().getFont());
    }

    @Test
    void partialStyleUpdateDoesNotClearExistingFont() {
        PFont existing = ProcessingTestSupport.font("Dialog", 14);
        NotificationManager manager = manager();
        manager.setStyle(new NotificationStyle()
                .setFont(existing)
                .setTextSize(14.0F));

        config("{\"style\":{\"textSize\":20.0}}").applyTo(manager);

        assertSame(existing, manager.getStyle().getFont());
        assertEquals(20.0F, manager.getStyle().getTextSize());
    }

    @Test
    void unknownJsonFieldsAreIgnored() {
        NotificationManager manager = manager();

        config("{\"unknown\":true,\"style\":{\"unknownStyle\":42}}").applyTo(manager);

        assertEquals(NotificationPlacement.TOP_RIGHT, manager.getPlacement());
        assertEquals(NotificationManager.DEFAULT_MAX_VISIBLE, manager.getMaxVisible());
    }

    @Test
    void applyingConfigToNullManagerFailsFast() {
        NotificationConfig config = config("{}");

        assertThrows(NullPointerException.class, () -> config.applyTo(null));
    }

    @Test
    void applyingConfigDoesNotCreateNotificationsOrRegisterOverlays() {
        OverlayManager overlayManager = new OverlayManager();
        NotificationManager manager = new NotificationManager(new PApplet(), overlayManager);

        config("{\"placement\":\"bottom-right\",\"defaultDurationMillis\":2500}").applyTo(manager);

        assertTrue(manager.isEmpty());
        assertEquals(0, overlayManager.getActiveOverlays().size());
    }

    @Test
    void loadFromSketchPathLoadsNotificationConfig() {
        JSONObject root = JSONObject.parse("{\"placement\":\"bottom-center\",\"maxVisible\":6}");
        NotificationManager manager = manager();

        NotificationConfigLoader.load(new JsonApplet(root), "config/notification.json").applyTo(manager);

        assertEquals(NotificationPlacement.BOTTOM_CENTER, manager.getPlacement());
        assertEquals(6, manager.getMaxVisible());
    }

    @Test
    void invalidNotificationFontPathUsesFontLoaderDiagnostics() {
        JsonFontApplet sketch = new JsonFontApplet(
                JSONObject.parse("{\"style\":{\"font\":\"missing.ttf\"}}"),
                ProcessingTestSupport.font("Dialog", 14)
        );
        sketch.setResourceAvailable(false);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> NotificationConfigLoader.load(sketch, "notification.json").applyTo(manager())
        );

        assertTrue(exception.getMessage().contains("notification"));
        assertTrue(exception.getMessage().contains("'font'"));
        assertTrue(exception.getMessage().contains("notification.json"));
        assertTrue(exception.getMessage().contains("missing.ttf"));
    }

    @Test
    void notificationIsNotCreatedByControlConfigLoader() {
        JSONObject root = JSONObject.parse("{\"controls\":[{\"type\":\"notification\",\"code\":\"toast\"}]}");

        assertThrows(IllegalArgumentException.class, () -> new ControlConfigLoader(new JsonApplet(root)).load("controls.json"));
    }

    private static void assertPlacement(String raw, NotificationPlacement expected) {
        NotificationManager manager = manager();

        config("{\"placement\":\"" + raw + "\"}").applyTo(manager);

        assertEquals(expected, manager.getPlacement());
    }

    private static void assertPosition(
            NotificationPosition position,
            MeasureMode xMode,
            float x,
            MeasureMode yMode,
            float y
    ) {
        assertEquals(xMode, position.x().mode());
        assertEquals(x, position.x().value());
        assertEquals(yMode, position.y().mode());
        assertEquals(y, position.y().value());
    }

    private static NotificationConfig config(String json) {
        return NotificationConfigLoader.loadFromJson(JSONObject.parse(json), "notification.json");
    }

    private static NotificationManager manager() {
        return new NotificationManager(new PApplet(), new OverlayManager());
    }

    private static final class JsonApplet extends PApplet {
        private final JSONObject root;

        private JsonApplet(JSONObject root) {
            this.root = root;
        }

        @Override
        public JSONObject loadJSONObject(String filename) {
            return this.root;
        }
    }

    private static final class JsonFontApplet extends ProcessingTestSupport.FontApplet {
        private final JSONObject root;

        private JsonFontApplet(JSONObject root, PFont font) {
            super(font);
            this.root = root;
        }

        @Override
        public JSONObject loadJSONObject(String filename) {
            return this.root;
        }
    }
}
