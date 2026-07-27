# 0.9.11 Release Notes

Version `0.9.11` consolidates input, focus, overlay, panel composition, and
validation behavior added after the published `0.9.10` release. It also
formalizes the supported public surface without removing the publicly linkable
infrastructure retained for compatibility.

## Input, Focus, And Overlay Lifecycle

- Input layers distinguish event reception, eligibility, capture, and
  consumption. Disabled, hidden, or occluded controls no longer capture input
  that belongs to another target.
- Every `InputManager` owns one exclusive focus authority. Registration,
  transfer, restoration, and cleanup remain isolated between managers.
- `OverlayManager.clearAll()` invokes producer lifecycle callbacks instead of
  silently dropping registrations. Dropdowns, tooltips, and notifications keep
  their visible state synchronized with the overlay registry.
- Existing cleanup paths through layer unregistration, panel removal, panel
  clearing, and disposal remain coordinated with focus and overlay ownership.

## Dropdown Coordination And Nested Panels

- Dropdown sibling coordination is scoped to each `InputManager`; separate
  application hosts no longer share operational dropdown state.
- Expanded dropdowns inside nested panels accumulate the current offset of
  every ancestor exactly once.
- Collapsed fields retain bounds local to their immediate panel. Expanded
  overlay rendering and hit-testing use the same sketch-space geometry.
- Moving an ancestor through the existing geometry API refreshes an open
  descendant overlay without changing local bounds or relative-measure rules.
- The click priority and dropdown-transfer contract established in `0.9.10`
  remains preserved.
- `DropDown.dispose()` remains terminal and permanently removes the instance
  from later coordination.

## Validation

- Explicit control identities consistently reject null, empty,
  ASCII-whitespace-only, and Unicode-whitespace-only values in runtime and JSON
  loading paths.
- Legacy constructors continue to generate automatic codes in their existing
  format.
- `ControlMeasure` rejects `NaN` and positive or negative infinity.
- JSON dimensions remain positive and must now also be finite.
- `ProgressBar` validates values and both range endpoints before changing
  state. Non-finite inputs are rejected atomically.
- Finite range sorting, value clamping, progress calculation, and relative
  geometry retain their previous behavior.

Configurations or runtime calls that relied on blank identities or non-finite
numbers now fail fast and may require migration.

## Public API Contract

- `docs/public-api-allowlist.md` classifies consumer API, advanced SPI, public
  infrastructure, and compatibility debt retained for later review.
- `docs/public-api-signatures.txt` records the complete reviewed
  public/protected surface.
- `PublicApiSurfaceTest` detects unreviewed signature drift, verifies
  deterministic inventory, and rejects category-D dependencies from supported
  A/B signatures.
- japicmp `0.26.1` reports the packaged `0.9.11` JAR as binary- and
  source-compatible with the published `0.9.10` JAR. See the
  [compatibility report](api-compatibility-0.9.11.md).
- The reviewed surface contains 280 types and 2,943 declared public/protected
  members across 123 packages containing classified types.
- No category-D exposure is removed or hidden in this release.

## Dependencies

- `cpz-utils` is updated from the `0.2.3` dependency published with `0.9.10` to
  `0.2.4`.
- Processing Core remains at `4.5.5`.
- The project continues to emit Java 17 bytecode.

## Documentation

Documentation now covers root JSON loading for `Panel`, runtime child
composition through `panel.add(...)`, nested local/global coordinates,
per-manager dropdown coordination, validation rules, dropdown disposal, and
the formal public API categories.
