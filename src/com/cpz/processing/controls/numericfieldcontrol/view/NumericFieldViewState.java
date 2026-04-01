package com.cpz.processing.controls.numericfieldcontrol.view;

public record NumericFieldViewState(float x,
                                    float y,
                                    float width,
                                    float height,
                                    String text,
                                    String textBeforeSelection,
                                    String selectedText,
                                    String textAfterSelection,
                                    int cursorPosition,
                                    int selectionStart,
                                    int selectionEnd,
                                    boolean focused,
                                    boolean hovered,
                                    boolean pressed,
                                    boolean editing,
                                    boolean showCursor,
                                    boolean enabled,
                                    float textX,
                                    float cursorX,
                                    float selectionStartX,
                                    float selectionEndX) {
}
