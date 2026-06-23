package com.cpz.processing.controls.controls.button.util;

/**
 * Utility component for button listener.
 *
 * Responsibilities:
 * - Define a focused public contract.
 * - Keep implementations replaceable.
 *
 * Behavior:
 * - Declares the contract without prescribing implementation details.
 *
 * Notes:
 * - This type is part of the public project surface.
 */
@FunctionalInterface
/**
 * @author CPZ
 */
public interface ButtonListener {
   void onClick();
}
