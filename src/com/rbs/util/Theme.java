package com.rbs.util;

import javax.swing.*;
import java.awt.*;

public final class Theme {
    private Theme() {}

    public static final Color PRIMARY_BG = new Color(0x0F172A); // blue-gray 900
    public static final Color SURFACE_BG = new Color(0x111827);
    public static final Color CARD_BG = new Color(0x1F2937);
    public static final Color ACCENT = new Color(0x3B82F6); // blue-500
    public static final Color ACCENT_DARK = new Color(0x2563EB);
    public static final Color TEXT_PRIMARY = new Color(0xE5E7EB);
    public static final Color TEXT_SECONDARY = new Color(0x9CA3AF);

    public static void applyNimbusLookAndFeel() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {
        }
    }

    public static void initGlobalFont() {
        Font uiFont = new Font("Segoe UI", Font.PLAIN, 15);
        UIManager.put("defaultFont", uiFont);
        for (java.util.Enumeration<Object> keys = UIManager.getDefaults().keys(); keys.hasMoreElements();) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof Font) {
                UIManager.put(key, uiFont);
            }
        }
    }
}



