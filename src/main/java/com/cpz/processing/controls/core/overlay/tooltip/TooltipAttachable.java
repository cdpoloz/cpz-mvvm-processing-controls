package com.cpz.processing.controls.core.overlay.tooltip;

/**
 * Public contract for sketch objects that own an assignable tooltip.
 *
 * <p>This extends {@link TooltipTarget} with mutation methods so controls and
 * manual areas can be handled uniformly without exposing control internals.</p>
 *
 * @author CPZ
 */
public interface TooltipAttachable extends TooltipTarget {
    Tooltip getTooltip();

    TooltipAttachable setTooltip(String text);

    TooltipAttachable setTooltip(Tooltip tooltip);

    TooltipAttachable setTooltipText(String text);

    TooltipAttachable clearTooltip();
}
