package com.cpz.processing.controls.common.binding;

@FunctionalInterface
public interface ValueListener<T> {
   void onChange(T var1);
}
