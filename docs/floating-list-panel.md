# Floating list panel

`FloatingListPanel<K, T>` renders an anchored, vertically scrollable list of
fixed-height rows in a Processing P2D sketch. The host supplies ordered items,
stable keys and a row renderer. The panel owns the header, close button, empty
state, clip, scrollbar and input layer.

```java
var list = new FloatingListPanel<Integer, String>(
        this, overlays, input, 100,
        new FloatingListPanel.Dimensions(390, 286, 34, 42, 16, 82),
        "Items", "Close", "No items",
        Integer::valueOf,
        (item, row) -> {
            fill(240);
            textAlign(LEFT, CENTER);
            text(item, row.x() + 12, row.y() + row.height() / 2);
        });
FloatingListConfigLoader.apply(this, "config/floating-list-mixed.json", list);
list.setItems(List.of("1", "2", "3"));
list.setTitle("Items · 3");

var anchor = ControlBounds.relative(0.04f, 0.08f, 0.20f, 0.07f);
list.open(() -> anchor.resolve(width, height));
```

The legacy constructor and `setDimensions(Dimensions)` take absolute integer
pixels in this order: width, maximum total height, row height, header height,
scrollbar width and close button width. They use zero canvas margin, anchor gap
and content padding, a close button as tall as the header and the original
width-based thumb minimum. The alternate constructor takes `Measures` and
`FloatingListStyle`. Use `getMeasures()` and `setMeasures()` to work with the
declared geometry. `Measures.fromDimensions()` converts legacy geometry to
the equivalent `ControlMeasure.absolute()` values.

## Measures and style

The reference space is the **entire canvas** in logical sketch pixels. Every
relative dimension below is multiplied by the canvas **height**, including
widths and horizontal padding. A `ControlBounds` used to supply an anchor
resolves relative X against canvas **width**, and Y, width and height against
canvas **height**. An anchor supplier must return already resolved top-left
`ResolvedBounds` with finite coordinates and nonnegative extents.

| Java/JSON property | Relative reference | Validation or resolved constraint |
| --- | --- | --- |
| `width`, `maxHeight`, `rowHeight`, `headerHeight` | Canvas height | Positive. With the same mode, maximum height must exceed header height. Mixed modes can produce a header-only panel. |
| `scrollbarWidth`, `minThumbSize` | Canvas height | Width positive, minimum nonnegative. A null minimum retains the legacy width-based minimum. The strip leaves one column for content when possible. A thumb is at least one pixel, with one pixel of travel reserved when the track can provide it. |
| `closeButtonWidth`, `closeButtonHeight` | Canvas height | Positive. A null height uses the entire header. The button is right-aligned and vertically centered, with its size capped by the header. |
| `canvasMargin`, `anchorGap`, `padding` | Canvas height | Nonnegative. Margin applies on both axes, gap between anchor and panel, padding around the viewport and scrollbar. |
| `style.title`, `style.empty` sizes and left/right padding | Canvas height | Text sizes positive, padding nonnegative. Drawing is confined to the available text box and its clip. |
| `style.frame`, `style.closeButton` stroke weights and radii | Canvas height | Nonnegative. Stroke and radius are capped by available bounds and zero stroke disables the border. |
| `style.scrollbar.cornerRadius`, `style.closeButton.textSize` | Canvas height | Radius nonnegative. Text size positive and capped by button height at draw time. |

`ControlMeasure` rejects nonfinite declarations. Layout rounds box geometry to
whole logical pixels for P2D scissor, applies a one-pixel lower bound to positive
sizes when space permits, and shares the resolved rectangles between painting,
clipping and hit testing. Typography, strokes and radii can use fractional
pixels. Resize and changes to row height retain the first visible key and the
hidden fraction of that row, subject to the new scroll limit.

`FloatingListStyle` groups the frame fill and border, optional header fill,
title and empty typography, track and thumb, and close button. The thumb and
button can each use distinct hover and pressed colors. Use
`FloatingListStyle.defaults()` for the original colors and typography, and
`setStyle()` to apply Java colors, measures and optional `PFont` instances.
A null Java text font uses the active Processing font. Rounded corners
shape the chrome. Row contents are clipped to the rectangular viewport.

`setTitle()` changes the title on an open panel without modifying registration,
scroll or an in-progress gesture. Long titles are bounded and clipped before
the close button. The caller supplies any count in the title.

## Anchor and data

At open, the popup tries below the anchor if the desired height fits there,
then above. If neither side fits the full height it uses the larger space.
The left edge aligns with the anchor and is clamped horizontally. Once open,
the panel retains its side as title or items change. After a resize or anchor
move it switches only if the current side loses space for the header plus
one viewport pixel (or the full requested height, if that is smaller) and the
opposite side can provide it.

