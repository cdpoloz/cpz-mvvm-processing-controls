package com.cpz.processing.controls.controls.radiogroup.state;

import java.util.List;

/**
 * Immutable state record for radio group view state.
 *
 * Responsibilities:
 * - Carry immutable public data.
 * - Keep frame or configuration payloads explicit.
 *
 * Behavior:
 * - Stores data only and does not contain workflow logic.
 *
 * Notes:
 * - This type is part of the public project surface.
 *
 * @param x left coordinate
 * @param y top coordinate
 * @param width control width
 * @param height control height
 * @param totalOptions number of options
 * @param enabled whether the radio group is enabled
 * @param items item view states
 * @author CPZ
 */
public record RadioGroupViewState(float x, float y, float width, float height, int totalOptions, boolean enabled, List<RadioGroupItemViewState> items) {
}
