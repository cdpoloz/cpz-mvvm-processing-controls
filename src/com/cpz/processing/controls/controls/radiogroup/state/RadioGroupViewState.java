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
 */
public record RadioGroupViewState(float x, float y, float width, float height, int totalOptions, boolean enabled, List<RadioGroupItemViewState> items) {
}
