package com.cpz.processing.controls.core.input;

import java.awt.Toolkit;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;

/**
 * Input component for clipboard service.
 *
 * Responsibilities:
 * - Translate public input flow into control operations.
 * - Keep raw event handling outside business state.
 *
 * Behavior:
 * - Keeps the public role isolated from unrelated concerns.
 *
 * Notes:
 * - This type is part of the public project surface.
 *
 * @author CPZ
 */
public final class ClipboardService {
   /**
    * Performs copy.
    *
    * @param text parameter used by this operation
    *
    * Behavior:
    * - Produces the public result required by the surrounding pipeline.
    */
   public void copy(String text) {
      try {
         String text2 = text == null ? "" : text;
         Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(text2), (ClipboardOwner)null);
      } catch (Exception exception) {
      }

   }

   /**
    * Performs paste.
    *
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public String paste() {
      try {
         Object data = Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
         return data instanceof String ? (String)data : "";
      } catch (Exception exception2) {
         return "";
      }
   }
}
