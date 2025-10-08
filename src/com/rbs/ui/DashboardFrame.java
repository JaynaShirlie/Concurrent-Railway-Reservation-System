package com.rbs.ui;

import com.rbs.util.Theme;

import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {
    public DashboardFrame(Runnable onLogout) {
        setTitle("RBS Dashboard");
        setSize(1280, 800);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Train Search", new TrainSearchPanel());
        tabs.addTab("Booking", new BookingPanel());
        tabs.addTab("Cancellation/Refund", new CancellationPanel());
        tabs.addTab("Favorites", new FavoritesPanel());
        tabs.addTab("My Bookings", new MyBookingsPanel());
        tabs.addTab("Admin", new AdminPanel());

        JButton logout = new JButton("Logout");
        logout.addActionListener(e -> {
            dispose();
            onLogout.run();
        });

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Theme.SURFACE_BG);
        top.setBorder(BorderFactory.createEmptyBorder(8,16,8,16));
        JLabel title = new JLabel("Dashboard");
        title.setForeground(Theme.TEXT_PRIMARY);
        top.add(title, BorderLayout.WEST);
        top.add(logout, BorderLayout.EAST);

        JPanel root = new JPanel(new BorderLayout());
        root.add(top, BorderLayout.NORTH);
        root.add(tabs, BorderLayout.CENTER);
        setContentPane(root);
    }
}


