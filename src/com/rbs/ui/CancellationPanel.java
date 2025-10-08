package com.rbs.ui;

import com.rbs.util.Theme;

import javax.swing.*;
import java.awt.*;

public class CancellationPanel extends JPanel {
    public CancellationPanel() {
        setLayout(new BorderLayout()); setBackground(Theme.PRIMARY_BG);
        add(new JLabel("Cancellation & Refund (placeholder)"), BorderLayout.CENTER);
    }
}



