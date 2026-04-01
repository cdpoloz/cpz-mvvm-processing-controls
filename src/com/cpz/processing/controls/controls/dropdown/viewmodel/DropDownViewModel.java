package com.cpz.processing.controls.controls.dropdown.viewmodel;

import com.cpz.processing.controls.controls.dropdown.model.DropDownModel;
import com.cpz.processing.controls.core.focus.Focusable;
import com.cpz.processing.controls.core.viewmodel.AbstractControlViewModel;
import java.util.List;

public final class DropDownViewModel extends AbstractControlViewModel implements Focusable {
   private boolean hovered;
   private boolean pressed;
   private boolean focused;
   private boolean expanded;

   public DropDownViewModel(DropDownModel var1) {
      super(var1);
   }

   public List getItems() {
      return ((DropDownModel)this.model).getItems();
   }

   public void setItems(List var1) {
      ((DropDownModel)this.model).setItems(var1);
   }

   public int getSelectedIndex() {
      return ((DropDownModel)this.model).getSelectedIndex();
   }

   public String getSelectedText() {
      int var1 = ((DropDownModel)this.model).getSelectedIndex();
      List var2 = ((DropDownModel)this.model).getItems();
      return var1 >= 0 && var1 < var2.size() ? (String)var2.get(var1) : "";
   }

   public boolean isHovered() {
      return this.hovered;
   }

   public void setHovered(boolean var1) {
      this.hovered = var1 && this.isInteractive();
   }

   public boolean isPressed() {
      return this.pressed;
   }

   public void setPressed(boolean var1) {
      this.pressed = var1 && this.isInteractive();
   }

   public boolean isFocused() {
      return this.focused;
   }

   public void setFocused(boolean var1) {
      this.focused = var1 && this.isInteractive();
      if (!this.focused) {
         this.close();
      }

   }

   public boolean isExpanded() {
      return this.expanded;
   }

   public void toggleExpanded() {
      if (this.isInteractive() && !((DropDownModel)this.model).getItems().isEmpty()) {
         this.expanded = !this.expanded;
      } else {
         this.expanded = false;
      }
   }

   public void close() {
      this.expanded = false;
      this.pressed = false;
   }

   public void selectIndex(int var1) {
      if (this.isInteractive()) {
         ((DropDownModel)this.model).setSelectedIndex(var1);
      }
   }

   public void onFocusGained() {
      this.focused = this.isInteractive();
   }

   public void onFocusLost() {
      this.focused = false;
      this.close();
   }

   protected void onAvailabilityChanged() {
      if (!this.isInteractive()) {
         this.hovered = false;
         this.pressed = false;
         this.focused = false;
         this.expanded = false;
      }

   }

   private boolean isInteractive() {
      return this.isVisible() && this.isEnabled();
   }
}
