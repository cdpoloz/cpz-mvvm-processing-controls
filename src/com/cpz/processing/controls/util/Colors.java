package com.cpz.processing.controls.util;

/**
 * @author CPZ
 */
public final class Colors {

    private Colors() {}

    // <editor-fold defaultstate="collapsed" desc="*** componentes 0 a 255 ***">
    public static int agray(int a, int value) {
        return argb(a, value, value, value);
    }

    public static int gray(int value) {
        return rgb(value, value, value);
    }

    public static int rgb(int r, int g, int b) {
        return argb(255, r, g, b);
    }

    public static int argb(int a, int r, int g, int b) {
        a = clamp(a);
        r = clamp(r);
        g = clamp(g);
        b = clamp(b);
        return ((a & 0xFF) << 24)
                | ((r & 0xFF) << 16)
                | ((g & 0xFF) << 8)
                |  (b & 0xFF);
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="*** componentes 0 a 100% ***">
    public static int rgbPct(int rPct, int gPct, int bPct) {
        return rgb(
                pctToByte(rPct),
                pctToByte(gPct),
                pctToByte(bPct)
        );
    }

    public static int argbPct(int aPct, int rPct, int gPct, int bPct) {
        return argb(
                pctToByte(aPct),
                pctToByte(rPct),
                pctToByte(gPct),
                pctToByte(bPct)
        );
    }

    public static int grayPct(int valuePct) {
        return gray(pctToByte(valuePct));
    }

    public static int agrayPct(int aPct, int valuePct) {
        return agray(
                pctToByte(aPct),
                pctToByte(valuePct)
        );
    }

    private static int pctToByte(int pct) {
        return Math.max(0, Math.min(100, pct)) * 255 / 100;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="*** métodos complementarios ***">
    /**
    * Le aplica un alpha a un color existente
     */
    public static int alpha(int a, int color) {
        return (color & 0x00FFFFFF) | ((a & 0xFF) << 24);
    }

    /**
     * Limita el valor de entrada para que su rango sea de 0 a 255
     */
    private static int clamp(int v) {
        return Math.max(0, Math.min(255, v));
    }
    // </editor-fold>
}