When neither side is usable, the panel may overlap the anchor to stay usable.
For a small canvas, sizes and margins contract to retain the close target
whenever there is a drawable pixel. A partial row is allowed. With height for
only a header, the viewport is absent. A canvas with a zero axis has no
visible panel or hit area. There is no persistence beyond the instance.

`setItems()` makes a shallow defensive copy in the supplied order. The list,
items and keys must be nonnull and keys must be unique. Rejected items leave the
previous list intact. Keep the keys' equality and hash stable. If the first
visible key disappears, the panel looks for the next surviving key in the
previous order, then the previous one. If none survive, it starts at the top.
The result is clamped to the scroll range. An empty update keeps the panel open,
shows the supplied empty text when there is room and resets the offset.

## Standalone JSON

Use `FloatingListConfigLoader.apply(sketch, path, list)` or
`applyFromJson(json, sourcePath, list)` against an existing instance.
The document has optional `title`, `measures` and `style` sections. It patches
present fields and preserves omitted or null fields, including nested fields.
Colors use the existing integer, `#RRGGBB` or `#AARRGGBB` syntax. A shared
`style.font` replaces the font for title, empty text and close button. The
existing font resolver loads and caches it at the effective text size when drawn.
Calling `setStyle()` reinstates Java/ambient fonts. Invalid presentation values
are rejected before application. A missing font resource is reported when
first drawn. Items, keys, renderers and anchor stay in Java.

| Example | Declaration |
| --- | --- |
| [Absolute](../data/config/floating-list-absolute.json) | Numbers are pixels, for example `"canvasMargin": 12`. |
| [Relative](../data/config/floating-list-relative.json) | Explicit objects, for example `"width": {"mode":"relative","value":0.60}`. |
| [Mixed](../data/config/floating-list-mixed.json) | Modes can be mixed in a section or between sections. Explicit `"mode":"absolute"` also means pixels. |

## Rendering and input

Draw active overlays after the scene. `OverlayManager.getActiveOverlays()`
returns descending Z. Sort the returned snapshot in ascending Z for a paint
pass where higher Z is on top. `InputManager` already dispatches higher
priorities first. The panel uses its constructor priority for both and manages
its own registration.

The rendering contract is the primary P2D canvas in Processing 4.5.6, with its
default screen projection, untransformed drawing matrix and no enclosing clip.
P2D implements the integer viewport clip as an OpenGL scissor rectangle.
P3D, JAVA2D, off-screen drawing and transformed overlay passes are outside
the supported contract.

The renderer receives the absolute bounds of the full row, even when only a
portion is visible. Only intersecting rows are drawn. A renderer may use local
styles and transforms, must balance additional stack pushes and must leave clip
and projection ownership with the panel. If the renderer throws, the panel
releases its clip and unwinds its drawing scopes before propagating the exception.

Route sketch pointer callbacks through `InputManager`. One wheel unit scrolls
one row height over the viewport or scrollbar. A track click pages toward the
click. The thumb uses the same bounded offset, including when dragged outside
the panel. The panel consumes input over empty space and at scroll limits.
Clicking outside hides it immediately and consumes that button's drag and release.

Forward keyboard press through `InputManager` for Escape. In Processing, save
the incoming Escape character and set `key = 0` in the press callback when
handling it to suppress Processing's exit behavior. The panel consumes only
Escape press while open and leaves existing focus in place.

`isOpen()` describes overlay visibility. `dismiss()` is idempotent and preserves
items and scroll for reopening. The close button, Escape, an outside press and
`OverlayManager.clearAll()` use the same path. `close()` permanently releases
registrations, pending capture and row data. Call it at sketch disposal. Use
the component from the sketch thread.

## Run the P2D example

From the repository root in a graphical session:

```bash
mvn -DskipTests compile dependency:build-classpath -Dmdep.outputFile=target/floating-list-classpath.txt
java -cp "target/classes:$(cat target/floating-list-classpath.txt)" \
  com.cpz.processing.controls.examples.floatinglist.FloatingListPanelDemo
```

- Roll the wheel, page the track and drag the thumb. The lower-layer counters
  should stay fixed even at the scroll limits. Use **A**, **D** and **F** to add
  and remove around the visible rows and inspect the first visible key.
- Use **M** while open to cycle legacy, absolute, relative and mixed configurations.
  Resize and compare widths, row heights, fonts and canvas margins. Use **T** to
  toggle a long title while keeping the same scroll position.
- Cycle **B** through four corners and a fifth anchor near the lower third.
  Open a long list at the fifth anchor, then press **L** to shorten it to two rows
  and verify it stays above. Move or resize so the current side loses space.
- Use **E** for an empty list and **R** to refill 60 rows. Close by button, Escape,
  **G**, or an outside press-drag-release. Reopen using the trigger. Check that
  the status text drawn after the panel remains unclipped.
