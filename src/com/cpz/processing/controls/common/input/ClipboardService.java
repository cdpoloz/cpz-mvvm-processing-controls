package com.cpz.processing.controls.common.input;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;

public final class ClipboardService {

    public void copy(String text) {
        try {
            String safeText = text == null ? "" : text;
            Toolkit.getDefaultToolkit()
                    .getSystemClipboard()
                    .setContents(new StringSelection(safeText), null);
        } catch (Exception ignored) {
        }
    }

    public String paste() {
        try {
            Object data = Toolkit.getDefaultToolkit()
                    .getSystemClipboard()
                    .getData(DataFlavor.stringFlavor);
            return data instanceof String ? (String) data : "";
        } catch (Exception ignored) {
            return "";
        }
    }
}
