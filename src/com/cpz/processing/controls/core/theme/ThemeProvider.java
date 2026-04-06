package com.cpz.processing.controls.core.theme;

/**
 * Theme component for theme provider.
 *
 * Responsibilities:
 * - Represent theme data or theme access for the rendering pipeline.
 * - Keep theme concerns explicit and reusable.
 *
 * Behavior:
 * - Declares the contract without prescribing implementation details.
 *
 * Notes:
 * - This type is part of the public project surface.
 */
public interface ThemeProvider {
   ThemeSnapshot getSnapshot();
}
