# Notification

`Notification` is a toast-style runtime overlay feature for short status
messages. It is not a `Control`, is not part of the `controls[]` JSON format,
and does not consume pointer or keyboard input in `0.9.0`.

Use notifications for transient application feedback such as saved state,
success messages, warnings, or errors. Use a future dialog/modal feature, not
`Notification`, when the user must make a blocking decision.

---

## Components

- `Notification`: read-only runtime item with id, message, severity, creation
  time, duration, and expiry check
- `NotificationManager`: app-facing manager that owns one render-only
  `OverlayEntry`
- `NotificationSeverity`: `INFO`, `SUCCESS`, `WARNING`, `ERROR`
- `NotificationPlacement`: top/bottom and left/right/center stack placement
- `NotificationStyle`: mutable visual style object

The host sketch still owns `OverlayManager` and renders active overlays.
Processing is used by notifications only for drawing, text measurement, and
reading the current canvas size during layout.

---

## Basic Usage

```java
OverlayManager overlayManager = new OverlayManager();
NotificationManager notifications = new NotificationManager(this, overlayManager);

notifications.info("Saved");
notifications.success("Export complete");
notifications.warning("Low disk space");
notifications.error("Connection failed");
```

Draw overlays after drawing controls:

```java
for (OverlayEntry entry : overlayManager.getActiveOverlays()) {
    entry.getRender().run();
}
```

`NotificationManager` registers its overlay lazily when the first notification
is shown and unregisters it when all notifications expire or when
`clear()` / `dispose()` is called.
`dispose()` performs the same cleanup as `clear()`; sketches usually call it
from `exit()`.

Notification timing is backed by `cpz-utils.time.TimeSource`. The default
manager constructor uses `SystemTimeSource`, which is monotonic and independent
from Processing's `millis()`.

---

## Public API

```java
Notification show(String message);
Notification show(String message, NotificationSeverity severity);
Notification show(String message, NotificationSeverity severity, long durationMillis);

Notification info(String message);
Notification success(String message);
Notification warning(String message);
Notification error(String message);

void clear();
void dispose();

int size();
boolean isEmpty();

NotificationPlacement getPlacement();
void setPlacement(NotificationPlacement placement);

NotificationStyle getStyle();
void setStyle(NotificationStyle style);

long getDefaultDurationMillis();
void setDefaultDurationMillis(long durationMillis);

long getSeverityDurationMillis(NotificationSeverity severity);
void setSeverityDurationMillis(NotificationSeverity severity, long durationMillis);

int getMaxVisible();
void setMaxVisible(int maxVisible);
```

Default behavior:

- placement: `TOP_RIGHT`
- info duration: `3000L`
- success duration: `3000L`
- warning duration: `4500L`
- error duration: `6000L`
- max visible notifications: `4`
- overlay z-index: `50`, below interactive dropdown overlays

Messages must not be null or blank. Public duration values must be positive.

Duration is behavioral configuration on `NotificationManager`, not visual
styling on `NotificationStyle`. `setDefaultDurationMillis(...)` is retained for
the simple compatibility path and updates all severity durations to the same
value. After that, individual severities can be customized again:

```java
notifications.setDefaultDurationMillis(3000L);
notifications.setSeverityDurationMillis(NotificationSeverity.WARNING, 4500L);
notifications.setSeverityDurationMillis(NotificationSeverity.ERROR, 6000L);
```

Calls such as `warning(...)`, `error(...)`, and
`show(message, severity)` use the configured duration for that severity.
`show(message, severity, durationMillis)` is an explicit per-message override
and wins over severity defaults.

---

## Placement And Stacking

Supported placements:

- `TOP_RIGHT`
- `TOP_LEFT`
- `BOTTOM_RIGHT`
- `BOTTOM_LEFT`
- `TOP_CENTER`
- `BOTTOM_CENTER`

Notifications are stored in insertion order. The newest notification appears
closest to the selected screen edge. Top placements stack downward; bottom
placements stack upward.

When the visible limit is exceeded, the oldest notification is dropped. There
is no queue or backlog in `0.9.0`.

Placement is defined by `NotificationPlacement`. Notification is an overlay
anchored to predefined screen regions, so it does not expose formal relative
positioning, relative sizing, or manual x/y placement APIs in this release.

---

## Styling

`NotificationStyle` controls the visual surface:

```java
PFont font = createFont("data/font/JetBrainsMono.ttf", 14.0F);

NotificationStyle style = new NotificationStyle()
        .setWidth(340.0F)
        .setMargin(18.0F)
        .setFont(font)
        .setTextSize(14.0F)
        .setTextPadding(12.0F)
        .setIconSize(24.0F)
        .setIconTextGap(10.0F)
        .setCornerRadius(8.0F)
        .setStrokeWeight(1.0F)
        .setSeverityIcon(NotificationSeverity.INFO, "data/img/test.svg")
        .setSeverityIcon(NotificationSeverity.WARNING, "data/img/test.svg");

notifications.setStyle(style);
```

The renderer draws:

- rounded background rectangle
- optional border
- severity accent strip
- optional severity SVG icon between the accent strip and text
- wrapped message text

`NotificationStyle.setFont(PFont)` configures one global font for the
`NotificationManager` style. It is not a per-message property.

