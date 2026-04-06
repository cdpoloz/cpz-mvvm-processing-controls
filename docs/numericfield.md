# NumericField

`NumericFieldView` reads a cached `ThemeSnapshot` once per draw or measurement pass and passes it into `NumericFieldStyle`. Measurement helpers reuse that same snapshot, so themed text layout stays allocation-free from the theme side.

## Performance note

Theme-related allocations were removed from the numeric field render path. Snapshot access is constant-time and reused across frame work.
