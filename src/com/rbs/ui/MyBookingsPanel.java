package com.rbs.ui;

import com.rbs.util.Theme;

import javax.swing.*;
import java.awt.*;

public class MyBookingsPanel extends JPanel {
    public MyBookingsPanel() {
        setLayout(new BorderLayout()); setBackground(Theme.PRIMARY_BG);
        add(new JLabel("My Bookings: Upcoming / Past (placeholder)"), BorderLayout.CENTER);
    }
}