`setStyle(null)` restores the default style. Negative stroke weight is clamped
to `0.0F`; `strokeWeight == 0.0F` disables border rendering.

Each `NotificationSeverity` can optionally use a distinct SVG path:

```java
style.setSeverityIcon(NotificationSeverity.WARNING, "data/img/test.svg");
String warningIcon = style.getSeverityIcon(NotificationSeverity.WARNING);
style.clearSeverityIcon(NotificationSeverity.WARNING);
```

`setSeverityIcon(severity, null)` and a blank runtime path clear that severity.
With no usable icon path, no icon is drawn and no icon area is reserved: the
text remains at its pre-icon position. With a resolved icon, its square area
starts where the text previously started, is vertically centered, and the text
is shifted by `iconSize + iconTextGap`. The defaults are `24.0F` and `8.0F`;
both values are clamped to zero or greater.

SVGs are fitted proportionally inside the `iconSize` square and retain the
colors and styles defined by the SVG; notification severity colors do not
recolor them. The manager loads each configured SVG once per severity/path and
reuses it until that path changes. A configured path that cannot be loaded is
reported as an `IllegalArgumentException` identifying the severity and path.

There are no shadows, countdown bars, animations, fade, slide, easing, hover
pause, close buttons, or actions.

---

## Input And Focus

Notifications are render-only overlays:

- no pointer input
- no keyboard input
- no focus target
- no modal behavior
- no input capture

Visible notifications do not block normal control input.

---

## JSON

Notification is runtime UI. It is not configured inside `controls[]`, is not
created by `ControlConfigLoader`, and is not registered in
`ControlFactoryRegistry`.

`0.9.0` includes a standalone notification JSON loader for manager/style
defaults only. It does not define messages, trigger notifications, register
input, or create controls:

```java
OverlayManager overlayManager = new OverlayManager();
NotificationManager notifications = new NotificationManager(this, overlayManager);

NotificationConfig config =
        NotificationConfigLoader.load(this, "config/notification.json");

config.applyTo(notifications);
```

The convenience form is also available:

```java
NotificationConfigLoader.apply(this, "config/notification.json", notifications);
```

Supported standalone JSON fields:

```json
{
  "placement": "top-right",
  "maxVisible": 4,
  "defaultDurationMillis": 3000,
  "severityDurations": {
    "info": 3000,
    "success": 3000,
    "warning": 4500,
    "error": 6000
  },
  "style": {
    "backgroundColor": "#202020",
    "textColor": "#FFFFFF",
    "borderColor": "#404040",
    "strokeWeight": 1.0,
    "cornerRadius": 8.0,
    "font": "data/font/JetBrainsMono.ttf",
    "textSize": 14.0,
    "textPadding": 12.0,
    "gap": 8.0,
    "margin": 24.0,
    "width": 320.0,
    "minHeight": 48.0,
    "accentWidth": 5.0,
    "iconSize": 24.0,
    "iconTextGap": 10.0,
    "severityIcons": {
      "info": "data/img/test.svg",
      "success": "data/img/test.svg",
      "warning": "data/img/test.svg",
      "error": "data/img/test.svg"
    },
    "infoAccentColor": "#3B82F6",
    "successAccentColor": "#22C55E",
    "warningAccentColor": "#F59E0B",
    "errorAccentColor": "#EF4444"
  }
}
```

Missing fields preserve the manager/style values already in use. Unknown fields
are ignored. `placement` and severity keys are case-insensitive and accept
hyphens, underscores, or spaces. Invalid placement falls back to `TOP_RIGHT`;
unknown severity keys and invalid or non-positive JSON duration values are
ignored.

`style.severityIcons` is optional and may be partial. For example, this leaves
the other severities without a newly configured icon:

```json
{
  "style": {
    "severityIcons": {
      "warning": "data/img/test.svg",
      "error": "data/img/test.svg"
    }
  }
}
```

An absent `severityIcons` block preserves current icon associations during a
partial config apply. An explicit `null` value for a known severity clears that
association. Icon paths use the existing optional non-blank string convention:
blank or non-string configured values are rejected, while unknown severity keys
are ignored. SVG paths are resolved lazily through Processing; a configured but
unloadable path raises an error when the icon is first needed.

`defaultDurationMillis` is applied before `severityDurations`, so a JSON file
can set a global duration and then override warning or error durations.

Colors use the same JSON color parsing convention as other controls: integer
ARGB values or hexadecimal strings in `#RRGGBB` / `#AARRGGBB` form.

`style.font` is optional. When present, `NotificationConfigLoader.load(...)`
or `NotificationConfigLoader.apply(...)` loads the `PFont` once through the
shared `FontLoader` and stores it in `NotificationStyle`. The font is global to
the manager style, not per notification message.

If `style.font` is omitted or `null`, the current/default manager font is
preserved. Other missing style fields also preserve the values already present
on the manager style.

Notification messages are still runtime events owned by the sketch:

```java
notifications.success("Saved");
notifications.error("Save failed");
```

---

## Example

See:

```text
src/main/java/com/cpz/processing/controls/examples/notification/NotificationTest.java
src/main/java/com/cpz/processing/controls/examples/notification/NotificationJsonTest.java
data/config/notification.json
```
