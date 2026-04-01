package com.cpz.processing.controls.controls.radiogroup.state;

import java.util.List;

public record RadioGroupViewState(float x, float y, float width, float height, int totalOptions, boolean enabled, List<RadioGroupItemViewState> items) {
}
