# DropDown

`DropDownView` keeps `DropDownViewState` focused on control state and layout data. It reads a cached `ThemeSnapshot` once and passes it into `DefaultDropDownStyle`, which resolves all themed values from the snapshot.

## Performance note

The dropdown style no longer performs theme-provider resolution during render. Theme changes rebuild the snapshot once; frames reuse it.
