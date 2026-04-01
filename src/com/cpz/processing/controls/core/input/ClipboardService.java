package com.cpz.processing.controls.core.input;

import java.awt.Toolkit;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;

public final class ClipboardService {
   public void copy(String var1) {
      try {
         String var2 = var1 == null ? "" : var1;
         Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(var2), (ClipboardOwner)null);
      } catch (Exception var3) {
      }

   }

   public String paste() {
      try {
         Object var1 = Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
         return var1 instanceof String ? (String)var1 : "";
      } catch (Exception var2) {
         return "";
      }
   }
}
