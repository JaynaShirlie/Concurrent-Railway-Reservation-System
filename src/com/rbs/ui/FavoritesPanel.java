package com.rbs.ui;

import com.rbs.util.Theme;

import javax.swing.*;
import java.awt.*;

public class FavoritesPanel extends JPanel {
    public FavoritesPanel() {
        setLayout(new BorderLayout()); setBackground(Theme.PRIMARY_BG);
        add(new JLabel("Rebook Favorite Journeys (placeholder)"), BorderLayout.CENTER);
    }
}



